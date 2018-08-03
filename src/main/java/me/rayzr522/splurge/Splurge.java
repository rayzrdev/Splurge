package me.rayzr522.splurge;

import me.rayzr522.splurge.arena.Arena;
import me.rayzr522.splurge.arena.ArenaManager;
import me.rayzr522.splurge.config.ConfigManager;
import me.rayzr522.splurge.config.Language;
import me.rayzr522.splurge.struct.Region;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public final class Splurge extends JavaPlugin {
    private static Splurge instance;
    private ConfigManager configManager;
    private Language language;
    private ArenaManager arenaManager;

    public static Splurge getInstance() {
        return instance;
    }

    public static void later(Runnable runnable, long delay, TimeUnit unit) {
        Bukkit.getScheduler().runTaskLater(instance, runnable, unit.toMillis(delay) / 50);
    }

    @Override
    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(Arena.class);
        ConfigurationSerialization.registerClass(Region.class);

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
