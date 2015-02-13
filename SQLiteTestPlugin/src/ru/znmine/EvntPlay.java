package ru.znmine;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.droppages.Skepter.SQL.SQLite;

public class EvntPlay implements Listener {
	public SQLite sqlite;
	private Main plugin;
	public ItemStack is;
	public EvntPlay(Main plugin) {
		this.plugin = plugin;
		this.sqlite = plugin.sqlite;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
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
		 is = new ItemStack(Material.APPLE, 1);
		 ItemMeta im = is.getItemMeta();
		 im.setDisplayName("dddname");
		 im.setLore(Arrays.asList("lore"));
		 
		 is.setItemMeta(im);
		 PlayerInventory pin = p.getInventory();
		 if(pin.contains(is))pin.remove(is);
		 pin.setHeldItemSlot(8);
		 pin.addItem(is);

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		// event.setQuitMessage(event.getPlayer().getName() +
		// " покинул сервер");
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// Bukkit.getServer().broadcastMessage(
		// "Player " + event.getPlayer().getName() + " placed "
		// + event.getBlock().getType() + " at "
		// + event.getBlock().getLocation());
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		// if (event.getInventory().getType() != InventoryType.CHEST)  return;
	
	}




	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack cur = event.getCursor();
		if (cur == null)return;
		//p.sendMessage(event.getClick().name());
		ItemMeta im = cur.getItemMeta();
		if (im == null)return;
		if (!(event.getWhoClicked() instanceof Player))
			return;
		final Player p = (Player) event.getWhoClicked();
		
		p.sendMessage(im.getDisplayName());
		if( !im.getDisplayName().matches("dddname"))return;
		//int slot = event.getRawSlot();
		//if(slot != 0)return;
		p.sendMessage("click event");
		//Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
          //  public void run() {
         
            //}
         Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
             public void run() {
            	 p.closeInventory();
            	 //plugin.menu.open(p);
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
