package me.rayzr522.splurge.arena;

import me.rayzr522.splurge.Splurge;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.stream.Collectors;

public class ArenaManager {
    private final Splurge plugin;
    private Map<String, Arena> arenas;

    public ArenaManager(Splurge plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        YamlConfiguration config = plugin.getConfigManager().getConfig("arenas.yml");
        arenas = config.getKeys(false).stream()
                .filter(key -> config.get(key) instanceof Arena)
                .collect(Collectors.toMap(key -> key, key -> (Arena) config.get(key)));
    }

    public void save() {
        YamlConfiguration config = plugin.getConfigManager().getConfig("arenas.yml");
        arenas.forEach(config::set);
        plugin.getConfigManager().saveConfig("arenas.yml", config);
    }
}
