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

public final class SubCommandGlow extends PlayerCommand {
    private final DiscoArmorPlugin plugin;

    public SubCommandGlow(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "glow");
        setPermissionName("disco-armor.command.disco-armor.glow");
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

        boolean glowing = !data.isGlowingArmor();
        data.setGlowingArmor(glowing);
        memoryDataManager.saveData(player);

        String messagePath = (glowing ? "glow.enabled" : "glow.disabled");
        sendMessage(player, messagePath);
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
