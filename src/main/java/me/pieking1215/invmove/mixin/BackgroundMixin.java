package me.pieking1215.invmove.mixin;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class BackgroundMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V"), method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V", cancellable = true)
	private void onDrawBackground(CallbackInfo info) {
		if(InvMove.shouldDisableScreenBackground((Screen)(Object)this)) {
			info.cancel();
		}
	}
}
