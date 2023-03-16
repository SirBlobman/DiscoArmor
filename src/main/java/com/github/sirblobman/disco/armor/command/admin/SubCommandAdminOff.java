package com.github.sirblobman.disco.armor.command.admin;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

final class SubCommandAdminOff extends Command {
    private final DiscoArmorPlugin plugin;

    public SubCommandAdminOff(DiscoArmorPlugin plugin) {
        super(plugin, "off");
        setPermissionName("disco-armor.command.disco-armor.admin.off");
        this.plugin = plugin;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            return getMatching(args[0], valueSet);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }

        String targetName = args[0];
        Player target = findTarget(sender, targetName);
        if (target == null) {
            return true;
        }

        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(target);
        configuration.set("pattern", null);
        playerDataManager.save(target);

        Replacer replacer = new StringReplacer("{target}", targetName);
        sendMessage(sender, "command.admin.disabled", replacer);
        return true;
    }
}
