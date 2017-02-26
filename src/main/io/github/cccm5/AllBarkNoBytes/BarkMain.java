package io.github.cccm5.AllBarkNoBytes;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

public class BarkMain extends JavaPlugin implements Listener{
    private Logger logger;
    private final String TAG = ChatColor.RED +  "[" + ChatColor.DARK_RED + "AllBarkNoBytes" + ChatColor.RED + "] " + ChatColor.RESET ;
    private ItemStack wandItem;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        getServer().getPluginManager().registerEvents(this, this);
        wandItem= new ItemStack(Material.GOLD_HOE);
        ItemMeta meta = wandItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Bark wand");
        wandItem.setItemMeta(meta);
    }
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if(e.hasItem() && e.getItem().isSimilar(wandItem)){
            if(e.getAction()== Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType()== Material.LOG || e.getClickedBlock().getType()== Material.LOG_2)){
                BlockState state = e.getClickedBlock().getState();
                if(((Tree)state.getData()).getDirection() == BlockFace.SELF)
                    return;
                if(!e.getPlayer().hasPermission("AllBarkNoBytes.wand")) {
                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(TAG + "You don't have permision for that!"));
                    return;
                }
                state.setData(new Tree(((Tree)state.getData()).getSpecies(), BlockFace.SELF));
                state.update();
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(TAG + "Block succesfully changed!"));
            }
            else
                e.setCancelled(true);
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
}
