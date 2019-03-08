/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 15:50
*/

package cz.cloudy.economysystem.locker;

import cz.cloudy.economysystem.Main;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Locker {
    private static List<LockedChest> lockedChests;
    private static List<LockedDoor>  lockedDoors;

    static {
        lockedChests = new LinkedList<>();
        lockedDoors = new LinkedList<>();
    }

    public static void initialize() {
        List<?> data = Main.instance.getConfig()
                                    .getList("locked-chests");
        if (data != null) {
            lockedChests.addAll((List<LockedChest>) data);
        }

        data = Main.instance.getConfig()
                            .getList("locked-doors");
        if (data != null) {
            lockedDoors.addAll((List<LockedDoor>) data);
        }

        System.out.println("Locked chests initialized");
        save();
    }

    public static void save() {
        while (lockedChests.contains(null)) {
            lockedChests.remove(null);
        }
        while (lockedDoors.contains(null)) {
            lockedDoors.remove(null);
        }

        Main.instance.getConfig()
                     .set("locked-chests", lockedChests);
        Main.instance.getConfig()
                     .set("locked-doors", lockedDoors);
        Main.instance.saveConfig();
    }

    public static LockedChest getLockedChest(Chest chest) {
        for (LockedChest lockedChest : lockedChests) {
            if (lockedChest.getChests()[0].equals(chest) ||
                (lockedChest.getChests().length > 1 && lockedChest.getChests()[1].equals(chest))) {
                return lockedChest;
            }
        }
        return null;
    }

    public static LockedDoor getLockedDoor(Block block) {
        if (!block.getType()
                  .toString()
                  .contains("_DOOR")) {
            return null;
        }

        for (LockedDoor lockedDoor : lockedDoors) {
            if (lockedDoor.isPart(block)) {
                return lockedDoor;
            }
        }
        return null;
    }

    private static Chest[] getChests(Chest chest) {
        Chest[] chests = new Chest[] {chest};
        if (chest.getInventory()
                 .getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) chest.getInventory()
                                                         .getHolder();
            Chest left = (Chest) doubleChest.getLeftSide();
            Chest right = (Chest) doubleChest.getRightSide();
            chests = new Chest[] {
                    left,
                    right
            };
        }
        return chests;
    }

    public static boolean lockChest(Chest chest, String code, Player owner) {
        if (getLockedChest(chest) != null) {
            return false;
        }
        Chest[] chests = getChests(chest);
        LockedChest lockedChest = new LockedChest(chests, code, new String[] {owner.getUniqueId().toString()});
        lockedChests.add(lockedChest);
        save();
        return true;
    }

    public static boolean addChestOwner(Chest chest, Player owner) {
        LockedChest lockedChest = getLockedChest(chest);
        if (lockedChest == null || lockedChest.getOwners()
                                              .contains(owner.getUniqueId()
                                                             .toString())) {
            return false;
        }
        lockedChest.getOwners()
                   .add(owner.getUniqueId()
                             .toString());
        save();
        return true;
    }

    public static boolean lockDoor(Block block, String code, Player owner) {
        if (getLockedDoor(block) != null) {
            return false;
        }

        LockedDoor lockedDoor = new LockedDoor(block, code, new String[] {owner.getUniqueId().toString()});
        lockedDoors.add(lockedDoor);
        save();
        return true;
    }

    public static boolean addDoorOwner(Block block, Player owner) {
        LockedDoor lockedDoor = getLockedDoor(block);
        if (lockedDoor == null || lockedDoor.getOwners()
                                            .contains(owner.getUniqueId().toString())) {
            return false;
        }
        lockedDoor.getOwners()
                  .add(owner.getUniqueId().toString());
        save();
        return true;
    }

    public static List<LockedChest> getLockedChests() {
        return lockedChests;
    }

    public static List<LockedDoor> getLockedDoors() {
        return lockedDoors;
    }
}
