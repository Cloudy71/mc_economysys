/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 22:36
*/

package cz.cloudy.economysystem.specialization.abilities;

import cz.cloudy.economysystem.specialization.IAbility;
import cz.cloudy.economysystem.specialization.PlayerAbilityData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@PlayerAbilityData(name = "2xDMG -> Monsters", description = "2x damage on monsters", color = ChatColor.DARK_RED)
public class MonsterDoubleDamageAbility
        implements IAbility {

    @Override
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (Monster.class.isAssignableFrom(event.getEntity()
                                                .getClass())) {
            event.setDamage(event.getDamage() * 2.);
        }
    }
}
