package me.none030.mortishoppers.hoppers;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import me.none030.mortishoppers.MortisHoppers;
import me.none030.mortishoppers.data.HopperData;
import me.none030.mortishoppers.utils.HopperMode;
import me.none030.mortishoppers.utils.HopperStatus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HopperListener implements Listener {

    private final MortisHoppers plugin = MortisHoppers.getInstance();
    private final HopperManager hopperManager;

    public HopperListener(HopperManager hopperManager) {
        this.hopperManager = hopperManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getHand() == null || !e.getHand().equals(EquipmentSlot.HAND) || !e.getAction().isRightClick()) {
            return;
        }
        Block block = e.getClickedBlock();
        if (block == null || !block.getType().equals(Material.HOPPER)) {
            return;
        }
        ItemStack item = e.getItem();
        if (item == null || item.getType().isAir() || !hopperManager.getSettings().isInteractive(item)) {
            return;
        }
        if (!player.hasPermission("mortishoppers.access")) {
            if (e.useInteractedBlock().equals(Event.Result.DENY) ) {
                return;
            }
            if (plugin.hasTowny()) {
                if (!PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.SWITCH)) {
                    player.sendMessage(hopperManager.getMessage("SWITCH"));
                    return;
                }
            }
        }
        e.setCancelled(true);
        HopperMenu menu = new HopperMenu(hopperManager, new HopperData(block));
        menu.open(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getHolder() instanceof HopperMenu) {
            e.setCancelled(true);
            HopperMenu menu = (HopperMenu) e.getClickedInventory().getHolder();
            menu.click(e.getRawSlot(), e.isRightClick());
            return;
        }
        if (e.getInventory().getHolder() instanceof HopperMenu) {
            e.setCancelled(true);
            HopperMenu menu = (HopperMenu) e.getInventory().getHolder();
            menu.click(e.getCurrentItem());
        }
    }

    @EventHandler
    public void onHopper(InventoryMoveItemEvent e) {
        if (!e.getDestination().getType().equals(InventoryType.HOPPER)) {
            return;
        }
        Inventory destination = e.getDestination();
        Inventory source = e.getSource();
        Location location = destination.getLocation();
        if (location == null) {
            return;
        }
        HopperData data = new HopperData(location);
        if (data.getStatus().equals(HopperStatus.DISABLED)) {
            return;
        }
        e.setCancelled(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack item : source.getContents()) {
                    if (item == null || item.getType().isAir()) {
                        continue;
                    }
                    if (data.getMode().equals(HopperMode.WHITELIST)) {
                        if (!data.canGoThrough(item)) {
                            continue;
                        }
                    } else {
                        if (data.canGoThrough(item)) {
                            continue;
                        }
                    }
                    ItemStack cloned = item.clone();
                    cloned.setAmount(1);
                    source.removeItem(cloned);
                    destination.addItem(cloned);
                    break;
                }
            }
        }.runTask(plugin);
    }
}
