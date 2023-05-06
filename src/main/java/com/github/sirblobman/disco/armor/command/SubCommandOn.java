package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.menu.DiscoArmorMainMenu;

public final class SubCommandOn extends PlayerCommand {
    private final DiscoArmorPlugin plugin;

    public SubCommandOn(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "on");
        setPermissionName("disco-armor.command.disco-armor.on");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull Player player, String @NotNull [] args) {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        new DiscoArmorMainMenu(plugin, player).open();
        return true;
    }

    private @NotNull DiscoArmorPlugin getDiscoArmorPlugin() {
        return this.plugin;
    }
}
