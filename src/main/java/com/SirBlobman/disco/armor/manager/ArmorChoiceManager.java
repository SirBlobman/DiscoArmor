package com.SirBlobman.disco.armor.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.object.ArmorType;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ArmorChoiceManager {
    private final DiscoArmorPlugin plugin;
    private final Map<UUID, ArmorType> armorTypeMap;
    private final Map<UUID, Boolean> glowingMap;
    public ArmorChoiceManager(DiscoArmorPlugin plugin) {
        this.plugin = plugin;
        this.armorTypeMap = new HashMap<>();
        this.glowingMap = new HashMap<>();
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
    
    public boolean shouldGlow(Player player) {
        UUID uuid = player.getUniqueId();
        return this.glowingMap.getOrDefault(uuid, getDefaultShouldGlow());
    }
    
    public void toggleGlow(Player player) {
        UUID uuid = player.getUniqueId();
        boolean shouldGlow = shouldGlow(player);
        this.glowingMap.put(uuid, !shouldGlow);
    }
    
    private boolean getDefaultShouldGlow() {
        YamlConfiguration config = this.plugin.getConfig();
        return config.getBoolean("glowing", false);
    }
}