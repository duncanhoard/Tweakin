package com.github.sachin.tweakin.modules.silencemobs;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

//Permission: tweakin.silencemobs.silence,tweakin.silencemobs.unsilence
@Tweak(name = "silence-mobs",clashingTeaksTweak = "Silence Mobs")
public class SilenceMobsTweak extends BaseTweak implements Listener{

    private List<String> silenceNames = new ArrayList<>();
    private List<String> unsilenceNames = new ArrayList<>();
    private List<String> blackListAnimals = new ArrayList<>(); 


    @Override
    public void reload() {
        super.reload();
        this.silenceNames = getConfig().getStringList("silence-names");
        this.unsilenceNames = getConfig().getStringList("unsilence-names");
        this.blackListAnimals = getConfig().getStringList("black-list-mobs");
    }


    @EventHandler
    public void nameTagUseEvent(PlayerInteractEntityEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        if(getBlackListWorlds().contains(e.getPlayer().getWorld().getName())) return;
        Entity entity = e.getRightClicked();
        if(blackListAnimals.contains(entity.getType().toString())) return;
        ItemStack clickedItem = player.getInventory().getItemInMainHand();
        if(clickedItem == null) return;
        if(clickedItem.getType() != Material.NAME_TAG) return;
        ItemMeta meta = clickedItem.getItemMeta();
        if(meta == null) return;
        boolean fired = false;
        if(silenceNames.contains(meta.getDisplayName()) && !entity.isSilent() && hasPermission(player, Permissions.SILENCEMOBS_SILENCE)){
            entity.setSilent(true);
            player.sendMessage(getTweakManager().getMessageManager().getMessage("mob-silenced").replace("%name%", entity.getType().toString()));
            fired = true;
        }
        else if(unsilenceNames.contains(meta.getDisplayName()) && entity.isSilent() && hasPermission(player, Permissions.SILENCEMOBS_UNSILENCE)){
            entity.setSilent(false);
            player.sendMessage(getTweakManager().getMessageManager().getMessage("mob-unsilenced").replace("%name%", entity.getType().toString()));
            fired = true;
        }
        if(!fired) return;
        player.swingMainHand();
        if(!getConfig().getBoolean("rename-mob",false)){
            e.setCancelled(true);
        }
        if(player.getGameMode() == GameMode.SURVIVAL && getConfig().getBoolean("consume-tag",false)){
            clickedItem.setAmount(clickedItem.getAmount()-1);
        }
    }
    
}
