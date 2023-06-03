package me.none030.mortishoppers.hoppers;

import me.none030.mortishoppers.utils.ItemEditor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HopperSettings {

    private final List<ItemStack> interactive;
    private final List<String> exactLore;

    public HopperSettings(List<ItemStack> interactive, List<String> exactLore) {
        this.interactive = interactive;
        this.exactLore = exactLore;
    }

    public boolean isInteractive(ItemStack item) {
        for (ItemStack interactiveItem : interactive) {
            if (interactiveItem.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack addLore(ItemStack item) {
        ItemEditor editor = new ItemEditor(item);
        for (String line: exactLore) {
            editor.addLore(line);
        }
        return editor.getItem();
    }

    public List<ItemStack> getInteractive() {
        return interactive;
    }

    public List<String> getExactLore() {
        return exactLore;
    }
}
