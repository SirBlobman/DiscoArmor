package com.github.sirblobman.disco.armor.task;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.ArmorSlot;
import com.github.sirblobman.disco.armor.configuration.playerdata.MemoryData;
import com.github.sirblobman.disco.armor.configuration.playerdata.MemoryDataManager;

public final class DiscoArmorTaskManager {
    private final DiscoArmorPlugin plugin;
    private final Map<UUID, DiscoArmorTask> taskMap;

    public DiscoArmorTaskManager(@NotNull DiscoArmorPlugin plugin) {
        this.plugin = plugin;
        this.taskMap = new ConcurrentHashMap<>();
    }

    private @NotNull DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    public @Nullable DiscoArmorTask getTask(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        return this.taskMap.get(playerId);
    }

    public void createTask(@NotNull Player player) {
        if (hasTask(player)) {
            return;
        }

        DiscoArmorPlugin plugin = getPlugin();
        MemoryDataManager memoryDataManager = plugin.getMemoryDataManager();
        MemoryData data = memoryDataManager.getData(player);
        data.setPreviousArmor(player.getInventory());
        memoryDataManager.saveData(player);

        DiscoArmorTask newTask = new DiscoArmorTask(plugin, player);
        FoliaHelper foliaHelper = plugin.getFoliaHelper();
        TaskScheduler scheduler = foliaHelper.getScheduler();
        scheduler.scheduleEntityTask(newTask);

        UUID playerId = player.getUniqueId();
        this.taskMap.put(playerId, newTask);
    }

    public void removeTask(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        DiscoArmorTask discoArmorTask = getTask(player);
        if (discoArmorTask != null) {
            discoArmorTask.cancel();
            this.taskMap.remove(playerId);

            DiscoArmorPlugin plugin = getPlugin();
            MemoryDataManager memoryDataManager = plugin.getMemoryDataManager();
            MemoryData data = memoryDataManager.getData(player);
            Map<ArmorSlot, ItemStack> previousArmor = data.getPreviousArmor();
            if (!previousArmor.isEmpty()) {
                PlayerInventory playerInventory = player.getInventory();
                Set<Map.Entry<ArmorSlot, ItemStack>> previousArmorEntrySet = previousArmor.entrySet();
                for (Map.Entry<ArmorSlot, ItemStack> previousArmorEntry : previousArmorEntrySet) {
                    ArmorSlot armorSlot = previousArmorEntry.getKey();
                    EquipmentSlot slot = armorSlot.getEquipmentSlot();
                    ItemStack itemStack = previousArmorEntry.getValue();
                    playerInventory.setItem(slot, itemStack);
                }
            }
        }
    }

    public boolean hasTask(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        return this.taskMap.containsKey(playerId);
    }
}
