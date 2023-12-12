package com.github.sachin.tweakin.modules.noteblock;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;

import java.util.*;

@Tweak(name = "noteblock-mob-sounds")
public class NoteBlockHeadsTweak extends BaseTweak implements Listener{

    private final List<BlockFace> VALID_FACES = Arrays.asList(BlockFace.DOWN,BlockFace.UP,BlockFace.EAST,BlockFace.WEST,BlockFace.NORTH,BlockFace.SOUTH);;
    private Map<String,Sound> allowedHeads;


    @EventHandler
    @SuppressWarnings("deprecation")
    public void noteBlockPlay(NotePlayEvent e){
        Block block = e.getBlock();
        if(getBlackListWorlds().contains(block.getWorld().getName())) return;
        Sound sound = null;
        for(BlockFace b : VALID_FACES){
            Block blockSide = block.getRelative(b);
            sound = getSound(blockSide.getType());
            if(sound != null) break;
        }
        if(sound != null){
            e.setCancelled(true);
            float pitch = (float) Math.pow(2.0, (e.getNote().getId() - 12) / 12.0);
            block.getWorld().playSound(block.getLocation(), sound, SoundCategory.BLOCKS, 1, pitch);
        }
    }

    @Override
    public void reload() {
        super.reload();
        createAllowedHeads();
        List<String> blackListHeads = getConfig().getStringList("black-list-heads");
        if(blackListHeads == null) return;
        for(String h : blackListHeads){
            if(allowedHeads.containsKey(h)){
                allowedHeads.remove(h);
            }
        }
    }

    private void createAllowedHeads(){
        allowedHeads = new HashMap<>();
        // if()
        // allowedHeads.clear();
        allowedHeads.put("CREEPER_WALL_HEAD", Sound.ENTITY_CREEPER_PRIMED);
        allowedHeads.put("SKELETON_WALL_SKULL", Sound.ENTITY_SKELETON_AMBIENT);
        allowedHeads.put("ZOMBIE_WALL_HEAD", Sound.ENTITY_ZOMBIE_AMBIENT);
        allowedHeads.put("DRAGON_WALL_HEAD", Sound.ENTITY_ENDER_DRAGON_AMBIENT);
        allowedHeads.put("WITHER_SKELETON_WALL_SKULL", Sound.ENTITY_WITHER_SKELETON_AMBIENT);
    }


    private Sound getSound(Material type){
        for(String h : allowedHeads.keySet()){
            if(type.name() == h){
                return allowedHeads.get(h);
            }
            
        }
        return null;
    }

    
    
}
