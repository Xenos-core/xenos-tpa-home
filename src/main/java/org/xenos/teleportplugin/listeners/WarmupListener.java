package org.xenos.teleportplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.teleportplugin;

public class WarmupListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        HomeManager homeManager = teleportplugin.getInstance().getHomeManager();
        TeleportManager teleportManager = teleportplugin.getInstance().getTeleportManager();

        if (homeManager.isInWarmup(player)) {
            homeManager.cancelWarmup(player);
            player.sendMessage("§c home tp cancelled becuze you got dammge ");
        }

        if (teleportManager.isInWarmup(player)) {
            teleportManager.cancelTpaWarmup(player);
            player.sendMessage("§c❌ tp canceled becuze you got dammge !");
        }
    }
}
