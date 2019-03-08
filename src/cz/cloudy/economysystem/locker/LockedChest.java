/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 15:50
*/

package cz.cloudy.economysystem.locker;

import cz.cloudy.economysystem.FillUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class LockedChest
        implements ConfigurationSerializable {
    private Chest[]       chests;
    private boolean       codeBased;
    private CodeInventory codeInventory;
    private List<String>  owners;
    private boolean       destroyProtected;

    public LockedChest(Chest[] chests, String code, String[] owners) {
        this.chests = chests;
        this.owners = new LinkedList<>();
        this.owners.addAll(Arrays.asList(owners));
        this.codeBased = false;
        if (code.length() != 0) {
            this.codeInventory = new CodeInventory(code);
            this.codeInventory.resetInventory();
            this.codeBased = true;
        }
        this.destroyProtected = false;
    }

    public Chest[] getChests() {
        return chests;
    }

    public CodeInventory getCodeInventory() {
        return codeInventory;
    }

    public boolean isCodeBased() {
        return codeBased;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setDestroyProtected(boolean destroyProtected) {
        this.destroyProtected = destroyProtected;
    }

    public boolean isDestroyProtected() {
        return destroyProtected;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        Location[] locs = new Location[chests.length];
        for (int i = 0; i < locs.length; i++) {
            locs[i] = chests[i].getLocation();
        }
        data.put("chests", locs);
        data.put("code", codeInventory != null ? codeInventory.getLock() : "");
        data.put("owners", owners.toArray(new String[0]));
        data.put("protected", destroyProtected);
        return data;
    }

    public static LockedChest deserialize(Map<String, Object> args) {
        Location[] locations = ((ArrayList<Location>) args.get("chests")).toArray(new Location[0]);
        Chest[] chests = new Chest[locations.length];
        for (int i = 0; i < chests.length; i++) {
            Block block = locations[i].getBlock();
            if (block.getType() != Material.CHEST) {
                return null;
            }
            chests[i] = (Chest) block.getState();
        }
        LockedChest lockedChest = new LockedChest(chests, FillUtil.fill(args.get("code"), ""),
                                                  (FillUtil.fill(args.get("owners"), new ArrayList<String>())).toArray(
                                                          new String[0]));
        lockedChest.destroyProtected = FillUtil.fill(args.get("protected"), false);
        return lockedChest;
    }
}
