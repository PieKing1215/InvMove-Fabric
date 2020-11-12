package me.pieking1215.invmove;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class InvMoveConfig {

    public static final General GENERAL = new General();

    public static boolean hasFinalizedConfig = false;
    public static File configFile;

    public static class General {
        public final AtomicBoolean enabled = new AtomicBoolean(true);
        public final AtomicBoolean moveInInventories = new AtomicBoolean(true);
        public final AtomicBoolean sneakInInventories = new AtomicBoolean(false);
        public final AtomicBoolean jumpInInventories = new AtomicBoolean(true);
        public final AtomicBoolean dismountInInventories = new AtomicBoolean(false);
        public final AtomicBoolean textFieldDisablesMovement = new AtomicBoolean(true);
        public final AtomicBoolean uiBackground = new AtomicBoolean(true);
        public final AtomicBoolean debugDisplay = new AtomicBoolean(false);
    }

    public static void doneLoading() {
        hasFinalizedConfig = true;

        try {
            File f = new File(FabricLoader.getInstance().getConfigDirectory(), "invmove.json");
            f.getParentFile().mkdirs();
            if (!f.exists()) f.createNewFile();
            configFile = f;

            JsonReader jr = new JsonReader(new FileReader(f));
            JsonElement jp = new JsonParser().parse(jr);
            if (jp.isJsonObject()) {
                JsonObject obj = jp.getAsJsonObject();
                GENERAL.enabled.set(obj.has("enabled") ? obj.get("enabled").getAsBoolean() : true);
                GENERAL.moveInInventories.set(obj.has("moveInInventories") ? obj.get("moveInInventories").getAsBoolean() : true);
                GENERAL.sneakInInventories.set(obj.has("sneakInInventories") ? obj.get("sneakInInventories").getAsBoolean() : false);
                GENERAL.jumpInInventories.set(obj.has("jumpInInventories") ? obj.get("jumpInInventories").getAsBoolean() : true);
                GENERAL.dismountInInventories.set(obj.has("dismountInInventories") ? obj.get("dismountInInventories").getAsBoolean() : false);
                GENERAL.textFieldDisablesMovement.set(obj.has("textFieldDisablesMovement") ? obj.get("textFieldDisablesMovement").getAsBoolean() : true);
                GENERAL.uiBackground.set(obj.has("uiBackground") ? obj.get("uiBackground").getAsBoolean() : true);
                GENERAL.debugDisplay.set(obj.has("debugDisplay") ? obj.get("debugDisplay").getAsBoolean() : false);
            }
            jr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Screen registerClothConfig(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(new TranslatableText("config.invmove.title"));
        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/spruce_planks.png"));
        builder.transparentBackground();

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("key.invmove.category.general"));
        general.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.enable"), getBoolSafe(GENERAL.enabled, true)).setDefaultValue(true).setSaveConsumer(GENERAL.enabled::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.enable").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        //general.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.debugDisplay"), getBoolSafe(GENERAL.debugDisplay, false)).setDefaultValue(false).setSaveConsumer(GENERAL.debugDisplay::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.debugDisplay").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());

        // movement

        ConfigCategory movement = builder.getOrCreateCategory(new TranslatableText("key.invmove.category.movement"));
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.enable"), getBoolSafe(GENERAL.moveInInventories, true)).setDefaultValue(true).setSaveConsumer(GENERAL.moveInInventories::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.enable").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.sneak"), getBoolSafe(GENERAL.sneakInInventories, false)).setDefaultValue(false).setSaveConsumer(GENERAL.sneakInInventories::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.sneak").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.jump"), getBoolSafe(GENERAL.jumpInInventories, true)).setDefaultValue(true).setSaveConsumer(GENERAL.jumpInInventories::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.jump").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.dismount"), getBoolSafe(GENERAL.dismountInInventories, false)).setDefaultValue(false).setSaveConsumer(GENERAL.dismountInInventories::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.dismount").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.textFieldDisables"), getBoolSafe(GENERAL.textFieldDisablesMovement, true)).setDefaultValue(true).setSaveConsumer(GENERAL.textFieldDisablesMovement::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.textFieldDisables").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());

//        SubCategoryBuilder movementTypes = eb.startSubCategory(new TranslatableText("key.invmove.category.types"));
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.inventory"),    getBoolSafe(UI_MOVEMENT.inventory, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.inventory::set    ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.creative"),     getBoolSafe(UI_MOVEMENT.creative, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.creative::set     ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.crafting"),     getBoolSafe(UI_MOVEMENT.crafting, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.crafting::set     ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.chest"),        getBoolSafe(UI_MOVEMENT.chest, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.chest::set        ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.shulker"),      getBoolSafe(UI_MOVEMENT.shulker, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.shulker::set      ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.dispenser"),    getBoolSafe(UI_MOVEMENT.dispenser, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.dispenser::set    ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.hopper"),       getBoolSafe(UI_MOVEMENT.hopper, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.hopper::set       ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.enchantment"),  getBoolSafe(UI_MOVEMENT.enchantment, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.enchantment::set  ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.anvil"),        getBoolSafe(UI_MOVEMENT.anvil, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.anvil::set        ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.beacon"),       getBoolSafe(UI_MOVEMENT.beacon, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.beacon::set       ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.brewing"),      getBoolSafe(UI_MOVEMENT.brewing, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.brewing::set      ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.furnace"),      getBoolSafe(UI_MOVEMENT.furnace, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.furnace::set      ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.blastFurnace"), getBoolSafe(UI_MOVEMENT.blastFurnace, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.blastFurnace::set ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.smoker"),       getBoolSafe(UI_MOVEMENT.smoker, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.smoker::set       ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.loom"),         getBoolSafe(UI_MOVEMENT.loom, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.loom::set         ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.cartography"),  getBoolSafe(UI_MOVEMENT.cartography, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.cartography::set  ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.grindstone"),   getBoolSafe(UI_MOVEMENT.grindstone, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.grindstone::set   ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.stonecutter"),  getBoolSafe(UI_MOVEMENT.stonecutter, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.stonecutter::set  ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.villager"),     getBoolSafe(UI_MOVEMENT.villager, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.villager::set     ).setYesNoTextSupplier(movement_yesNoText).build());
//        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.book"),         getBoolSafe(UI_MOVEMENT.book, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.book::set     ).setYesNoTextSupplier(movement_yesNoText).build());
//        movement.addEntry(movementTypes.build());

//        for(String modid : Compatibility.getCompatibilities().keySet()){
//            SubCategoryBuilder compatCat = eb.startSubCategory(new LiteralText(ModList.get().getModContainerById(modid).get().getModInfo().getDisplayName()));
//            compatCat.setTooltip(new LiteralText(TextFormatting.GRAY + "ModID: " + modid));
//            if(Compatibility.getCompatibilities().get(modid).setupClothMovement(compatCat, eb)) {
//                movement.addEntry(compatCat.build());
//            }
//        }

//        SubCategoryBuilder movementTypesSeen = eb.startSubCategory(new TranslatableText("key.invmove.category.types.unrecognized"));
//        movementTypesSeen.setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.unrecognized_desc").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new));
//        for(String scr : UI_MOVEMENT.seenScreens.keySet()){
//            movementTypesSeen.add(eb.startBooleanToggle(new LiteralText(scr), UI_MOVEMENT.seenScreens.get(scr)).setDefaultValue(true).setSaveConsumer(b -> {
//                UI_MOVEMENT.seenScreens.put(scr, b);
//            }).setYesNoTextSupplier(movement_yesNoText).build());
//        }
//        movement.addEntry(movementTypesSeen.build());

        // background

        ConfigCategory background = builder.getOrCreateCategory(new TranslatableText("key.invmove.category.background"));
        background.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.background.enable"), getBoolSafe(GENERAL.uiBackground, true)).setDefaultValue(true).setSaveConsumer(GENERAL.uiBackground::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.background.enable").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());

//        SubCategoryBuilder backgroundTypes = eb.startSubCategory(new TranslatableText("key.invmove.category.types"));
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.inventory"),    !getBoolSafe(UI_BACKGROUND.inventory, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.inventory.set(!b)    ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.creative"),     !getBoolSafe(UI_BACKGROUND.creative, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.creative.set(!b)     ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.crafting"),     !getBoolSafe(UI_BACKGROUND.crafting, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.crafting.set(!b)     ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.chest"),        !getBoolSafe(UI_BACKGROUND.chest, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.chest.set(!b)        ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.shulker"),      !getBoolSafe(UI_BACKGROUND.shulker, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.shulker.set(!b)      ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.dispenser"),    !getBoolSafe(UI_BACKGROUND.dispenser, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.dispenser.set(!b)    ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.hopper"),       !getBoolSafe(UI_BACKGROUND.hopper, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.hopper.set(!b)       ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.enchantment"),  !getBoolSafe(UI_BACKGROUND.enchantment, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.enchantment.set(!b)  ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.anvil"),        !getBoolSafe(UI_BACKGROUND.anvil, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.anvil.set(!b)        ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.beacon"),       !getBoolSafe(UI_BACKGROUND.beacon, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.beacon.set(!b)       ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.brewing"),      !getBoolSafe(UI_BACKGROUND.brewing, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.brewing.set(!b)      ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.furnace"),      !getBoolSafe(UI_BACKGROUND.furnace, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.furnace.set(!b)      ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.blastFurnace"), !getBoolSafe(UI_BACKGROUND.blastFurnace, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.blastFurnace.set(!b) ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.smoker"),       !getBoolSafe(UI_BACKGROUND.smoker, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.smoker.set(!b)       ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.loom"),         !getBoolSafe(UI_BACKGROUND.loom, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.loom.set(!b)         ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.cartography"),  !getBoolSafe(UI_BACKGROUND.cartography, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.cartography.set(!b)  ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.grindstone"),   !getBoolSafe(UI_BACKGROUND.grindstone, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.grindstone.set(!b)   ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.stonecutter"),  !getBoolSafe(UI_BACKGROUND.stonecutter, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.stonecutter.set(!b)  ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.villager"),     !getBoolSafe(UI_BACKGROUND.villager, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.villager.set(!b)     ).setYesNoTextSupplier(background_yesNoText).build());
//        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.book"),         !getBoolSafe(UI_BACKGROUND.book, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.book.set(!b)     ).setYesNoTextSupplier(background_yesNoText).build());
//        background.addEntry(backgroundTypes.build());

//        for(String modid : Compatibility.getCompatibilities().keySet()){
//            SubCategoryBuilder compatCat = eb.startSubCategory(new LiteralText(ModList.get().getModContainerById(modid).get().getModInfo().getDisplayName()));
//            compatCat.setTooltip(new LiteralText(TextFormatting.GRAY + "ModID: " + modid));
//            if(Compatibility.getCompatibilities().get(modid).setupClothBackground(compatCat, eb)) {
//                background.addEntry(compatCat.build());
//            }
//        }

//        SubCategoryBuilder backgroundTypesSeen = eb.startSubCategory(new TranslatableText("key.invmove.category.types.unrecognized"));
//        backgroundTypesSeen.setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.unrecognized_desc").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new));
//        for(String scr : UI_BACKGROUND.seenScreens.keySet()){
//            backgroundTypesSeen.add(eb.startBooleanToggle(new LiteralText(scr), !UI_BACKGROUND.seenScreens.get(scr)).setDefaultValue(true).setSaveConsumer(b -> {
//                UI_BACKGROUND.seenScreens.put(scr, !b);
//            }).setYesNoTextSupplier(background_yesNoText).build());
//        }
//        background.addEntry(backgroundTypesSeen.build());

        //enabled
        //moveInInventories
        //sneakInInventories
        //jumpInInventories
        //dismountInInventories
        //textFieldDisablesMovement
        //uiBackground
        //debugDisplay

        builder.setSavingRunnable(() -> {
            try {
                if (configFile != null) {
                    configFile.getParentFile().mkdirs();
                    if (!configFile.exists()) configFile.createNewFile();
                    JsonWriter jw = new JsonWriter(new FileWriter(configFile));
                    jw.setIndent("  ");
                    jw.beginObject();

                    jw.name("enabled").value(GENERAL.enabled.get());
                    jw.name("moveInInventories").value(GENERAL.moveInInventories.get());
                    jw.name("sneakInInventories").value(GENERAL.sneakInInventories.get());
                    jw.name("jumpInInventories").value(GENERAL.jumpInInventories.get());
                    jw.name("dismountInInventories").value(GENERAL.dismountInInventories.get());
                    jw.name("textFieldDisablesMovement").value(GENERAL.textFieldDisablesMovement.get());
                    jw.name("uiBackground").value(GENERAL.uiBackground.get());
                    jw.name("debugDisplay").value(GENERAL.debugDisplay.get());

                    jw.endObject();
                    jw.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }

        });

        return builder.build();
    }

    public static boolean getBoolSafe(AtomicBoolean bool, boolean def){
        return bool.get();
    }

}
