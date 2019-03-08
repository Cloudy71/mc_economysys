/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 13:12
*/

package cz.cloudy.economysystem.specialization;

import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface ISpec {
    static PlayerSpecData getSpecData(ISpec spec) {
        PlayerSpecData data = spec.getClass()
                                  .getDeclaredAnnotation(PlayerSpecData.class);
        return data;
    }

    default void onPlayerAttack(EntityDamageByEntityEvent event) {

    }

    default void onBlockDamage(BlockDamageEvent event) {

    }
}
