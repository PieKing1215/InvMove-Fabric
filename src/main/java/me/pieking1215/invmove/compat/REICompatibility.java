package me.pieking1215.invmove.compat;

import me.pieking1215.invmove.InvMoveConfig;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.impl.client.gui.screen.AbstractDisplayViewingScreen;
import net.minecraft.client.gui.screen.Screen;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class REICompatibility extends ModCompatibility {
    AtomicBoolean RecipeGUI_movement = new AtomicBoolean(true);
    AtomicBoolean SPECIAL_OverlayFocus_movement = new AtomicBoolean(false);

    AtomicBoolean RecipeGUI_background_disable = new AtomicBoolean(true);

    public REICompatibility() {
        movementOptions.addAll(Arrays.asList(
                new BoolOption("Recipe GUI", "RecipeGUI_movement", RecipeGUI_movement,true),
                new BoolOption("When Search Field Focused", "SPECIAL_OverlayFocus_movement", SPECIAL_OverlayFocus_movement,false)
        ));
        backgroundOptions.addAll(Arrays.asList(
                new BoolOption("Recipe GUI (option broken right now)", "RecipeGUI_background_disable", RecipeGUI_background_disable,true)
        ));
    }

    @Override
    Optional<Boolean> shouldAllowMovement(Screen screen) {
        if(!SPECIAL_OverlayFocus_movement.get() && InvMoveConfig.getBoolSafe(InvMoveConfig.GENERAL.textFieldDisablesMovement, true)
                && REIRuntime.getInstance().getSearchTextField() != null
                && REIRuntime.getInstance().getSearchTextField().isFocused()) {
                return Optional.of(false);
        }

        if(screen instanceof AbstractDisplayViewingScreen) return Optional.of(RecipeGUI_movement.get());

        return Optional.empty();
    }

    @Override
    Optional<Boolean> shouldDisableBackground(Screen screen) {

        // this doesn't work right now because of https://github.com/shedaniel/RoughlyEnoughItems/issues/620
        if(screen instanceof AbstractDisplayViewingScreen) return Optional.of(RecipeGUI_background_disable.get());

        return Optional.empty();
    }
}
