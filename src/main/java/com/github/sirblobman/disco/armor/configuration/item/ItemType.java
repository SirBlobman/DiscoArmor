package com.github.sirblobman.disco.armor.configuration.item;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

public enum ItemType {
    /**
     * Default Item Type.<br/>
     * Requires a 'id' which is a minecraft item value.<br/>
     * Also loads other attributes through configuration settings, such as 'quantity' and 'display-name'.<br/>
     * Example: "minecraft:red_banner"<br/>
     * @see org.bukkit.Material
     */
    ITEM,

    /**
     * Random Tag Item Type.<br/>
     * Requires a 'tag' which is a registry name for a tag that contains item materials.<br/>
     * Also loads other attributes through configuration settings such as 'quantity' and 'display-name'.<br/>
     * Example: "minecraft:wool"<br/>
     * @see org.bukkit.Tag
     */
    TAG,

    /**
     * Base64 Item Type.<br/>
     * Requires a 'base64' which is a Base64-encoded NBT ItemStack.</br>
     * Requires BlueSlimeCore command '/item-to-base64'.<br/>
     * Does not accept added attributes through configuration settings.<br/>
     * Example: "H4sIAAAAAAAA...."<br/>
     * @see com.github.sirblobman.api.nms.ItemHandler#fromBase64String(String) 
     */
    BASE64,

    /**
     * Mojangson NBT Item Type.<br/>
     * Requires a 'nbt' which is a GSON-based NBT ItemStack<br/>
     * Requires BlueSlimeCore command '/item-to-nbt'.<br/>
     * Does not accept added attributes through configuration settings.<br/>
     * Example: '{"id":"minecraft:wool","Count":"1b"}'<br/>
     * @see com.github.sirblobman.api.nms.ItemHandler#fromNBT(String) 
     */
    NBT_JSON,

    /**
     * Bukkit YAML Item Type
     * Requires an 'item' which is a YAML-formatted serialization of an ItemStack.
     * Does not accept added attributes through configuration settings.<br/>
     * @see ItemStack#deserialize(Map)
     * @see ItemStack#serialize()
     */
    YAML
}
