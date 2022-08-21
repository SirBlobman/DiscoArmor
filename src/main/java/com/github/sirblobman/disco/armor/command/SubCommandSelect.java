package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.language.SimpleReplacer;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.manager.PatternManager;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;

public final class SubCommandSelect extends PlayerCommand {
    private final DiscoArmorPlugin plugin;

    public SubCommandSelect(DiscoArmorPlugin plugin) {
        super(plugin, "select");
        this.plugin = plugin;
    }

    @Override
    protected List<String> onTabComplete(Player player, String[] args) {
        if(args.length == 1) {
            PatternManager patternManager = this.plugin.getPatternManager();
            List<String> valueList = patternManager.getPatternIds();
            return getMatching(args[0], valueList);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if(args.length < 1) {
            return false;
        }

        String patternId = args[0].toLowerCase();
        PatternManager patternManager = this.plugin.getPatternManager();
        DiscoArmorPattern pattern = patternManager.getPattern(patternId);
        if(pattern == null) {
            Replacer replacer = new SimpleReplacer("{pattern}", patternId);
            sendMessage(player, "error.invalid-pattern", replacer);
            return true;
        }

        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", patternId);
        playerDataManager.save(player);

        String displayName = pattern.getDisplayName();
        String displayNameColored = MessageUtility.color(displayName);

        Replacer replacer = new SimpleReplacer("{pattern}", displayNameColored);
        sendMessage(player, "command.change-type", replacer);
        return true;
    }
}
