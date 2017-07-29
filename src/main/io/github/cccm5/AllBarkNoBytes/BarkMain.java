package io.github.cccm5.AllBarkNoBytes;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Tree;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


public class BarkMain extends JavaPlugin implements Listener{
    private Logger logger;
    private final String TAG = ChatColor.RED +  "[" + ChatColor.DARK_RED + "AllBarkNoBytes" + ChatColor.RED + "] " + ChatColor.RESET,
                         ITEMTAG = "" + ChatColor.BLACK+ ChatColor.AQUA+ChatColor.RED+ChatColor.RESET;
    private ItemStack wandItem;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        getServer().getPluginManager().registerEvents(this, this);
        {
            wandItem = new ItemStack(Material.GOLD_HOE);
            ItemMeta meta = wandItem.getItemMeta();
            meta.setDisplayName(ITEMTAG + "Bark wand");
            wandItem.setItemMeta(meta);
        }

        for(TreeSpecies species : TreeSpecies.values()) {
            ItemStack item = new Tree(species).toItemStack(4);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("Smooth " + speciesToName(species) + " Log");
            item.setItemMeta(meta);
            logger.info("Setting recipe for " + item.getItemMeta().getDisplayName() + " of type " + item.getData());
            registerRecipe(item);
        }
    }
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if(e.hasItem() && e.getItem().isSimilar(wandItem)){
            if(e.getAction()== Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Tree){
                BlockState state = e.getClickedBlock().getState();
                if(((Tree)state.getData()).getDirection() == BlockFace.SELF)
                    return;
                if(!e.getPlayer().hasPermission("AllBarkNoBytes.wand")) {
                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("You don't have permission for that!"));
                    return;
                }
                if(e.isCancelled()) {
                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("You don't have permission for that!"));
                    return;
                }
                state.setData(new Tree(((Tree)state.getData()).getSpecies(), BlockFace.SELF));
                state.update();
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("Block successfully changed!"));
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(!(e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().getDisplayName().startsWith(ITEMTAG)))
            return;
        if(e.getBlock().getState().getData() instanceof Tree) {
            BlockState state = e.getBlock().getState();
                state.setData(new Tree(((Tree) state.getData()).getSpecies(), BlockFace.SELF));
                state.update();
        }
    }

    private void registerRecipe(ItemStack item){
        ItemStack result = item.clone();
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ITEMTAG + meta.getDisplayName());
        result.setItemMeta(meta);
        ShapedRecipe customRecipe = new ShapedRecipe(result);
        customRecipe.shape("II","II");
        customRecipe.setIngredient('I',item.getData());
        Bukkit.addRecipe(customRecipe);
    }

    /*
     * Attempts to generate an item name based on the TreeSpecies of a tree without using nms
     * @param species the species to retrieve a display name of
     * @return the display name of the species
     */
    private static String speciesToName(TreeSpecies species){
        if(species == TreeSpecies.GENERIC)
            return "Oak";
        if(species == TreeSpecies.REDWOOD)
            return "Spruce";
        StringBuilder build = new StringBuilder();
        for(int i =0; i<species.toString().length();i++){
            if(species.toString().charAt(i) == '_')
                build.append(' ');
            if(i==0 || species.toString().charAt(i-1) == ' ')
                build.append(Character.toUpperCase(species.toString().charAt(i)));
            else
                build.append(Character.toLowerCase(species.toString().charAt(i)));
        }
        return build.toString();
    }

}
