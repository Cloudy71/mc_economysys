/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 13:02
*/

package cz.cloudy.economysystem.specialization.specs;

import cz.cloudy.economysystem.specialization.ISpec;
import cz.cloudy.economysystem.specialization.PlayerSpecData;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@PlayerSpecData(name = "None")
public class DefaultSpecs
        implements ISpec {
    @Override
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (Monster.class.isAssignableFrom(event.getEntity()
                                                .getClass())) {
            event.setDamage(event.getDamage() / 1.5);
        }
    }

    @Override
    public void onBlockDamage(BlockDamageEvent event) {
        Material type = event.getBlock()
                             .getType();
        if (type.toString()
                .contains("_WOOD") || type.toString()
                                          .contains("_LOG") || type.toString()
                                                                   .contains("_PLANKS") || type.toString()
                                                                                               .contains("_FENCE") ||
            type.toString()
                .contains("_DOOR")) {
            if (!event.getItemInHand()
                      .getType()
                      .toString()
                      .contains("_AXE")) {
                event.setCancelled(true);
            }
        }
    }
}
