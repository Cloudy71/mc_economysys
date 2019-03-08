/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 22:39
*/

package cz.cloudy.economysystem.specialization;

import org.bukkit.ChatColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlayerAbilityData {
    String name();

    String description() default "";

    ChatColor color() default ChatColor.GRAY;
}
