/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 15:43
*/

package cz.cloudy.economysystem.locker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CodeInventory {
    private Inventory inventory;
    private String    lock;

    public CodeInventory(String code) {
        this.inventory = Bukkit.createInventory(null, 18, "Code lock");
        this.lock = code;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void resetInventory() {
        inventory.clear();
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemStack(Material.ICE));
        }
    }

    public String getLock() {
        return lock;
    }
}
