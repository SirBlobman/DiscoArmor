package com.github.sirblobman.disco.armor.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.manager.PatternManager;
import com.github.sirblobman.disco.armor.menu.DiscoArmorMenu;
import com.github.sirblobman.disco.armor.pattern.Pattern;

public class CommandDiscoArmor extends PlayerCommand {
    private final DiscoArmorPlugin plugin;
    public CommandDiscoArmor(DiscoArmorPlugin plugin) {
        super(plugin, "disco-armor");
        this.plugin = plugin;
    }

    @Override
    public LanguageManager getLanguageManager() {
        return this.plugin.getLanguageManager();
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        if(args.length == 1) {
            List<String> valueList = Arrays.asList("on", "off", "glow", "select", "reload");
            return getMatching(valueList, args[0]);
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("select")) {
            PatternManager patternManager = this.plugin.getPatternManager();
            List<String> valueList = patternManager.getPatternIds();
            return getMatching(valueList, args[1]);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if(args.length < 1) return showCommandUsage(player);

        String sub = args[0].toLowerCase();
        String[] newArgs = (args.length < 2 ? new String[0] : Arrays.copyOfRange(args, 1, args.length));
        switch(sub) {
            case "on": return openMenu(player);
            case "off": return disableArmor(player);
            case "glow": return toggleGlow(player);
            case "select": return selectCommand(player, newArgs);
            case "reload": return reloadCommand(player);
            default: break;
        }

        return showCommandUsage(player);
    }

    private boolean showCommandUsage(Player player) {
        LanguageManager languageManager = getLanguageManager();
        languageManager.sendMessage(player, "command.usage", null, true);
        return true;
    }

    private boolean reloadCommand(Player player) {
        if(!checkPermission(player, "disco-armor.reload", true)) return true;
        this.plugin.reloadConfig();

        LanguageManager languageManager = getLanguageManager();
        languageManager.sendMessage(player, "command.reload-sucess", null, true);
        return true;
    }

    private boolean openMenu(Player player) {
        DiscoArmorMenu discoArmorMenu = new DiscoArmorMenu(this.plugin, player);
        discoArmorMenu.open();
        return true;
    }

    private boolean disableArmor(Player player) {
        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", null);
        playerDataManager.save(player);
        return true;
    }

    private boolean toggleGlow(Player player) {
        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        boolean glowing = !configuration.getBoolean("glowing");
        configuration.set("glowing", glowing);
        playerDataManager.save(player);

        String messagePath = (glowing ? "glow.enabled" : "glow.disabled");
        LanguageManager languageManager = getLanguageManager();
        languageManager.sendMessage(player, messagePath, null, true);
        return true;
    }

    private boolean selectCommand(Player player, String[] args) {
        if(args.length < 1) return false;
        String patternId = args[0].toLowerCase();

        LanguageManager languageManager = getLanguageManager();
        PatternManager patternManager = this.plugin.getPatternManager();
        Pattern pattern = patternManager.getPattern(patternId);
        if(pattern == null) {
            Replacer replacer = message -> message.replace("{pattern}", patternId);
            languageManager.sendMessage(player, "error.invalid-pattern", replacer, true);
            return true;
        }

        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", patternId);
        playerDataManager.save(player);

        Replacer replacer = message -> message.replace("{pattern}", MessageUtility.color(pattern.getDisplayName()));
        languageManager.sendMessage(player, "command.change-type", replacer, true);
        return true;
    }
}