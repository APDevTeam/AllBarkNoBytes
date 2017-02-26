package io.github.cccm5.AllBarkNoBytes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Tree;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BarkMain extends JavaPlugin implements Listener{
    private FileConfiguration config;
    private Logger logger;
    private final String TAG = ChatColor.RED +  "[" + ChatColor.DARK_RED + "AllBarkNoBytes" + ChatColor.RED + "] " + ChatColor.RESET ;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        getServer().getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if(e.getAction()== Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType()== Material.LOG || e.getClickedBlock().getType()== Material.LOG_2)){
            BlockState state = e.getClickedBlock().getState();
            if(((Tree)state.getData()).getDirection() == BlockFace.SELF)
                return;
            state.setData(new Tree(((Tree)state.getData()).getSpecies(), BlockFace.SELF));
            state.update();
            e.getPlayer().sendMessage(TAG + "Block succesfully changed!");
        }
    }

}
