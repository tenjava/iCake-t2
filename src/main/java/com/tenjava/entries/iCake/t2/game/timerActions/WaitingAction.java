package com.tenjava.entries.iCake.t2.game.timerActions;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.tenjava.entries.iCake.t2.game.WorldUtils;

public class WaitingAction implements TimerAction {

    public void doAction() {
        World world = WorldUtils.createWorld();
        world.setSpawnLocation(0, 150, 0);
        
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getGameMode() != GameMode.CREATIVE) {
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                
                for(PotionEffect pot : player.getActivePotionEffects()) {
                    player.removePotionEffect(pot.getType());
                }
                
                player.setGameMode(GameMode.SURVIVAL);
                
                if(player.getVehicle() != null) {
                    player.getVehicle().eject();
                }
                
                player.teleport(world.getSpawnLocation());
                
                player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.LOG, 3) });
            }
        }
    }
    
}