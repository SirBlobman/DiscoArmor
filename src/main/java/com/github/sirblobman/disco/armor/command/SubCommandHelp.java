package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class SubCommandHelp extends Command {
    public SubCommandHelp(DiscoArmorPlugin plugin) {
        super(plugin, "help");
        setPermissionName("disco-armor.command.disco-armor.help");
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        sender.sendMessage("");

        List<String> playerCommandList = List.of("glow", "help", "off", "on", "select");
        for (String playerCommandName : playerCommandList) {
            String permissionName = ("disco-armor.command.disco-armor." + playerCommandName);
            if (!sender.hasPermission(permissionName)) {
                continue;
            }

            String messagePath = ("help-player." + playerCommandName);
            sendMessage(sender, messagePath);
        }

        List<String> adminCommandList = List.of("off", "on", "reload");
        for (String adminCommandName : adminCommandList) {
            String permissionName = ("disco-armor.command.disco-armor.admin." + adminCommandName);
            if (!sender.hasPermission(permissionName)) {
                continue;
            }

            String messagePath = ("help-admin." + adminCommandName);
            sendMessage(sender, messagePath);
        }

        return true;
    }
}
