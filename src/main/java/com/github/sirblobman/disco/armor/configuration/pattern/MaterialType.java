package com.github.sirblobman.disco.armor.configuration.pattern;

public enum MaterialType {
    /**
     * Default Material Type.<br/>
     * Requires a 'material' which is an XMaterial value.<br/>
     * Also loads other attributes through configuration settings such as 'quantity' and 'display-name'.<br/>
     * Example: "RED_BANNER"<br/>
     * @see com.github.sirblobman.api.shaded.xseries.XMaterial
     */
    DEFAULT,

    /**
     * Random Tag Material Type.<br/>
     * Requires a 'tag-key' which is a registry name for a tag that contains item materials.<br/>
     * Also loads other attributes through configuration settings such as 'quantity' and 'display-name'.<br/>
     * Example: "minecraft:wool"<br/>
     * @see org.bukkit.Tag
     */
    TAG_RANDOM,

    /**
     * Base64 ItemStack Material Type.<br/>
     * Requires a 'base64' which is a Base64-encoded NBT ItemStack.</br>
     * Requires BlueSlimeCore command '/item-to-base64'.<br/>
     * Does not accept added attributes through configuration settings.<br/>
     * Example: "H4sIAAAAAAAA...."<br/>
     * @see com.github.sirblobman.api.nms.ItemHandler#fromBase64String(String) 
     */
    BASE64_ITEM,

    /**
     * Mojangson NBT Material Type.<br/>
     * Requires a 'nbt-value' which is a GSON-based NBT ItemStack<br/>
     * Requires BlueSlimeCore command '/item-to-nbt'.<br/>
     * Does not accept added attributes through configuration settings.<br/>
     * Example: '{"id":"minecraft:wool","Count":"1b"}'<br/>
     * @see com.github.sirblobman.api.nms.ItemHandler#fromNBT(String) 
     */
    NBT_JSON
}
