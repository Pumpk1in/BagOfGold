package one.lindegaard.BagOfGold.rewards;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import one.lindegaard.BagOfGold.BagOfGold;
import one.lindegaard.BagOfGold.storage.PlayerSettings;
import one.lindegaard.BagOfGold.util.Misc;

public class RewardListeners implements Listener {

	private BagOfGold plugin;

	public RewardListeners(BagOfGold plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		//if (player.getGameMode() == GameMode.SURVIVAL) {
			PlayerSettings ps = plugin.getPlayerSettingsManager().getPlayerSettings(player);
			double amountInInventory = plugin.getEconomyManager().getAmountInInventory(player);
			if (Misc.round(amountInInventory) != Misc.round(ps.getBalance()) + Misc.round(ps.getBalanceChanges())) {
				plugin.getMessages().debug("%s closed inventory: balance error (%s,%s,%s)", player.getName(),
						Misc.round(amountInInventory), Misc.round(ps.getBalance()), Misc.round(ps.getBalanceChanges()));
				ps.setBalance(amountInInventory);
				ps.setBalanceChanges(0);
				plugin.getPlayerSettingsManager().setPlayerSettings(player, ps);
				plugin.getMessages().debug("%s closed inventory: new balance is %s", player.getName(),
						plugin.getEconomyManager().getBalance(player));

			}
		//}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onGameChange(PlayerGameModeChangeEvent event){
		
		if (event.isCancelled())
			return;
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			
				Player player = (Player) event.getPlayer();
				PlayerSettings ps = plugin.getPlayerSettingsManager().getPlayerSettings(player);
				double amountInInventory = plugin.getEconomyManager().getAmountInInventory(player);
				//if (Misc.round(amountInInventory) != Misc.round(ps.getBalance()) + Misc.round(ps.getBalanceChanges())) {
					//plugin.getMessages().debug("%s gamemodechange: balance error (%s,%s,%s)", player.getName(),
					//		Misc.round(amountInInventory), Misc.round(ps.getBalance()), Misc.round(ps.getBalanceChanges()));
					ps.setBalance(amountInInventory);
					ps.setBalanceChanges(0);
					plugin.getPlayerSettingsManager().setPlayerSettings(player, ps);
					plugin.getMessages().debug("%s gamemodechange: new balance is %s", player.getName(),
							plugin.getEconomyManager().getBalance(player));
			//}

				
			}
		}, 3);
	}
}
