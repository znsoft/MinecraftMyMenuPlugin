package ru.znmine;

//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
//import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
//import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.droppages.Skepter.SQL.SQLite;

public class EvntPlay implements Listener {
	public Logger log = Logger.getLogger("Minecraft");	
	public SQLite sqlite;
	private Main plugin;
	
	
	public HashMap<Player, СостояниеИгрока> состояниеИгрока = new HashMap<Player, СостояниеИгрока>(); 
	
	public EvntPlay(Main plugin) {
		this.plugin = plugin;
		this.sqlite = plugin.sqlite;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		СостояниеИгрока ИгрокСостояние = new СостояниеИгрока("Меню игры","5 руб. на счету");
//		try {
//			sqlite.execute("insert into Players (playername,ip,money) VALUES('"
//					+ p.getName().replace('\'', ' ')
//					+ "','"
//					+ p.getAddress().getAddress().getHostAddress() + "',0);");
//			plugin.PlayerMoney.put(p, 0.0);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		ДобавитьМенюВИнвентарь(p,ИгрокСостояние.ВещьМеню);
			состояниеИгрока.put(p, ИгрокСостояние);
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
	public void onBlockPlace(BlockPlaceEvent event) {
		// Bukkit.getServer().broadcastMessage(
		// "Player " + event.getPlayer().getName() + " placed "
		// + event.getBlock().getType() + " at "
		// + event.getBlock().getLocation());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryOpen(InventoryOpenEvent event) {

	}

   

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		//log.info("1");
		//log.info(event.getInventory().getType().name());
		//if(event.getInventory().getType() != InventoryType.CRAFTING )return;
		//log.info("1.0");
		if (!(event.getWhoClicked() instanceof Player))			return;
		//log.info("1.1");
		final Player p = (Player) event.getWhoClicked();
		if (p == null)return;
		//log.info("2");
		ItemMeta im = event.getCursor().getItemMeta();
		im = im==null?event.getCurrentItem().getItemMeta():im;
//		p.sendMessage(im.getDisplayName());		
		if (im == null)return;
		final СостояниеИгрока СИ = состояниеИгрока.get(p);
		if(!im.equals(СИ.ВещьМеню.getItemMeta()))return;
		//if( !im.getDisplayName().matches("dddname"))return;
		//log.info("3");
		event.setCancelled(true);
		//int slot = event.getRawSlot();
		//if(slot != 0)return;
		//p.sendMessage("click event");
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
