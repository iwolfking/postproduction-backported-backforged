package moe.paring.createlogisticsbackport.content.logistics.packagerLink;

import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.simibubi.create.content.equipment.bell.CustomRotationParticle;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import moe.paring.createlogisticsbackport.registry.ExtraParticleTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class WiFiParticle extends CustomRotationParticle {

	private SpriteSet animatedSprite;
	private boolean downward;

	public WiFiParticle(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz,
		SpriteSet spriteSet) {
		super(worldIn, x, y + (vy < 0 ? -1 : 1), z, spriteSet, 0);
		this.animatedSprite = spriteSet;
		this.quadSize = 0.5f;
		this.setSize(this.quadSize, this.quadSize);
		this.loopLength = 16;
		this.lifetime = 16;
		this.setSpriteFromAge(spriteSet);
		this.stoppedByCollision = true; // disable movement
		this.downward = vy < 0;
	}

	@Override
	public void tick() {
		setSpriteFromAge(animatedSprite);
		if (age++ >= lifetime)
			remove();
	}

	@Override
	public com.mojang.math.Quaternion getCustomRotation(Camera camera, float partialTicks) {
		com.mojang.math.Quaternion q = new com.mojang.math.Quaternion(new com.mojang.math.Vector3f(0, 1, 0), -camera.getYRot() * Mth.DEG_TO_RAD, false);
		com.mojang.math.Quaternion z = new com.mojang.math.Quaternion(new com.mojang.math.Vector3f(0, 0, 1), downward ? Mth.PI : 0, false);
		q.mul(z);
		return q;
	}

	public static class Data extends BasicParticleData<WiFiParticle> implements ParticleOptions {

		@Override
		public IBasicParticleFactory<WiFiParticle> getBasicFactory() {
			return WiFiParticle::new;
		}

		@Override
		public ParticleType<?> getType() {
			return ExtraParticleTypes.WIFI.get();
		}

	}

}
