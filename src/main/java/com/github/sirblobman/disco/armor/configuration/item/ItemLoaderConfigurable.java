package com.github.sirblobman.disco.armor.configuration.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.ShieldMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.tag.DamageTypeTags;
import org.bukkit.util.NumberConversions;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

import com.github.sirblobman.api.plugin.IMultiVersionPlugin;
import com.github.sirblobman.api.utility.ConfigurationHelper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ItemLoaderConfigurable extends ItemLoader {
    private final MiniMessage miniMessage;

    public ItemLoaderConfigurable(@NotNull IMultiVersionPlugin plugin) {
        super(plugin);
        this.miniMessage = MiniMessage.miniMessage();
    }

    private @NotNull MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    @Override
    public @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String itemId = section.getString("id", "minecraft:barrier");
        NamespacedKey itemIdKey = NamespacedKey.fromString(itemId);
        if (itemIdKey == null) {
            logger.warning("Invalid item id key format '" + itemId + "'.");
            return null;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull ItemType> registry = registryAccess.getRegistry(RegistryKey.ITEM);
        ItemType itemType = registry.get(itemIdKey);
        if (itemType == null) {
            logger.warning("Unknown or unregistered item id key '" + itemId + "'.");
            return null;
        }

        int quantity = section.getInt("quantity", 1);
        ItemStack itemStack = itemType.createItemStack(quantity);

        // Common Item Meta
        loadAttributeModifiers(itemStack, section);
        loadEnchantments(itemStack, section);
        loadItemFlags(itemStack, section);
        loadHideTooltip(itemStack, section);
        loadTooltipStyle(itemStack, section);
        loadDisplayName(itemStack, section);
        loadItemName(itemStack, section);
        loadLore(itemStack, section);
        loadItemModel(itemStack, section);
        loadCustomModelData(itemStack, section);
        loadGlowing(itemStack, section);
        loadUnbreakable(itemStack, section);
        loadGlider(itemStack, section);
        loadDamageResistances(itemStack, section);
        loadMaxStackSize(itemStack, section);
        loadRarity(itemStack, section);
        loadEnchantable(itemStack, section);

        // Component Meta
        loadUseCooldownComponent(itemStack, section);
        loadFoodComponent(itemStack, section);
        loadToolComponent(itemStack, section);
        loadEquippableComponent(itemStack, section);
        loadJukeboxPlayableComponent(itemStack, section);

        // Other Meta
        loadArmorMeta(itemStack, section);
        loadAxolotlBucketMeta(itemStack, section);
        loadBannerMeta(itemStack, section);
        loadBlockDataMeta(itemStack, section);
        loadBlockStateMeta(itemStack, section);
        loadBookMeta(itemStack, section);
        loadBundleMeta(itemStack, section);
        loadColorableArmorMeta(itemStack, section);
        loadCompassMeta(itemStack, section);
        loadCrossbowMeta(itemStack, section);
        loadDamageableMeta(itemStack, section);
        loadEnchantmentStorageMeta(itemStack, section);
        loadFireworkEffectMeta(itemStack, section);
        loadFireworkMeta(itemStack, section);
        loadKnowledgeBookMeta(itemStack, section);
        loadLeatherArmorMeta(itemStack, section);
        loadMapMeta(itemStack, section);
        loadMusicInstrumentMeta(itemStack, section);
        loadOminousBottleMeta(itemStack, section);
        loadPotionMeta(itemStack, section);
        loadRepairableMeta(itemStack, section);
        loadShieldMeta(itemStack, section);
        loadSkullMeta(itemStack, section);
        loadSpawnEggMeta(itemStack, section);
        loadSuspiciousStewMeta(itemStack, section);
        loadTropicalFishBucketMeta(itemStack, section);
        loadWritableBookMeta(itemStack, section);

        return itemStack;
    }

    private void loadAttributeModifiers(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        ConfigurationSection attributeModifiersSection = section.getConfigurationSection("attribute-modifiers");
        if (attributeModifiersSection == null) {
            return;
        }

        Set<String> attributeModifierConfigurationKeySet = attributeModifiersSection.getKeys(false);
        for (String configurationKey : attributeModifierConfigurationKeySet) {
            ConfigurationSection attributeModifierSection = attributeModifiersSection.getConfigurationSection(configurationKey);
            if (attributeModifierSection == null) {
                continue;
            }

            loadAttributeModifier(itemMeta, attributeModifierSection);
        }

        itemStack.setItemMeta(itemMeta);
    }

    private void loadAttributeModifier(@NotNull ItemMeta meta, @NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String attributeId = section.getString("attribute", "minecraft:scale");
        NamespacedKey attributeIdKey = NamespacedKey.fromString(attributeId);
        if (attributeIdKey == null) {
            logger.info("Invalid attribute id format '" + attributeId + "'.");
            return;
        }

        Attribute attribute = Registry.ATTRIBUTE.get(attributeIdKey);
        if (attribute == null) {
            logger.info("Unknown or unregistered attribute id '" + attributeId + "'.");
            return;
        }

        String attributeKeyString = section.getString("id", "custom:custom");
        NamespacedKey attributeKey = NamespacedKey.fromString(attributeKeyString);
        if (attributeKey == null) {
            logger.info("Invalid attribute id format '" + attributeKeyString + "'.");
            return;
        }

        double value = section.getDouble("value", 0.0D);
        AttributeModifier.Operation operation = loadAttributeModifierOperation(section);
        EquipmentSlotGroup slotGroup = loadAttributeModifierEquipmentSlotGroup(section);
        AttributeModifier attributeModifier = new AttributeModifier(attributeKey, value, operation, slotGroup);
        meta.addAttributeModifier(attribute, attributeModifier);
    }

    @SuppressWarnings("UnstableApiUsage")
    private @NotNull EquipmentSlotGroup loadAttributeModifierEquipmentSlotGroup(@NotNull ConfigurationSection section) {
        String slotGroupName = section.getString("slot-group");
        if (slotGroupName == null || slotGroupName.isBlank()) {
            return EquipmentSlotGroup.ANY;
        }

        EquipmentSlotGroup byName = EquipmentSlotGroup.getByName(slotGroupName);
        return (byName == null ? EquipmentSlotGroup.ANY : byName);
    }

    private @NotNull AttributeModifier.Operation loadAttributeModifierOperation(@NotNull ConfigurationSection section) {
        String operationName = section.getString("operation", "ADD_NUMBER");
        AttributeModifier.Operation defaultValue = AttributeModifier.Operation.ADD_NUMBER;
        return ConfigurationHelper.parseEnum(AttributeModifier.Operation.class, operationName, defaultValue);
    }

    private void loadEnchantments(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");
        if (enchantmentsSection == null) {
            return;
        }

        Set<String> enchantmentConfigurationKeySet = enchantmentsSection.getKeys(false);
        for (String configurationKey : enchantmentConfigurationKeySet) {
            ConfigurationSection enchantmentSection = enchantmentsSection.getConfigurationSection(configurationKey);
            if (enchantmentSection == null) {
                continue;
            }

            loadEnchantment(itemMeta, section);
        }

        itemStack.setItemMeta(itemMeta);
    }

    private void loadEnchantment(@NotNull ItemMeta itemMeta, @NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String enchantmentKeyString = section.getString("id", "minecraft:sharpness");
        NamespacedKey enchantmentKey = NamespacedKey.fromString(enchantmentKeyString);
        if (enchantmentKey == null) {
            logger.warning("Invalid enchantment id format '" + enchantmentKeyString + "'.");
            return;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull Enchantment> registry = registryAccess.getRegistry(RegistryKey.ENCHANTMENT);
        Enchantment enchantment = registry.get(enchantmentKey);
        if (enchantment == null) {
            logger.warning("Unknown or unregistered enchantment id '" + enchantmentKeyString + "'.");
            return;
        }

        if (itemMeta.hasConflictingEnchant(enchantment)) {
            logger.warning("This item has an enchantment that conflicts with '" + enchantmentKeyString + "'.");
        }

        int level = section.getInt("level");
        boolean unsafe = section.getBoolean("unsafe", false);
        boolean enchant = itemMeta.addEnchant(enchantment, level, unsafe);
        if (!enchant) {
            logger.info("Failed to enchant with enchantment '"
                    + enchantmentKeyString + "' and level '" + level + "'.");
        }
    }

    private void loadItemFlags(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        List<String> flagNameList = section.getStringList("flags");
        Set<ItemFlag> itemFlagSet = ConfigurationHelper.parseEnums(flagNameList, ItemFlag.class);
        ItemFlag[] itemFlagArray = itemFlagSet.toArray(ItemFlag[]::new);

        itemMeta.addItemFlags(itemFlagArray);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadHideTooltip(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        boolean hideTooltip = section.getBoolean("hide-tooltip", false);
        itemMeta.setHideTooltip(hideTooltip);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadTooltipStyle(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        String tooltipStyleKeyString = section.getString("tooltip-style");
        if (tooltipStyleKeyString == null || tooltipStyleKeyString.isBlank()) {
            return;
        }

        NamespacedKey tooltipStyleKey = NamespacedKey.fromString(tooltipStyleKeyString);
        if (tooltipStyleKey == null) {
            getLogger().warning("Invalid tooltip style key format '" + tooltipStyleKeyString + "'.");
            return;
        }

        itemMeta.setTooltipStyle(tooltipStyleKey);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadDisplayName(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("display-name")) {
            return;
        }

        String displayNameString = section.getString("display-name");
        if (displayNameString == null || displayNameString.isBlank()) {
            return;
        }

        Component displayName = parseComponent(displayNameString);
        itemMeta.displayName(displayName);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadItemName(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("item-name")) {
            return;
        }

        String itemNameString = section.getString("item-name");
        if (itemNameString == null || itemNameString.isBlank()) {
            return;
        }

        Component itemName = parseComponent(itemNameString);
        itemMeta.itemName(itemName);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    private @NotNull Component parseComponent(@NotNull String text) {
        if (text.contains("&")) {
            LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();
            return legacy.deserialize(text);
        }

        if (text.contains("\u00A7")) {
            LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();
            return legacy.deserialize(text);
        }

        MiniMessage miniMessage = getMiniMessage();
        return miniMessage.deserialize(text);
    }

    private @NotNull List<Component> parseComponents(@NotNull List<String> textList) {
        int textListSize = textList.size();
        List<Component> componentList = new ArrayList<>(textListSize);
        for (String text : textList) {
            Component component = parseComponent(text);
            componentList.add(component);
        }

        return componentList;
    }

    private void loadLore(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("lore")) {
            return;
        }

        List<String> loreStringList = section.getStringList("lore");
        if (loreStringList.isEmpty()) {
            return;
        }

        List<Component> loreList = parseComponents(loreStringList);
        itemMeta.lore(loreList);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadItemModel(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        String itemModelKeyString = section.getString("item-model");
        if (itemModelKeyString == null || itemModelKeyString.isBlank()) {
            return;
        }

        NamespacedKey itemModelKey = NamespacedKey.fromString(itemModelKeyString);
        if (itemModelKey == null) {
            getLogger().warning("Invalid item model key format '" + itemModelKeyString + "'.");
            return;
        }

        itemMeta.setItemModel(itemModelKey);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadCustomModelData(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        ConfigurationSection customModelDataSection = section.getConfigurationSection("custom-model-data");
        if (customModelDataSection == null) {
            return;
        }

        List<String> strings = customModelDataSection.getStringList("strings");
        List<Float> floats = customModelDataSection.getFloatList("floats");
        List<Boolean> flags = customModelDataSection.getBooleanList("flags");

        CustomModelDataComponent customModelDataComponent = itemMeta.getCustomModelDataComponent();
        customModelDataComponent.setStrings(strings);
        customModelDataComponent.setFloats(floats);
        customModelDataComponent.setFlags(flags);

        List<?> unknownColorList = customModelDataSection.getList("colors");
        if (unknownColorList != null) {
            List<Color> colors = parseColors(unknownColorList);
            customModelDataComponent.setColors(colors);
        }

        itemMeta.setCustomModelDataComponent(customModelDataComponent);
        itemStack.setItemMeta(itemMeta);
    }

    private @NotNull List<Color> parseColors(@NotNull List<?> unknownList) {
        int unknownListSize = unknownList.size();
        List<Color> colors = new ArrayList<>(unknownListSize);

        for (Object unknownObject : unknownList) {
            if (unknownObject instanceof Color color) {
                colors.add(color);
            }

            if (unknownObject instanceof Integer integer) {
                Color color = Color.fromARGB(integer);
                colors.add(color);
            }

            if (unknownObject instanceof String string) {
                Color color = parseColor(string);
                if (color != null) {
                    colors.add(color);
                }
            }
        }

        return colors;
    }

    private @Nullable Color parseColor(@NotNull String string) {
        String[] split = string.split(Pattern.quote(";"), 3);
        if (split.length != 4) {
            return null;
        }

        String alphaString = split[0];
        String redString = split[1];
        String greenString = split[2];
        String blueString = split[3];

        try {
            int alpha = Integer.parseInt(alphaString);
            int red = Integer.parseInt(redString);
            int green = Integer.parseInt(greenString);
            int blue = Integer.parseInt(blueString);

            if (alpha < 0 || red < 0 || green < 0 || blue < 0) {
                throw new NumberFormatException("< 0");
            }

            if (alpha > 255 || red > 255 || green > 255 || blue > 255) {
                throw new NumberFormatException("> 255");
            }

            return Color.fromARGB(alpha, red, green, blue);
        } catch (NumberFormatException ex) {
            Logger logger = getLogger();
            logger.warning("Failed to parse color from string '" + string + "'.");
            logger.warning("Colors should be in format 'alpha;red;green;blue' with integers between 0 and 255");
            logger.log(Level.WARNING, "Full Exception:", ex);
            return null;
        }
    }

    private void loadGlowing(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("glowing")) {
            itemMeta.setEnchantmentGlintOverride(null);
        } else {
            boolean glowing = section.getBoolean("glowing", false);
            itemMeta.setEnchantmentGlintOverride(glowing);
        }

        itemStack.setItemMeta(itemMeta);
    }

    private void loadUnbreakable(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        boolean unbreakable = section.getBoolean("unbreakable", false);
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadGlider(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        boolean glider = section.getBoolean("glider", false);
        itemMeta.setGlider(glider);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadDamageResistances(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        String damageResistanceKeyString = section.getString("damage-resistance");
        if (damageResistanceKeyString == null || damageResistanceKeyString.isBlank()) {
            return;
        }

        NamespacedKey damageResistanceKey = NamespacedKey.fromString(damageResistanceKeyString);
        if (damageResistanceKey == null) {
            getLogger().warning("Invalid damage resistance tag key format '" + damageResistanceKeyString + "'.");
            return;
        }

        Tag<DamageType> damageTypeTag = Bukkit.getTag(DamageTypeTags.REGISTRY_DAMAGE_TYPES, damageResistanceKey, DamageType.class);
        if (damageTypeTag == null) {
            getLogger().warning("Unknown or unregistered damage resistance tag key '" + damageResistanceKeyString + "'.");
            return;
        }

        itemMeta.setDamageResistant(damageTypeTag);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadMaxStackSize(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("max-stack-size")) {
            return;
        }

        int currentMax = itemStack.getMaxStackSize();
        int maxStackSize = section.getInt("max-stack-size", currentMax);
        itemMeta.setMaxStackSize(maxStackSize);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadRarity(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("rarity")) {
            return;
        }

        String rarityName = section.getString("rarity", "COMMON");
        ItemRarity rarity = ConfigurationHelper.parseEnum(ItemRarity.class, rarityName, ItemRarity.COMMON);
        itemMeta.setRarity(rarity);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadEnchantable(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("enchantable")) {
            return;
        }

        int defaultEnchantable = itemMeta.getEnchantable();
        int enchantable = section.getInt("enchantable", defaultEnchantable);
        itemMeta.setEnchantable(enchantable);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadUseCooldownComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.use-cooldown")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.use-cooldown");
        if (componentSection == null) {
            return;
        }

        UseCooldownComponent useCooldownComponent = itemMeta.getUseCooldown();

        if (componentSection.isSet("group")) {
            String groupKeyString = componentSection.getString("group");
            if (groupKeyString != null) {
                NamespacedKey groupKey = NamespacedKey.fromString(groupKeyString);
                if (groupKey != null) {
                    useCooldownComponent.setCooldownGroup(groupKey);
                }
            }
        }

        if (componentSection.isSet("seconds")) {
            float seconds = getFloat(componentSection, "seconds");
            useCooldownComponent.setCooldownSeconds(seconds);
        }

        itemMeta.setUseCooldown(useCooldownComponent);
        itemStack.setItemMeta(itemMeta);
    }

    private float getFloat(@NotNull ConfigurationSection section, @NotNull String path) {
        Object object = section.get(path, 0.0F);
        return (object instanceof Number) ? NumberConversions.toFloat(object) : 0.0F;
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadFoodComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.food")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.food");
        if (componentSection == null) {
            return;
        }

        boolean canAlwaysEat = componentSection.getBoolean("can-always-eat", false);
        int nutrition = componentSection.getInt("nutrition", 0);
        float saturation = getFloat(componentSection, "saturation");

        FoodComponent foodComponent = itemMeta.getFood();
        foodComponent.setCanAlwaysEat(canAlwaysEat);
        foodComponent.setNutrition(nutrition);
        foodComponent.setSaturation(saturation);

        itemMeta.setFood(foodComponent);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadToolComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.tool")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.tool");
        if (componentSection == null) {
            return;
        }

        int damagePerBlock = componentSection.getInt("damage-per-block");
        float defaultMiningSpeed = getFloat(componentSection, "default-mining-speed");

        ToolComponent toolComponent = itemMeta.getTool();
        toolComponent.setDamagePerBlock(damagePerBlock);
        toolComponent.setDefaultMiningSpeed(defaultMiningSpeed);

        if (componentSection.isSet("rules")) {
            List<ToolComponent.ToolRule> toolRuleList = parseRulesList(componentSection.getList("rules"));
            toolComponent.setRules(toolRuleList);
        }

        itemMeta.setTool(toolComponent);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private List<ToolComponent.ToolRule> parseRulesList(@Nullable List<?> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        int listSize = list.size();
        List<ToolComponent.ToolRule> toolRuleList = new ArrayList<>(listSize);

        for (Object object : list) {
            if (object instanceof ToolComponent.ToolRule toolRule) {
                toolRuleList.add(toolRule);
            }
        }

        return toolRuleList;
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadEquippableComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.equippable")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.equippable");
        if (componentSection == null) {
            return;
        }

        EquippableComponent equippableComponent = itemMeta.getEquippable();
        // TODO
//        equippableComponent.setAllowedEntities(allowedEntityList);
//        equippableComponent.setDispensable(dispensable);
//        equippableComponent.setSlot(equipmentSlot);
//        equippableComponent.setCameraOverlay(cameraOverlayKey);
//        equippableComponent.setDamageOnHurt(damageOnHurt);
//        equippableComponent.setEquipOnInteract(equipOnInteract);
//        equippableComponent.setEquipSound(equipSound);
//        equippableComponent.setModel(modelKey);
//        equippableComponent.setSwappable(swappable);

        itemMeta.setEquippable(equippableComponent);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadJukeboxPlayableComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.jukebox-playable")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.jukebox-playable");
        if (componentSection == null) {
            return;
        }

        // TODO
        JukeboxPlayableComponent jukeboxPlayableComponent = itemMeta.getJukeboxPlayable();
//        jukeboxPlayableComponent.setSongKey(songKey);

        itemMeta.setJukeboxPlayable(jukeboxPlayableComponent);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadArmorMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof ArmorMeta armorMeta)) {
            return;
        }

        if (!section.isSet("armor-trim")) {
            return;
        }

        ConfigurationSection armorTrimSection = section.getConfigurationSection("armor-trim");
        if (armorTrimSection == null) {
            return;
        }

        String trimMaterialKeyString = armorTrimSection.getString("material", "minecraft:amethyst");
        String trimPatternKeyString = armorTrimSection.getString("pattern", "minecraft:eye");
        NamespacedKey trimMaterialKey = NamespacedKey.fromString(trimMaterialKeyString);
        NamespacedKey trimPatternKey = NamespacedKey.fromString(trimPatternKeyString);
        Logger logger = getLogger();

        if (trimMaterialKey == null) {
            logger.warning("Invalid trim material key '" + trimMaterialKeyString + "'.");
            return;
        }

        if (trimPatternKey == null) {
            logger.warning("Invalid trim pattern key '" + trimPatternKeyString + "'.");
            return;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull TrimMaterial> registryMaterial = registryAccess.getRegistry(RegistryKey.TRIM_MATERIAL);
        Registry<@NotNull TrimPattern> registryPattern = registryAccess.getRegistry(RegistryKey.TRIM_PATTERN);

        TrimMaterial trimMaterial = registryMaterial.get(trimMaterialKey);
        if (trimMaterial == null) {
            logger.warning("Unknown trim material with key '" + trimMaterialKeyString + "'.");
            return;
        }

        TrimPattern trimPattern = registryPattern.get(trimPatternKey);
        if (trimPattern == null) {
            logger.warning("Unknown trim pattern with key '" + trimPatternKeyString + "'.");
            return;
        }

        ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);
        armorMeta.setTrim(armorTrim);
        itemStack.setItemMeta(armorMeta);
    }

    private void loadAxolotlBucketMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            return;
        }

        if (!section.isSet("axolotl-variant")) {
            return;
        }

        String variantName = section.getString("axolotl-variant", "WILD");
        Axolotl.Variant variant = ConfigurationHelper.parseEnum(Axolotl.Variant.class, variantName, Axolotl.Variant.WILD);
        axolotlBucketMeta.setVariant(variant);

        itemStack.setItemMeta(axolotlBucketMeta);
    }

    private void loadBannerMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BannerMeta bannerMeta)) {
            return;
        }

        if (!section.isSet("banner")) {
            return;
        }

        // TODO

        itemStack.setItemMeta(bannerMeta);
    }

    private void loadBlockDataMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BlockDataMeta blockDataMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(blockDataMeta);
    }

    private void loadBlockStateMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BlockStateMeta blockStateMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(blockStateMeta);
    }

    private void loadBookMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BookMeta bookMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(bookMeta);
    }

    private void loadBundleMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BundleMeta bundleMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(bundleMeta);
    }

    private void loadColorableArmorMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof ColorableArmorMeta colorableArmorMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(colorableArmorMeta);
    }

    private void loadCompassMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof CompassMeta compassMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(compassMeta);
    }

    private void loadCrossbowMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof CrossbowMeta crossbowMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(crossbowMeta);
    }

    private void loadDamageableMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof Damageable damageable)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(damageable);
    }

    private void loadEnchantmentStorageMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof EnchantmentStorageMeta enchantmentStorageMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(enchantmentStorageMeta);
    }

    private void loadFireworkEffectMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof FireworkEffectMeta fireworkEffectMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(fireworkEffectMeta);
    }

    private void loadFireworkMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof FireworkMeta fireworkMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(fireworkMeta);
    }

    private void loadKnowledgeBookMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof KnowledgeBookMeta knowledgeBookMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(knowledgeBookMeta);
    }

    private void loadLeatherArmorMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof LeatherArmorMeta leatherArmorMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(leatherArmorMeta);
    }

    private void loadMapMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof MapMeta mapMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(mapMeta);
    }

    private void loadMusicInstrumentMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof MusicInstrumentMeta musicInstrumentMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(musicInstrumentMeta);
    }

    private void loadOminousBottleMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof OminousBottleMeta ominousBottleMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(ominousBottleMeta);
    }

    private void loadPotionMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof PotionMeta potionMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(potionMeta);
    }

    private void loadRepairableMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof Repairable repairable)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(repairable);
    }

    private void loadShieldMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof ShieldMeta shieldMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(shieldMeta);
    }

    private void loadSkullMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(skullMeta);
    }

    private void loadSpawnEggMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof SpawnEggMeta spawnEggMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(spawnEggMeta);
    }

    private void loadSuspiciousStewMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof SuspiciousStewMeta suspiciousStewMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(suspiciousStewMeta);
    }

    private void loadTropicalFishBucketMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof TropicalFishBucketMeta tropicalFishBucketMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(tropicalFishBucketMeta);
    }

    private void loadWritableBookMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof WritableBookMeta writableBookMeta)) {
            return;
        }

        // TODO

        itemStack.setItemMeta(writableBookMeta);
    }
}
