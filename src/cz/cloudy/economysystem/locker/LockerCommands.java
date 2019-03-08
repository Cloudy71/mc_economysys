/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 16:49
*/

package cz.cloudy.economysystem.locker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockerCommands
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (label.equals("lock")) {
            if (args.length > 1) {
                return false;
            }

            Block block = player.getTargetBlockExact(4);
            if (block.getType() != Material.CHEST && !block.getType()
                                                           .toString()
                                                           .contains("_DOOR")) {
                player.sendMessage(ChatColor.AQUA + "Only chests and doors can be locked!");
                return true;
            }

            if (block.getType() == Material.CHEST) {
                Chest chest = (Chest) block.getState();
                if (Locker.lockChest(chest, args.length == 1 ? args[0] : "", player)) {
                    player.sendMessage(ChatColor.AQUA + "Chest was successfully locked!");
                } else {
                    player.sendMessage(
                            ChatColor.AQUA + "Current chest couldn't be locked because it's already locked!");
                }
            } else if (block.getType()
                            .toString()
                            .contains("_DOOR")) {
                if (Locker.lockDoor(block, args.length == 1 ? args[0] : "", player)) {
                    player.sendMessage(ChatColor.AQUA + "Door was successfully locked!");
                } else {
                    player.sendMessage(ChatColor.AQUA + "Current door couldn't be locked because it's already locked!");
                }
            }
            return true;
        } else if (label.equals("addowner")) {
            if (args.length != 1) {
                return false;
            }

            Player selectedPlayer = Bukkit.getPlayer(args[0]);
            if (selectedPlayer == null) {
                player.sendMessage(ChatColor.AQUA + "Invalid player.");
                return true;
            }

            Block block = player.getTargetBlockExact(4);
            if (block.getType() != Material.CHEST && !block.getType()
                                                           .toString()
                                                           .contains("_DOOR")) {
                player.sendMessage(ChatColor.AQUA + "Only works on chests and doors!");
                return true;
            }

            if (block.getType() == Material.CHEST) {
                Chest chest = (Chest) block.getState();
                LockedChest lockedChest = Locker.getLockedChest(chest);
                if (lockedChest == null) {
                    player.sendMessage(ChatColor.AQUA + "Only works on locked chests!");
                    return true;
                }
                if (!lockedChest.getOwners()
                                .contains(player.getUniqueId()
                                                .toString())) {
                    player.sendMessage("You're not chest's owner!");
                    return true;
                }
                if (Locker.addChestOwner(chest, selectedPlayer)) {
                    player.sendMessage(ChatColor.AQUA + "New owner has been added!");
                } else {
                    player.sendMessage(ChatColor.AQUA + "Chest isn't locked or selected player is already owner.");
                }
            } else if (block.getType()
                            .toString()
                            .contains("_DOOR")) {
                LockedDoor lockedDoor = Locker.getLockedDoor(block);
                if (lockedDoor == null) {
                    player.sendMessage(ChatColor.AQUA + "Only works on locked doors!");
                    return true;
                }
                if (!lockedDoor.getOwners()
                               .contains(player.getUniqueId()
                                               .toString())) {
                    player.sendMessage("You're not chest's owner!");
                    return true;
                }
                if (Locker.addDoorOwner(block, selectedPlayer)) {
                    player.sendMessage(ChatColor.AQUA + "New owner has been added!");
                } else {
                    player.sendMessage(ChatColor.AQUA + "Chest isn't locked or selected player is already owner.");
                }
            }
            return true;
        } else if (label.equals("protect")) {
            Block block = player.getTargetBlockExact(4);
            if (block.getType() == Material.CHEST) {
                LockedChest lockedChest = Locker.getLockedChest((Chest) block.getState());
                if (lockedChest != null && !lockedChest.isDestroyProtected() && lockedChest.getOwners()
                                                                                           .contains(
                                                                                                   player.getUniqueId()
                                                                                                         .toString())) {
                    lockedChest.setDestroyProtected(true);
                    player.sendMessage(ChatColor.AQUA + "Your chest is now protected!");
                } else {
                    player.sendMessage(ChatColor.RED + "Chest couldn't be protected.");
                }
            } else if (block.getType()
                            .toString()
                            .contains("_DOOR")) {
                LockedDoor lockedDoor = Locker.getLockedDoor(block);
                if (lockedDoor != null && !lockedDoor.isDestroyProtected() && lockedDoor.getOwners()
                                                                                        .contains(player.getUniqueId()
                                                                                                        .toString())) {
                    lockedDoor.setDestroyProtected(true);
                    player.sendMessage(ChatColor.AQUA + "Your door is now protected!");
                } else {
                    player.sendMessage(ChatColor.RED + "Door couldn't be protected.");
                }
            }
            Locker.save();
            return true;
        }
        return false;
    }
}
