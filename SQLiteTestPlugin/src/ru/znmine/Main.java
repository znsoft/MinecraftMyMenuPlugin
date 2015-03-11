package ru.znmine;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.droppages.Skepter.SQL.SQLite;

public class Main extends РаботаСМеню {

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
		
		if(command.equalsIgnoreCase("bt")){
			final Main m = this;
			ПотокЛетающийБлок ЛетающийБлок = new ПотокЛетающийБлок(p.getLocation());
			//Bukkit.getScheduler().scheduleSyncDelayedTask(m, ЛетающийБлок, 1);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(m, ЛетающийБлок, 0, 1);
		}
			
			
		if (command.equalsIgnoreCase("mymenu")
				|| command.equalsIgnoreCase("менюшка")
				|| command.equalsIgnoreCase("меню")) { // If the player typed
			if (args.length == 0)
				return false;
			ДобавлениеУдалениеКоманд(sender, args, p, СИ);

			return true;
		}
		return false;
	}



}