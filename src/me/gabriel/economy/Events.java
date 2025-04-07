package me.gabriel.economy;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class Events implements org.bukkit.event.Listener {

	private final Main plugin;

	public Events(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		String joinMessage = plugin.getConfig().getString("join-message", "&a{player} entrou no servidor!");
		joinMessage = joinMessage.replace("{player}", player.getName());

		e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', joinMessage));
	}

	@EventHandler
	public void OnLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();

		String quitMessage = plugin.getConfig().getString("quit-message", "&c{player} saiu do servidor!");
		quitMessage = quitMessage.replace("{player}", player.getName());

		e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', quitMessage));
	}

	@EventHandler
	public void OnRightClick(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null)
			return;

		Player player = e.getPlayer();
		Block block = e.getClickedBlock();

		if (e.getAction().toString().contains("RIGHT_CLICK")) {
			if (block.getType() == Material.CHEST) {
				e.setCancelled(true);
				player.sendMessage("Você clicou em um baú");
			}

		}
	}

}
