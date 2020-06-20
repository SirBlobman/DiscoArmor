package com.SirBlobman.disco.armor.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.object.ArmorType;

import org.bukkit.entity.Player;

public class ArmorChoiceManager {
    private final DiscoArmorPlugin plugin;
    private final Map<UUID, ArmorType> armorTypeMap;
    public ArmorChoiceManager(DiscoArmorPlugin plugin) {
        this.plugin = plugin;
        this.armorTypeMap = new HashMap<>();
    }
    
    public DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }
    
    public ArmorType getArmorType(Player player) {
        UUID uuid = player.getUniqueId();
        return this.armorTypeMap.getOrDefault(uuid, null);
    }
    
    public void setArmorType(Player player, ArmorType type) {
        UUID uuid = player.getUniqueId();
        if(type == null) {
            this.armorTypeMap.remove(uuid);
            return;
        }
        
        this.armorTypeMap.put(uuid, type);
    }
}