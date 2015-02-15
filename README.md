SQLiteTestPlugin & IconMenuTestPlugin
================

Source for Skepter's SQLite API

Simply put, my SQLite API is designed to help people who require SQLite for databases when creating bukkit plugins. It is created for support for that only, but should be compatible with other services as required.

BUKKIT
------
To start off (with eclipse), download the files (com.droppages.Skepter.SQL) and add it to your JavaProject. From there, you can continue your project. It is crutial to add these lines of code into your Main class:

	public SQLite sqlite;

This goes in your onEnable();

	File file = new File(getDataFolder().getAbsolutePath(), "file.db");
	sqlite = new SQLite(file);
	try {
	sqlite.open();


This goes in your onDisable();
	
	sqlite.close();

IconMenu
---------------
init menu like this:
		menu = new IconMenu("My Fancy Menu", 9,
				new IconMenu.OptionClickEventHandler() {
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						
						event.getPlayer().performCommand("spawn");
						event.getPlayer().sendMessage("You have chosen " + event.getName());
						event.setWillClose(true);
					}
				}, this)
				.setOption(3, new ItemStack(Material.APPLE, 1), "Food",	"The food is delicious")
				.setOption(4, new ItemStack(Material.IRON_SWORD, 1), "Weapon",	"Weapons are for awesome people")
				.setOption(4, new ItemStack(Material.DIAMOND, 1), "Weapon",	"Weapons are for awesome people")
				.setOption(5, new ItemStack(Material.EMERALD, 1), "Money",		"Money brings happiness");
		getServer().getPluginManager().registerEvents(new EvntPlay(this), this);//add icon menu initializer listener
		getServer().getPluginManager().registerEvents(menu, this);// add icon menu listener
