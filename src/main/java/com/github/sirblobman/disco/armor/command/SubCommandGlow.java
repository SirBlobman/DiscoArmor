package com.github.sirblobman.disco.armor.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

final class SubCommandGlow extends PlayerCommand {
    private final DiscoArmorPlugin plugin;

    public SubCommandGlow(DiscoArmorPlugin plugin) {
        super(plugin, "glow");
        setPermissionName("disco-armor.command.disco-armor.glow");
        this.plugin = plugin;
    }

    @Override
    protected List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        boolean glowing = !configuration.getBoolean("glowing");

        configuration.set("glowing", glowing);
        playerDataManager.save(player);

        String messagePath = (glowing ? "glow.enabled" : "glow.disabled");
        sendMessage(player, messagePath);
        return true;
    }
}
