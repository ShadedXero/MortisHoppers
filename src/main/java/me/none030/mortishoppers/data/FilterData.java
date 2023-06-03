package me.none030.mortishoppers.data;

import me.none030.mortishoppers.MortisHoppers;
import me.none030.mortishoppers.utils.ItemUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class FilterData extends ItemData {

    private final MortisHoppers plugin = MortisHoppers.getInstance();
    private final String originalKey = "MortisHoppersOriginal";
    private final String exactKey = "MortisHoppersExact";

    public FilterData(@NotNull ItemMeta meta) {
        super(meta);
    }

    public void create(ItemStack original, boolean exact) {
        setOriginal(original);
        setExact(exact);
    }

    public void setOriginal(ItemStack original) {
        if (original != null) {
            set(originalKey, ItemUtils.serialize(original));
        }else {
            set(originalKey, null);
        }
    }

    public ItemStack getOriginal() {
        String data = get(originalKey);
        if (data == null) {
            return null;
        }
        return ItemUtils.deserialize(data);
    }

    public void setExact(boolean exact) {
        if (exact) {
            getContainer().set(new NamespacedKey(plugin, exactKey), PersistentDataType.BYTE, (byte) 1);
        }else {
            getContainer().set(new NamespacedKey(plugin, exactKey), PersistentDataType.BYTE, (byte) 0);
        }
    }

    public boolean isExact() {
        Byte value = getContainer().get(new NamespacedKey(plugin, exactKey), PersistentDataType.BYTE);
        if (value == null) {
            return false;
        }
        if (value == 0) {
            return false;
        }
        return value == 1;
    }
}
