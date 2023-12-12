package com.github.sachin.tweakin.modules.swingthroughgrass;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

import java.util.function.Predicate;


// permission: tweakin.swingthroughgrass
@Tweak(name = "swing-through-grass")
public class SwingThroughGrassTweak extends BaseTweak implements Listener{



    @EventHandler
    public void onSwordSwing(PlayerInteractEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getClickedBlock() == null) return;
        if(!e.getClickedBlock().isPassable()) return;
        if(e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if(!hasPermission(player, Permissions.SWINGGRASS)) return;
        Predicate<Entity> p = new EntityTest<>(player);
        // RayTraceResult raytrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 3.5,0,p);
        RayTraceResult raytrace = player.getWorld().rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), 3.5, FluidCollisionMode.NEVER, true, 0,p);
        if(raytrace != null){
            if(raytrace.getHitEntity() != null && raytrace.getHitBlock() == null){
                
                Entity hitEntity = raytrace.getHitEntity();
                getPlugin().getNMSHandler().attack(player, hitEntity);
                
            }
        }
    }

    
}

