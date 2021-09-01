package me.pieking1215.invmove.mixin;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MovementMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onMovement(Lnet/minecraft/client/input/Input;)V"), method = "tickMovement()V")
	private void onInputUpdate(CallbackInfo info) {
		InvMove.onInputUpdate(((ClientPlayerEntity)(Object)this).input);
	}
}
