package com.github.sirblobman.disco.armor.pattern;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public class OldGloryPattern extends Pattern {
    private static final Color OLD_GLORY_RED = Color.fromRGB(191, 10, 48);
    private static final Color OLD_GLORY_WHITE = Color.fromRGB(255, 255, 255);
    private static final Color OLD_GLORY_BLUE = Color.fromRGB(0, 40, 104);
    public OldGloryPattern(DiscoArmorPlugin plugin) {
        super(plugin, "old_glory");
    }

    @Override
    public String getDisplayName() {
        return "&9Old &cGlory";
    }

    @Override
    public Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int choice = rng.nextInt(3);
        switch(choice) {
            case 0: return OLD_GLORY_RED;
            case 1: return OLD_GLORY_WHITE;
            default: break;
        }

        return OLD_GLORY_BLUE;
    }

    @Override
    public Map<ArmorType, ItemStack> getNextArmor(Player player) {
        Map<ArmorType, ItemStack> armorMap = new EnumMap<>(ArmorType.class);
        Color nextColor = getNextColor(player);

        ArmorType[] armorTypeArray = ArmorType.values();
        for(ArmorType armorType : armorTypeArray) {
            ItemStack armor = createArmor(player, armorType, nextColor);
            armorMap.put(armorType, armor);
        }

        return armorMap;
    }

    @Override
    protected ItemStack getMenuItem() {
        ItemStack item = new ItemStack(Material.RED_BANNER);
        BannerMeta bannerMeta = (BannerMeta) item.getItemMeta();
        if(bannerMeta == null) throw new IllegalStateException("null banner meta!");

        org.bukkit.block.banner.Pattern whitePaly = new org.bukkit.block.banner.Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL);
        bannerMeta.addPattern(whitePaly);

        org.bukkit.block.banner.Pattern blueChiefDexterCanton = new org.bukkit.block.banner.Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_LEFT);
        bannerMeta.addPattern(blueChiefDexterCanton);

        item.setItemMeta(bannerMeta);
        return item;
    }
}