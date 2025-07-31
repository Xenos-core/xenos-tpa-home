package org.xenos.teleportplugin.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
            MessageUtil.send(player, "<red>Teleportation canceled because you took damage!");
        }

        if (teleportManager.isInWarmup(player)) {
            teleportManager.cancelTpaWarmup(player);
            MessageUtil.send(player, "<red>Teleportation canceled because you took damage!");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if the player is in a warmup
        boolean inHomeWarmup = homeManager.isInWarmup(player);
        boolean inTpaWarmup = teleportManager.isInWarmup(player);

        if (!inHomeWarmup && !inTpaWarmup) {
            return;
        }

        // Check if the player has actually moved a block, not just looked around
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ() && from.getBlockY() == to.getBlockY()) {
            return;
        }

        // Cancel the appropriate warmup
        if (inHomeWarmup) {
            homeManager.cancelWarmup(player);
            MessageUtil.send(player, "<red>Teleportation canceled because you moved!");
        }

        if (inTpaWarmup) {
            teleportManager.cancelTpaWarmup(player);
            MessageUtil.send(player, "<red>Teleportation canceled because you moved!");
        }
    }
}
