package me.none030.mortishoppers.data;

import com.jeff_media.customblockdata.CustomBlockData;
import me.none030.mortishoppers.MortisHoppers;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public abstract class BlockData extends Data {

    private final Location core;

    public BlockData(@NotNull Location core) {
        super(new CustomBlockData(core.getBlock(), MortisHoppers.getInstance()));
        this.core = core;
    }

    public BlockData(@NotNull Block block) {
        super(new CustomBlockData(block, MortisHoppers.getInstance()));
        this.core = block.getLocation();
    }

    public Location getCore() {
        return core;
    }
}
