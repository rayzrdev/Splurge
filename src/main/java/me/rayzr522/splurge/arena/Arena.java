package me.rayzr522.splurge.arena;

import me.rayzr522.splurge.struct.Region;
import me.rayzr522.splurge.util.MapBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Map;

public class Arena implements ConfigurationSerializable {
    private final String id;
    private final String name;
    private final int minPlayers;
    private final int maxPlayers;
    private final Region arenaRegion;
    private final Region lobbyRegion;
    private final List<Location> arenaSpawns;
    private final Location lobbySpawn;

    public Arena(String id, String name, int minPlayers, int maxPlayers, Region arenaRegion, Region lobbyRegion, List<Location> arenaSpawns, Location lobbySpawn) {
        this.id = id;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.arenaRegion = arenaRegion;
        this.lobbyRegion = lobbyRegion;
        this.arenaSpawns = arenaSpawns;
        this.lobbySpawn = lobbySpawn;
    }

    @SuppressWarnings("unchecked")
    public static Arena deserialize(Map<String, Object> data) {
        Validate.isTrue(data.get("id") instanceof String, "id must be a string!");
        Validate.isTrue(data.get("name") instanceof String, "name must be a string!");
        Validate.isTrue(data.get("min-players") instanceof Integer, "min-players must be an integer!");
        Validate.isTrue(data.get("max-players") instanceof Integer, "max-players must be an integer!");
        Validate.isTrue(data.get("arena-region") instanceof Region, "arena-region must be a region!");
        Validate.isTrue(data.get("lobby-region") instanceof Region, "lobby-region must be a region!");
        Validate.isTrue(data.get("arena-spawns") instanceof List, "arena-spawns must be a location list!");
        Validate.isTrue(data.get("lobby-spawn") instanceof Location, "lobby-spawn must be a location!");

        return new Arena(
                (String) data.get("id"),
                (String) data.get("name"),
                (Integer) data.get("min-players"),
                (Integer) data.get("max-players"),
                (Region) data.get("arena-region"),
                (Region) data.get("lobby-region"),
                (List<Location>) data.get("arena-spawns"),
                (Location) data.get("lobby-spawn")
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Region getArenaRegion() {
        return arenaRegion;
    }

    public Region getLobbyRegion() {
        return lobbyRegion;
    }

    public List<Location> getArenaSpawns() {
        return arenaSpawns;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    @Override
    public Map<String, Object> serialize() {
        return MapBuilder.<String, Object>ofHashMap()
                .put("id", id)
                .put("name", name)
                .put("min-players", minPlayers)
                .put("max-players", maxPlayers)
                .put("arena-region", arenaRegion)
                .put("lobby-region", lobbyRegion)
                .put("arena-spawns", arenaSpawns)
                .put("lobby-spawn", lobbySpawn)
                .build();
    }
}
