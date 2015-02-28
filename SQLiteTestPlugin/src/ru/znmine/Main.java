package ru.znmine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.logging.Logger;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin {

	public Logger log = Logger.getLogger("Minecraft");
	public SQLite sqlite;
	public IconMenu menu;
	public HashMap<Player, СостояниеИгрока> состояниеИгрока = new HashMap<Player, СостояниеИгрока>();

	public void onEnable() {
		File file = new File(getDataFolder(), "znmine.db");
		sqlite = new SQLite(file);
		sqlite.open();
		final Main m = this;
		EvntPlay ОбработчикСобытий = new EvntPlay(this);
		sqlite.execute("create table if not exists Players ( playername primary key , op , privileges , money , ip , chatlog , commands , level );");
		sqlite.execute("create table if not exists PlayerMenu ( playername , command , name , icon , position );");
		menu = new IconMenu("Player Menu",
				new IconMenu.OptionClickEventHandler() {
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						try {
							String cmds = event.getItemMeta().getLore().get(0);
							String[] МассивКоманд = cmds.split(";");
							final Player p = event.getPlayer();
							if (МассивКоманд.length == 1) {
								p.performCommand(cmds);
							} else {
								for (int i = 0; i < МассивКоманд.length; i++) {
									final String Команда = МассивКоманд[i];
									Bukkit.getScheduler()
											.scheduleSyncDelayedTask(m,
													new Runnable() {
														public void run() {
															p.performCommand(Команда);
														}
													}, i * 2);
								}
							}
							event.setWillClose(true);
						} catch (Exception e) {
						}
					}
				}, this);
		getServer().getPluginManager().registerEvents(ОбработчикСобытий, this);
		getServer().getPluginManager().registerEvents(menu, this);

	}

	public void onDisable() {
		sqlite.close();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("znmine:This command can only be run by a player.");
			return false;
		}
		Player p = (Player) sender;
		СостояниеИгрока СИ = состояниеИгрока.get(p);
		String command = cmd.getName().toLowerCase();
		if (command.equalsIgnoreCase("mymenu")
				|| command.equalsIgnoreCase("менюшка")
			|| command.equalsIgnoreCase("меню")) { // If the player typed
															// /basic then do
															// the following...
			if (args.length == 0)
				return false;
			if (args.length > 1
					&& (args[0].equalsIgnoreCase("add") || args[0]
							.toLowerCase().equalsIgnoreCase("добавить"))) {
				String s = args[1];
				for (int i = 2; i <= args.length - 1; i++)
					s = s + ' ' + args[i];
				СИ.ДобавитьЭлементМеню(args[1], s);
				ДобавитьМенювИБД(args[1], p, s);
				sender.sendMessage("Команда добавлена к вам в меню");
			}

			if (args[0].equalsIgnoreCase("remove")
					|| args[0].toLowerCase().equalsIgnoreCase("удалить")) {
				if (args.length == 1) {
					УдалитьПоследнийИзИБД(p, СИ);
					СИ.УдалитьПоследнийЭлементМеню();
					sender.sendMessage("Последний элемент меню удален");
				}
				if (args.length > 1) {
				}
			}

			return true;
		}
		return false;
	}

	private void УдалитьПоследнийИзИБД(Player p, СостояниеИгрока СИ) {
		ArrayList<ItemStack> Menu = СИ.МенюИгрока;
		ItemMeta im = Menu.get(Menu.size() - 1).getItemMeta();
		try {
			sqlite.execute("delete from PlayerMenu where playername = '"
					+ p.getName().replace('\'', ' ') + "' and command = '"
					+ im.getLore().get(0) + "';");
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private void ДобавитьМенювИБД(String a, Player p, String s) {
		try {
			sqlite.execute("insert into PlayerMenu (playername, command, name) VALUES('"
					+ p.getName().replace('\'', ' ')
					+ "','"
					+ s
					+ "','"
					+ a
					+ "');");
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

}