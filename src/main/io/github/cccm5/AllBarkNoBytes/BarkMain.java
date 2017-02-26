package io.github.cccm5.AllBarkNoBytes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Tree;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import io.github.cccm5.AllBarkNoBytes.actionbar.*;

public class BarkMain extends JavaPlugin implements Listener{
    private Logger logger;
    private final String TAG = ChatColor.RED +  "[" + ChatColor.DARK_RED + "AllBarkNoBytes" + ChatColor.RED + "] " + ChatColor.RESET ;
    private ItemStack wandItem;
    private Actionbar actionbar;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        if(!setupActionbar()) {
            logger.severe("Unsupourted version found, disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }
        getServer().getPluginManager().registerEvents(this, this);
        wandItem= new ItemStack(Material.GOLD_HOE);
        ItemMeta meta = wandItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Bark wand");
        wandItem.setItemMeta(meta);
    }
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if(e.getAction()== Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType()== Material.LOG || e.getClickedBlock().getType()== Material.LOG_2) &&e.hasItem() && e.getItem().isSimilar(wandItem)){
            BlockState state = e.getClickedBlock().getState();
            if(((Tree)state.getData()).getDirection() == BlockFace.SELF)
                return;
            state.setData(new Tree(((Tree)state.getData()).getSpecies(), BlockFace.SELF));
            state.update();
            //e.getPlayer().sendMessage(TAG + "Block succesfully changed!");
            e.getPlayer().sendTitle("",TAG + "Block succesfully changed!");
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(TAG + "You need to be a player to use that command!");
            return true;
        }
        Player player = (Player)sender;
        boolean emptySlot=false;
        for(ItemStack i : player.getInventory())
            if(i==null || i.getType() == Material.AIR) {
                emptySlot = true;
                break;
            }
        if(!emptySlot) {
            player.sendMessage(TAG + "You have no space for the wand!");
        return true;
        }
        player.getInventory().addItem(wandItem);
        return true;
    }

    private boolean setupActionbar() {

        String version;

        try {

            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }

        getLogger().info("Your server is running version " + version);

        if (version.equals("v1_9_R1")) {
            //server is running 1.8-1.8.1 so we need to use the 1.8 R1 NMS class
            actionbar = new Actionbar_1_9_R1();

        } else if (version.equals("v1_10_R1")) {
            //server is running 1.8.3 so we need to use the 1.8 R2 NMS class
            actionbar = new Actionbar_1_10_R1();
        }
        // This will return true if the server version was compatible with one of our NMS classes
        // because if it is, our actionbar would not be null
        return actionbar != null;
    }


}
