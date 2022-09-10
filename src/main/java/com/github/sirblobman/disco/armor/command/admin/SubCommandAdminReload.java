package com.github.sirblobman.disco.armor.command.admin;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

final class SubCommandAdminReload extends Command {
    public SubCommandAdminReload(DiscoArmorPlugin plugin) {
        super(plugin, "reload");
        setPermissionName("disco-armor.command.disco-armor.admin.reload");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        JavaPlugin plugin = getPlugin();
        plugin.reloadConfig();

        sendMessage(sender, "command.admin.reload-success", null);
        return true;
    }
}
