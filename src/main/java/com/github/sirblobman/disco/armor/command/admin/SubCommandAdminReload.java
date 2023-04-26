package com.github.sirblobman.disco.armor.command.admin;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class SubCommandAdminReload extends Command {
    public SubCommandAdminReload(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "reload");
        setPermissionName("disco-armor.command.disco-armor.admin.reload");
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        JavaPlugin plugin = getPlugin();
        plugin.reloadConfig();

        sendMessage(sender, "command.admin.reload-success");
        return true;
    }
}
