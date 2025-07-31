package org.xenos.teleportplugin.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class HomeGuiListener implements Listener {

    private final TeleportPlugin plugin;
    private final HomeManager homeManager;

    public HomeGuiListener(TeleportPlugin plugin, HomeManager homeManager) {
        this.plugin = plugin;
        this.homeManager = homeManager;
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Your Homes")) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getItemMeta() == null) {
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();
        // Check for sethome button first
        if (meta.getPersistentDataContainer().has(HomeGui.SET_HOME_BUTTON_KEY, PersistentDataType.BYTE)) {
            player.closeInventory();
            MessageUtil.send(player, "<green>To set a new home, type: <white>/sethome <name>");
            return;
        }

        String homeName = meta.getPersistentDataContainer().get(HomeGui.HOME_NAME_KEY, PersistentDataType.STRING);

        if (homeName == null) {
            return;
        }

        if (event.getClick() == ClickType.LEFT) {
            player.closeInventory();
            player.performCommand("home " + homeName);
        } else if (event.getClick() == ClickType.RIGHT) {
            homeManager.deleteHome(player, homeName);
            // Refresh the GUI
            new HomeGui(plugin, homeManager, player).open();
        }
    }
}
