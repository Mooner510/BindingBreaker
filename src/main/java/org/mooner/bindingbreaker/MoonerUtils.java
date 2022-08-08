package org.mooner.bindingbreaker;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class MoonerUtils {
    public static String chat(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void playSound(Location l, Sound s, double volume, double pitch) {
        Bukkit.getScheduler().runTask(BindingBreaker.plugin, () -> {
            World w;
            if((w = l.getWorld()) == null) return;
            w.playSound(l, s, (float) volume, (float) pitch);
        });
    }

    public static void playSound(Location l, Sound s, double volume, double pitch, int delay) {
        Bukkit.getScheduler().runTaskLater(BindingBreaker.plugin, () -> {
            World w;
            if((w = l.getWorld()) == null) return;
            w.playSound(l, s, (float) volume, (float) pitch);
        }, delay);
    }

    public static void playSound(Player p, String s, double volume, double pitch) {
        p.playSound(p.getLocation(), s, (float) volume, (float) pitch);
    }

    public static void playSound(Player p, Sound s, double volume, double pitch) {
        p.playSound(p.getLocation(), s, (float) volume, (float) pitch);
    }

    public static void playSound(Player p, Sound s, double volume, double pitch, double delay) {
        Bukkit.getScheduler().runTaskLater(BindingBreaker.plugin, () -> p.playSound(p.getLocation(), s, (float) volume, (float) pitch), (long) Math.floor(delay * 20));
    }

    public static void playSound(Player p, Sound s, double volume, double pitch, int delay) {
        Bukkit.getScheduler().runTaskLater(BindingBreaker.plugin, () -> p.playSound(p.getLocation(), s, (float) volume, (float) pitch), delay);
    }
}
