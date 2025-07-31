package org.xenos.teleportplugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.managers.HomeManager;

import java.util.Arrays;
import java.util.Set;

public class HomeGui {

    private final TeleportPlugin plugin;
    private final HomeManager homeManager;
    private final Player player;
    public static final NamespacedKey HOME_NAME_KEY = new NamespacedKey("xenos-homes", "home_name");
    public static final NamespacedKey SET_HOME_BUTTON_KEY = new NamespacedKey("xenos-homes", "set_home_button");

    public HomeGui(TeleportPlugin plugin, HomeManager homeManager, Player player) {
        this.plugin = plugin;
        this.homeManager = homeManager;
        this.player = player;
    }

    public void open() {
        Set<String> homeNames = homeManager.getHomeNames(player);
        int homeCount = homeNames.size();
        // Calculate size to fit homes + 1 for the sethome button, then round up to multiple of 9
        int size = (int) (Math.ceil((homeCount + 1) / 9.0) * 9);
        size = Math.max(9, Math.min(54, size));

        Inventory gui = Bukkit.createInventory(null, size, "Your Homes");

        // Add home items
        for (String homeName : homeNames) {
            ItemStack homeItem = new ItemStack(Material.RED_BED);
            ItemMeta meta = homeItem.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§r§b" + homeName);
                meta.setLore(Arrays.asList(
                        "§7Left-click to teleport.",
                        "§7Right-click to delete."
                ));
                meta.getPersistentDataContainer().set(HOME_NAME_KEY, PersistentDataType.STRING, homeName);
                homeItem.setItemMeta(meta);
            }
            gui.addItem(homeItem);
        }

        // Add sethome button
        ItemStack setHomeItem = new ItemStack(Material.ANVIL);
        ItemMeta setHomeMeta = setHomeItem.getItemMeta();
        if (setHomeMeta != null) {
            setHomeMeta.setDisplayName("§r§aSet a new home");
            setHomeMeta.setLore(Arrays.asList("§7Click here to learn how to", "§7set a new home."));
            setHomeMeta.getPersistentDataContainer().set(SET_HOME_BUTTON_KEY, PersistentDataType.BYTE, (byte) 1);
            setHomeItem.setItemMeta(setHomeMeta);
        }
        // Place it in the last slot
        gui.setItem(size - 1, setHomeItem);

        player.openInventory(gui);
    }
}
