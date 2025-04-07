package me.gabriel.economy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Economy implements CommandExecutor {

	private final HashMap<UUID, Double> playerSaldos = new HashMap<>();
	private final JavaPlugin plugin;

	public Economy(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void addSaldo(Player player, double amount) {
		UUID playerId = player.getUniqueId();
		playerSaldos.put(playerId, getSaldo(player) + amount);
	}

	public void removeSaldo(Player player, double amount) {
		UUID playerId = player.getUniqueId();
		double newBalance = Math.max(0, getSaldo(player) - amount);
		playerSaldos.put(playerId, newBalance);
	}

	public void setSaldo(Player player, double amount) {
		UUID playerId = player.getUniqueId();
		playerSaldos.put(playerId, amount);
	}

	public double getSaldo(Player player) {
		UUID playerId = player.getUniqueId();
		return playerSaldos.getOrDefault(playerId, 0.0);
	}

	public void saveSaldosCSV() {
		File file = new File(plugin.getDataFolder(), "variables.csv");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write("uuid,saldo");
			writer.newLine();

			for (Map.Entry<UUID, Double> entry : playerSaldos.entrySet()) {
				writer.write(entry.getKey().toString() + "," + entry.getValue());
				writer.newLine();
			}
		} catch (IOException e) {
			plugin.getLogger().severe("Erro ao salvar o CSV: " + e.getMessage());
		}
	}

	public void loadSaldosCSV() {
		File file = new File(plugin.getDataFolder(), "variables.csv");
		if (!file.exists()) {
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			if ((line = reader.readLine()) != null && line.startsWith("uuid,")) {
			}
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					try {
						UUID uuid = UUID.fromString(parts[0]);
						double saldo = Double.parseDouble(parts[1]);
						playerSaldos.put(uuid, saldo);
					} catch (IllegalArgumentException ex) {
						plugin.getLogger().warning("Linha inválida no CSV: " + line);
					}
				}
			}
		} catch (IOException e) {
			plugin.getLogger().severe("Erro ao carregar o CSV: " + e.getMessage());
		}
	}
	
	public void GuiPagar(Player player, double amount) {
		Inventory pagar = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Confirmação de pagamento");
	}
	

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar este comando.");
			return true;
		}

		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("saldo")) {
			if (args.length == 0) {
				player.sendMessage(ChatColor.YELLOW + "Seu saldo: " + ChatColor.GREEN + "R$" + getSaldo(player));
				return true;
			} else {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				UUID playerId = offlinePlayer.getUniqueId();
				double playerSaldo = playerSaldos.getOrDefault(playerId, 0.0);
				player.sendMessage(ChatColor.YELLOW + "Saldo de " + offlinePlayer.getName() + ": " + ChatColor.GREEN
						+ "R$" + playerSaldo);
			}
		}

		if (cmd.getName().equalsIgnoreCase("setsaldo")) {
			if (args.length < 2) {
				player.sendMessage(ChatColor.RED + "Uso correto: /setsaldo <jogador> <valor>");
				return true;
			}

			Player target = Bukkit.getPlayer(args[0]);
			double amount;
			try {
				amount = Double.parseDouble(args[1]);
			} catch (NumberFormatException e) {
				player.sendMessage(ChatColor.RED + "O valor precisa ser um número!");
				return true;
			}

			if (target != null) {
				setSaldo(target, amount);
				player.sendMessage(ChatColor.GREEN + "Saldo de " + target.getName() + " definido para R$" + amount);
				if (target != sender) {
					target.sendMessage(ChatColor.YELLOW + "Seu saldo foi atualizado para: " + ChatColor.GREEN + "R$"
							+ getSaldo(target));
				}
			} else {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				UUID playerId = offlinePlayer.getUniqueId();
				playerSaldos.put(playerId, amount);
				player.sendMessage(ChatColor.GREEN + "Saldo de " + args[0] + " (offline) definido para R$" + amount);
			}

			return true;
		}
		if(cmd.getName().equalsIgnoreCase("pagar")) {
			if(args.length < 2) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUse /pagar <player> <quantia>"));
				return true;
			}
			
			
			
		}
		
		
		
		
		

		return false;
	}
}
