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
	public EvntPlay ОбработчикСобытий;

	public void onEnable() {
		File file = new File(getDataFolder(), "znmine.db");
		sqlite = new SQLite(file);
		sqlite.open();
		final Main m = this;
		ОбработчикСобытий = new EvntPlay(this);
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
			sender.sendMessage("This command can only be run by a player.");
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
			ДобавлениеУдалениеКоманд(sender, args, p, СИ);

			return true;
		}
		return false;
	}

	private void ДобавлениеУдалениеКоманд(CommandSender sender, String[] args,
			Player p, СостояниеИгрока СИ) {
		if (args.length > 1
				&& (args[0].equalsIgnoreCase("add") || args[0]
						.toLowerCase().equalsIgnoreCase("добавить"))) {
			ДобавлениеКоманды(sender, args, p, СИ);
		}

		if (args[0].equalsIgnoreCase("remove")
				|| args[0].toLowerCase().equalsIgnoreCase("удалить")) {
			if (args.length == 1) {
				УдалениеКоманды(sender, p, СИ);
			}
			if (args.length > 1) {
			}
		}
	}

	private void УдалениеКоманды(CommandSender sender, Player p,
			СостояниеИгрока СИ) {
		УдалитьПоследнийИзИБД(p, СИ);
		СИ.УдалитьПоследнийЭлементМеню();
		if(СИ.МенюИгрока.size()==0)СИ.УдалитьМенюИзИнвентаря(p);
		sender.sendMessage("Последний элемент меню удален");
	}

	private void ДобавлениеКоманды(CommandSender sender, String[] args,
			Player p, СостояниеИгрока СИ) {
		String s = args[1];
		for (int i = 2; i <= args.length - 1; i++)
			s = s + ' ' + args[i];
		СИ.ДобавитьЭлементМеню(args[1], s);
		ДобавитьМенювИБД(args[1], p, s);
		СИ.ДобавитьМенюВИнвентарь(p);
		sender.sendMessage("Команда добавлена к вам в меню");
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