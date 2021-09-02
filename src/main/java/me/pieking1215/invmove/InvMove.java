package me.pieking1215.invmove;

import com.google.common.base.Preconditions;
import me.pieking1215.invmove.compat.Compatibility;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.BackupPromptScreen;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.screen.CustomizeFlatLevelScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.DemoScreen;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.OutOfMemoryScreen;
import net.minecraft.client.gui.screen.PresetsScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.BlastFurnaceScreen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.CartographyTableScreen;
import net.minecraft.client.gui.screen.ingame.CommandBlockScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.JigsawBlockScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.screen.ingame.MinecartCommandBlockScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.gui.screen.ingame.SmokerScreen;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.OptimizeWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.Input;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class InvMove implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Compatibility.loadCompatibility();
		if(!InvMoveConfig.hasFinalizedConfig) InvMoveConfig.doneLoading();

		HudRenderCallback.EVENT.register((ms, v) -> {

			if(InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.debugDisplay, false)) {
				Screen screen = MinecraftClient.getInstance().currentScreen;
				if(screen == null) return;

				int i = 0;
				Class<?> cl = screen.getClass();
				while (cl.getSuperclass() != null) {
					//double scale = 1;
					//String className = FabricLoader.getInstance().getMappingResolver().unmapClassName("named", cl.getName());
					String className = cl.getName();
					//RenderSystem.scaled(scale, scale, 1); // this doesn't work in 1.17 and idc enough to figure it out
					MinecraftClient.getInstance().textRenderer.drawWithShadow(new MatrixStack(), className, 4, 4 + 10 * i, 0xffffffff);
					//RenderSystem.scaled(1 / scale, 1 / scale, 1);

					i++;
					cl = cl.getSuperclass();
				}
			}
		});
	}

	public static void onInputUpdate(Input input){
		if(allowMovementInScreen(MinecraftClient.getInstance().currentScreen)){

			// tick keybinds (since opening the ui unpresses all keys)
			KeyBinding.updatePressedStates();

			// this is needed for compatibility with ItemPhysic
			MinecraftClient.getInstance().options.keyDrop.setPressed(false);

			// tick movement
			Preconditions.checkNotNull(MinecraftClient.getInstance().player);
			manualTickMovement(input, MinecraftClient.getInstance().player.shouldSlowDown(), MinecraftClient.getInstance().player.isSpectator());

			// set sprinting using raw keybind data
			if(!MinecraftClient.getInstance().player.isSprinting()) {
				MinecraftClient.getInstance().player.setSprinting(rawIsKeyDown(MinecraftClient.getInstance().options.keySprint));
			}

		}else if(MinecraftClient.getInstance().currentScreen != null){
			KeyBinding.unpressAll();
		}
	}

	public static boolean allowMovementInScreen(Screen screen) {
		if(screen == null) return false;

		if(!InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.enabled, true)) return false;
		if(!InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.moveInInventories, true)) return false;

		if(screen.isPauseScreen() && MinecraftClient.getInstance().isInSingleplayer()){
			Preconditions.checkNotNull(MinecraftClient.getInstance().getServer());
			if(!MinecraftClient.getInstance().getServer().isRemote()) return false;
		}

		if(screen instanceof AddServerScreen) return false;
		if(screen instanceof NoticeScreen) return false;
		if(screen instanceof BackupPromptScreen) return false;
		if(screen instanceof ConfirmScreen) return false;
		if(screen instanceof ConnectScreen) return false;
		if(screen instanceof CustomizeBuffetLevelScreen) return false;
		if(screen instanceof CustomizeFlatLevelScreen) return false;
		if(screen instanceof CreateWorldScreen) return false;
		if(screen instanceof DeathScreen) return false;
		if(screen instanceof DemoScreen) return false;
		if(screen instanceof SaveLevelScreen) return false;
		if(screen instanceof DisconnectedScreen) return false;
		if(screen instanceof DownloadingTerrainScreen) return false;
		if(screen instanceof FatalErrorScreen) return false;
		if(screen instanceof PresetsScreen) return false;
		if(screen instanceof GameMenuScreen) return false;
		if(screen instanceof TitleScreen) return false;
		if(screen instanceof OutOfMemoryScreen) return false;
		//if(screen instanceof ModListScreen) return false;
		if(screen instanceof MultiplayerScreen) return false;
		if(screen instanceof MultiplayerWarningScreen) return false;
		if(screen instanceof OptimizeWorldScreen) return false;
		if(screen instanceof OptionsScreen) return false;
		if(screen instanceof DirectConnectScreen) return false;
		if(screen instanceof GameOptionsScreen) return false;
		if(screen instanceof OpenToLanScreen) return false;
		if(screen instanceof StatsScreen) return false;
		if(screen instanceof CreditsScreen) return false;
		if(screen instanceof ProgressScreen) return false;
		if(screen instanceof LevelLoadingScreen) return false;
		if(screen instanceof SelectWorldScreen) return false;

		if(screen instanceof AdvancementsScreen) return false; // config?
		if(screen instanceof ChatScreen) return false;
		if(screen instanceof CommandBlockScreen) return false;
		if(screen instanceof BookEditScreen) return false;
		if(screen instanceof MinecartCommandBlockScreen) return false;
		if(screen instanceof SignEditScreen) return false;
		if(screen.getTitle().equals(new TranslatableText("sign.edit", new Object[0]))) return false;

		if(screen instanceof StructureBlockScreen) return false;
		if(screen instanceof EditWorldScreen) return false;
		if(screen instanceof JigsawBlockScreen) return false;

		if(InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.textFieldDisablesMovement, true)) {
			// don't allow movement when focused on an active textfield

			// search all fields and superclass fields for a TextFieldWidget
			try {
				Field[] fs = getDeclaredFieldsSuper(screen.getClass());

				for (Field f : fs) {
					f.setAccessible(true);
					if (TextFieldWidget.class.isAssignableFrom(f.getType())) {
						TextFieldWidget tfw = (TextFieldWidget) f.get(screen);
						if (tfw != null && tfw.isActive()) return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (screen instanceof RecipeBookProvider) {
				try {
					RecipeBookWidget wid = ((RecipeBookProvider) screen).getRecipeBookWidget();
					Field searchField = Stream.of(RecipeBookWidget.class.getDeclaredFields()).filter(f -> f.getType() == TextFieldWidget.class).findFirst().orElse(null);
					if(searchField != null) {
						searchField.setAccessible(true);
						TextFieldWidget searchBar = (TextFieldWidget) searchField.get(wid);
						if (searchBar != null && searchBar.isActive()) return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		Optional<Boolean> returnAndIgnoreUnrecognized = Optional.empty();
		if(screen instanceof InventoryScreen)           returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.inventory, true));
		if(screen instanceof HorseScreen)   		    returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.horseInventory, true));
		if(screen instanceof CreativeInventoryScreen)   returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.creative, true));
		if(screen instanceof CraftingScreen)            returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.crafting, true));
		if(screen instanceof GenericContainerScreen)    returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.chest, true));
		if(screen instanceof ShulkerBoxScreen)          returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.shulker, true));
		if(screen instanceof Generic3x3ContainerScreen) returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.dispenser, true));
		if(screen instanceof HopperScreen)              returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.hopper, true));
		if(screen instanceof EnchantmentScreen)         returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.enchantment, true));
		if(screen instanceof AnvilScreen)               returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.anvil, true));
		if(screen instanceof BeaconScreen)              returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.beacon, true));
		if(screen instanceof BrewingStandScreen)        returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.brewing, true));
		if(screen instanceof FurnaceScreen)             returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.furnace, true));
		if(screen instanceof BlastFurnaceScreen)        returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.blastFurnace, true));
		if(screen instanceof SmokerScreen)              returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.smoker, true));
		if(screen instanceof LoomScreen)                returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.loom, true));
		if(screen instanceof CartographyTableScreen)    returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.cartography, true));
		if(screen instanceof GrindstoneScreen)          returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.grindstone, true));
		if(screen instanceof StonecutterScreen)         returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.stonecutter, true));
		if(screen instanceof MerchantScreen)            returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.villager, true));
		if(screen instanceof BookScreen)                returnAndIgnoreUnrecognized = Optional.of(InvMoveConfig.getBoolSafe(InvMoveConfig.UI_MOVEMENT.book, true));

		Optional<Boolean> compatMove = Compatibility.shouldAllowMovement(screen);
		if(compatMove.isPresent()) return compatMove.get();

		if(returnAndIgnoreUnrecognized.isPresent()) return returnAndIgnoreUnrecognized.get();

//		Class<? extends Screen> scr = screen.getClass();
//		if(Config.UI_MOVEMENT.seenScreens.containsKey(scr.getName())){
//			return Config.UI_MOVEMENT.seenScreens.get(scr.getName());
//		}else{
//			Config.UI_MOVEMENT.seenScreens.put(scr.getName(), true);
//		}

		return true;
	}

	public static Field[] getDeclaredFieldsSuper(Class<?> aClass) {
		List<Field> fs = new ArrayList<>();

		do{
			fs.addAll(Arrays.asList(aClass.getDeclaredFields()));
		}while((aClass = aClass.getSuperclass()) != null);

		return fs.toArray(new Field[0]);
	}

	/**
	 * Clone of Input.tick but uses raw keybind data
	 */
	public static void manualTickMovement(Input input, boolean slow, boolean noDampening) {

		input.pressingForward = rawIsKeyDown(MinecraftClient.getInstance().options.keyForward);
		input.pressingBack = rawIsKeyDown(MinecraftClient.getInstance().options.keyBack);
		input.pressingLeft = rawIsKeyDown(MinecraftClient.getInstance().options.keyLeft);
		input.pressingRight = rawIsKeyDown(MinecraftClient.getInstance().options.keyRight);
		input.movementForward = input.pressingForward == input.pressingBack ? 0.0F : (float)(input.pressingForward ? 1 : -1);
		input.movementSideways = input.pressingLeft == input.pressingRight ? 0.0F : (float)(input.pressingLeft ? 1 : -1);
		input.jumping = rawIsKeyDown(MinecraftClient.getInstance().options.keyJump) && InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.jumpInInventories, true);
		boolean allowSneak = InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.sneakInInventories, false);
		if(MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasVehicle()){
			allowSneak = InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.dismountInInventories, false);
		}
		input.sneaking = rawIsKeyDown(MinecraftClient.getInstance().options.keySneak) && allowSneak;
		if (!noDampening && (input.sneaking || slow)) {
			input.movementSideways = (float)((double)input.movementSideways * 0.3D);
			input.movementForward = (float)((double)input.movementForward * 0.3D);
		}
	}

	/**
	 * Returns KeyBinding.pressed, which is normally a private field
	 **/
	public static boolean rawIsKeyDown(KeyBinding key){
		return key.isPressed();
	}

	public static boolean shouldDisableScreenBackground(Screen screen) {

		if(!InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.enabled, true)) return false;

		if(!InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.uiBackground, true)) return false;

		if(screen == null) return false;

		if(screen.isPauseScreen() && MinecraftClient.getInstance().isInSingleplayer()){
			Preconditions.checkNotNull(MinecraftClient.getInstance().getServer());
			if(!MinecraftClient.getInstance().getServer().isRemote()) return false;
		}

		if(screen instanceof AddServerScreen) return false;
		if(screen instanceof NoticeScreen) return false;
		if(screen instanceof BackupPromptScreen) return false;
		if(screen instanceof ConfirmScreen) return false;
		if(screen instanceof ConnectScreen) return false;
		if(screen instanceof CustomizeBuffetLevelScreen) return false;
		if(screen instanceof CustomizeFlatLevelScreen) return false;
		if(screen instanceof CreateWorldScreen) return false;
		if(screen instanceof DeathScreen) return false;
		if(screen instanceof DemoScreen) return false;
		if(screen instanceof SaveLevelScreen) return false;
		if(screen instanceof DisconnectedScreen) return false;
		if(screen instanceof DownloadingTerrainScreen) return false;
		if(screen instanceof FatalErrorScreen) return false;
		if(screen instanceof PresetsScreen) return false;
		if(screen instanceof GameMenuScreen) return false;
		if(screen instanceof TitleScreen) return false;
		if(screen instanceof OutOfMemoryScreen) return false;
		//if(screen instanceof ModListScreen) return false;
		if(screen instanceof MultiplayerScreen) return false;
		if(screen instanceof MultiplayerWarningScreen) return false;
		if(screen instanceof OptimizeWorldScreen) return false;
		if(screen instanceof OptionsScreen) return false;
		if(screen instanceof DirectConnectScreen) return false;
		if(screen instanceof GameOptionsScreen) return false;
		if(screen instanceof OpenToLanScreen) return false;
		if(screen instanceof StatsScreen) return false;
		if(screen instanceof CreditsScreen) return false;
		if(screen instanceof ProgressScreen) return false;
		if(screen instanceof LevelLoadingScreen) return false;
		if(screen instanceof SelectWorldScreen) return false;

		if(screen instanceof AdvancementsScreen) return false; // config?
		if(screen instanceof ChatScreen) return false;
		if(screen instanceof CommandBlockScreen) return false;
		if(screen instanceof MinecartCommandBlockScreen) return false;
		if(screen instanceof SignEditScreen) return false;
		if(screen.getTitle().equals(new TranslatableText("sign.edit", new Object[0]))) return false;
		if(screen instanceof StructureBlockScreen) return false;
		if(screen instanceof EditWorldScreen) return false;
		if(screen instanceof JigsawBlockScreen) return false;

		if(screen instanceof InventoryScreen)           return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.inventory, false);
		if(screen instanceof HorseScreen)      			return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.horseInventory, false);
		if(screen instanceof CreativeInventoryScreen)   return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.creative, false);
		if(screen instanceof CraftingScreen)            return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.crafting, false);
		if(screen instanceof GenericContainerScreen)    return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.chest, false);
		if(screen instanceof ShulkerBoxScreen)          return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.shulker, false);
		if(screen instanceof Generic3x3ContainerScreen) return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.dispenser, false);
		if(screen instanceof HopperScreen)              return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.hopper, false);
		if(screen instanceof EnchantmentScreen)         return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.enchantment, false);
		if(screen instanceof AnvilScreen)               return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.anvil, false);
		if(screen instanceof BeaconScreen)              return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.beacon, false);
		if(screen instanceof BrewingStandScreen)        return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.brewing, false);
		if(screen instanceof FurnaceScreen)             return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.furnace, false);
		if(screen instanceof BlastFurnaceScreen)        return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.blastFurnace, false);
		if(screen instanceof SmokerScreen)              return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.smoker, false);
		if(screen instanceof LoomScreen)                return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.loom, false);
		if(screen instanceof CartographyTableScreen)    return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.cartography, false);
		if(screen instanceof GrindstoneScreen)          return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.grindstone, false);
		if(screen instanceof StonecutterScreen)         return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.stonecutter, false);
		if(screen instanceof MerchantScreen)            return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.villager, false);
		if(screen instanceof BookScreen)            	return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.book, false);
		if(screen instanceof BookEditScreen)            return !InvMoveConfig.getBoolSafe(InvMoveConfig.UI_BACKGROUND.book, false);


		Optional<Boolean> compatBack = Compatibility.shouldDisableBackground(screen);
		if(compatBack.isPresent()) return compatBack.get();

//		Class<? extends Screen> scr = screen.getClass();
//		if(Config.UI_BACKGROUND.seenScreens.containsKey(scr.getName())){
//			return !Config.UI_BACKGROUND.seenScreens.get(scr.getName());
//		}else{
//			Config.UI_BACKGROUND.seenScreens.put(scr.getName(), true);
//		}

		return false;
	}

}
