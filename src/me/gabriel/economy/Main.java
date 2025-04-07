package me.gabriel.economy;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Economy economy;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		economy = new Economy(this);
		getCommand("saldo").setExecutor(economy);
		getCommand("setsaldo").setExecutor(economy);
		economy.loadSaldosCSV();
		getLogger().info("Plugin de economia ativado!");
	}

	@Override
	public void onDisable() {
		economy.saveSaldosCSV();
		getLogger().info("Plugin de economia desativado!");
	}
}