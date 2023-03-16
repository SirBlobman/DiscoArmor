package com.github.sirblobman.disco.armor.command.admin;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.replacer.ComponentReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.manager.PatternManager;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;

final class SubCommandAdminOn extends Command {
    private final DiscoArmorPlugin plugin;

    public SubCommandAdminOn(DiscoArmorPlugin plugin) {
        super(plugin, "on");
        setPermissionName("disco-armor.command.disco-armor.admin.on");
        this.plugin = plugin;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            return getMatching(args[0], valueSet);
        }

        if (args.length == 2) {
            PatternManager patternManager = this.plugin.getPatternManager();
            List<String> valueList = patternManager.getPatternIds();
            return getMatching(args[1], valueList);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }

        String targetName = args[0];
        Player target = findTarget(sender, targetName);
        if (target == null) {
            return true;
        }

        String patternId = args[1].toLowerCase();
        PatternManager patternManager = this.plugin.getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternId);
        if (pattern == null) {
            Replacer replacer = new StringReplacer("{pattern}", patternId);
            sendMessage(sender, "error.invalid-pattern", replacer);
            return true;
        }

        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(target);
        configuration.set("pattern", patternId);
        playerDataManager.save(target);

        Component displayName = pattern.getDisplayName();
        Replacer patternReplacer = new ComponentReplacer("{pattern}", displayName);
        Replacer targetReplacer = new StringReplacer("{target}", targetName);
        sendMessage(sender, "command.admin.change-type", patternReplacer, targetReplacer);
        return true;
    }
}
