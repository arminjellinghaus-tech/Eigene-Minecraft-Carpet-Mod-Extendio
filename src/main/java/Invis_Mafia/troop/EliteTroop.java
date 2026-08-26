package Invis_Mafia.troop;

import Invis_Mafia.entity.MafiaBot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;

public class EliteTroop extends MafiaBot {
    private int detonateCooldown;

    public EliteTroop(EntityType<? extends EliteTroop> type, Level level) {
        super(type, level, 2, 17.0F, 0.72F);
        this.setCustomName(net.minecraft.network.chat.Component.literal("Elite Troop"));
        this.setInvisible(true);
        this.setHealth(40.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            return;
        }

        if (detonateCooldown > 0) {
            detonateCooldown--;
        }

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        if (this.distanceToSqr(target) <= 4.0D && detonateCooldown <= 0) {
            detonateCooldown = 60;
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.explode(
                        this,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        2.4F,
                        true,
                        Level.ExplosionInteraction.MOB
                );
            }

            for (Player player : this.level().players()) {
                if (player != null && player.isAlive() && player.distanceToSqr(this) <= 9.0D) {
                    player.hurt(this.damageSources().explosion(this, this), 10.0F);
                    double dx = player.getX() - this.getX();
                    double dz = player.getZ() - this.getZ();
                    player.setDeltaMovement(dx * 0.18D, 0.55D, dz * 0.18D);
                }
            }
        }
    }
}
