package com.github.sachin.tweakin.modules.morerecipes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.bstats.Metrics;
import com.github.sachin.tweakin.bstats.Metrics.AdvancedPie;
import com.github.sachin.tweakin.modules.morerecipes.recipes.BackToBlocksSlabs;
import com.github.sachin.tweakin.modules.morerecipes.recipes.BackToBlocksStairs;
import com.github.sachin.tweakin.modules.morerecipes.recipes.BlackDyeRecipe;
import com.github.sachin.tweakin.modules.morerecipes.recipes.CompactRecipes;
import com.github.sachin.tweakin.modules.morerecipes.recipes.CompatShulkerRecipe;
import com.github.sachin.tweakin.modules.morerecipes.recipes.CraftableCoralBlocks;
import com.github.sachin.tweakin.modules.morerecipes.recipes.CraftableHorseArmor;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyDispenser;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyMinecarts;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyRepeaters;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyStoneTools;
import com.github.sachin.tweakin.modules.morerecipes.recipes.MoreBlocks;
import com.github.sachin.tweakin.modules.morerecipes.recipes.MoreStoneCutterRecipes;
import com.github.sachin.tweakin.modules.morerecipes.recipes.PowderToGlass;
import com.github.sachin.tweakin.modules.morerecipes.recipes.RottenFleshToLeather;
import com.github.sachin.tweakin.modules.morerecipes.recipes.BlackDyeRecipe.Charcoal;
import com.github.sachin.tweakin.modules.morerecipes.recipes.universaldyeing.UniversalDyeing;
import com.github.sachin.tweakin.utils.ConfigUpdater;
import com.google.common.base.Enums;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.scheduler.BukkitRunnable;

public class MoreRecipesTweak extends BaseTweak{

    private FileConfiguration recipeFile;
    private final String RECIPE_FILE_NAME = "more-recipes.yml";
    private final List<NamespacedKey> simpleRecipes = new ArrayList<>();
    private List<BaseRecipe> baseRecipes;


    public MoreRecipesTweak(Tweakin plugin) {
        super(plugin, "more-recipes");
        
    }

    public List<BaseRecipe> getBaseRecipes() {
        if(baseRecipes == null) baseRecipes = new ArrayList<>();
        if(baseRecipes.isEmpty()){
            
            baseRecipes.add(new BackToBlocksSlabs(this));
            baseRecipes.add(new BackToBlocksStairs(this));
            baseRecipes.add(new EasyDispenser(this));
            baseRecipes.add(new EasyRepeaters(this));
            baseRecipes.add(new EasyMinecarts(this));
            baseRecipes.add(new CompactRecipes(this));
            baseRecipes.add(new CompatShulkerRecipe(this));
            baseRecipes.add(new EasyStoneTools(this));
            baseRecipes.add(new MoreStoneCutterRecipes(this));
            baseRecipes.add(new RottenFleshToLeather(this));
            baseRecipes.add(new UniversalDyeing(this));
            baseRecipes.add(new BlackDyeRecipe.Charcoal(this));
            baseRecipes.add(new BlackDyeRecipe.Coal(this));
            baseRecipes.add(new PowderToGlass(this));
            baseRecipes.add(new MoreBlocks(this));
            baseRecipes.add(new CraftableHorseArmor(this));
            baseRecipes.add(new CraftableCoralBlocks.TwoByTwo(this));
            baseRecipes.add(new CraftableCoralBlocks.ThreeByThree(this));
        }
        return baseRecipes;
    }

    // @EventHandler
    // public void onBreak(BlockBreakEvent event) {
    //     if (event.getBlock().getType() == Material.CRAFTING_TABLE) {
    //         event.setCancelled(true);
    //         Player player = event.getPlayer();
    //         // CraftingInventory inv = (CraftingInventory) player.openWorkbench(null, true).getTopInventory();
    //         Inventory inv = player.openInventory(Bukkit.createInventory(player, InventoryType.STONECUTTER, "Stonecutter")).getTopInventory();
    //         // player.openInventory(inv);
    //         new BukkitRunnable() {
    //             int count =0;
    //             List<NamespacedKey> recipes = baseRecipes.get(8).getRecipes();
    //             @Override
    //             public void run() {
    //                 if(count==recipes.size()){
    //                     cancel();
    //                     return;
    //                 }
    //                 // ShapelessRecipe recipe = (ShapelessRecipe) Bukkit.getRecipe(recipes.get(count));
    //                 // ShapedRecipe recipe = (ShapedRecipe) Bukkit.getRecipe(recipes.get(count));
    //                 StonecuttingRecipe recipe = (StonecuttingRecipe) Bukkit.getRecipe(recipes.get(count));
    //                 inv.setItem(0, recipe.getInput());
    //                 count++;
    //             }
    //         }.runTaskTimer(plugin, 0, 10);
    //         // for(NamespacedKey key : baseRecipes.get(0).getRecipes()){
                
    //         //     ShapedRecipe recipe = (ShapedRecipe) Bukkit.getRecipe(key);
    //         // }
    //     }
    // }

    @Override
    public void register() {
        super.register();
        saveRecipeFile();
        int registerdRecipes = 0;
        int enabledModules = 0;
        registerdRecipes += registerSimpleRecipes();
        for(BaseRecipe baseRecipe : getBaseRecipes()){
            if(baseRecipe instanceof UniversalDyeing){
                baseRecipe.unregister();
                baseRecipe.register();
                enabledModules++;
            }
            else if(recipeFile.getBoolean(baseRecipe.name, true)){
                baseRecipe.register();
                enabledModules++;
            }
            else{
                baseRecipe.unregister();
            }
            registerdRecipes = baseRecipe.getRecipes().size() + registerdRecipes;
        }
        plugin.getLogger().info("Registered " + registerdRecipes + " recipes");
        if(enabledModules == baseRecipes.size()){
            plugin.getLogger().info("Recipes go brrrrr..");
        }
    }

    @Override
    public void unregister() {
        super.unregister();
        for(NamespacedKey key : simpleRecipes){
            Bukkit.removeRecipe(key);
        }
        for (BaseRecipe baseRecipe : getBaseRecipes()) {
            if(baseRecipe.getRecipes().isEmpty()){
                continue;
            }
            baseRecipe.unregister();
        }
    }
    
    public void saveRecipeFile(){
        File file = new File(plugin.getDataFolder(), RECIPE_FILE_NAME);
        if(!file.exists()) {
            plugin.saveResource(RECIPE_FILE_NAME, false);
        }
        try {
            ConfigUpdater.update(plugin, RECIPE_FILE_NAME, file,new ArrayList<>(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeFile = YamlConfiguration.loadConfiguration(file);
    }

    public int registerSimpleRecipes(){
        int registeredRecipes = 0;
        for(String key : getRecipeFile().getConfigurationSection("simple-recipes").getKeys(false)){
            ConfigurationSection recipe = getRecipeFile().getConfigurationSection("simple-recipes."+key);
            ItemStack result = new ItemStack(Enums.getIfPresent(Material.class, recipe.getString("result","AIR").toUpperCase()).get());
            if(!recipe.getBoolean("enabled",true)) continue;
            if(result.getType()==Material.AIR) continue;
            result.setAmount(recipe.getInt("amount",1));
            String type = recipe.getString("type","none");
            NamespacedKey recipeKey = Tweakin.getKey("simple-recipes-"+recipe.getName()+"-"+type);
            if(type.equalsIgnoreCase("shaped") && recipe.contains("pattern") && recipe.contains("keys")){
                ShapedRecipe shapedRecipe = new ShapedRecipe(recipeKey, result);    
                List<String> patternList = recipe.getStringList("pattern");
                boolean invalidPattern = false;
                if(patternList.size()>3) invalidPattern = true;
                for(String s : patternList){
                    if(s.length()>3){
                        invalidPattern = true;
                    }
                }
                if(invalidPattern) continue;
                shapedRecipe.shape(patternList.toArray(new String[0]));
                for(String ing : recipe.getConfigurationSection("keys").getKeys(false)){
                    RecipeChoice choice = TweakItem.getIngredient(recipe.getString("keys."+ing), recipe.getBoolean("exact",false));
                    if(choice != null){
                        shapedRecipe.setIngredient(ing.charAt(0), choice);
                    }
                }
                addRecipe(recipeKey, shapedRecipe);
                registeredRecipes++;
            }
            else if(type.equalsIgnoreCase("shapeless")){
                ShapelessRecipe shapelessRecipe = new ShapelessRecipe(recipeKey, result);
                
                for(String ing : recipe.getStringList("ingredients")){
                    RecipeChoice choice = TweakItem.getIngredient(ing, recipe.getBoolean("exact",false));
                    if(choice != null){
                        shapelessRecipe.addIngredient(choice);
                    }
                }
                addRecipe(recipeKey, shapelessRecipe);
                registeredRecipes++;
            }
            
        }
        return registeredRecipes;
    }

    private void addRecipe(NamespacedKey key,Recipe recipe){
        if(Bukkit.getRecipe(key) == null){
            Bukkit.addRecipe(recipe);
            simpleRecipes.add(key);
        }
    }

    public FileConfiguration getRecipeFile() {
        return recipeFile;
    }

    

    public void addBstatsGraph(Metrics metrics){
        metrics.addCustomChart(new AdvancedPie("Enabled-Recipes", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> map = new HashMap<>();
                for(BaseRecipe recipe : baseRecipes){
                    if(recipe instanceof UniversalDyeing){
                        continue;
                    }
                    if(recipeFile.getBoolean(recipe.name,true)){
                        map.put(recipe.name, 1);
                    }
        
                }
                return map;
            }
        }));

        metrics.addCustomChart(new AdvancedPie("Universal-Dyeing-Recipes-Single", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> map = new HashMap<>();
                for(BaseRecipe recipe : baseRecipes){
                    if(recipe instanceof UniversalDyeing){
                        ConfigurationSection section = recipeFile.getConfigurationSection(recipe.name);
                        if(section.getBoolean("bed.single")){
                            map.put("bed",1);
                        }
                        if(section.getBoolean("wool.single")){
                            map.put("wool",1);
                        }
                        if(section.getBoolean("carpet.single")){
                            map.put("carpet",1);
                        }
                        if(section.getBoolean("concrete.single")){
                            map.put("concrete",1);
                        }
                        if(section.getBoolean("concrete_powder.single")){
                            map.put("concrete_powder",1);
                        }
                        if(section.getBoolean("terracotta")){
                            map.put("terracotta",1);

                        }
                        if(section.getBoolean("stained_glass.single")){
                            map.put("stained_glass", 1);
                        }
                        if(section.getBoolean("stained_glass_pane.single")){
                            map.put("stained_glass_pane", 1);
                        }

                    }
                    
        
                }
                return map;
            }
        }));

        metrics.addCustomChart(new AdvancedPie("Universal-Dyeing-Recipes-Sorounded", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> map = new HashMap<>();
                for(BaseRecipe recipe : baseRecipes){
                    if(recipe instanceof UniversalDyeing){
                        ConfigurationSection section = recipeFile.getConfigurationSection(recipe.name);
                        if(section.getBoolean("bed.sorounded")){
                            map.put("bed",1);
                        }
                        if(section.getBoolean("wool.sorounded")){
                            map.put("wool",1);
                        }
                        if(section.getBoolean("carpet.sorounded")){
                            map.put("carpet",1);
                        }
                        if(section.getBoolean("concrete.sorounded")){
                            map.put("concrete",1);
                        }
                        if(section.getBoolean("concrete_powder.sorounded")){
                            map.put("concrete_powder",1);
                        }
                        if(section.getBoolean("terracotta")){
                            map.put("terracotta",1);

                        }
                        if(section.getBoolean("stained_glass.sorounded")){
                            map.put("stained_glass", 1);
                        }
                        if(section.getBoolean("stained_glass_pane.sorounded")){
                            map.put("stained_glass_pane", 1);
                        }
                        
                    }
                    
        
                }
                return map;
            }
        }));
    }

    
    


    
}