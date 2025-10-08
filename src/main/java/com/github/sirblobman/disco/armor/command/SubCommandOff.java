package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.playerdata.MemoryData;
import com.github.sirblobman.disco.armor.configuration.playerdata.MemoryDataManager;
import com.github.sirblobman.disco.armor.task.DiscoArmorTaskManager;

public final class SubCommandOff extends PlayerCommand {
    private final DiscoArmorPlugin plugin;

    public SubCommandOff(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "off");
        setPermissionName("disco-armor.command.disco-armor.off");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull Player player, String @NotNull [] args) {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        MemoryDataManager memoryDataManager = plugin.getMemoryDataManager();
        MemoryData data = memoryDataManager.getData(player);
        data.setArmorStatus(false);
        memoryDataManager.saveData(player);

        DiscoArmorTaskManager taskManager = plugin.getTaskManager();
        if (taskManager.hasTask(player)) {
            taskManager.removeTask(player);
        }

        return true;
    }

    private @NotNull DiscoArmorPlugin getDiscoArmorPlugin() {
        return this.plugin;
    }

    private @NotNull PlayerDataManager getPlayerDataManager() {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        return plugin.getPlayerDataManager();
    }
}
