package Invis_Mafia.troop;

import Invis_Mafia.config.MafiaConfig;
import Invis_Mafia.entity.MafiaBot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GroundTroop extends MafiaBot {
    private int shockwaveCooldown;

    public GroundTroop(EntityType<? extends GroundTroop> type, Level level) {
        super(type, level, 0, 8.0F, 0.46F);
        this.setCustomName(Component.literal("§8Ground Troop"));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level().isClientSide()) {
            return;
        }

        if (shockwaveCooldown > 0) {
            shockwaveCooldown--;
        }

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        if (this.distanceToSqr(target) <= 4.5D && shockwaveCooldown <= 0) {
            shockwaveCooldown = 80;
            for (Player player : this.level().players()) {
                if (player != null && player.isAlive() && player.distanceToSqr(this) <= 9.0D) {
                    player.hurt(this.damageSources().mobAttack(this), 6.5F);
                    double dx = player.getX() - this.getX();
                    double dz = player.getZ() - this.getZ();
                    double len = Math.max(Math.sqrt(dx * dx + dz * dz), 0.1D);
                    player.setDeltaMovement((dx / len) * 1.1D, 0.55D, (dz / len) * 1.1D);
                }
            }
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.2F, true, Level.ExplosionInteraction.MOB);
        }
    }

    public int getGroupSize() {
        return MafiaConfig.GROUND_TROOP_COUNT;
    }
}
