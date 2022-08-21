package com.github.sirblobman.disco.armor.pattern;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class OldGloryPattern extends DiscoArmorPattern {
    private final Color oldGloryRed, oldGloryWhite, oldGloryBlue;

    public OldGloryPattern(DiscoArmorPlugin plugin) {
        super(plugin, "old_glory");
        this.oldGloryRed = Color.fromRGB(0xB31942);
        this.oldGloryWhite = Color.fromRGB(0xFFFFFF);
        this.oldGloryBlue = Color.fromRGB(0x0A3161);
    }

    @Override
    public String getDisplayName() {
        return "<gradient:#B31942:#FFFFFF:#0A3161>Old Glory</gradient>";
    }

    @Override
    protected Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int choice = rng.nextInt(3);

        return switch(choice) {
            case 0 -> this.oldGloryRed;
            case 1 -> this.oldGloryWhite;
            default -> this.oldGloryBlue;
        };
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
        if(bannerMeta == null) {
            throw new IllegalStateException("null banner meta!");
        }

        Pattern whitePaly = new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL);
        bannerMeta.addPattern(whitePaly);

        Pattern blueChiefDexterCanton = new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_LEFT);
        bannerMeta.addPattern(blueChiefDexterCanton);

        item.setItemMeta(bannerMeta);
        return item;
    }
}
