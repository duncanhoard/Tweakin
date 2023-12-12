package com.github.sachin.tweakin.modules.bettersignedit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

// tweakin.bettersignedit.use
@Tweak(name = "better-sign-edit")
public class BetterSignEditTweak extends BaseTweak implements Listener{

    public ProtocolManager manager;
    private UpdateSignListener listener;
    private BSEFlag flag;
    public final NamespacedKey key = Tweakin.getKey("sign-edited");
    public List<Player> lines = new ArrayList<>();


    @Override
    public void onLoad() {
        this.manager = ProtocolLibrary.getProtocolManager();
        this.listener = new UpdateSignListener(plugin,this);
        if(plugin.isWorldGuardEnabled){
            this.flag = (BSEFlag) plugin.getWGFlagManager().getFlag(TConstants.BSE_FLAG);
        }
    }

    @Override
    public void register() {
        super.register();
        
        manager.addPacketListener(listener);
    }

    @Override
    public void unregister() {
        super.unregister();
        manager.removePacketListener(listener);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignClick(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND || e.getItem() != null) return;
        Player player = e.getPlayer();
        if(!player.isSneaking()) return;
        if(!e.getClickedBlock().getType().toString().endsWith("_SIGN") || !hasPermission(player, Permissions.BETTERSIGNEDIT) || getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(flag != null && !flag.queryFlag(player,e.getClickedBlock().getLocation())){
            return;
        }

        Sign sign = (Sign) e.getClickedBlock().getState();
        sign.getPersistentDataContainer().set(key, PersistentDataType.STRING, "");
        
        sign.update(true);
        lines.add(player);
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        packet.getBlockPositionModifier().write(0, new BlockPosition(e.getClickedBlock().getX(), e.getClickedBlock().getY(), e.getClickedBlock().getZ()));
        try {
            manager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }


    
    
}


