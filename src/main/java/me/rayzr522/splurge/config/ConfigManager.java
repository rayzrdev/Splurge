package me.rayzr522.splurge.config;

import me.rayzr522.splurge.Splurge;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigManager {
    private final Splurge plugin;

    public ConfigManager(Splurge plugin) {
        this.plugin = plugin;
    }

    private File resolveFile(String path) {
        Objects.requireNonNull(path, "path cannot be null!");
        return new File(plugin.getDataFolder(), path.replace('/', File.separatorChar));
    }

    public YamlConfiguration getConfig(String path) {
        if (!resolveFile(path).exists() && plugin.getResource(path) != null) {
            plugin.saveResource(path, false);
        }
        return YamlConfiguration.loadConfiguration(resolveFile(path));
    }

    public void saveConfig(String path, YamlConfiguration config) {
        Objects.requireNonNull(path, "path cannot be null!");
        Objects.requireNonNull(config, "config cannot be null!");

        try {
            config.save(resolveFile(path));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, String.format("Failed to save config to file '%s'", path), e);
        }
    }
}
