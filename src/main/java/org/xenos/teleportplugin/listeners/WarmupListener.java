package org.xenos.teleportplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class WarmupListener implements Listener {

    private final HomeManager homeManager;
    private final TeleportManager teleportManager;

    public WarmupListener(HomeManager homeManager, TeleportManager teleportManager) {
        this.homeManager = homeManager;
        this.teleportManager = teleportManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (homeManager.isInWarmup(player)) {
            homeManager.cancelWarmup(player);
            // The cancelWarmup method already sends a message, but we can add one specific to the cause.
            MessageUtil.send(player, "<red>Teleportation canceled because you took damage!");
        }

        if (teleportManager.isInWarmup(player)) {
            teleportManager.cancelTpaWarmup(player);
            MessageUtil.send(player, "<red>Teleportation canceled because you took damage!");
        }
    }
}
