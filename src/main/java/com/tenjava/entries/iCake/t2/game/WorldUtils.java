package com.tenjava.entries.iCake.t2.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.util.org.apache.commons.io.FileUtils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.tenjava.entries.iCake.t2.TenJava;
import com.tenjava.entries.iCake.t2.timers.CoreTask;
import com.tenjava.entries.iCake.t2.utils.Chat;
import com.tenjava.entries.iCake.t2.utils.Utils;

public class WorldUtils {

    public static String WORLD_NAME = "core_world";
    private static World coreWorld;

    private static BukkitTask coreTask;
    private static ArrayList<Location> coreBlocks = new ArrayList<Location>();

    public static World getCoreWorld() {
        return coreWorld;
    }

    public static ArrayList<Location> getCoreBlocks() {
        return coreBlocks;
    }

    public static World createWorld() {
        Chat.broadcast("&e&oCreating world...");

        WorldCreator creator = new WorldCreator(WORLD_NAME);
        creator.generateStructures(false);

        World world = Bukkit.createWorld(creator);
        world.setAutoSave(false);
        world.setDifficulty(Difficulty.HARD);

        return coreWorld = world;
    }

    public static boolean removeWorld() {
        World world = Bukkit.getWorld(WORLD_NAME);

        if(world != null) {
            final File folder = world.getWorldFolder();
            Bukkit.unloadWorld(world, true);

            try {
                FileUtils.deleteDirectory(folder);
            } catch(IOException e) {
                Bukkit.getLogger().severe("FAILED TO DELETE WORLD. HELL WILL BURN DOWN ON THIS WORLD.");
                e.printStackTrace();
                Bukkit.getLogger().severe("----------[ error has end ] ---------------------");
            }
        }

        if(coreTask != null) {
            coreTask.cancel();
            coreTask = null;
        }

        coreWorld = null;
        return false;
    }

    public static void spawnCore() {
        if(coreTask != null) {
            coreTask.cancel();
            coreTask = null;
        }

        for(Player player : coreWorld.getPlayers()) {
            if(player.getGameMode() != GameMode.CREATIVE) {
                player.playSound(player.getEyeLocation(), Sound.ZOMBIE_WOODBREAK, 2f, -1f);
                player.playSound(player.getEyeLocation(), Sound.ZOMBIE_INFECT, 2f, -1.5f);
                player.playSound(player.getEyeLocation(), Sound.ZOMBIE_METAL, 2f, -1f);
            }
        }

        Location coreCentral = new Location(coreWorld, Utils.getRandom().nextBoolean() ? -Utils.getCentral(50, 200) : Utils.getCentral(50, 200), 0, Utils.getRandom().nextBoolean() ? -Utils.getCentral(50, 200) : Utils.getCentral(50, 200));
        coreCentral.setY(coreWorld.getHighestBlockYAt(coreCentral) + 10);
        coreCentral.getBlock().setType(Material.GOLD_BLOCK);
        
        for(int i = 0; i <= 3; i++) {
            Location loc = coreCentral.clone().add(0, i, 0);
            
            if(!coreBlocks.contains(loc)) {
                loc.getBlock().setType(Material.GOLD_BLOCK);
                coreBlocks.add(loc);
            }
        }

        Chat.broadcast("&9&nThe CORE has spawned! (X:" + coreCentral.getBlockX() + ", Z:" + coreCentral.getBlockZ() + ")");
        Chat.broadcast("");

        coreTask = new CoreTask(coreCentral).runTaskTimer(TenJava.getInstance(), 20 * 5, 1);
    }

}
