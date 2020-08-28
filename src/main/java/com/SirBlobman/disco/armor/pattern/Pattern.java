package com.SirBlobman.disco.armor.pattern;

import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.SirBlobman.api.configuration.ConfigurationManager;
import com.SirBlobman.api.configuration.PlayerDataManager;
import com.SirBlobman.api.item.ArmorType;
import com.SirBlobman.api.item.ItemBuilder;
import com.SirBlobman.api.item.LeatherArmorBuilder;
import com.SirBlobman.api.nms.ItemHandler;
import com.SirBlobman.api.nms.MultiVersionHandler;
import com.SirBlobman.api.utility.MessageUtility;
import com.SirBlobman.api.utility.Validate;
import com.SirBlobman.api.xseries.XMaterial;
import com.SirBlobman.disco.armor.DiscoArmorPlugin;

public abstract class Pattern {
    private final String id;
    private final DiscoArmorPlugin plugin;
    public Pattern(DiscoArmorPlugin plugin, String id) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.id = Validate.notEmpty(id, "id cannot be empty or null!");
    }

    public DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    public final String getId() {
        return this.id;
    }

    public final ItemStack createArmor(Player player, ArmorType armorType, Color color) {
        DiscoArmorPlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        LeatherArmorBuilder builder = new LeatherArmorBuilder(armorType).withColor(color);

        String displayName = configuration.getString("display-name");
        if(displayName != null) {
            String displayNameColored = MessageUtility.color(displayName);
            builder.withName(displayNameColored);
        }

        List<String> loreList = configuration.getStringList("lore");
        if(!loreList.isEmpty()) {
            List<String> loreListColored = MessageUtility.colorList(loreList);
            builder.withLore(loreListColored);
        }

        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        YamlConfiguration playerData = playerDataManager.get(player);
        if(playerData.getBoolean("glowing")) builder.withGlowing();

        ItemStack item = builder.build();
        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        return itemHandler.setCustomNBT(item, "disco", "armor");
    }

    public final ItemStack getMenuIcon() {
        ItemStack item = getMenuItem();
        ItemBuilder builder = (item == null ? new ItemBuilder(XMaterial.BARRIER) : new ItemBuilder(item));

        String displayName = getDisplayName();
        String displayNameColored = MessageUtility.color(displayName);
        builder.withName(displayNameColored);

        ItemFlag[] itemFlagArray = ItemFlag.values();
        builder.withFlags(itemFlagArray);
        return builder.build();
    }

    public abstract String getDisplayName();
    public abstract Color getNextColor(Player player);
    public abstract Map<ArmorType, ItemStack> getNextArmor(Player player);
    protected abstract ItemStack getMenuItem();
}
