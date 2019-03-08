/*
  User: Cloudy
  Date: 13-Feb-19
  Time: 19:59
*/

package cz.cloudy.economysystem.locker;

import cz.cloudy.economysystem.FillUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.material.Door;

import java.util.*;

public class LockedDoor
        implements ConfigurationSerializable {
    private Block         door;
    private boolean       codeBased;
    private CodeInventory codeInventory;
    private List<String>  owners;
    private boolean       destroyProtected;

    public LockedDoor(Block door, String code, String[] owners) {
        this.door = door;
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

    public Block getDoorBlock() {
        return door;
    }

    public boolean isCodeBased() {
        return codeBased;
    }

    public CodeInventory getCodeInventory() {
        return codeInventory;
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

    public Block getTopPart() {
        Door doorData = (Door) door.getState()
                                   .getData();
        return doorData.isTopHalf() ? door : door.getRelative(BlockFace.UP);
    }

    public Block getBottomPart() {
        Door doorData = (Door) door.getState()
                                   .getData();
        return doorData.isTopHalf() ? door.getRelative(BlockFace.DOWN) : door;
    }

    public boolean isPart(Block block) {
        return block.equals(getTopPart()) || block.equals(getBottomPart());
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("door", getBottomPart().getLocation());
        data.put("code", codeInventory != null ? codeInventory.getLock() : "");
        data.put("owners", owners.toArray(new String[0]));
        data.put("protected", destroyProtected);
        return data;
    }

    public static LockedDoor deserialize(Map<String, Object> args) {
        Location location = (Location) args.get("door");
        LockedDoor lockedDoor = new LockedDoor(location.getBlock(), FillUtil.fill(args.get("code"), ""),
                                               (FillUtil.fill(args.get("owners"), new ArrayList<String>())).toArray(
                                                       new String[0]));
        lockedDoor.destroyProtected = FillUtil.fill(args.get("protected"), false);
        return lockedDoor;
    }
}
