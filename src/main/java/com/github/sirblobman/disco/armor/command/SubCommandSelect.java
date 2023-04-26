package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.replacer.ComponentReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;
import com.github.sirblobman.disco.armor.pattern.PatternManager;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class SubCommandSelect extends PlayerCommand {
    private final DiscoArmorPlugin plugin;

    public SubCommandSelect(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "select");
        setPermissionName("disco-armor.command.disco-armor.select");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        if (args.length == 1) {
            PatternManager patternManager = getPatternManager();
            List<String> valueList = patternManager.getPatternIds();
            return getMatching(args[0], valueList);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull Player player, String @NotNull [] args) {
        if (args.length < 1) {
            return false;
        }

        String patternId = args[0].toLowerCase();
        PatternManager patternManager = getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternId);
        if (pattern == null) {
            Replacer replacer = new StringReplacer("{pattern}", patternId);
            sendMessage(player, "error.invalid-pattern", replacer);
            return true;
        }

        String permissionName = ("disco-armor.pattern." + patternId);
        if (!player.hasPermission(permissionName)) {
            Replacer replacer = new StringReplacer("{pattern}", patternId);
            sendMessage(player, "error.no-pattern-permission", replacer);
            return true;
        }

        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", patternId);
        playerDataManager.save(player);

        Component displayName = pattern.getDisplayName();
        Replacer replacer = new ComponentReplacer("{pattern}", displayName);
        sendMessage(player, "command.change-type", replacer);
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
