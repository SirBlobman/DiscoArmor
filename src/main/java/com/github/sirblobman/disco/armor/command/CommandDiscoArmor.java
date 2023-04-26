package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.command.admin.SubCommandAdmin;

public class CommandDiscoArmor extends Command {
    public CommandDiscoArmor(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "disco-armor");
        setPermissionName("disco-armor.command.disco-armor");
        addSubCommand(new SubCommandAdmin(plugin));
        addSubCommand(new SubCommandGlow(plugin));
        addSubCommand(new SubCommandHelp(plugin));
        addSubCommand(new SubCommandOff(plugin));
        addSubCommand(new SubCommandOn(plugin));
        addSubCommand(new SubCommandSelect(plugin));
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        return false;
    }
}
