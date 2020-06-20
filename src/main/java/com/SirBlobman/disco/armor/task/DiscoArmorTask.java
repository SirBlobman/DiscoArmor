package com.SirBlobman.disco.armor.task;

import java.util.*;

import com.SirBlobman.api.item.ItemUtil;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.manager.ArmorChoiceManager;
import com.SirBlobman.disco.armor.object.ArmorType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class DiscoArmorTask extends BukkitRunnable {
    private final DiscoArmorPlugin plugin;
    private final Map<UUID, ItemStack[]> oldArmorMap;
    public DiscoArmorTask(DiscoArmorPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.oldArmorMap = new HashMap<>();
    }
    
    @Override
    public void run() {
        ArmorChoiceManager armorChoiceManager = this.plugin.getArmorChoiceManager();
        Collection<? extends Player> onlinePlayerList = Bukkit.getOnlinePlayers();
        for(Player player : onlinePlayerList) {
            ArmorType armorType = armorChoiceManager.getArmorType(player);
            if(armorType == null) {
                loadOldArmor(player);
                continue;
            }
            
            saveOldArmor(player);
            Map<EquipmentSlot, ItemStack> nextArmor = armorType.getNextArmor(player);
            nextArmor.forEach((slot, item) -> setItem(player, slot, item));
        }
    }
    
    public void saveOldArmor(Player player) {
        UUID uuid = player.getUniqueId();
        if(this.oldArmorMap.containsKey(uuid)) return;
        
        PlayerInventory playerInventory = player.getInventory();
        ItemStack[] armor = getArmor(playerInventory);
        this.oldArmorMap.put(uuid, armor);
    }
    
    public void loadOldArmor(Player player) {
        UUID uuid = player.getUniqueId();
        ItemStack[] oldArmor = this.oldArmorMap.remove(uuid);
        if(oldArmor == null) return;
    
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setArmorContents(oldArmor);
    }
    
    private ItemStack[] getArmor(PlayerInventory playerInventory) {
        ItemStack[] armorContents = playerInventory.getArmorContents();
        int armorContentsLength = armorContents.length;
        
        for(int slot = 0; slot < armorContentsLength; slot++) {
            ItemStack item = armorContents[slot];
            if(!this.plugin.isDiscoArmor(item)) continue;
            armorContents[slot] = ItemUtil.getAir();
        }
        
        return armorContents;
    }
    
    private void setItem(Player player, EquipmentSlot slot, ItemStack item) {
        PlayerInventory playerInventory = player.getInventory();
        switch(slot) {
            case HEAD: playerInventory.setHelmet(item); return;
            case CHEST: playerInventory.setChestplate(item); return;
            case LEGS: playerInventory.setLeggings(item); return;
            case FEET: playerInventory.setBoots(item); return;
            default: break;
        }
    }
}