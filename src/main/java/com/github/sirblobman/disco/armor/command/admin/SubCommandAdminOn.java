package com.github.sirblobman.disco.armor.command.admin;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.replacer.ComponentReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;
import com.github.sirblobman.disco.armor.pattern.PatternManager;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class SubCommandAdminOn extends Command {
    private final DiscoArmorPlugin plugin;

    public SubCommandAdminOn(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "on");
        setPermissionName("disco-armor.command.disco-armor.admin.on");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            return getMatching(args[0], valueSet);
        }

        if (args.length == 2) {
            PatternManager patternManager = getPatternManager();
            List<String> valueList = patternManager.getPatternIds();
            return getMatching(args[1], valueList);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length < 2) {
            return false;
        }

        String targetName = args[0];
        Player target = findTarget(sender, targetName);
        if (target == null) {
            return true;
        }

        String patternId = args[1].toLowerCase();
        PatternManager patternManager = getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternId);
        if (pattern == null) {
            Replacer replacer = new StringReplacer("{pattern}", patternId);
            sendMessage(sender, "error.invalid-pattern", replacer);
            return true;
        }

        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(target);
        configuration.set("pattern", patternId);
        playerDataManager.save(target);

        Component displayName = pattern.getDisplayName();
        Replacer patternReplacer = new ComponentReplacer("{pattern}", displayName);
        Replacer targetReplacer = new StringReplacer("{target}", targetName);
        sendMessage(sender, "command.admin.change-type", patternReplacer, targetReplacer);
        return true;
    }

    private @NotNull DiscoArmorPlugin getDiscoArmorPlugin() {
        return this.plugin;
    }

    private @NotNull PatternManager getPatternManager() {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        return plugin.getPatternManager();
    }

    private @NotNull PlayerDataManager getPlayerDataManager() {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        return plugin.getPlayerDataManager();
    }
}
