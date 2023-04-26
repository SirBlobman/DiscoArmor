package com.github.sirblobman.disco.armor.command.admin;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class SubCommandAdmin extends Command {
    public SubCommandAdmin(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "admin");
        setPermissionName("disco-armor.command.disco-armor.admin");
        addSubCommand(new SubCommandAdminOff(plugin));
        addSubCommand(new SubCommandAdminOn(plugin));
        addSubCommand(new SubCommandAdminReload(plugin));
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        return false;
    }
}
