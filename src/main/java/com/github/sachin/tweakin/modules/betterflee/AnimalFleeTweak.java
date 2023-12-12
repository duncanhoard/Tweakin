package com.github.sachin.tweakin.modules.betterflee;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

@Tweak(name = "animal-flee")
// tweakin.fleemobs.bypass
public class AnimalFleeTweak extends BaseTweak implements Listener{

    public static final NamespacedKey key = Tweakin.getKey("animal-flee-flag");



    @EventHandler
    public void onAnimalDamage(EntityDamageByEntityEvent e){
        if(e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK){
            if(containsWorld(e.getEntity().getWorld())) return;
            if(e.getDamager() instanceof Player){
                Player player = (Player) e.getDamager();
                
                if(hasPermission(player, Permissions.FLEEMOBS)) return;
                Entity attacked = e.getEntity();
                if(getConfig().getStringList("fleeable-mobs").contains(attacked.getType().toString())){
                    if(attacked.getPersistentDataContainer().has(key, PersistentDataType.INTEGER) && getConfig().getBoolean("ignore-breeded")){
                        return;
                    }
                    plugin.getPrilib().getNmsHandler().avoidPlayer(attacked,player,getConfig(),key);
//                    plugin.getNmsHelper().avoidPlayer(attacked, player,getConfig());
                }
            }
        }
    }


    @EventHandler
    public void onAnimalBreed(EntityBreedEvent e){
        if(getConfig().getStringList("fleeable-mobs").contains(e.getEntityType().toString())){
            for(LivingEntity l : Arrays.asList(e.getMother(),e.getFather(),e.getEntity())){
                l.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            }
        }
    }



    
}
