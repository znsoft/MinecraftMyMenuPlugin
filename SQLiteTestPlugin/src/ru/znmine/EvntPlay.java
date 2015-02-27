package ru.znmine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.droppages.Skepter.SQL.SQLite;

public class EvntPlay implements Listener {
	Logger log = Logger.getLogger("Minecraft");	
	private SQLite sqlite;//private needed
	private Main plugin;
	private HashMap<Player, СостояниеИгрока> состояниеИгрока;//new HashMap<Player, СостояниеИгрока>(); 
	
	public EvntPlay(Main plugin) {
		this.plugin = plugin;
		this.sqlite = plugin.sqlite;
		this.состояниеИгрока = plugin.состояниеИгрока;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		ИнициализироватьИгрокавИБД(p);
		Double Рублей = ЗагрузитьБалансИгрока(p);
		СостояниеИгрока ИгрокСостояние = (new СостояниеИгрока("Меню игры",Рублей.toString()+ " руб. на счету"))
				.УстановитьРубли(Рублей)
				.ДобавитьЭлементМеню("Помощь", "help");
		ЗагрузитьМенюИгрока(p, ИгрокСостояние);
		ДобавитьМенюВИнвентарь(p, ИгрокСостояние.ВещьМеню);
		состояниеИгрока.put(p, ИгрокСостояние);
	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent event) {
		//Player p = event.getEntity();
		//if(p==null)return;
		//СостояниеИгрока ИгрокСостояние = состояниеИгрока.get(p);
		
	}
	
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        СостояниеИгрока ИгрокСостояние = состояниеИгрока.get(p);
        ДобавитьМенюВИнвентарь(p, ИгрокСостояние.ВещьМеню);
        
    }
    
	private void ЗагрузитьМенюИгрока(Player p, СостояниеИгрока ИгрокСостояние) {
		ResultSet rs = sqlite.executeQuery("select command from PlayerMenu where playername = '"+p.getName().replace('\'', ' ')+"';");
		try {
			while (rs.next()){
				ИгрокСостояние.ДобавитьЭлементМеню(rs.getString(1), rs.getString(1));
			}
		} catch (SQLException e) {
		}
	}

	private Double ЗагрузитьБалансИгрока(Player p) {
		ResultSet rs = sqlite.executeQuery("select ifnull(money,0.0) from Players where playername = '"+p.getName().replace('\'', ' ')+"';");
		try {
			rs.next();
			return rs.getDouble(1);
		} catch (SQLException e) {
			return 0.0;
		}
	}

	private void ИнициализироватьИгрокавИБД(Player p) {
		try {
			sqlite.execute("insert or ignore into Players (playername,ip) VALUES('"
					+ p.getName().replace('\'', ' ')
					+ "','"
					+ p.getAddress().getAddress().getHostAddress() + "');");
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	private void ДобавитьМенюВИнвентарь(Player p, ItemStack вещьМеню) {
		 PlayerInventory pin = p.getInventory();
		 if(pin.contains(вещьМеню))pin.remove(вещьМеню);
		 pin.setHeldItemSlot(1);
		 pin.setItem(9,вещьМеню);	// TODO Auto-generated method stub
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		состояниеИгрока.remove(event.getPlayer());
	}

	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))			return;
		final Player p = (Player) event.getWhoClicked();
		if (p == null)return;
		ItemMeta im = event.getCursor().getItemMeta();
		im = im==null?event.getCurrentItem().getItemMeta():im;
		if (im == null)return;
		final СостояниеИгрока СИ = состояниеИгрока.get(p);
		if(!im.equals(СИ.ВещьМеню.getItemMeta()))return;
		event.setCancelled(true);
         Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
             public void run() {
            	 p.closeInventory();
            	 plugin.menu.open(p);
             }
         }, 1);
		
	}

	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent evt) {
//		Player p = evt.getPlayer();
//		if (!p.isOp())
//			return;
//		Location loc = p.getLocation();
//		World w = loc.getWorld();
//		loc.setY(loc.getY() + 5);
//		Block b = w.getBlockAt(loc);
		// loc.distance(o);
		//b.setType(Material.SAND);
	}
}
