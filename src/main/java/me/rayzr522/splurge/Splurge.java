package me.rayzr522.splurge;

import me.rayzr522.splurge.arena.ArenaManager;
import me.rayzr522.splurge.config.ConfigManager;
import me.rayzr522.splurge.config.Language;
import org.bukkit.plugin.java.JavaPlugin;

public final class Splurge extends JavaPlugin {
    private static Splurge instance;
    private ConfigManager configManager;
    private Language language;
    private ArenaManager arenaManager;

    public static Splurge getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        language = new Language();
        arenaManager = new ArenaManager(this);

        reload();
    }

    @Override
    public void onDisable() {
        save();

        instance = null;
    }

    private void reload() {
        saveDefaultConfig();
        reloadConfig();

        arenaManager.reload();
    }

    private void save() {
        arenaManager.save();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Language getLanguage() {
        return language;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }
}
