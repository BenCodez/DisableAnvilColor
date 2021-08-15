package com.bencodez.disableanvilcolor;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class DisableAnvilColorMain extends JavaPlugin implements Listener {
	static DisableAnvilColorMain plugin;

	@Override
	public void onDisable() {
		plugin = null;
	}

	@Override
	public void onEnable() {
		plugin = this;
		Bukkit.getPluginManager().registerEvents(this, plugin);

		new Metrics(this, 1688);
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
				ItemStack item = event.getClickedInventory().getItem(0);
				boolean hasCustomDisplayName = false;
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						hasCustomDisplayName = true;
					}
				}
				if (event.getCurrentItem().hasItemMeta() && hasCustomDisplayName) {
					String str = item.getItemMeta().getDisplayName();
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
