/*
  User: Cloudy
  Date: 02-Mar-19
  Time: 15:15
*/

package cz.cloudy.economysystem.redstonecoding;

import cz.cloudy.economysystem.ActiveConst;
import cz.cloudy.economysystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Switch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class CodingListener
        implements Listener {
    @EventHandler
    public void onRedstoneBlock(BlockRedstoneEvent event) {
        if (!ActiveConst.CODING) return;

        Block block = event.getBlock();
        boolean setFace = false;
        if (block.getType() == Material.LEVER) {
            Switch sw = (Switch) block.getBlockData();
            if (sw.getFace() == Switch.Face.WALL) {
                block = block.getRelative(sw.getFacing()
                                            .getOppositeFace());
                setFace = sw.isPowered();
            }
        }

        Block finalBlock = block;
        boolean finalSetFace = setFace;
        Bukkit.getServer()
              .getScheduler()
              .scheduleSyncDelayedTask(Main.instance, () -> {
                  Sign[] signs = CodingChecker.getSignsNear(finalBlock);
                  for (Sign sign : signs) {
                      boolean[] forceTrue = new boolean[] {
                              false,
                              false,
                              false
                      };
                      if (finalSetFace) {
                          Object[] finals = CodingRegister.getOnsFromBlockFace(sign, finalBlock);
                          forceTrue[0] = (boolean) finals[0];
                          forceTrue[1] = (boolean) finals[1];
                          forceTrue[2] = (boolean) finals[2];
                      }
                      CodingRegister.workSign(sign, false, false, forceTrue);
                  }
              }, 3);
    }
}
