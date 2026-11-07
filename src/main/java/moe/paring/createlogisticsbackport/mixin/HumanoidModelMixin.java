package moe.paring.createlogisticsbackport.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {
	@Inject(method = "setupAnim*", at = @At("RETURN"))
	private void create$afterSetupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo callbackInfo) {
		if (!(pEntity instanceof AbstractClientPlayer player))
			return;

	}

	@Inject(method = "setupAnim*", at = @At("HEAD"))
	private void create$beforeSetupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo callbackInfo) {
		if (!(pEntity instanceof AbstractClientPlayer player))
			return;

	}
}
