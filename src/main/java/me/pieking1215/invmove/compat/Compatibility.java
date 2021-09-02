package me.pieking1215.invmove.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class Compatibility {

    private static LinkedHashMap<String, ModCompatibility> compats;

    public static void loadCompatibility(){
        // it's linked since the order matters
        compats = new LinkedHashMap<>();

        LinkedHashMap<String, Supplier<ModCompatibility>> compatMods = new LinkedHashMap<>();

        // mods with special behavior in shouldAllowMovement or
        //   shouldDisableBackground that need to have higher priority
        compatMods.put("roughlyenoughitems", REICompatibility::new);
//        compatMods.put("jei"                 , "me.pieking1215.invmove.compat.JEICompatibility");
//        compatMods.put("quark"               , "me.pieking1215.invmove.compat.QuarkCompatibility");
//
//        compatMods.put("cloth-config"        , "me.pieking1215.invmove.compat.ClothConfigCompatibility");
//        compatMods.put("immersiveengineering", "me.pieking1215.invmove.compat.ImmersiveEngineeringCompatibility");
//        compatMods.put("engineersdecor"      , "me.pieking1215.invmove.compat.EngineersDecorCompatibility");
//        compatMods.put("computercraft"       , "me.pieking1215.invmove.compat.CCTweakedCompatibility");
//        compatMods.put("xercamusic"          , "me.pieking1215.invmove.compat.MusicMakerModCompatibility");
//        compatMods.put("embellishcraft"      , "me.pieking1215.invmove.compat.EmbellishCraftCompatibility");
//        compatMods.put("refinedstorage"      , "me.pieking1215.invmove.compat.RefinedStorageCompatibility");
//        compatMods.put("cfm"                 , "me.pieking1215.invmove.compat.CrayfishFurnitureCompatibility");
//        //compatMods.put("charm"               , "me.pieking1215.invmove.compat.CharmCompatibility");
//        //compatMods.put("create"              , "me.pieking1215.invmove.compat.CreateCompatibility");
//        compatMods.put("curios"              , "me.pieking1215.invmove.compat.CuriosCompatibility");
//        compatMods.put("ironchest"           , "me.pieking1215.invmove.compat.IronChestsCompatibility");
//        compatMods.put("cookingforblockheads", "me.pieking1215.invmove.compat.CookingForBlockheadsCompatibility");
//        compatMods.put("patchouli"           , "me.pieking1215.invmove.compat.PatchouliCompatibility");
//        compatMods.put("botania"             , "me.pieking1215.invmove.compat.BotaniaCompatibility");
//        //compatMods.put("enderstorage"        , "me.pieking1215.invmove.compat.EnderStorageCompatibility");
//        compatMods.put("mekanism"            , "me.pieking1215.invmove.compat.MekanismCompatibility");
//        compatMods.put("mekanismgenerators"  , "me.pieking1215.invmove.compat.MekanismGeneratorsCompatibility");
//        compatMods.put("waystones"           , "me.pieking1215.invmove.compat.WaystonesCompatibility");
//        compatMods.put("industrialforegoing" , "me.pieking1215.invmove.compat.IndustrialForegoingCompatibility");
//        compatMods.put("locks"               , "me.pieking1215.invmove.compat.LocksCompatibility");

        for (String s : compatMods.keySet()){
            if(FabricLoader.getInstance().isModLoaded(s)){
                try {
                    compats.put(s, compatMods.get(s).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static Optional<Boolean> shouldAllowMovement(Screen screen) {
        for(ModCompatibility c : compats.values()){
            if(c == null) continue;
            try{
                Optional<Boolean> ob = c.shouldAllowMovement(screen);
                if(ob != null && ob.isPresent()){
                    return ob;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        // stupid way of doing it but whatever
        if(screen.getClass().getName().equals("net.optifine.gui.GuiChatOF")) return Optional.of(false);

        return Optional.empty();
    }

    public static Optional<Boolean> shouldDisableBackground(Screen screen) {
        for(ModCompatibility c : compats.values()){
            try{
                Optional<Boolean> ob = c.shouldDisableBackground(screen);
                if(ob.isPresent()){
                    return ob;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        // stupid way of doing it but whatever
        if(screen.getClass().getName().equals("net.optifine.gui.GuiChatOF")) return Optional.of(false);

        return Optional.empty();
    }

    public static HashMap<String, ModCompatibility> getCompatibilities(){
        return compats;
    }

}
