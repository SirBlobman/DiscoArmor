package com.github.sirblobman.disco.armor.pattern;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.item.LeatherArmorBuilder;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.configuration.DiscoArmorConfiguration;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public abstract class DiscoArmorPattern {
    private final String id;
    private final DiscoArmorPlugin plugin;

    private transient ItemBuilder itemBuilder;

    public DiscoArmorPattern(@NotNull DiscoArmorPlugin plugin, @NotNull String id) {
        this.plugin = plugin;
        this.id = id;
        this.itemBuilder = null;
    }

    protected final @NotNull DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    protected final @NotNull LanguageManager getLanguageManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }

    protected final @NotNull MultiVersionHandler getMultiVersionHandler() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getMultiVersionHandler();
    }

    protected final @NotNull ItemHandler getItemHandler() {
        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        return multiVersionHandler.getItemHandler();
    }

    public final @NotNull String getId() {
        return this.id;
    }

    private @NotNull ItemBuilder getItemBuilder() {
        if (this.itemBuilder != null) {
            return this.itemBuilder;
        }

        ItemStack baseItem = getMenuItem();
        ItemBuilder builder = new ItemBuilder(baseItem);

        ItemFlag[] flags = ItemFlag.values();
        this.itemBuilder = builder.withFlags(flags);
        return this.itemBuilder;
    }

    public final @NotNull ItemStack getMenuIcon(@NotNull Player player) {
        DiscoArmorPlugin plugin = getPlugin();
        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();

        Component displayName = getDisplayName(player);
        ItemBuilder builder = new ItemBuilder(getItemBuilder().build());
        builder = builder.withName(itemHandler, displayName);
        return builder.build();
    }

    protected final Component getArmorDisplayName(Player player) {
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMessage(player, "armor-item.display-name");
    }

    protected final List<Component> getArmorLore(Player player) {
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMessageList(player, "armor-item.lore");
    }

    protected final ItemStack createArmor(Player player, ArmorType armorType, Color color) {
        ItemHandler itemHandler = getItemHandler();
        LeatherArmorBuilder builder = new LeatherArmorBuilder(armorType);
        builder.withColor(color);

        Component displayName = getArmorDisplayName(player);
        builder.withName(itemHandler, displayName);

        List<Component> lore = getArmorLore(player);
        builder.withLore(itemHandler, lore);

        DiscoArmorPlugin plugin = getPlugin();
        DiscoArmorConfiguration configuration = plugin.getConfiguration();
        boolean defaultGlowing = configuration.isGlowing();

        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        YamlConfiguration playerData = playerDataManager.get(player);
        if (playerData.getBoolean("glowing", defaultGlowing)) {
            builder.withGlowing();
        }

        ItemMeta itemMeta = builder.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey discoArmorKey = new NamespacedKey(plugin, "disco");
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            persistentDataContainer.set(discoArmorKey, PersistentDataType.BYTE, (byte) 1);
            builder.withItemMeta(itemMeta);
        }

        return builder.build();
    }

    public @NotNull Map<ArmorType, ItemStack> getNextArmor(@NotNull Player player) {
        Map<ArmorType, ItemStack> armorMap = new EnumMap<>(ArmorType.class);
        ArmorType[] armorTypeArray = ArmorType.values();

        for (ArmorType armorType : armorTypeArray) {
            Color nextColor = getNextColor(player);
            ItemStack armor = createArmor(player, armorType, nextColor);
            armorMap.put(armorType, armor);
        }

        return Collections.unmodifiableMap(armorMap);
    }

    public @NotNull Component getDisplayName(@NotNull Player player) {
        String id = getId();
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMessage(player, "pattern." + id);
    }

    protected abstract @NotNull Color getNextColor(@NotNull Player player);

    protected abstract @NotNull ItemStack getMenuItem();
}
