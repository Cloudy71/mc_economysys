/*
  User: Cloudy
  Date: 02-Mar-19
  Time: 15:26
*/

package cz.cloudy.economysystem.redstonecoding;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.LinkedList;
import java.util.List;

public class CodingChecker {
    private static Block[] getBlocksNear(Block block) {
        List<Block> blocks = new LinkedList<>();
        BlockFace[] faces = new BlockFace[] {
                BlockFace.EAST,
                BlockFace.NORTH,
                BlockFace.SOUTH,
                BlockFace.WEST
        };

        for (BlockFace blockFace : faces) {
            blocks.add(block.getRelative(blockFace));
        }

        return blocks.toArray(new Block[0]);
    }

    public static Sign[] getSignsNear(Block block) {
        List<Sign> signs = new LinkedList<>();

        Block[] blocks = getBlocksNear(block);
        for (Block block1 : blocks) {
            if (block1.getType() == Material.SIGN) {
                Sign sign = (Sign) block1.getState();
                CodingRegister.registerSign(sign);
                signs.add(sign);
            }
        }

        return signs.toArray(new Sign[0]);
    }

    public static Block[] getRedstonesNear(Block block) {
        List<Block> blocks = new LinkedList<>();

        Block[] blockss = getBlocksNear(block);
        for (Block block1 : blockss) {
            if (block1.getType() == Material.REDSTONE_WIRE || block1.getType() == Material.REPEATER ||
                block1.getType() == Material.REDSTONE_TORCH || block1.getType() == Material.REDSTONE_BLOCK ||
                block1.getType() == Material.LEVER || block1.getType()
                                                            .toString()
                                                            .contains("_BUTTON") || block1.getType()
                                                                                          .toString()
                                                                                          .contains("_PLATE") ||
                block1.getType() == Material.SIGN) {
                blocks.add(block1);
            }
        }

        return blocks.toArray(new Block[0]);
    }
}
