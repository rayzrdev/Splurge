package me.rayzr522.splurge.state;

import me.rayzr522.splurge.arena.Arena;
import me.rayzr522.splurge.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static me.rayzr522.splurge.Splurge.later;

public class Match {
    private final Arena arena;
    private final int maxPlayers;

    private State state = State.WAITING_FOR_PLAYERS;
    private List<UUID> players = new ArrayList<>();
    private Map<UUID, Integer> scores = new HashMap<>();
    private Map<UUID, PlayerData> playerData = new HashMap<>();

    public Match(Arena arena, int maxPlayers) {
        this.arena = arena;
        this.maxPlayers = maxPlayers;

        later(this::waitForPlayers, 50, TimeUnit.MILLISECONDS);
    }

    private void waitForPlayers() {
        state = State.WAITING_FOR_PLAYERS;
        broadcastMessage(ChatColor.GREEN + "Waiting 30 seconds for more players...");

        later(() -> {
            if (players.size() >= 2) {
                startMatch();
            } else {
                waitForPlayers();
            }
        }, 30, TimeUnit.SECONDS);
    }

    private void startMatch() {
        state = State.STARTING;
        broadcastMessage(ChatColor.YELLOW + "Game is beginning in 5 seconds...");

        later(() -> broadcastMessage(ChatColor.YELLOW + "3..."), 2, TimeUnit.SECONDS);
        later(() -> broadcastMessage(ChatColor.YELLOW + "2..."), 3, TimeUnit.SECONDS);
        later(() -> broadcastMessage(ChatColor.RED + "1..."), 4, TimeUnit.SECONDS);
        later(() -> {
            state = State.IN_GAME;
            
            broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "The game has begun!");
            allPlayers(player -> {
                Location spawn = arena.getArenaSpawns().get((int) Math.floor(Math.random() * arena.getArenaSpawns().size()));
                player.teleport(spawn);
            });
        }, 5, TimeUnit.SECONDS);
    }

    private void endMatch() {
        state = State.ENDING;
        broadcastMessage(ChatColor.RED + "Game over!");

        int topScore = -1;
        UUID topScoringPlayer = null;
        for (UUID player : players) {
            int score = scores.getOrDefault(player, 0);
            if (score > topScore) {
                topScore = score;
                topScoringPlayer = player;
            }
        }

        int finalTopScore = topScore;

        if (players.stream().filter(player -> scores.getOrDefault(player, 0) == finalTopScore).count() > 1) {
            broadcastMessage(ChatColor.YELLOW + "It was a tie!");
        } else {
            broadcastMessage(ChatColor.GREEN + String.format("%s won with %d points!", Bukkit.getPlayer(topScoringPlayer).getDisplayName(), topScore));
        }

        later(() -> {
            allPlayers(this::leaveMatch);
        }, 5, TimeUnit.SECONDS);
    }

    private void allPlayers(Consumer<Player> function) {
        players.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(function);
    }

    private void broadcastMessage(String message) {
        allPlayers(player -> player.sendMessage(message));
    }

    public boolean joinMatch(Player player) {
        if (state != State.WAITING_FOR_PLAYERS) {
            return false;
        }

        players.add(player.getUniqueId());
        player.teleport(arena.getLobbySpawn());

        PlayerData data = new PlayerData();
        data.saveFrom(player);
        playerData.put(player.getUniqueId(), data);

        return true;
    }

    public void leaveMatch(Player player) {
        players.remove(player.getUniqueId());

        Optional.ofNullable(playerData.get(player.getUniqueId()))
                .ifPresent(data -> data.restoreTo(player));

        if (state == State.IN_GAME && players.size() <= 1) {
            endMatch();
        }
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public Map<UUID, Integer> getScores() {
        return scores;
    }

    public int getScore(UUID player) {
        return scores.computeIfAbsent(player, id -> 0);
    }

    public boolean isInMatch(UUID player) {
        return players.contains(player);
    }

    public enum State {
        WAITING_FOR_PLAYERS,
        STARTING,
        IN_GAME,
        ENDING
    }
}
