package com.github.sirblobman.disco.armor.menu;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.AbstractPagedMenu;
import com.github.sirblobman.api.menu.button.IButton;
import com.github.sirblobman.api.menu.button.NextPageButton;
import com.github.sirblobman.api.menu.button.OpenParentMenuButton;
import com.github.sirblobman.api.menu.button.PreviousPageButton;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.menu.button.PatternButton;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;
import com.github.sirblobman.disco.armor.pattern.PatternManager;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class DiscoArmorPatternMenu extends AbstractPagedMenu<DiscoArmorPlugin> {
    private final List<DiscoArmorPattern> patternList;

    public DiscoArmorPatternMenu(@NotNull DiscoArmorMainMenu parentMenu, @NotNull DiscoArmorPlugin plugin,
                                 @NotNull Player player) {
        super(parentMenu, plugin, player);
        PatternManager patternManager = plugin.getPatternManager();
        this.patternList = patternManager.getPatterns();
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
        return 18;
    }

    @Override
    public int getMaxPages() {
        List<DiscoArmorPattern> patternList = getPatterns();
        int patternListSize = patternList.size();

        int divide = (patternListSize / 9);
        int extra = (patternListSize % 9 == 0 ? 0 : 1);
        return (divide + extra);
    }

    @Override
    public @NotNull Component getTitleFormat() {
        Player player = getPlayer();
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMessage(player, "menu-title");
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        int page = getCurrentPage();
        int maxPage = getMaxPages();

        if (slot < 9) {
            int startIndex = ((page - 1) * 9);
            int index = (startIndex + slot);

            List<DiscoArmorPattern> patternList = getPatterns();
            int patternListSize = patternList.size();
            if (index < patternListSize) {
                Player player = getPlayer();
                DiscoArmorPattern pattern = patternList.get(index);
                return pattern.getMenuIcon(player);
            }
        }

        if (slot == 13) {
            return getExitItem();
        }

        if (slot == 12 && page > 1) {
            return getPreviousPageItem();
        }

        if (slot == 14 && page < maxPage) {
            return getNextPageItem();
        }

        return getFillerItem();
    }

    @Override
    public @Nullable IButton getButton(int slot) {
        int page = getCurrentPage();
        int maxPage = getMaxPages();

        if (slot < 9) {
            int startIndex = ((page - 1) * 9);
            int index = (startIndex + slot);

            List<DiscoArmorPattern> patternList = getPatterns();
            int patternListSize = patternList.size();
            if (index < patternListSize) {
                DiscoArmorPlugin plugin = getPlugin();
                DiscoArmorPattern pattern = patternList.get(index);
                return new PatternButton(plugin, pattern);
            }
        }

        if (slot == 13) {
            return new OpenParentMenuButton<>(this);
        }

        if (slot == 12 && page > 1) {
            return new PreviousPageButton(this);
        }

        if (slot == 14 && page < maxPage) {
            return new NextPageButton(this);
        }

        return null;
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return true;
    }

    private @NotNull List<DiscoArmorPattern> getPatterns() {
        return this.patternList;
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

    private @NotNull ItemStack getExitItem() {
        return createItem(XMaterial.BARRIER, "menu-parent");
    }

    private @NotNull ItemStack getPreviousPageItem() {
        return createItem(XMaterial.PAPER, "menu-previous-page");
    }

    private @NotNull ItemStack getNextPageItem() {
        return createItem(XMaterial.PAPER, "menu-next-page");
    }

    private @NotNull ItemStack getFillerItem() {
        ItemBuilder builder = buildItem(XMaterial.GRAY_STAINED_GLASS_PANE);
        return builder.build();
    }
}
