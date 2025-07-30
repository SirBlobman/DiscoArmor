package com.github.sirblobman.disco.armor.configuration.pattern.custom;

import org.jetbrains.annotations.NotNull;

import org.bukkit.inventory.EquipmentSlot;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

public enum ArmorSlot {
    HELMET(EquipmentSlot.HEAD, XMaterial.LEATHER_HELMET),
    CHESTPLATE(EquipmentSlot.CHEST, XMaterial.LEATHER_CHESTPLATE),
    LEGGINGS(EquipmentSlot.LEGS, XMaterial.LEATHER_LEGGINGS),
    BOOTS(EquipmentSlot.FEET, XMaterial.LEATHER_BOOTS);

    private final EquipmentSlot equipmentSlot;
    private final XMaterial material;

    ArmorSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull XMaterial material) {
        this.equipmentSlot = equipmentSlot;
        this.material = material;
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    public @NotNull XMaterial getMaterial() {
        return this.material;
    }
}
