package com.github.sirblobman.disco.armor.command.admin;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class SubCommandAdmin extends Command {
    public SubCommandAdmin(DiscoArmorPlugin plugin) {
        super(plugin, "admin");
        setPermissionName("disco-armor.command.disco-armor.admin");
        addSubCommand(new SubCommandAdminOff(plugin));
        addSubCommand(new SubCommandAdminOn(plugin));
        addSubCommand(new SubCommandAdminReload(plugin));
    }

    @Override
    protected List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender commandSender, String[] strings) {
        return false;
    }
}
