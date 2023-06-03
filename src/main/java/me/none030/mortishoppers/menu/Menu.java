package me.none030.mortishoppers.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Menu {

    private final Component title;
    private final HashMap<String, ItemStack> itemById;

    public Menu(Component title) {
        this.title = title;
        this.itemById = new HashMap<>();
    }

    public Component getTitle() {
        return title;
    }

    public void addItem(String id, ItemStack item) {
        itemById.put(id, item);
    }

    public ItemStack getItem(String id) {
        ItemStack item = itemById.get(id);
        if (item == null) {
            return null;
        }
        return item.clone();
    }
}
