/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 17:36
*/

package cz.cloudy.economysystem.specialization;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerSpecListener
        implements Listener {
    private Player getPlayer(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        }
        return null;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Player player = getPlayer(event.getDamager());
        if (player == null) {
            return;
        }

        PlayerSpec playerSpec = PlayerSpecs.getPlayerSpec(player);
        playerSpec.getSpecs()
                  .onPlayerAttack(event);
        for (IAbility ability : playerSpec.getAbilities()) {
            ability.onPlayerAttack(event);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        PlayerSpec playerSpec = PlayerSpecs.getPlayerSpec(player);
        playerSpec.getSpecs()
                  .onBlockDamage(event);
        for (IAbility ability : playerSpec.getAbilities()) {
            ability.onBlockDamage(event);
        }
    }
}
