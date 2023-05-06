package com.github.sirblobman.disco.armor.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.AbstractMenu;
import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.menu.button.ExitButton;
import com.github.sirblobman.api.menu.button.IButton;
import com.github.sirblobman.api.menu.button.OpenMenuButton;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.menu.button.DisableArmorButton;
import com.github.sirblobman.disco.armor.menu.button.ToggleGlowButton;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class DiscoArmorMainMenu extends AbstractMenu<DiscoArmorPlugin> {
    public DiscoArmorMainMenu(@NotNull DiscoArmorPlugin plugin, @NotNull Player player) {
        super(plugin, player);
    }

    @Override
    public @NotNull LanguageManager getLanguageManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }

    @Override
    public @NotNull MultiVersionHandler getMultiVersionHandler() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getMultiVersionHandler();
    }

    @Override
    public @NotNull ItemHandler getItemHandler() {
        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        return multiVersionHandler.getItemHandler();
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return switch(slot) {
            case 0 -> getDisableArmorItem();
            case 1 -> getToggleGlowItem();
            case 2 -> getSelectPatternItem();
            case 4 -> getExitItem();
            default -> getFillerItem();
        };
    }

    @Override
    public @Nullable IButton getButton(int slot) {
        return switch(slot) {
            case 0 -> new DisableArmorButton(this);
            case 1 -> new ToggleGlowButton(this);
            case 2 -> {
                Player player = getPlayer();
                DiscoArmorPlugin plugin = getPlugin();
                IMenu patternMenu = new DiscoArmorPatternMenu(this, plugin, player);
                yield new OpenMenuButton(patternMenu);
            }
            case 4 -> new ExitButton(this);
            default -> null;
        };
    }

    @Override
    public @NotNull Component getTitle() {
        Player player = getPlayer();
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMessage(player, "menu-title");
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return true;
    }

    private @NotNull ItemBuilder buildItem(@NotNull XMaterial material) {
        ItemHandler itemHandler = getItemHandler();
        ItemBuilder builder = new ItemBuilder(material);

        ItemFlag[] flags = ItemFlag.values();
        builder = builder.withFlags(flags);

        Component displayName = Component.empty();
        return builder.withName(itemHandler, displayName);
    }

    private @NotNull ItemStack createItem(@NotNull XMaterial material, @NotNull String path) {
        ItemHandler itemHandler = getItemHandler();
        ItemBuilder builder = buildItem(material);
        Player player = getPlayer();

        LanguageManager languageManager = getLanguageManager();
        Component displayName = languageManager.getMessage(player, path);
        return builder.withName(itemHandler, ComponentHelper.wrapNoItalics(displayName)).build();
    }

    private @NotNull ItemStack getDisableArmorItem() {
        return createItem(XMaterial.LEATHER_CHESTPLATE, "menu-disable");
    }

    private @NotNull ItemStack getToggleGlowItem() {
        return createItem(XMaterial.GLOWSTONE_DUST, "menu-glow");
    }

    private @NotNull ItemStack getSelectPatternItem() {
        return createItem(XMaterial.WHITE_BANNER, "menu-select-pattern");
    }

    private @NotNull ItemStack getExitItem() {
        return createItem(XMaterial.BARRIER, "menu-exit");
    }

    private @NotNull ItemStack getFillerItem() {
        ItemBuilder builder = buildItem(XMaterial.GRAY_STAINED_GLASS_PANE);
        return builder.build();
    }
}
