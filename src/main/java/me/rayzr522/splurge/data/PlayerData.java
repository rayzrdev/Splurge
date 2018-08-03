package me.rayzr522.splurge.data;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class PlayerData {
    private double health;
    private double maxHealth;
    private int foodLevel;
    private float saturation;
    private float exp;
    private int expTotal;
    private ItemStack[] items;
    private ItemStack[] armor;
    private GameMode gamemode;
    private Collection<PotionEffect> potionEffects;

    public void saveFrom(Player player) {
        this.health = player.getHealth();
        this.maxHealth = player.getMaxHealth();
        this.foodLevel = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.exp = player.getExp();
        this.expTotal = player.getTotalExperience();
        this.items = player.getInventory().getContents();
        this.armor = player.getEquipment().getArmorContents();
        this.gamemode = player.getGameMode();
        this.potionEffects = player.getActivePotionEffects();
    }

    public void restoreTo(Player player) {
        player.setHealth(health);
        player.setMaxHealth(maxHealth);
        player.setFoodLevel(foodLevel);
        player.setSaturation(saturation);
        player.setExp(exp);
        player.setTotalExperience(expTotal);
        player.getInventory().setContents(items);
        player.getEquipment().setArmorContents(armor);
        player.setGameMode(gamemode);
        player.getActivePotionEffects().clear();
        player.addPotionEffects(potionEffects);
    }
}
