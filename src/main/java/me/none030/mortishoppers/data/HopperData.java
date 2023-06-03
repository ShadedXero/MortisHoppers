package me.none030.mortishoppers.data;

import me.none030.mortishoppers.utils.HopperMode;
import me.none030.mortishoppers.utils.HopperStatus;
import me.none030.mortishoppers.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HopperData extends BlockData {

    private final String statusKey = "MortisHoppersStatus";
    private final String modeKey = "MortisHoppersMode";
    private final String filterKey = "MortisHoppersFilter";

    public HopperData(@NotNull Location core) {
        super(core);
    }

    public HopperData(@NotNull Block block) {
        super(block);
    }

    public void create(HopperStatus status, HopperMode mode, List<ItemStack> filter) {
        setStatus(status);
        setMode(mode);
        setFilter(filter);
    }

    public void setStatus(HopperStatus status) {
        if (status != null) {
            set(statusKey, status.name());
        }else {
            set(statusKey, null);
        }
    }

    public HopperStatus getStatus() {
        String statusType = get(statusKey);
        if (statusType == null) {
            return HopperStatus.DISABLED;
        }
        HopperStatus status;
        try {
            status = HopperStatus.valueOf(statusType);
        }catch (IllegalArgumentException exp) {
            return HopperStatus.DISABLED;
        }
        return status;
    }

    public void setMode(HopperMode mode) {
        if (mode != null) {
            set(modeKey, mode.name());
        }else {
            set(modeKey, null);
        }
    }

    public HopperMode getMode() {
        String modeType = get(modeKey);
        if (modeType == null) {
            return HopperMode.WHITELIST;
        }
        HopperMode mode;
        try {
            mode = HopperMode.valueOf(modeType);
        }catch (IllegalArgumentException exp) {
            return HopperMode.WHITELIST;
        }
        return mode;
    }

    public void setFilter(List<ItemStack> filter) {
        if (filter == null || filter.size() == 0) {
            set(filterKey, null);
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < filter.size(); i++) {
            ItemStack item = filter.get(i);
            if (item == null) {
                continue;
            }
            String rawItem = ItemUtils.serialize(item);
            builder.append(rawItem);
            if ((i + 1) != filter.size()) {
                builder.append(",");
            }
        }
        set(filterKey, builder.toString());
    }

    public List<ItemStack> getFilter() {
        String rawFilter = get(filterKey);
        if (rawFilter == null) {
            return new ArrayList<>();
        }
        String[] rawData = rawFilter.split(",");
        if (rawData.length == 0) {
            return new ArrayList<>();
        }
        List<ItemStack> filter = new ArrayList<>();
        boolean change = false;
        for (String raw : rawData) {
            ItemStack item = ItemUtils.deserialize(raw);
            if (item == null || item.getType().isAir()) {
                change = true;
               continue;
            }
            filter.add(item);
        }
        if (change) {
            setFilter(filter);
        }
        return filter;
    }

    public List<ItemStack> getOriginalFilter() {
        List<ItemStack> originals = new ArrayList<>();
        for (ItemStack filterItem : getFilter()) {
            FilterData filterData = new FilterData(filterItem.getItemMeta());
            ItemStack original = filterData.getOriginal();
            originals.add(original);
        }
        return originals;
    }

    public boolean hasFilter(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        for (ItemStack original : getOriginalFilter()) {
            if (item.isSimilar(original)) {
                return true;
            }
        }
        return false;
    }

    public boolean canGoThrough(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        for (ItemStack filter : getFilter()) {
            FilterData data = new FilterData(filter.getItemMeta());
            ItemStack original = data.getOriginal();
            if (data.isExact()) {
                if (item.isSimilar(original)) {
                    return true;
                }
            }else {
                if (item.getType().equals(original.getType())) {
                    return true;
                }
            }
        }
        return false;
    }
}
