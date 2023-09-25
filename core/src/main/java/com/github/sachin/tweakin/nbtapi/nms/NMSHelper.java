package com.github.sachin.tweakin.nbtapi.nms;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public abstract class NMSHelper {


    public abstract NMSHelper newItem(ItemStack item);

    
    
    public abstract void setString(String key,String value);
    public abstract void setBoolean(String key,boolean value);
    public abstract void setInt(String key,int value);
    public abstract void setLong(String key,long value);
    public abstract void setDouble(String key,double value);
    
    
    public abstract String getString(String key);
    public abstract boolean getBoolean(String key);
    public abstract int getInt(String key);
    public abstract long getLong(String key);
    public abstract double getDouble(String key);
    
    
    public abstract boolean hasKey(String key);
    
    public abstract ItemStack getItem();
    
    public abstract void removeKey(String key);

    public abstract void placeWater(Block block);

    public abstract void attack(Player player,Entity target);

    public abstract boolean placeItem(Player player,Location location,ItemStack item,BlockFace hitFace,String tweakName,boolean playSound);

    public abstract void spawnVillager(Villager villager,boolean update);
    
    public abstract void avoidPlayer(Entity entity,Player player,ConfigurationSection config);


    public abstract boolean matchAxoltlVariant(Entity entity,String color);
    public abstract boolean isScreamingGoat(Entity entity);

    public abstract List<Entity> getEntitiesWithinRadius(int radius,Entity center);



    public abstract void applyHeadTexture(SkullMeta meta,String b64);

    public abstract ItemStack createMap(Location dist,byte zoom,boolean biomePreview);

    public abstract boolean matchFrogVariant(Entity entity, String variant);

    public Object getBlockHighlightPacket(Location loc,int color){return null;}
}
