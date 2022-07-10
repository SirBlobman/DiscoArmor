package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public class CommandDiscoArmor extends Command {
    public CommandDiscoArmor(DiscoArmorPlugin plugin) {
        super(plugin, "disco-armor");

        addSubCommand(new SubCommandGlow(plugin));
        addSubCommand(new SubCommandOff(plugin));
        addSubCommand(new SubCommandOn(plugin));
        addSubCommand(new SubCommandReload(plugin));
        addSubCommand(new SubCommandSelect(plugin));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sendMessage(sender, "command.usage", null, true);
        return true;
    }
}
