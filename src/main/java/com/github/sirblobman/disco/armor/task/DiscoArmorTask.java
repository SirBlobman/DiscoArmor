package com.github.sirblobman.disco.armor.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
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
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayerCollection) {
            check(player);
        }
    }

    public void disableAll() {
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayerCollection) {
            disable(player);
        }
    }

    public void disable(Player player) {
        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", null);
        playerDataManager.save(player);
        check(player);
    }

    private void check(Player player) {
        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        String patternName = configuration.getString("pattern");

        PatternManager patternManager = this.plugin.getPatternManager();
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
        UUID uuid = player.getUniqueId();
        if(this.oldArmorMap.containsKey(uuid)) {
            return;
        }

        ItemStack[] oldArmor = getArmor(player);
        this.oldArmorMap.put(uuid, oldArmor);
    }

    private void loadOldArmor(Player player) {
        UUID uuid = player.getUniqueId();
        ItemStack[] oldArmor = this.oldArmorMap.remove(uuid);
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
            if(isDiscoArmor(item)) armorContents[slot] = new ItemStack(Material.AIR);
        }

        return armorContents;
    }

    private boolean isDiscoArmor(ItemStack item) {
        if(ItemUtility.isAir(item)) return false;
        MultiVersionHandler multiVersionHandler = this.plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();

        String customNBT = itemHandler.getCustomNBT(item, "disco", "not");
        return customNBT.equals("armor");
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
}
