package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.menu.DiscoArmorMenu;

final class SubCommandOn extends PlayerCommand {
    private final DiscoArmorPlugin plugin;

    public SubCommandOn(DiscoArmorPlugin plugin) {
        super(plugin, "on");
        setPermissionName("disco-armor.command.disco-armor.on");
        this.plugin = plugin;
    }

    @Override
    protected List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        new DiscoArmorMenu(this.plugin, player).open();
        return true;
    }
}
