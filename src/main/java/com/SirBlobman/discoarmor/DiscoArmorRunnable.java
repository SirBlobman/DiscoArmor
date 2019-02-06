package com.SirBlobman.discoarmor;

import com.SirBlobman.discoarmor.type.DiscoArmorType;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class DiscoArmorRunnable extends BukkitRunnable {
    private final DiscoArmorType type;
    public DiscoArmorRunnable(DiscoArmorType type) {this.type = type;}
    
    @Override
    public void run() {
        final ItemStack[] discoArmor = type.getDiscoArmor();
        Bukkit.getOnlinePlayers().stream().filter(DiscoArmor::hasDiscoArmorEnabled).forEach(player -> {
            Bukkit.getScheduler().scheduleSyncDelayedTask(DiscoArmor.INSTANCE, () -> {
                PlayerInventory pi = player.getInventory();
                pi.setArmorContents(discoArmor.clone());
            });
        });
    }
}