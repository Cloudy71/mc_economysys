/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 21:45
*/

package cz.cloudy.economysystem.bank;

import cz.cloudy.economysystem.ActiveConst;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Deprecated
public class BankListener
        implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!ActiveConst.BANK) return;

        Player player = event.getPlayer();

        BankAccount bankAccount = Bank.getBankAccount(player);
        if (bankAccount == null) {
            Bank.createBankAccount(player);
        }
    }
}
