package ru.znmine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.droppages.Skepter.SQL.SQLite;

public abstract class РаботаСМеню extends JavaPlugin {

	public SQLite sqlite;
	public IconMenu menu;
	public HashMap<Player, СостояниеИгрока> состояниеИгрока = new HashMap<Player, СостояниеИгрока>();

	public РаботаСМеню() {
		super();
	}

	/**
	 * @param sender
	 * @param args
	 * @param p
	 * @param СИ
	 */
	protected void ДобавлениеУдалениеКоманд(CommandSender sender, String[] args,
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
				if ((p.isOp()||p.hasPermission("mymenu.admin")) && args.length > 2){
					ДобавлениеКомандыИгрокуОтОператора(args, p);
				sender.sendMessage("Команда добавлена игроку в меню");
				}
			}

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

	private void УдалениеКоманды(CommandSender sender, Player p, СостояниеИгрока СИ) {
		УдалитьПоследнийИзИБД(p, СИ);
		СИ.УдалитьПоследнийЭлементМеню();
		if (СИ.МенюИгрока.size() == 0)
			СИ.УдалитьМенюИзИнвентаря(p);
		sender.sendMessage("Последний элемент меню удален");
	}

	private void ДобавлениеКоманды(CommandSender sender, String[] args, Player p,
			СостояниеИгрока СИ, int Нач) {
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

	private void ДобавитьМенювИБД(String НазваниеИконки, Player p, String Команда) {
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