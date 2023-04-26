package com.github.sirblobman.disco.armor.task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.pattern.PatternManager;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;

public final class DiscoArmorTask extends EntityTaskDetails<ConfigurablePlugin, Player> {
    private final DiscoArmorPlugin plugin;
    private final Map<UUID, ItemStack[]> oldArmorMap;

    public DiscoArmorTask(@NotNull DiscoArmorPlugin plugin, @NotNull Player player) {
        super(plugin, player);
        this.plugin = plugin;
        this.oldArmorMap = new HashMap<>();
    }

    @Override
    public void run() {
        check();
    }

    private @NotNull DiscoArmorPlugin getDiscoArmor() {
        return this.plugin;
    }

    private PlayerDataManager getPlayerDataManager() {
        DiscoArmorPlugin plugin = getDiscoArmor();
        return plugin.getPlayerDataManager();
    }

    private PatternManager getPatternManager() {
        DiscoArmorPlugin plugin = getDiscoArmor();
        return plugin.getPatternManager();
    }

    public void disable() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration playerData = playerDataManager.get(player);
        playerData.set("pattern", null);
        playerDataManager.save(player);
        check();
    }

    private void check() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        String patternName = configuration.getString("pattern");
        if (patternName == null) {
            loadOldArmor(player);
            return;
        }

        PatternManager patternManager = getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternName);
        if (pattern == null) {
            loadOldArmor(player);
            return;
        }

        saveOldArmor(player);
        Map<ArmorType, ItemStack> nextArmor = pattern.getNextArmor(player);
        nextArmor.forEach((armorType, armor) -> setArmor(player, armorType, armor));
    }

    private void saveOldArmor(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        if (this.oldArmorMap.containsKey(playerId)) {
            return;
        }

        ItemStack[] oldArmor = getArmor(player);
        this.oldArmorMap.put(playerId, oldArmor);
    }

    private void loadOldArmor(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        ItemStack[] oldArmor = this.oldArmorMap.remove(playerId);
        if (oldArmor == null) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setArmorContents(oldArmor);
        player.updateInventory();
    }

    private ItemStack @NotNull [] getArmor(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack[] armorContents = playerInventory.getArmorContents();
        int armorContentsLength = armorContents.length;

        for (int slot = 0; slot < armorContentsLength; slot++) {
            ItemStack item = armorContents[slot];
            if (isDiscoArmor(item)) {
                armorContents[slot] = new ItemStack(Material.AIR);
            }
        }

        return armorContents;
    }

    private boolean isDiscoArmor(@Nullable ItemStack item) {
        if (ItemUtility.isAir(item)) {
            return false;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        DiscoArmorPlugin plugin = getDiscoArmor();
        NamespacedKey discoArmorKey = new NamespacedKey(plugin, "disco");
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        byte discoArmor = persistentDataContainer.getOrDefault(discoArmorKey, PersistentDataType.BYTE, (byte) 0);
        return (discoArmor == 1);
    }

    private void setArmor(@NotNull Player player, @NotNull ArmorType armorType, @NotNull ItemStack armor) {
        PlayerInventory playerInventory = player.getInventory();
        switch (armorType) {
            case HELMET -> playerInventory.setHelmet(armor);
            case CHESTPLATE -> playerInventory.setChestplate(armor);
            case LEGGINGS -> playerInventory.setLeggings(armor);
            case BOOTS -> playerInventory.setBoots(armor);
        }
    }

    public boolean isEnabled() {
        Player player = getEntity();
        if (player == null) {
            return false;
        }

        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        String patternName = configuration.getString("pattern");
        if (patternName == null) {
            return false;
        }

        PatternManager patternManager = getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternName);
        return (pattern != null);
    }
}
