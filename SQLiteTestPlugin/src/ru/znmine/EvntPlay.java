package ru.znmine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.droppages.Skepter.SQL.SQLite;

public class EvntPlay implements Listener {
	Logger log = Logger.getLogger("Minecraft");
	private SQLite sqlite;// private needed
	private Main plugin;
	private HashMap<Player, СостояниеИгрока> состояниеИгрока;// new
																// HashMap<Player,
																// СостояниеИгрока>();

	public EvntPlay(Main plugin) {
		this.plugin = plugin;
		this.sqlite = plugin.sqlite;
		this.состояниеИгрока = plugin.состояниеИгрока;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		ИнициализироватьИгрокавИБД(p);
		Double Рублей = 0.0;// ЗагрузитьБалансИгрока(p);
		СостояниеИгрока ИгрокСостояние = (new СостояниеИгрока("Меню игры "
				+ p.getName(), "Для добавления пунктов меню",
				"используйте команду", "/mymenu add команда1 ; команда2 "))
				.УстановитьРубли(Рублей);
		if (ЗагрузитьМенюИгрока(p, ИгрокСостояние))
			ИгрокСостояние.ДобавитьМенюВИнвентарь(p);
		состояниеИгрока.put(p, ИгрокСостояние);
	}

	@EventHandler
	public void onDie(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if (event.getEntity() instanceof Player)
			return;
		List<ItemStack> Дроп = event.getDrops();
		СостояниеИгрока ИгрокСостояние = состояниеИгрока.get(p);
		ItemStack Меню = ИгрокСостояние.ВещьМеню;
		Дроп.remove(Меню);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		final Player p = e.getPlayer();
		СостояниеИгрока ИгрокСостояние = состояниеИгрока.get(p);
		if (!ИгрокСостояние.МенюИгрока.isEmpty())
			ИгрокСостояние.ДобавитьМенюВИнвентарь(p);

	}

	private boolean ЗагрузитьМенюИгрока(Player p, СостояниеИгрока ИгрокСостояние) {
		boolean ЕстьМеню = false;
		ResultSet rs = sqlite
				.executeQuery("select command from PlayerMenu where playername = '"
						+ p.getName().replace('\'', ' ') + "';");
		try {

			while (rs.next()) {
				ИгрокСостояние.ДобавитьЭлементМеню(rs.getString(1),
						rs.getString(1));
				ЕстьМеню = true; // ResultSet метода, чтоб узнать
									// пустой он или нет, пришлось городить
									// костыль
			}
			return ЕстьМеню;
		} catch (SQLException e) {
			return false;
		}
	}

	private void ИнициализироватьИгрокавИБД(Player p) {
		try {
			sqlite.execute("insert or ignore into Players (playername,ip) VALUES('"
					+ p.getName().replace('\'', ' ')
					+ "','"
					+ p.getAddress().getAddress().getHostAddress() + "');");
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		состояниеИгрока.remove(event.getPlayer());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		final Player p = (Player) event.getWhoClicked();
		if (p == null)
			return;
		ItemMeta im = event.getCursor().getItemMeta();
		im = im == null ? event.getCurrentItem().getItemMeta() : im;
		if (im == null)
			return;
		final СостояниеИгрока СИ = состояниеИгрока.get(p);
		if (!im.equals(СИ.ВещьМеню.getItemMeta()))
			return;
		event.setCancelled(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				p.closeInventory();
				plugin.menu.open(p);
			}
		}, 1);

	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.VOID) {
			event.setCancelled(true);
			event.getEntity().teleport(
					event.getEntity().getWorld().getSpawnLocation(),
					TeleportCause.PLUGIN);
		}
	}

}
