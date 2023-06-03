package me.none030.mortishoppers.hoppers;

import me.none030.mortishoppers.data.FilterData;
import me.none030.mortishoppers.data.HopperData;
import me.none030.mortishoppers.utils.HopperMode;
import me.none030.mortishoppers.utils.HopperStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HopperMenu implements InventoryHolder {

    private final HopperManager hopperManager;
    private final HopperData data;
    private final Inventory menu;
    private final int statusSlot = 11;
    private final int modeSlot = 15;
    private final List<Integer> filterSlots = List.of(28,29,30,31,32,33,34,37,38,39,40,41,42,43);

    public HopperMenu(HopperManager hopperManager, HopperData data) {
        this.hopperManager = hopperManager;
        this.data = data;
        this.menu = getMenu();
        update();
    }

    private Inventory getMenu() {
        Inventory menu = Bukkit.createInventory(this, 54, hopperManager.getMenu().getTitle());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, hopperManager.getMenu().getItem("FILTER"));
        }
        return menu;
    }

    private void update() {
        updateOptions();
        updateFilter();
    }

    private void updateOptions() {
        if (data.getStatus().equals(HopperStatus.DISABLED)) {
            menu.setItem(statusSlot, hopperManager.getMenu().getItem("DISABLED"));
        }else {
            menu.setItem(statusSlot, hopperManager.getMenu().getItem("ENABLED"));
        }
        if (data.getMode().equals(HopperMode.WHITELIST)) {
            menu.setItem(modeSlot, hopperManager.getMenu().getItem("WHITELIST"));
        }else {
            menu.setItem(modeSlot, hopperManager.getMenu().getItem("BLACKLIST"));
        }
    }

    private void updateFilter() {
        for (int filterSlot : filterSlots) {
            menu.setItem(filterSlot, new ItemStack(Material.AIR));
        }
        List<ItemStack> filter = data.getFilter();
        for (int i = 0; i < filter.size(); i++) {
            ItemStack item = filter.get(i);
            if (item == null) {
                return;
            }
            FilterData filterData = new FilterData(item.getItemMeta());
            ItemStack original = filterData.getOriginal();
            if (original == null) {
                continue;
            }
            if (filterData.isExact()) {
                menu.setItem(filterSlots.get(i), hopperManager.getSettings().addLore(original));
            }else {
                menu.setItem(filterSlots.get(i), original);
            }
        }
    }

    public void click(int slot, boolean right) {
        if (slot == statusSlot) {
            if (data.getStatus().equals(HopperStatus.DISABLED)) {
                data.setStatus(HopperStatus.ENABLED);
            }else {
                data.setStatus(HopperStatus.DISABLED);
            }
            updateOptions();
        }
        if (slot == modeSlot) {
            if (data.getMode().equals(HopperMode.WHITELIST)) {
                data.setMode(HopperMode.BLACKLIST);
            }else {
                data.setMode(HopperMode.WHITELIST);
            }
            updateOptions();
        }
        int filterPosition = getFilterPosition(slot);
        if (filterPosition == -1) {
            return;
        }
        List<ItemStack> filter = data.getFilter();
        if (filterPosition >= filter.size()) {
            return;
        }
        if (right) {
            ItemStack filterItem = filter.get(filterPosition);
            FilterData filterData = new FilterData(filterItem.getItemMeta());
            filterData.setExact(!filterData.isExact());
            filterItem.setItemMeta(filterData.getMeta());
            filter.set(filterPosition, filterItem);
        }else {
            filter.remove(filterPosition);
        }
        data.setFilter(filter);
        updateFilter();
    }

    public void click(ItemStack currentItem) {
        if (currentItem == null || currentItem.getType().isAir()) {
            return;
        }
        ItemStack cloned = currentItem.clone();
        cloned.setAmount(1);
        List<ItemStack> filter = data.getFilter();
        int filterLimit = 14;
        if (filter.size() >= filterLimit) {
            return;
        }
        if (data.hasFilter(currentItem)) {
            return;
        }
        FilterData filterData = new FilterData(cloned.getItemMeta());
        filterData.create(cloned, false);
        cloned.setItemMeta(filterData.getMeta());
        filter.add(cloned);
        data.setFilter(filter);
        updateFilter();
    }

    private int getFilterPosition(int slot) {
        return filterSlots.indexOf(slot);
    }

    public void open(Player player) {
        player.openInventory(menu);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public HopperManager getHopperManager() {
        return hopperManager;
    }

    public HopperData getData() {
        return data;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return menu;
    }

    public int getStatusSlot() {
        return statusSlot;
    }

    public int getModeSlot() {
        return modeSlot;
    }

    public List<Integer> getFilterSlots() {
        return filterSlots;
    }
}
