/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 17:46
*/

package cz.cloudy.economysystem.specialization;

import org.bukkit.ChatColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlayerSpecData {
    String name();

    ChatColor color() default ChatColor.GRAY;
}
