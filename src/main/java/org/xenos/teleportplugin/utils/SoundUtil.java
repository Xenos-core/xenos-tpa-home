package org.xenos.teleportplugin.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.TeleportPlugin;

public class SoundUtil {

    public static void playSound(Player player, String configPath, TeleportPlugin plugin) {
        String soundName = plugin.getConfig().getString("sounds." + configPath);
        if (soundName == null || soundName.equalsIgnoreCase("NONE")) {
            return; // Sound is disabled
        }

        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound name in config.yml at 'sounds." + configPath + "': " + soundName);
        }
    }
}
