package com.Ben12345rocks.DisableAnvilColor;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
import com.Ben12345rocks.AdvancedCore.Util.Metrics.BStatsMetrics;
import com.Ben12345rocks.AdvancedCore.Util.Metrics.MCStatsMetrics;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	static Main plugin;

	@Override
	public void onDisable() {
		plugin = null;
	}

	@Override
	public void onEnable() {
		plugin = this;
		Bukkit.getPluginManager().registerEvents(this, plugin);

		try {
			MCStatsMetrics metrics = new MCStatsMetrics(this);
			metrics.start();
		} catch (IOException e) {
			plugin.getLogger().info("Can't submit metrics stats");
		}

		new BStatsMetrics(this);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onAnvilClick(InventoryClickEvent event) {

		if (event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.ANVIL)) {
			// check for anvil inventory
			return;
		}

		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}

		if (event.getWhoClicked().hasPermission("DisableAnvilColor.Bypass")) {
			return;
		}

		try {
			if (event.getClickedInventory().getItem(0) != null
					&& !event.getClickedInventory().getItem(0).getType().equals(Material.AIR)) {
				ItemBuilder item = new ItemBuilder(event.getClickedInventory().getItem(0));
				if (event.getCurrentItem().hasItemMeta() && item.hasCustomDisplayName()) {
					String str = item.getName();
					boolean hasColorCodes = !(str.equals(ChatColor.stripColor(str)));
					if (hasColorCodes) {
						event.setCancelled(true);
						event.getWhoClicked().closeInventory();
					}
				}
			}
		} catch (Exception e) {
		}

	}
}
