package com.github.sirblobman.disco.armor.task;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.DiscoArmorConfiguration;

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
        DiscoArmorTask oldTask = getTask(player);
        if (oldTask != null) {
            return;
        }

        DiscoArmorPlugin plugin = getPlugin();
        DiscoArmorTask newTask = new DiscoArmorTask(plugin, player);

        DiscoArmorConfiguration configuration = plugin.getConfiguration();
        long armorSpeed = configuration.getArmorSpeed();
        newTask.setPeriod(armorSpeed);

        FoliaHelper<ConfigurablePlugin> foliaHelper = plugin.getFoliaHelper();
        TaskScheduler<ConfigurablePlugin> scheduler = foliaHelper.getScheduler();
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
        }
    }
}
