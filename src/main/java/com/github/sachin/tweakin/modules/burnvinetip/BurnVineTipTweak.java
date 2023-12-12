package com.github.sachin.tweakin.modules.burnvinetip;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.CustomBlockData;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

// Permission: tweakin.burnvinetip.use
@Tweak(name = "burn-vine-tip")
public class BurnVineTipTweak extends BaseTweak implements Listener{

    private final NamespacedKey key = Tweakin.getKey("stop-vine-growth");


    @EventHandler
    public void vineGrowEvent(BlockSpreadEvent e){
        if(e.getSource().getType() != Material.VINE) return;
        Location loc = e.getSource().getLocation();
        CustomBlockData data = new CustomBlockData(loc);
        if(data.has(key, PersistentDataType.INTEGER)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void vineLiteEvent(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND) return;
        if(e.getClickedBlock().getType() != Material.VINE) return;
        if(e.getItem() == null) return;
        if(e.getItem().getType() != Material.FLINT_AND_STEEL) return;
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();
        if(!hasPermission(player, Permissions.BURNVINETIP_USE)) return;
        if(getBlackListWorlds().contains(block.getWorld().getName())) return;
        if(block.getRelative(BlockFace.DOWN).getType() == Material.VINE) return;
        CustomBlockData blockData = new CustomBlockData(block.getLocation());
        if(!blockData.has(key, PersistentDataType.INTEGER)){
            blockData.set(key, PersistentDataType.INTEGER, 1);
            e.setCancelled(true);
            World world = block.getWorld();
            if(player.getGameMode() == GameMode.SURVIVAL){
                ItemStack item = e.getItem();
                ItemMeta meta = item.getItemMeta();
                Damageable damageable = (Damageable) meta;
                damageable.setDamage(damageable.getDamage()+1);
                item.setItemMeta(meta);
            }
            world.playSound(block.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 0.5f, 1f);
            player.swingMainHand();
            getVineBlock(block);
        }
        
    }


    private Block getVineBlock(Block clickedBlock){
        Block upBlock = clickedBlock.getRelative(BlockFace.UP);
        if(upBlock.getType() != Material.VINE){
            return upBlock;
        }
        
        else if(upBlock.getType() == Material.VINE){
            CustomBlockData blockData = new CustomBlockData(upBlock.getLocation());
            if(!blockData.has(key, PersistentDataType.INTEGER)){
                blockData.set(key, PersistentDataType.INTEGER, 1);
                return getVineBlock(upBlock);
            }
        }
        return null;
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e){
        if(e.getBlock().getType() == Material.VINE){
            removeVine(e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void pistonPushBlock(BlockPistonExtendEvent e){
        for(Block b : e.getBlocks()){
            if(b.getType() == Material.VINE){
                removeVine(b.getLocation());
            }
        }
    }

    @EventHandler
    public void pistonRetractBlock(BlockPistonRetractEvent e){
        for(Block b : e.getBlocks()){
            if(b.getType() == Material.VINE){
                removeVine(b.getLocation());
            }
        }
    }

    private void removeVine(Location loc){
        CustomBlockData data = new CustomBlockData(loc);
        if(data.has(key, PersistentDataType.INTEGER)){
            data.remove(key);
        }
    }
}
