package me.rayzr522.splurge.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Language {
    private Map<String, String> messages = new HashMap<>();

    private static String getBase(String key) {
        if (key.isEmpty()) {
            return "";
        }

        return key.substring(0, key.lastIndexOf('.'));
    }

    public void load(ConfigurationSection config) {
        messages = config.getKeys(true).stream()
                .collect(Collectors.toMap(key -> key, key -> Objects.toString(config.get(key))));
    }

    private String getPrefixFor(String key) {
        String base = getBase(key);
        String prefix = messages.getOrDefault(base + ".prefix", messages.getOrDefault("prefix", ""));
        String addon = messages.getOrDefault(base + ".prefix-addon", "");
        return ChatColor.translateAlternateColorCodes('&', prefix + addon);
    }

    private String getMessage(String key) {
        return messages.get(key);
    }

    public String trRaw(String key, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(key), args));
    }

    public String tr(String key, Object... args) {
        return getPrefixFor(key) + trRaw(key, args);
    }
}
