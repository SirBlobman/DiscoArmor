package com.github.sirblobman.disco.armor.pattern;

import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.api.shaded.xseries.XMaterial;
import com.github.sirblobman.api.shaded.xseries.XPatternType;

public final class OldGloryPattern extends DiscoArmorPattern {
    private static final Color OLD_GLORY_RED;
    private static final Color OLD_GLORY_WHITE;
    private static final Color OLD_GLORY_BLUE;

    static {
        OLD_GLORY_RED = Color.fromRGB(0xB31942);
        OLD_GLORY_WHITE = Color.fromRGB(0xFFFFFF);
        OLD_GLORY_BLUE = Color.fromRGB(0x0A3161);
    }

    public OldGloryPattern(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "old_glory");
    }

    @Override
    protected @NotNull Color getNextColor(@NotNull Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int choice = rng.nextInt(3);

        return switch (choice) {
            case 0 -> OLD_GLORY_RED;
            case 1 -> OLD_GLORY_WHITE;
            default -> OLD_GLORY_BLUE;
        };
    }

    @Override
    protected @NotNull ItemStack getMenuItem() {
        ItemStack item = XMaterial.RED_BANNER.parseItem();
        if (item == null) {
            return new ItemStack(Material.BARRIER);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof BannerMeta bannerMeta)) {
            return new ItemStack(Material.BARRIER);
        }

        Pattern smallStripesWhite = getPattern(DyeColor.WHITE, XPatternType.SMALL_STRIPES);
        Pattern squareTopLeftBlue = getPattern(DyeColor.BLUE, XPatternType.SQUARE_TOP_LEFT);
        if (smallStripesWhite == null || squareTopLeftBlue == null) {
            return new ItemStack(Material.BARRIER);
        }

        bannerMeta.addPattern(smallStripesWhite);
        bannerMeta.addPattern(squareTopLeftBlue);
        item.setItemMeta(bannerMeta);
        return item;
    }

    private @Nullable Pattern getPattern(@NotNull DyeColor color, @NotNull XPatternType patternType) {
        PatternType patternTypeBukkit = patternType.get();
        if (patternTypeBukkit == null) {
            return null;
        }

        return new Pattern(color, patternTypeBukkit);
    }
}
