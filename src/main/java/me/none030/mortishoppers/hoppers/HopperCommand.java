package me.none030.mortishoppers.hoppers;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import me.none030.mortishoppers.MortisHoppers;
import me.none030.mortishoppers.data.HopperData;
import me.none030.mortishoppers.managers.MainManager;
import me.none030.mortishoppers.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HopperCommand implements TabExecutor {

    private final MortisHoppers plugin = MortisHoppers.getInstance();
    private final MainManager manager;

    public HopperCommand(MainManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!sender.hasPermission("mortishoppers")) {
                sender.sendMessage(MessageUtils.color("&cYou don't have permission to use this"));
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageUtils.color("&cYou cannot execute this command here"));
                return false;
            }
            Player player = (Player) sender;
            Block block = player.getTargetBlock(Collections.singleton(Material.AIR), 10);
            if (!block.getType().equals(Material.HOPPER)) {
                sender.sendMessage(MessageUtils.color("&cYou must look at a hopper before using this command"));
                return false;
            }
            if (!player.hasPermission("mortishoppers.access")) {
                if (plugin.hasTowny()) {
                    if (!PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.SWITCH)) {
                        sender.sendMessage(manager.getHopperManager().getMessage("SWITCH"));
                        return false;
                    }
                }
            }
            HopperMenu menu = new HopperMenu(manager.getHopperManager(), new HopperData(block));
            menu.open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("mortishoppers.give")) {
                sender.sendMessage(MessageUtils.color("&cYou don't have permission to use this"));
                return false;
            }
            if (args.length != 4) {
                sender.sendMessage(MessageUtils.color("&cUsage: /hopper give <player_name> <item-id> <amount>"));
                return false;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(MessageUtils.color("&cPlease enter a valid target"));
                return false;
            }
            ItemStack item = manager.getItemManager().getItem(args[2]);
            if (item == null) {
                sender.sendMessage(MessageUtils.color("&cPlease enter a valid item id"));
                return false;
            }
            int amount;
            try {
                amount = Integer.parseInt(args[3]);
            }catch (IllegalArgumentException exp) {
                sender.sendMessage(MessageUtils.color("&cPlease enter a valid amount"));
                return false;
            }
            item.setAmount(amount);
            if (target.getInventory().firstEmpty() != -1) {
                target.getInventory().addItem(item);
            }else {
                target.getWorld().dropItemNaturally(target.getLocation(), item);
            }
            sender.sendMessage(MessageUtils.color("&aItem given to the target"));
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("mortishoppers.reload")) {
                sender.sendMessage(MessageUtils.color("&cYou don't have permission to use this"));
                return false;
            }
            manager.reload();
            sender.sendMessage(MessageUtils.color("&aMortis Hoppers Reloaded"));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
           List<String> arguments = new ArrayList<>();
           arguments.add("give");
           arguments.add("reload");
           return arguments;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 3) {
                return new ArrayList<>(manager.getItemManager().getItemById().keySet());
            }
        }
        return null;
    }
}
