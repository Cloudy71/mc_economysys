/*
  User: Cloudy
  Date: 20-Mar-19
  Time: 22:03
*/

package cz.cloudy.economysystem.items;

import cz.cloudy.economysystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class ItemsModifier {
    private static Random random = new Random();

    private static Map<Player, Integer> randomBlinksTasks = new LinkedHashMap<>();

    public static void changeArmorColor(Player player, Color color) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = new ItemStack[] {
                inventory.getHelmet(),
                inventory.getChestplate(),
                inventory.getLeggings(),
                inventory.getBoots()
        };
        for (ItemStack item : items) {
            if (item == null) continue;
            if (item.getType()
                    .toString()
                    .contains("LEATHER_")) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
                leatherArmorMeta.setColor(color);
                item.setItemMeta(leatherArmorMeta);
            }
        }
    }

    public static void stopRandomBlinkingTask(Player player) {
        if (!randomBlinksTasks.containsKey(player)) return;
        Bukkit.getServer()
              .getScheduler()
              .cancelTask(randomBlinksTasks.get(player));
        randomBlinksTasks.remove(player);
    }

    public static void setRandomBlinkingTask(Player player, int interval) {
        stopRandomBlinkingTask(player);
        randomBlinksTasks.put(player, Bukkit.getServer()
                                            .getScheduler()
                                            .scheduleSyncRepeatingTask(Main.instance, () -> {
                                                changeArmorColor(player,
                                                                 Color.fromRGB(random.nextInt(255), random.nextInt(255),
                                                                               random.nextInt(255)));
                                            }, 0, interval));
    }
}
