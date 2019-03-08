/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 16:03
*/

package cz.cloudy.economysystem.locker;

import cz.cloudy.economysystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("Duplicates")
public class LockerListener
        implements Listener {
    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!event.hasBlock()) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block.getType() != Material.CHEST && !block.getType()
                                                       .toString()
                                                       .contains("_DOOR")) {
            return;
        }

        if (block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            LockedChest lockedChest = Locker.getLockedChest(chest);
            if (lockedChest == null) {
                return;
            }

            if (!lockedChest.getOwners()
                            .contains(event.getPlayer()
                                           .getUniqueId()
                                           .toString())) {
                event.setCancelled(true);
                if (lockedChest.isCodeBased()) {
                    lockedChest.getCodeInventory()
                               .resetInventory();
                    event.getPlayer()
                         .openInventory(lockedChest.getCodeInventory()
                                                   .getInventory());
                }
            }
        } else if (block.getType()
                        .toString()
                        .contains("_DOOR")) {
            LockedDoor lockedDoor = Locker.getLockedDoor(block);
            if (lockedDoor == null) {
                return;
            }

            if (!lockedDoor.getOwners()
                           .contains(event.getPlayer()
                                          .getUniqueId()
                                          .toString())) {
                event.setCancelled(true);
                if (lockedDoor.isCodeBased()) {
                    lockedDoor.getCodeInventory()
                              .resetInventory();
                    event.getPlayer()
                         .openInventory(lockedDoor.getCodeInventory()
                                                  .getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
//        if (!event.getWhoClicked()
//                  .getUniqueId().toString()
//                  .equals("Cloudy69")) {
//            return;
//        }

        for (LockedChest lockedChest : Locker.getLockedChests()) {
            if (lockedChest.isCodeBased() && event.getInventory()
                                                  .equals(lockedChest.getCodeInventory()
                                                                     .getInventory())) {
                if (!event.getClickedInventory()
                          .equals(event.getInventory()) ||
                    event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    event.setCancelled(true);
                    return;
                }

                Bukkit.getServer()
                      .getScheduler()
                      .scheduleSyncDelayedTask(Main.instance, new Runnable() {
                          @Override
                          public void run() {
                              int correct = 0;
                              int maxCorrect = 0;
                              for (int i = 0; i < 9; i++) {
                                  String k = lockedChest.getCodeInventory()
                                                        .getLock()
                                                        .substring(i, i + 1);
                                  ItemStack itemStack = event.getInventory()
                                                             .getItem(9 + i);
                                  if (k.equals("0") && itemStack != null) {
                                      event.getWhoClicked()
                                           .closeInventory();
                                      return;
                                  }
                                  if (k.equals("1")) {
                                      maxCorrect++;
                                      if (itemStack != null) {
                                          correct++;
                                      }
                                  }
                              }

                              if (maxCorrect == correct) {
                                  event.getWhoClicked()
                                       .openInventory(lockedChest.getChests()[0].getBlockInventory());
                              }
                          }
                      }, 5);
            }
        }
        // TODO: Make for locked doors.
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        if (event.getBlock()
                 .getType() == Material.CHEST) {
            LockedChest lockedChest = Locker.getLockedChest((Chest) event.getBlock()
                                                                         .getState());
            if (lockedChest != null && lockedChest.isDestroyProtected()) {
                event.setCancelled(true);
            }
        } else if (event.getBlock()
                        .getType()
                        .toString()
                        .contains("_DOOR")) {
            LockedDoor lockedDoor = Locker.getLockedDoor(event.getBlock());
            if (lockedDoor != null && lockedDoor.isDestroyProtected()) {
                event.setCancelled(true);
            }
        }
    }
}
