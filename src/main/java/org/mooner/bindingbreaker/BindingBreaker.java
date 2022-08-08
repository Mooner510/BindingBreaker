package org.mooner.bindingbreaker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mooner.bindingbreaker.gui.viewer.BindingGUI;

public final class BindingBreaker extends JavaPlugin {
    public static BindingBreaker plugin;

    @Override
    public void onEnable() {
        plugin = this;
        this.getLogger().info("Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {
            if (command.getName().equals("bindingbreaker")) {
                new BindingGUI(p);
                return true;
            }
        }
        return false;
    }
}
