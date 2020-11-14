package com.github.flrque.netherkeepinv;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import sun.security.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class NetherKeepInv extends JavaPlugin implements Listener {

    private final List<String> activeWorldNames = new ArrayList<>();

    @Override
    public void onEnable() {
        // Saving default config from resources
        saveDefaultConfig();
        reloadConfig();

        // Loading configured worlds into the memory
        activeWorldNames.addAll(getConfig().getStringList("active-worlds"));

        final String activeWorldsLine = ArrayUtils.toString(activeWorldNames.toArray());
        getLogger().log(Level.INFO, "Active worlds: " + activeWorldsLine);

        // Registering Event Listener (this class) - it listens for PlayerDeathEvent
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerDeath(@NotNull final PlayerDeathEvent event) {
        final String worldName = event.getEntity().getWorld().getName();

        // Return if place of death don't match the rules from config.yml
        if(!activeWorldNames.contains(worldName))
            return;

        // Keeping inventory of dead person
        event.setKeepInventory(true);
        event.setKeepLevel(true);

        // Clearing drops from the player so there should be no item duplication bug
        event.getDrops().clear();
        event.setDroppedExp(0);
    }
}
