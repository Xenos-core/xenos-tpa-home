package org.xenos.teleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.utils.MessageUtil;
import org.xenos.teleportplugin.utils.SoundUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class TeleportManager {

    public static class TpaRequest {
        public final Player from;
        public final Player to;
        public final long timestamp;

        public TpaRequest(Player from, Player to) {
            this.from = from;
            this.to = to;
            this.timestamp = System.currentTimeMillis();
        }
    }

    private final Map<UUID, TpaRequest> pendingRequests = new HashMap<>();
    private final Set<UUID> teleportToggles = new HashSet<>();
    private final Map<UUID, Set<UUID>> ignoredPlayers = new HashMap<>();

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, BukkitRunnable> warmupTasks = new HashMap<>();

    private final TeleportPlugin plugin;
    private long cooldown;
    private long warmupTicks;

    public TeleportManager(TeleportPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        cooldown = config.getLong("teleport.tpa.cooldown", 60) * 1000;
        warmupTicks = config.getLong("teleport.tpa.warmup", 15) * 20;
    }

    public void sendRequest(Player from, Player to) {
        pendingRequests.put(to.getUniqueId(), new TpaRequest(from, to));
    }

    public TpaRequest getRequest(Player to) {
        return pendingRequests.get(to.getUniqueId());
    }

    public void removeRequest(Player to) {
        pendingRequests.remove(to.getUniqueId());
    }

    public void toggleTeleport(Player player) {
        UUID id = player.getUniqueId();
        if (!teleportToggles.add(id)) {
            teleportToggles.remove(id);
        }
    }

    public boolean isTeleportEnabled(Player player) {
        return !teleportToggles.contains(player.getUniqueId());
    }

    public void ignorePlayer(Player target, Player ignored) {
        ignoredPlayers.computeIfAbsent(target.getUniqueId(), k -> new HashSet<>()).add(ignored.getUniqueId());
    }

    public boolean isIgnored(Player target, Player sender) {
        return ignoredPlayers.getOrDefault(target.getUniqueId(), Collections.emptySet()).contains(sender.getUniqueId());
    }

    public boolean isInCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId()) &&
                System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < cooldown;
    }

    public long getCooldownLeft(Player player) {
        return Math.max(0, (cooldowns.getOrDefault(player.getUniqueId(), 0L) + cooldown - System.currentTimeMillis()) / 1000);
    }

    public void startTpaWarmup(Player from, Player to) {
        UUID uuid = from.getUniqueId();
        MessageUtil.send(from, plugin.getConfig().getString("messages.home-teleport-start", "<yellow>Teleporting in {time} seconds. Don't move or take damage!").replace("{time}", String.valueOf(warmupTicks / 20)));
        SoundUtil.playSound(from, "warmup-start", plugin);

        BukkitRunnable task = new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (!from.isOnline()) {
                    cancelTpaWarmup(from);
                    cancel();
                    return;
                }

                // Particle effect
                Location loc = from.getLocation().add(0, 1, 0);
                from.getWorld().spawnParticle(Particle.PORTAL, loc, 30, 0.5, 0.5, 0.5, 0.05);

                ticks++;
                if (ticks >= warmupTicks) {
                    if (from.isOnline()) {
                        from.teleport(to);
                        MessageUtil.send(from, plugin.getConfig().getString("messages.teleport-success", "<green>Teleport successful!"));
                        SoundUtil.playSound(from, "teleport-success", plugin);
                        from.getWorld().spawnParticle(Particle.END_ROD, to.getLocation(), 60, 1, 1, 1, 0.1);
                        cooldowns.put(uuid, System.currentTimeMillis());
                    }
                    cancelTpaWarmup(from);
                    cancel();
                }
            }
        };

        warmupTasks.put(uuid, task);
        task.runTaskTimer(plugin, 0L, 1L);
    }

    public void cancelTpaWarmup(Player from) {
        UUID uuid = from.getUniqueId();
        BukkitRunnable task = warmupTasks.remove(uuid);
        if (task != null) {
            task.cancel();
            MessageUtil.send(from, plugin.getConfig().getString("messages.home-teleport-cancel", "<red>Teleport canceled."));
            SoundUtil.playSound(from, "teleport-cancel", plugin);
        }
    }

    public boolean isInWarmup(Player player) {
        return warmupTasks.containsKey(player.getUniqueId());
    }

    public void cancelAllWarmups() {
        for (BukkitRunnable task : warmupTasks.values()) {
            task.cancel();
        }
        warmupTasks.clear();
    }
}
