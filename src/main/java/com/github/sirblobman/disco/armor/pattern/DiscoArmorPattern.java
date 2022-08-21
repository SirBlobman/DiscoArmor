package com.github.sirblobman.disco.armor.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.adventure.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.item.LeatherArmorBuilder;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.xseries.XMaterial;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public abstract class DiscoArmorPattern {
    private final String id;
    private final DiscoArmorPlugin plugin;

    public DiscoArmorPattern(DiscoArmorPlugin plugin, String id) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.id = Validate.notEmpty(id, "id cannot be empty or null!");
    }

    public final String getId() {
        return this.id;
    }

    public final ItemStack getMenuIcon() {
        ItemStack item = getMenuItem();
        ItemBuilder builder = (item == null ? new ItemBuilder(XMaterial.BARRIER) : new ItemBuilder(item));

        DiscoArmorPlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        MiniMessage miniMessage = languageManager.getMiniMessage();

        String displayNameString = getDisplayName();
        Component displayNameComponent = miniMessage.deserialize(displayNameString);
        String displayName = ComponentHelper.toLegacy(displayNameComponent);
        builder.withName(displayName);

        ItemFlag[] itemFlagArray = ItemFlag.values();
        builder.withFlags(itemFlagArray);
        return builder.build();
    }

    protected final DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    protected final LanguageManager getLanguageManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }

    @SuppressWarnings("deprecation")
    protected final String getArmorDisplayName(Player player) {
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMessageLegacy(player, "armor-item.display-name", null);
    }

    protected final List<String> getArmorLore(Player player) {
        LanguageManager languageManager = getLanguageManager();
        String loreUnsplit = languageManager.getMessageString(player, "armor-item.lore", null);
        String[] loreSplit = loreUnsplit.split(Pattern.quote("\n"));

        MiniMessage miniMessage = languageManager.getMiniMessage();
        List<String> lore = new ArrayList<>();
        for (String splitLine : loreSplit) {
            Component deserialize = miniMessage.deserialize(splitLine);
            lore.add(ComponentHelper.toLegacy(deserialize));
        }

        return lore;
    }

    protected final ItemStack createArmor(Player player, ArmorType armorType, Color color) {
        LeatherArmorBuilder builder = new LeatherArmorBuilder(armorType);
        builder.withColor(color);

        String displayName = getArmorDisplayName(player);
        builder.withName(displayName);

        List<String> lore = getArmorLore(player);
        builder.withLore(lore);

        DiscoArmorPlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        boolean defaultGlowing = configuration.getBoolean("glowing", false);

        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        YamlConfiguration playerData = playerDataManager.get(player);
        if(playerData.getBoolean("glowing", defaultGlowing)) {
            builder.withGlowing();
        }

        ItemMeta itemMeta = builder.getItemMeta();
        if(itemMeta != null) {
            NamespacedKey discoArmorKey = new NamespacedKey(plugin, "disco");
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            persistentDataContainer.set(discoArmorKey, PersistentDataType.BYTE, (byte) 1);
            builder.withItemMeta(itemMeta);
        }

        return builder.build();
    }

    public abstract String getDisplayName();

    protected abstract Color getNextColor(Player player);

    protected abstract ItemStack getMenuItem();

    public abstract Map<ArmorType, ItemStack> getNextArmor(Player player);
}
