package org.xenos.teleportplugin.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.xenos.teleportplugin.teleportplugin;

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
    private final Map<UUID, BukkitTask> warmupTasks = new HashMap<>();

    private static final long COOLDOWN = 60 * 1000; // 60s
    private static final long WARMUP_TICKS = 15 * 20; // 15s

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
                System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < COOLDOWN;
    }

    public long getCooldownLeft(Player player) {
        return Math.max(0, (cooldowns.getOrDefault(player.getUniqueId(), 0L) + COOLDOWN - System.currentTimeMillis()) / 1000);
    }

    public void startTpaWarmup(Player from, Player to) {
        UUID uuid = from.getUniqueId();

        from.sendMessage("§e⏳ Teleporting in 15 seconds... Don’t move or take damage!");
        warmupTasks.put(uuid, Bukkit.getScheduler().runTaskTimer(teleportplugin.getInstance(), new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (!from.isOnline() || !warmupTasks.containsKey(uuid)) {
                    cancelTpaWarmup(from);
                    return;
                }

                Location loc = from.getLocation().add(0, 1, 0);
                from.getWorld().spawnParticle(Particle.PORTAL, loc, 30, 0.5, 0.5, 0.5, 0.05);

                counter++;
                if (counter >= WARMUP_TICKS / 20) {
                    if (from.isOnline()) {
                        from.teleport(to);
                        from.sendMessage("§a✅ Teleport successful!");
                        from.getWorld().spawnParticle(Particle.END_ROD, to.getLocation(), 60, 1, 1, 1, 0.1);
                        cooldowns.put(uuid, System.currentTimeMillis());
                    }
                    cancelTpaWarmup(from);
                }
            }
        }, 0L, 20L)); // runs every second
    }

    public void cancelTpaWarmup(Player from) {
        UUID uuid = from.getUniqueId();
        BukkitTask task = warmupTasks.remove(uuid);
        if (task != null) {
            task.cancel();
            from.sendMessage("§c❌ Teleport canceled.");
        }
    }

    public boolean isInWarmup(Player player) {
        return warmupTasks.containsKey(player.getUniqueId());
    }
}
