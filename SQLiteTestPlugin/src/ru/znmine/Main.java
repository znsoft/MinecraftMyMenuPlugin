package ru.znmine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin {

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
		menu = (new IconMenu("Player Menu",
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
				}, this));
		getServer().getPluginManager().registerEvents(ОбработчикСобытий, this);
		getServer().getPluginManager().registerEvents(menu, this);

	}

	public void onDisable() {
		sqlite.close();
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
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
	
	

	/**
	 * @param sender
	 * @param args
	 * @param p
	 * @param СИ
	 */
	private void ДобавлениеУдалениеКоманд(CommandSender sender, String[] args,
			Player p, СостояниеИгрока СИ) {
		if (args.length > 1
				&& (args[0].equalsIgnoreCase("add") || args[0].toLowerCase()
						.equalsIgnoreCase("добавить"))) {
			ДобавлениеКоманды(sender, args, p, СИ, 1);
			return;
		}
		// -----------------------------

		if (args[0].equalsIgnoreCase("remove")
				|| args[0].toLowerCase().equalsIgnoreCase("удалить"))
			if (args.length == 1) {
				УдалениеКоманды(sender, p, СИ);
				return;
			}
		//	--------------------
		if (p.isOp() && args.length > 2)
			ДобавлениеКомандыИгрокуОтОператора(args, p);

	}

	// /Summary

	/**
	 * @param args
	 * @param p
	 */
	private void ДобавлениеКомандыИгрокуОтОператора(String[] args, Player p) {
		СостояниеИгрока СИ;

		if (args[1].equalsIgnoreCase("add")
				|| args[1].toLowerCase().equalsIgnoreCase("добавить")) {
			// состояниеИгрока.forEach( (key, value) ->
			// {if(key.getName().equalsIgnoreCase(args[0]))НайденИгрок =
			// key;});//lamda only in jre 1.8
			Player НайденИгрок = null;
			НайденИгрок = НайтиИгрокаВСтруктуре(args, НайденИгрок);
			if (НайденИгрок == null) {
				String s = args[2];
				for (int i = 3; i <= args.length - 1; i++)
					s = s + ' ' + args[i];
				ДобавитьМенювИБД(args[2], args[1], s);
				return;
			}
			СИ = состояниеИгрока.get(НайденИгрок);
			ДобавлениеКоманды(НайденИгрок, args, НайденИгрок, СИ, 2);
		}
	}

	private Player НайтиИгрокаВСтруктуре(String[] args, Player НайденИгрок) {
		Iterator<HashMap.Entry<Player, СостояниеИгрока>> iter = состояниеИгрока
				.entrySet().iterator();
		if (iter != null) {

			while (iter.hasNext()) {

				HashMap.Entry<Player, СостояниеИгрока> entry = iter.next();
				Player p1 = entry.getKey();
				if (p1 == null)
					continue;
				if (p1.getName().equalsIgnoreCase(args[0]))
					НайденИгрок = p1;
			}

		}
		return НайденИгрок;
	}

	private void УдалениеКоманды(CommandSender sender, Player p,
			СостояниеИгрока СИ) {
		УдалитьПоследнийИзИБД(p, СИ);
		СИ.УдалитьПоследнийЭлементМеню();
		if (СИ.МенюИгрока.size() == 0)
			СИ.УдалитьМенюИзИнвентаря(p);
		sender.sendMessage("Последний элемент меню удален");
	}

	private void ДобавлениеКоманды(CommandSender sender, String[] args,
			Player p, СостояниеИгрока СИ, int Нач) {
		String s = args[Нач];
		for (int i = Нач + 1; i <= args.length - 1; i++)
			s = s + ' ' + args[i];
		СИ.ДобавитьЭлементМеню(args[Нач], s);
		ДобавитьМенювИБД(args[Нач], p, s);
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

	private void ДобавитьМенювИБД(String НазваниеИконки, Player p,
			String Команда) {
		ДобавитьМенювИБД(НазваниеИконки, p.getName().replace('\'', ' '),
				Команда);
	}

	private void ДобавитьМенювИБД(String a, String p, String s) {
		try {
			sqlite.execute("insert into PlayerMenu (playername, command, name) VALUES('"
					+ p + "','" + s + "','" + a + "');");
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

}