package com.github.sirblobman.disco.armor.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.manager.PatternManager;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;

public final class DiscoArmorTask extends BukkitRunnable {
    private final DiscoArmorPlugin plugin;
    private final Map<UUID, ItemStack[]> oldArmorMap;

    public DiscoArmorTask(DiscoArmorPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.oldArmorMap = new HashMap<>();
    }

    @Override
    public void run() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            check(player);
        }
    }

    public void disableAll() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            disable(player);
        }
    }

    private DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    private PlayerDataManager getPlayerDataManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getPlayerDataManager();
    }

    private PatternManager getPatternManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getPatternManager();
    }

    public void disable(Player player) {
        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration playerData = playerDataManager.get(player);
        playerData.set("pattern", null);
        playerDataManager.save(player);
        check(player);
    }

    private void check(Player player) {
        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        String patternName = configuration.getString("pattern");
        if(patternName == null) {
            loadOldArmor(player);
            return;
        }

        PatternManager patternManager = getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternName);
        if(pattern == null) {
            loadOldArmor(player);
            return;
        }

        saveOldArmor(player);
        Map<ArmorType, ItemStack> nextArmor = pattern.getNextArmor(player);
        nextArmor.forEach((armorType, armor) -> setArmor(player, armorType, armor));
    }

    private void saveOldArmor(Player player) {
        UUID playerId = player.getUniqueId();
        if(this.oldArmorMap.containsKey(playerId)) {
            return;
        }

        ItemStack[] oldArmor = getArmor(player);
        this.oldArmorMap.put(playerId, oldArmor);
    }

    private void loadOldArmor(Player player) {
        UUID playerId = player.getUniqueId();
        ItemStack[] oldArmor = this.oldArmorMap.remove(playerId);
        if(oldArmor == null) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setArmorContents(oldArmor);
        player.updateInventory();
    }

    private ItemStack[] getArmor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack[] armorContents = playerInventory.getArmorContents();
        int armorContentsLength = armorContents.length;

        for(int slot = 0; slot < armorContentsLength; slot++) {
            ItemStack item = armorContents[slot];
            if(isDiscoArmor(item)) {
                armorContents[slot] = new ItemStack(Material.AIR);
            }
        }

        return armorContents;
    }

    private boolean isDiscoArmor(ItemStack item) {
        if(ItemUtility.isAir(item)) {
            return false;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return false;
        }

        DiscoArmorPlugin plugin = getPlugin();
        NamespacedKey discoArmorKey = new NamespacedKey(plugin, "disco");
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        byte discoArmor = persistentDataContainer.getOrDefault(discoArmorKey, PersistentDataType.BYTE, (byte) 0);
        return (discoArmor == 1);
    }

    private void setArmor(Player player, ArmorType armorType, ItemStack armor) {
        PlayerInventory playerInventory = player.getInventory();
        switch (armorType) {
            case HELMET -> playerInventory.setHelmet(armor);
            case CHESTPLATE -> playerInventory.setChestplate(armor);
            case LEGGINGS -> playerInventory.setLeggings(armor);
            case BOOTS -> playerInventory.setBoots(armor);
        }
    }

    public boolean isEnabled(Player player) {
        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        String patternName = configuration.getString("pattern");
        if(patternName == null) {
            return false;
        }

        PatternManager patternManager = getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternName);
        return (pattern != null);
    }
}
