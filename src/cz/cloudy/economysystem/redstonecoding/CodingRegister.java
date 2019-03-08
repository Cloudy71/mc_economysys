/*
  User: Cloudy
  Date: 02-Mar-19
  Time: 17:07
*/

// TODO: BUG: When running signs from lever on wall, its ran only when wall lever is used, later its not checking its power
//  in other conditions.

package cz.cloudy.economysystem.redstonecoding;

import cz.cloudy.economysystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;

import java.util.LinkedList;
import java.util.List;

public class CodingRegister {
    private static List<Sign> signList;
    private static List<Sign> signTempIgnore;

    static {
        signList = new LinkedList<>();
        signTempIgnore = new LinkedList<>();
    }

    public static void initialize() {
        List<?> data = Main.instance.getConfig()
                                    .getList("code-signs");
        if (data != null) {
            List<Location> signs = (List<Location>) data;
            for (Location sign : signs) {
                Block block = sign.getBlock();
                if (block.getType() == Material.SIGN) {
                    signList.add((Sign) block.getState());
                }
            }
        }

        save();
        workAllSigns();
        System.out.println("Code signs initialized.");
        // TODO: Save new created signs...
    }

    public static void save() {
        while (signList.contains(null)) {
            signList.remove(null);
        }

        List<Location> signs = new LinkedList<>();
        for (Sign sign : signList) {
            signs.add(sign.getLocation());
        }

        Main.instance.getConfig()
                     .set("code-signs", signs);

        Main.instance.saveConfig();
    }

    public static void registerSign(Sign sign) {
        if (signList.contains(sign)) return;
        signList.add(sign);
        save();
    }

    public static void workAllSigns() {
        for (Sign sign : signList) {
            workSign(sign, false);
        }
    }

    public static Object[] getSignAroundData(Sign sign) {
        boolean front = false, left = false, right = false;
        Block[] redstones = CodingChecker.getRedstonesNear(sign.getBlock());
        Block resultBlock = null;
        Block finalResultBlock = null;
        org.bukkit.material.Sign data = (org.bukkit.material.Sign) sign.getData();
        BlockFace signFace = data.getFacing();
        for (Block redstone : redstones) {
            boolean val = getRedstoneValue(redstone);
            Object[] finals = getOnsFromBlockFace(sign, redstone);
            front = (val && (boolean) finals[0]) || front;
            left = (val && (boolean) finals[1]) || left;
            right = (val && (boolean) finals[2]) || right;
            resultBlock = finals[3] != null ? (Block) finals[3] : resultBlock;
        }

        finalResultBlock = resultBlock;
        if (resultBlock != null && resultBlock.getType() == Material.SIGN) {
            finalResultBlock = (Block) (getSignAroundData((Sign) resultBlock.getState())[4]);
        }

        return new Object[] {
                front,
                left,
                right,
                resultBlock,
                finalResultBlock
        };
    }

    public static Object[] getOnsFromBlockFace(Sign from, Block target) {
        BlockFace signFace = ((org.bukkit.material.Sign) from.getData()).getFacing();
        BlockFace blockFace = from.getBlock()
                                  .getFace(target);
        Object[] finals = new Object[] {
                false,
                false,
                false,
                null
        }; //F, L ,R
        switch (blockFace) {
            case SOUTH:
                if (signFace == blockFace) finals[0] = true;
                else if (signFace == BlockFace.EAST) finals[1] = true;
                else if (signFace == BlockFace.WEST) finals[2] = true;
                else finals[3] = target;
                break;
            case WEST:
                if (signFace == blockFace) finals[0] = true;
                else if (signFace == BlockFace.SOUTH) finals[1] = true;
                else if (signFace == BlockFace.NORTH) finals[2] = true;
                else finals[3] = target;
                break;
            case EAST:
                if (signFace == blockFace) finals[0] = true;
                else if (signFace == BlockFace.NORTH) finals[1] = true;
                else if (signFace == BlockFace.SOUTH) finals[2] = true;
                else finals[3] = target;
                break;
            case NORTH:
                if (signFace == blockFace) finals[0] = true;
                else if (signFace == BlockFace.WEST) finals[1] = true;
                else if (signFace == BlockFace.EAST) finals[2] = true;
                else finals[3] = target;
                break;
        }
        return finals;
    }

    public static void workSign(Sign sign, boolean extender) {
        workSign(sign, extender, false);
    }

    public static void workSign(Sign sign, boolean extender, boolean off) {
        workSign(sign, extender, off, new boolean[] {
                false,
                false,
                false
        });
    }

    public static void workSign(Sign sign, boolean extender, boolean off, boolean[] forceTrue) {
        workSign(sign, extender, off, forceTrue, 0);
    }

    public static void workSign(Sign sign, boolean extender, boolean off, boolean[] forceTrue, int fromLine) {
        if (!extender) {
            if (sign.getLines()[0].startsWith("EXT")) return;

            if (isSignIgnored(sign, false)) {
                return;
            }
        }
        registerSign(sign);
        System.out.println("BEGIN: " + sign.getLines()[0]);
        Object[] data = getSignAroundData(sign);
        boolean front = forceTrue[0] || (boolean) data[0], left = forceTrue[1] || (boolean) data[1], right =
                forceTrue[2] || (boolean) data[2];
        Block resultBlock = (Block) data[3];
        Block finalResultBlock = (Block) data[4];

        String[] lines = sign.getLines();
        for (int i = 1; i <= fromLine; i++) {
            if (lines.length - 1 >= i) lines[i] = "";
        }

        for (String line : lines) {
            System.out.println("LINE: " + line);
        }

        if (!CodingParser.isParsable(sign.getLines())) return;
        boolean output = CodingParser.getOutput(sign, lines, front, left, right, !extender, off);
        System.out.println("V: " + front + ", " + left + ", " + right);
        System.out.println((extender ? "EXT" : "") + "CODE: " + output);

        setSignIgnored(sign);

        if (resultBlock != null) {
//            if (resultBlock.getType() == Material.SIGN && output) {
//                workSign((Sign) resultBlock.getState(), true);
//            } else {
//                if (resultBlock.getType() == Material.SIGN && finalResultBlock == null) return;
//                setRedstoneTorchLit(resultBlock.getType() == Material.SIGN ? finalResultBlock : resultBlock, output,
//                                    getSignCodeType(sign) == 1);
//            }
            if (resultBlock.getType() == Material.SIGN) {
                workSign((Sign) resultBlock.getState(), true, !output);
            } else {
                setRedstoneTorchLit(resultBlock, output, getSignCodeType(sign) == 1);
            }
        }
    }

    public static boolean getRedstoneValue(Block block) {
        if (block.getType() == Material.REPEATER || block.getType() == Material.LEVER || block.getType()
                                                                                              .toString()
                                                                                              .contains("_BUTTON") ||
            block.getType()
                 .toString()
                 .contains("_PLATE")) {
            return ((Powerable) block.getBlockData()).isPowered();
        } else if (block.getType() == Material.SIGN) {
            return false;
        }
        return block.getBlockPower() > 0;
    }

    public static void setRedstoneValue(Block block) {
        BlockData blockData = block.getBlockData();
        ((AnaloguePowerable) blockData).setPower(15);
        block.setBlockData(blockData);
    }

    public static void setRedstoneTorchLit(Block block, boolean lit, boolean coder) {
        Material material = coder ? Material.REPEATER : Material.REDSTONE_WIRE;
        if (lit) {
            material = Material.REDSTONE_TORCH;
        }
        if (block.getType() != material) block.setType(material);
    }

    public static boolean isSignIgnored(Sign sign, boolean remove) {
        if (signTempIgnore.contains(sign)) {
            if (remove) signTempIgnore.remove(sign);
            return true;
        }
        return false;
    }

    public static void setSignIgnored(Sign sign) {
        if (!signTempIgnore.contains(sign)) {
            signTempIgnore.add(sign);
            Bukkit.getServer()
                  .getScheduler()
                  .scheduleSyncDelayedTask(Main.instance, () -> isSignIgnored(sign, true), 5);
        }
    }

    public static Sign getSignByName(String name) {
        for (Sign sign : signList) {
            if ((sign.getLines()[0].startsWith("CODE") || sign.getLines()[0].startsWith("EXT")) &&
                sign.getLines()[0].substring(sign.getLines()[0].indexOf(":") + 1)
                                  .equals(name)) {
                return sign;
            }
        }
        return null;
    }

    public static int getSignCodeType(Sign sign) {
        return sign.getLines()[0].startsWith("CODE2") || sign.getLines()[0].startsWith("EXT2") ? 1 : 0;
    }

    public static Sign[] getSignsByName(String name) {
        List<Sign> signs = new LinkedList<>();
        for (Sign sign : signList) {
            if ((sign.getLines()[0].startsWith("CODE") || sign.getLines()[0].startsWith("EXT")) &&
                sign.getLines()[0].substring(sign.getLines()[0].indexOf(":") + 1)
                                  .equals(name)) {
                signs.add(sign);
            }
        }
        return signs.toArray(new Sign[0]);
    }
}
