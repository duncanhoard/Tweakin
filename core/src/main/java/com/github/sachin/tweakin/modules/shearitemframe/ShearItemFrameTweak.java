package com.github.sachin.tweakin.modules.shearitemframe;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

@Tweak(name = "shear-item-frame",clashingTeaksTweak = "Invisible Item Frames")
public class ShearItemFrameTweak extends BaseTweak implements Listener{

    private final NamespacedKey key = Tweakin.getKey("tweakin-frame");
    private SIFFlag flag;


    @Override
    public void onLoad() {
        super.onLoad();
        if(plugin.isWorldGuardEnabled){
            this.flag = (SIFFlag) plugin.getWGFlagManager().getFlag(TConstants.SIF_FLAG);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShear(PlayerInteractEntityEvent e){
        if(!(e.getRightClicked() instanceof ItemFrame)) return;
        Player player = e.getPlayer();
        if(plugin.griefCompat != null && !plugin.griefCompat.canBuild(player,e.getRightClicked().getLocation(), Material.SHEARS)) return;
        if(player.isSneaking() || !hasPermission(player, Permissions.SHEARITEMFRAME)) return;
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(flag != null && !flag.queryFlag(player, e.getRightClicked().getLocation())) return;
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand().clone();
        if(item.getType() != Material.SHEARS) return;
        ItemFrame frame = (ItemFrame) e.getRightClicked();
        if(frame.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;
        if(!frame.getItem().getType().isAir()){
            frame.setVisible(false);
            player.getWorld().playSound(frame.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
            frame.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        }
    }

    // @EventHandler
    // public void onSkeleSpawn(EntitySpawnEvent e){
    //     if(e.getEntity().getType() == EntityType.SKELETON){
    //         Skeleton skele = (Skeleton) e.getEntity();
    //         skele.setAI(false);
    //         skele.setInvulnerable(true);
    //         skele.teleport(skele.getLocation().clone().subtract(0,2,0));
    //         new BukkitRunnable(){
    //             int i = 0;
    //             Location loc = skele.getLocation().clone().subtract(0,2,0);
    //             public void run() {
    //                 i++;
    //                 if(i==200){
    //                     skele.setAI(true);
    //                     skele.setInvulnerable(false);
    //                     cancel();
    //                 }
    //                 else{
    //                     loc = loc.add(0, 0.01, 0);
    //                     skele.teleport(loc);
    //                 }
                    
    //             };
    //         }.runTaskTimer(plugin, 0, 1);
            
    //     }
    // }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof ItemFrame){
            ItemFrame frame = (ItemFrame) e.getEntity();
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(frame.getItem().getType().isAir() && frame.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
                        frame.setVisible(true);
                        frame.getPersistentDataContainer().remove(key);
                    }
                
                }
            }.runTaskLater(plugin, 2);
        }
    }

    
}
