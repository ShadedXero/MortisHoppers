package me.none030.mortishoppers.data;

import me.none030.mortishoppers.MortisHoppers;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class Data {

    private final MortisHoppers plugin = MortisHoppers.getInstance();
    private final PersistentDataContainer container;

    public Data(@NotNull PersistentDataContainer container) {
        this.container = container;
    }

    public void set(String key, String value) {
        if (key == null) {
            return;
        }
        if (value == null) {
            remove(key);
            return;
        }
        container.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
    }

    public String get(String key) {
        if (key == null) {
            return null;
        }
        return container.get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
    }

    public void remove(String key) {
        if (key == null) {
            return;
        }
        container.remove(new NamespacedKey(plugin, key));
    }

    public void clear() {
        container.getKeys().clear();
    }

    public PersistentDataContainer getContainer() {
        return container;
    }
}
