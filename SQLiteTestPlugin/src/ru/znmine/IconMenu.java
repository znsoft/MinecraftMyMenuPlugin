package ru.znmine;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import ru.znmine.СостояниеИгрока.МоиИконки;
 
public class IconMenu implements Listener {
    private String name;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private HashMap<Player, СостояниеИгрока> состояниеИгрока; 
   
   
    public IconMenu(String name, OptionClickEventHandler handler, Plugin plugin, EvntPlay ОбработчикСобытий) {
        this.name = name;
        this.handler = handler;
        this.plugin = plugin;
     	this.состояниеИгрока = ОбработчикСобытий.состояниеИгрока; 
    }
   
    public IconMenu ДобавитьЭлемент(ItemStack icon, String name, String... info) {

    	
    	return this;
    }
   
   public IconMenu УдаолитьЭлемент(ItemStack icon, String name, String... info) {

    	
    	return this;
    }
        
    
    public void ПриготовитьМеню(Player player){
    	СостояниеИгрока СИ = состояниеИгрока.get(player);
  	
        Inventory inventory = Bukkit.createInventory(player, СИ.МенюИгрока.size(), name);
        int i = 0;
        for ( МоиИконки Иконка: СИ.МенюИгрока) {
                inventory.setItem(i++, Иконка.Иконка);
                //inventory.addItem(arg0)
        }
        player.openInventory(inventory);	
    }
   
    public void open(Player player) {
    	ПриготовитьМеню(player);
    }
   
    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
    }
   
    @EventHandler(priority=EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name)) {
            event.setCancelled(true);
    		if (!(event.getWhoClicked() instanceof Player))			return;
    		final Player p = (Player) event.getWhoClicked();
    		ItemMeta im = event.getCursor().getItemMeta();
    		im = im==null?event.getCurrentItem().getItemMeta():im;
    		if (im == null)return;
    		//final СостояниеИгрока СИ = состояниеИгрока.get(p);
    		//if(!im.equals(СИ.ВещьМеню.getItemMeta()))return;
            int slot = event.getRawSlot();
            if (slot >= 0 ) {
                Plugin plugin = this.plugin;
                OptionClickEvent e = new OptionClickEvent((Player)event.getWhoClicked(), slot, im.getDisplayName(), im);
                handler.onOptionClick(e);
                if (e.willClose()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { public void run() { p.closeInventory();  }  }, 1);
                }
                if (e.willDestroy()) {
                    destroy();
                }
            }
        }
    }
   
    public interface OptionClickEventHandler {
        public void onOptionClick(OptionClickEvent event);      
    }
   
    public class OptionClickEvent {
        private Player player;
        private int position;
        private String name;
        private boolean close;
        private boolean destroy;
        private ItemMeta ДанныеИконки;
       
        public OptionClickEvent(Player player, int position, String name, ItemMeta ДанныеИконки ) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
            this.ДанныеИконки = ДанныеИконки;
            
        }
       
        public Player getPlayer() {
            return player;
        }
       
        public int getPosition() {
            return position;
        }
        
        public ItemMeta getItemMeta() {
            return ДанныеИконки;
        }
       
        public String getName() {
            return name;
        }
       
        public boolean willClose() {
            return close;
        }
       
        public boolean willDestroy() {
            return destroy;
        }
       
        public void setWillClose(boolean close) {
            this.close = close;
        }
       
        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }
   
    public ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
            im.setDisplayName(name);
            im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }
   
}