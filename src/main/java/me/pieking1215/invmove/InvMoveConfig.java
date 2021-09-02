package me.pieking1215.invmove;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.pieking1215.invmove.compat.Compatibility;
import me.pieking1215.invmove.compat.ModCompatibility;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class InvMoveConfig {

    public static final General GENERAL = new General();
    public static final UIBackground UI_BACKGROUND = new UIBackground();
    public static final UIMovement UI_MOVEMENT = new UIMovement();

    public static boolean hasFinalizedConfig = false;
    public static File configFile;
    public static HashMap<String, File> modCompatConfigs;

    public static final Function<Boolean, Text> MOVEMENT_YES_NO_TEXT = b -> new LiteralText(b ? Formatting.GREEN + "Allow Movement" : Formatting.RED + "Disallow Movement");
    public static final Function<Boolean, Text> BACKGROUND_YES_NO_TEXT = b -> new LiteralText(b ? Formatting.GREEN + "Hide Background" : Formatting.RED + "Show Background");

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

    public static class UIBackground {
        public final AtomicBoolean inventory = new AtomicBoolean(false);
        public final AtomicBoolean horseInventory = new AtomicBoolean(false);
        public final AtomicBoolean creative = new AtomicBoolean(false);
        public final AtomicBoolean crafting = new AtomicBoolean(false);
        public final AtomicBoolean chest = new AtomicBoolean(false);
        public final AtomicBoolean shulker = new AtomicBoolean(false);
        public final AtomicBoolean dispenser = new AtomicBoolean(false);
        public final AtomicBoolean hopper = new AtomicBoolean(false);
        public final AtomicBoolean enchantment = new AtomicBoolean(false);
        public final AtomicBoolean anvil = new AtomicBoolean(false);
        public final AtomicBoolean beacon = new AtomicBoolean(false);
        public final AtomicBoolean brewing = new AtomicBoolean(false);
        public final AtomicBoolean furnace = new AtomicBoolean(false);
        public final AtomicBoolean blastFurnace = new AtomicBoolean(false);
        public final AtomicBoolean smoker = new AtomicBoolean(false);
        public final AtomicBoolean loom = new AtomicBoolean(false);
        public final AtomicBoolean cartography = new AtomicBoolean(false);
        public final AtomicBoolean grindstone = new AtomicBoolean(false);
        public final AtomicBoolean stonecutter = new AtomicBoolean(false);
        public final AtomicBoolean villager = new AtomicBoolean(false);
        public final AtomicBoolean book = new AtomicBoolean(false);
        public final HashMap<String, Boolean> seenScreens = new HashMap<>();
    }

    public static class UIMovement {
        public final AtomicBoolean inventory = new AtomicBoolean(true);
        public final AtomicBoolean horseInventory = new AtomicBoolean(true);
        public final AtomicBoolean creative = new AtomicBoolean(true);
        public final AtomicBoolean crafting = new AtomicBoolean(true);
        public final AtomicBoolean chest = new AtomicBoolean(true);
        public final AtomicBoolean shulker = new AtomicBoolean(true);
        public final AtomicBoolean dispenser = new AtomicBoolean(true);
        public final AtomicBoolean hopper = new AtomicBoolean(true);
        public final AtomicBoolean enchantment = new AtomicBoolean(true);
        public final AtomicBoolean anvil = new AtomicBoolean(true);
        public final AtomicBoolean beacon = new AtomicBoolean(true);
        public final AtomicBoolean brewing = new AtomicBoolean(true);
        public final AtomicBoolean furnace = new AtomicBoolean(true);
        public final AtomicBoolean blastFurnace = new AtomicBoolean(true);
        public final AtomicBoolean smoker = new AtomicBoolean(true);
        public final AtomicBoolean loom = new AtomicBoolean(true);
        public final AtomicBoolean cartography = new AtomicBoolean(true);
        public final AtomicBoolean grindstone = new AtomicBoolean(true);
        public final AtomicBoolean stonecutter = new AtomicBoolean(true);
        public final AtomicBoolean villager = new AtomicBoolean(true);
        public final AtomicBoolean book = new AtomicBoolean(true);
        public final HashMap<String, Boolean> seenScreens = new HashMap<>();
    }

    public static void doneLoading() {
        hasFinalizedConfig = true;

        try {
            File f = new File(FabricLoader.getInstance().getConfigDir().toFile(), "invmove.json");
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

                if(obj.has("movement")) {
                    JsonObject movement = obj.getAsJsonObject("movement");

                    UI_MOVEMENT.inventory.set(movement.has("inventory") ? movement.get("inventory").getAsBoolean() : true);
                    UI_MOVEMENT.horseInventory.set(movement.has("horseInventory") ? movement.get("horseInventory").getAsBoolean() : true);
                    UI_MOVEMENT.creative.set(movement.has("creative") ? movement.get("creative").getAsBoolean() : true);
                    UI_MOVEMENT.crafting.set(movement.has("crafting") ? movement.get("crafting").getAsBoolean() : true);
                    UI_MOVEMENT.chest.set(movement.has("chest") ? movement.get("chest").getAsBoolean() : true);
                    UI_MOVEMENT.shulker.set(movement.has("shulker") ? movement.get("shulker").getAsBoolean() : true);
                    UI_MOVEMENT.dispenser.set(movement.has("dispenser") ? movement.get("dispenser").getAsBoolean() : true);
                    UI_MOVEMENT.hopper.set(movement.has("hopper") ? movement.get("hopper").getAsBoolean() : true);
                    UI_MOVEMENT.enchantment.set(movement.has("enchantment") ? movement.get("enchantment").getAsBoolean() : true);
                    UI_MOVEMENT.anvil.set(movement.has("anvil") ? movement.get("anvil").getAsBoolean() : true);
                    UI_MOVEMENT.beacon.set(movement.has("beacon") ? movement.get("beacon").getAsBoolean() : true);
                    UI_MOVEMENT.brewing.set(movement.has("brewing") ? movement.get("brewing").getAsBoolean() : true);
                    UI_MOVEMENT.furnace.set(movement.has("furnace") ? movement.get("furnace").getAsBoolean() : true);
                    UI_MOVEMENT.blastFurnace.set(movement.has("blastFurnace") ? movement.get("blastFurnace").getAsBoolean() : true);
                    UI_MOVEMENT.smoker.set(movement.has("smoker") ? movement.get("smoker").getAsBoolean() : true);
                    UI_MOVEMENT.loom.set(movement.has("loom") ? movement.get("loom").getAsBoolean() : true);
                    UI_MOVEMENT.cartography.set(movement.has("cartography") ? movement.get("cartography").getAsBoolean() : true);
                    UI_MOVEMENT.grindstone.set(movement.has("grindstone") ? movement.get("grindstone").getAsBoolean() : true);
                    UI_MOVEMENT.stonecutter.set(movement.has("stonecutter") ? movement.get("stonecutter").getAsBoolean() : true);
                    UI_MOVEMENT.villager.set(movement.has("villager") ? movement.get("villager").getAsBoolean() : true);
                    UI_MOVEMENT.book.set(movement.has("book") ? movement.get("book").getAsBoolean() : true);

                }

                if(obj.has("background")) {
                    JsonObject background = obj.getAsJsonObject("background");

                    UI_BACKGROUND.inventory.set(background.has("inventory") ? background.get("inventory").getAsBoolean() : false);
                    UI_BACKGROUND.horseInventory.set(background.has("horseInventory") ? background.get("horseInventory").getAsBoolean() : false);
                    UI_BACKGROUND.creative.set(background.has("creative") ? background.get("creative").getAsBoolean() : false);
                    UI_BACKGROUND.crafting.set(background.has("crafting") ? background.get("crafting").getAsBoolean() : false);
                    UI_BACKGROUND.chest.set(background.has("chest") ? background.get("chest").getAsBoolean() : false);
                    UI_BACKGROUND.shulker.set(background.has("shulker") ? background.get("shulker").getAsBoolean() : false);
                    UI_BACKGROUND.dispenser.set(background.has("dispenser") ? background.get("dispenser").getAsBoolean() : false);
                    UI_BACKGROUND.hopper.set(background.has("hopper") ? background.get("hopper").getAsBoolean() : false);
                    UI_BACKGROUND.enchantment.set(background.has("enchantment") ? background.get("enchantment").getAsBoolean() : false);
                    UI_BACKGROUND.anvil.set(background.has("anvil") ? background.get("anvil").getAsBoolean() : false);
                    UI_BACKGROUND.beacon.set(background.has("beacon") ? background.get("beacon").getAsBoolean() : false);
                    UI_BACKGROUND.brewing.set(background.has("brewing") ? background.get("brewing").getAsBoolean() : false);
                    UI_BACKGROUND.furnace.set(background.has("furnace") ? background.get("furnace").getAsBoolean() : false);
                    UI_BACKGROUND.blastFurnace.set(background.has("blastFurnace") ? background.get("blastFurnace").getAsBoolean() : false);
                    UI_BACKGROUND.smoker.set(background.has("smoker") ? background.get("smoker").getAsBoolean() : false);
                    UI_BACKGROUND.loom.set(background.has("loom") ? background.get("loom").getAsBoolean() : false);
                    UI_BACKGROUND.cartography.set(background.has("cartography") ? background.get("cartography").getAsBoolean() : false);
                    UI_BACKGROUND.grindstone.set(background.has("grindstone") ? background.get("grindstone").getAsBoolean() : false);
                    UI_BACKGROUND.stonecutter.set(background.has("stonecutter") ? background.get("stonecutter").getAsBoolean() : false);
                    UI_BACKGROUND.villager.set(background.has("villager") ? background.get("villager").getAsBoolean() : false);
                    UI_BACKGROUND.book.set(background.has("book") ? background.get("book").getAsBoolean() : false);

                }

            }
            jr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        modCompatConfigs = new HashMap<>();
        for(String modid : Compatibility.getCompatibilities().keySet()){
            try {
                File f = new File(FabricLoader.getInstance().getConfigDir().toFile(), "invMove/" + modid + ".json");
                f.getParentFile().mkdirs();
                if(!f.exists()) f.createNewFile();
                modCompatConfigs.put(modid, f);

                JsonReader jr = new JsonReader(new FileReader(f));
                JsonElement jp = new JsonParser().parse(jr);
                if(jp.isJsonObject()) {
                    JsonObject obj = jp.getAsJsonObject();
                    Compatibility.getCompatibilities().get(modid).loadConfig(obj);
                }
                jr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Screen registerClothConfig(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(new TranslatableText("config.invmove.title"));
        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/spruce_planks.png"));
        builder.transparentBackground();

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("key.invmove.category.general"));
        general.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.enable"), getBoolSafe(GENERAL.enabled, true)).setDefaultValue(true).setSaveConsumer(GENERAL.enabled::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.enable").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        general.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.debugDisplay"), getBoolSafe(GENERAL.debugDisplay, false)).setDefaultValue(false).setSaveConsumer(GENERAL.debugDisplay::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.debugDisplay").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());

        // movement

        ConfigCategory movement = builder.getOrCreateCategory(new TranslatableText("key.invmove.category.movement"));
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.enable"), getBoolSafe(GENERAL.moveInInventories, true)).setDefaultValue(true).setSaveConsumer(GENERAL.moveInInventories::set                           ).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.enable").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.sneak"), getBoolSafe(GENERAL.sneakInInventories, false)).setDefaultValue(false).setSaveConsumer(GENERAL.sneakInInventories::set                        ).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.sneak").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.jump"), getBoolSafe(GENERAL.jumpInInventories, true)).setDefaultValue(true).setSaveConsumer(GENERAL.jumpInInventories::set                             ).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.jump").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.dismount"), getBoolSafe(GENERAL.dismountInInventories, false)).setDefaultValue(false).setSaveConsumer(GENERAL.dismountInInventories::set               ).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.dismount").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());
        movement.addEntry(eb.startBooleanToggle(new TranslatableText("config.invmove.movement.textFieldDisables"), getBoolSafe(GENERAL.textFieldDisablesMovement, true)).setDefaultValue(true).setSaveConsumer(GENERAL.textFieldDisablesMovement::set).setTooltip(Arrays.stream(I18n.translate("tooltip.config.invmove.movement.textFieldDisables").split("\n")).map(LiteralText::new).toArray(LiteralText[]::new)).build());

        SubCategoryBuilder movementTypes = eb.startSubCategory(new TranslatableText("key.invmove.category.types"));
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.inventory"),    getBoolSafe(UI_MOVEMENT.inventory, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.inventory::set      ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.creative"),     getBoolSafe(UI_MOVEMENT.creative, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.creative::set        ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.horse"),        getBoolSafe(UI_MOVEMENT.horseInventory, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.horseInventory::set).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.crafting"),     getBoolSafe(UI_MOVEMENT.crafting, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.crafting::set        ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.chest"),        getBoolSafe(UI_MOVEMENT.chest, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.chest::set              ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.shulker"),      getBoolSafe(UI_MOVEMENT.shulker, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.shulker::set          ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.dispenser"),    getBoolSafe(UI_MOVEMENT.dispenser, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.dispenser::set      ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.hopper"),       getBoolSafe(UI_MOVEMENT.hopper, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.hopper::set            ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.enchantment"),  getBoolSafe(UI_MOVEMENT.enchantment, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.enchantment::set  ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.anvil"),        getBoolSafe(UI_MOVEMENT.anvil, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.anvil::set              ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.beacon"),       getBoolSafe(UI_MOVEMENT.beacon, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.beacon::set            ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.brewing"),      getBoolSafe(UI_MOVEMENT.brewing, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.brewing::set          ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.furnace"),      getBoolSafe(UI_MOVEMENT.furnace, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.furnace::set          ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.blastFurnace"), getBoolSafe(UI_MOVEMENT.blastFurnace, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.blastFurnace::set).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.smoker"),       getBoolSafe(UI_MOVEMENT.smoker, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.smoker::set            ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.loom"),         getBoolSafe(UI_MOVEMENT.loom, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.loom::set                ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.cartography"),  getBoolSafe(UI_MOVEMENT.cartography, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.cartography::set  ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.grindstone"),   getBoolSafe(UI_MOVEMENT.grindstone, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.grindstone::set    ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.stonecutter"),  getBoolSafe(UI_MOVEMENT.stonecutter, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.stonecutter::set  ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.villager"),     getBoolSafe(UI_MOVEMENT.villager, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.villager::set        ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movementTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.book"),         getBoolSafe(UI_MOVEMENT.book, true)).setDefaultValue(true).setSaveConsumer(UI_MOVEMENT.book::set                ).setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT).build());
        movement.addEntry(movementTypes.build());

        for(String modid : Compatibility.getCompatibilities().keySet()){
            SubCategoryBuilder compatCat = eb.startSubCategory(new LiteralText(FabricLoader.getInstance().getModContainer(modid).get().getMetadata().getName()));
            compatCat.setTooltip(new LiteralText(Formatting.GRAY + "ModID: " + modid));
            if(Compatibility.getCompatibilities().get(modid).setupClothMovement(compatCat, eb)) {
                movement.addEntry(compatCat.build());
            }
        }

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

        SubCategoryBuilder backgroundTypes = eb.startSubCategory(new TranslatableText("key.invmove.category.types"));
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.inventory"),    !getBoolSafe(UI_BACKGROUND.inventory, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.inventory.set(!b)      ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.creative"),     !getBoolSafe(UI_BACKGROUND.creative, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.creative.set(!b)        ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.horse"),        !getBoolSafe(UI_BACKGROUND.horseInventory, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.horseInventory.set(!b)        ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.crafting"),     !getBoolSafe(UI_BACKGROUND.crafting, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.crafting.set(!b)        ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.chest"),        !getBoolSafe(UI_BACKGROUND.chest, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.chest.set(!b)              ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.shulker"),      !getBoolSafe(UI_BACKGROUND.shulker, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.shulker.set(!b)          ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.dispenser"),    !getBoolSafe(UI_BACKGROUND.dispenser, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.dispenser.set(!b)      ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.hopper"),       !getBoolSafe(UI_BACKGROUND.hopper, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.hopper.set(!b)            ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.enchantment"),  !getBoolSafe(UI_BACKGROUND.enchantment, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.enchantment.set(!b)  ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.anvil"),        !getBoolSafe(UI_BACKGROUND.anvil, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.anvil.set(!b)              ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.beacon"),       !getBoolSafe(UI_BACKGROUND.beacon, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.beacon.set(!b)            ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.brewing"),      !getBoolSafe(UI_BACKGROUND.brewing, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.brewing.set(!b)          ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.furnace"),      !getBoolSafe(UI_BACKGROUND.furnace, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.furnace.set(!b)          ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.blastFurnace"), !getBoolSafe(UI_BACKGROUND.blastFurnace, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.blastFurnace.set(!b)).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.smoker"),       !getBoolSafe(UI_BACKGROUND.smoker, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.smoker.set(!b)            ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.loom"),         !getBoolSafe(UI_BACKGROUND.loom, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.loom.set(!b)                ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.cartography"),  !getBoolSafe(UI_BACKGROUND.cartography, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.cartography.set(!b)  ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.grindstone"),   !getBoolSafe(UI_BACKGROUND.grindstone, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.grindstone.set(!b)    ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.stonecutter"),  !getBoolSafe(UI_BACKGROUND.stonecutter, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.stonecutter.set(!b)  ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.villager"),     !getBoolSafe(UI_BACKGROUND.villager, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.villager.set(!b)        ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        backgroundTypes.add(eb.startBooleanToggle(new TranslatableText("config.invmove.type.book"),         !getBoolSafe(UI_BACKGROUND.book, false)).setDefaultValue(true).setSaveConsumer(b -> UI_BACKGROUND.book.set(!b)                ).setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT).build());
        background.addEntry(backgroundTypes.build());

        for(String modid : Compatibility.getCompatibilities().keySet()){
            SubCategoryBuilder compatCat = eb.startSubCategory(new LiteralText(FabricLoader.getInstance().getModContainer(modid).get().getMetadata().getName()));
            compatCat.setTooltip(new LiteralText(Formatting.GRAY + "ModID: " + modid));
            if(Compatibility.getCompatibilities().get(modid).setupClothBackground(compatCat, eb)) {
                background.addEntry(compatCat.build());
            }
        }

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

                    jw.name("movement").beginObject();

                    jw.name("inventory").value(UI_MOVEMENT.inventory.get());
                    jw.name("horseInventory").value(UI_MOVEMENT.horseInventory.get());
                    jw.name("creative").value(UI_MOVEMENT.creative.get());
                    jw.name("crafting").value(UI_MOVEMENT.crafting.get());
                    jw.name("chest").value(UI_MOVEMENT.chest.get());
                    jw.name("shulker").value(UI_MOVEMENT.shulker.get());
                    jw.name("dispenser").value(UI_MOVEMENT.dispenser.get());
                    jw.name("hopper").value(UI_MOVEMENT.hopper.get());
                    jw.name("enchantment").value(UI_MOVEMENT.enchantment.get());
                    jw.name("anvil").value(UI_MOVEMENT.anvil.get());
                    jw.name("beacon").value(UI_MOVEMENT.beacon.get());
                    jw.name("brewing").value(UI_MOVEMENT.brewing.get());
                    jw.name("furnace").value(UI_MOVEMENT.furnace.get());
                    jw.name("blastFurnace").value(UI_MOVEMENT.blastFurnace.get());
                    jw.name("smoker").value(UI_MOVEMENT.smoker.get());
                    jw.name("loom").value(UI_MOVEMENT.loom.get());
                    jw.name("cartography").value(UI_MOVEMENT.cartography.get());
                    jw.name("grindstone").value(UI_MOVEMENT.grindstone.get());
                    jw.name("stonecutter").value(UI_MOVEMENT.stonecutter.get());
                    jw.name("villager").value(UI_MOVEMENT.villager.get());
                    jw.name("book").value(UI_MOVEMENT.book.get());

                    jw.endObject();

                    jw.name("background").beginObject();

                    jw.name("inventory").value(UI_BACKGROUND.inventory.get());
                    jw.name("horseInventory").value(UI_BACKGROUND.horseInventory.get());
                    jw.name("creative").value(UI_BACKGROUND.creative.get());
                    jw.name("crafting").value(UI_BACKGROUND.crafting.get());
                    jw.name("chest").value(UI_BACKGROUND.chest.get());
                    jw.name("shulker").value(UI_BACKGROUND.shulker.get());
                    jw.name("dispenser").value(UI_BACKGROUND.dispenser.get());
                    jw.name("hopper").value(UI_BACKGROUND.hopper.get());
                    jw.name("enchantment").value(UI_BACKGROUND.enchantment.get());
                    jw.name("anvil").value(UI_BACKGROUND.anvil.get());
                    jw.name("beacon").value(UI_BACKGROUND.beacon.get());
                    jw.name("brewing").value(UI_BACKGROUND.brewing.get());
                    jw.name("furnace").value(UI_BACKGROUND.furnace.get());
                    jw.name("blastFurnace").value(UI_BACKGROUND.blastFurnace.get());
                    jw.name("smoker").value(UI_BACKGROUND.smoker.get());
                    jw.name("loom").value(UI_BACKGROUND.loom.get());
                    jw.name("cartography").value(UI_BACKGROUND.cartography.get());
                    jw.name("grindstone").value(UI_BACKGROUND.grindstone.get());
                    jw.name("stonecutter").value(UI_BACKGROUND.stonecutter.get());
                    jw.name("villager").value(UI_BACKGROUND.villager.get());
                    jw.name("book").value(UI_BACKGROUND.book.get());

                    jw.endObject();


                    jw.endObject();
                    jw.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }

            for(String modid : modCompatConfigs.keySet()){
                ModCompatibility compat;
                File f;
                if((f = modCompatConfigs.get(modid)) != null && (compat = Compatibility.getCompatibilities().get(modid)) != null){
                    try {
                        f.getParentFile().mkdirs();
                        if (!f.exists()) f.createNewFile();
                        JsonWriter jw = new JsonWriter(new FileWriter(f));
                        jw.setIndent("  ");
                        jw.beginObject();
                        compat.saveConfig(jw);
                        jw.endObject();
                        jw.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }

        });

        return builder.build();
    }

    public static boolean getBoolSafe(AtomicBoolean bool, boolean def){
        return bool.get();
    }

}
