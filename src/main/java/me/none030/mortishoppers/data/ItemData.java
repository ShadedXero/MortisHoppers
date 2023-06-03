package me.none030.mortishoppers.data;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class ItemData extends Data {

    private final ItemMeta meta;

    public ItemData(@NotNull ItemMeta meta) {
        super(meta.getPersistentDataContainer());
        this.meta = meta;
    }

    public ItemMeta getMeta() {
        return meta;
    }
}
