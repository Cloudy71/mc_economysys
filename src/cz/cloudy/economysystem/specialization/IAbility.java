/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 18:49
*/

package cz.cloudy.economysystem.specialization;

public interface IAbility
        extends ISpec {
    static PlayerAbilityData getAbilityData(IAbility ability) {
        PlayerAbilityData abilityData = ability.getClass()
                                               .getDeclaredAnnotation(PlayerAbilityData.class);
        return abilityData;
    }
}
