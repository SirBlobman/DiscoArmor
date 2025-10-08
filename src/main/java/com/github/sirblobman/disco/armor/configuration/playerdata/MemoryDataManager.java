package com.github.sirblobman.disco.armor.configuration.playerdata;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class MemoryDataManager {
    private final DiscoArmorPlugin plugin;
    private final Map<UUID, MemoryData> memoryDataMap;

    public MemoryDataManager(@NotNull DiscoArmorPlugin plugin) {
        this.plugin = plugin;
        this.memoryDataMap = new HashMap<>();
    }

    public @NotNull MemoryData getData(@NotNull OfflinePlayer player) {
        UUID uniqueId = player.getUniqueId();
        if (this.memoryDataMap.containsKey(uniqueId)) {
            return this.memoryDataMap.get(uniqueId);
        }

        MemoryData memoryData = loadData(player);
        this.memoryDataMap.put(uniqueId, memoryData);
        return memoryData;
    }

    public void saveData(@NotNull OfflinePlayer player) {
        DiscoArmorPlugin plugin = getPlugin();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);

        MemoryData data = getData(player);
        data.save(configuration);
        playerDataManager.save(player);
    }

    private @NotNull DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull MemoryData loadData(@NotNull OfflinePlayer player) {
        DiscoArmorPlugin plugin = getPlugin();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        MemoryData memoryData = new MemoryData();
        memoryData.load(configuration);
        return memoryData;
    }
}
