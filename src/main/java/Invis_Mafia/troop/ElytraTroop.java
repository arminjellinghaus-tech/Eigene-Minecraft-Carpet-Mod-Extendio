package Invis_Mafia.troop;

import Invis_Mafia.entity.MafiaBot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ElytraTroop extends MafiaBot {
    private int dashCooldown;

    public ElytraTroop(EntityType<? extends ElytraTroop> type, Level level) {
        super(type, level, 1, 11.0F, 0.62F);
        this.setCustomName(net.minecraft.network.chat.Component.literal("Elytra Troop"));
        this.setInvisible(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            return;
        }

        if (dashCooldown > 0) {
            dashCooldown--;
        }

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        if (this.distanceToSqr(target) <= 25.0D && dashCooldown <= 0) {
            dashCooldown = 60;
            double dx = target.getX() - this.getX();
            double dy = target.getY() - this.getY();
            double dz = target.getZ() - this.getZ();
            double length = Math.max(Math.sqrt(dx * dx + dy * dy + dz * dz), 0.1D);

            this.setDeltaMovement(
                    (dx / length) * 1.2D,
                    Math.max(0.45D, Math.abs(dy) / length + 0.25D),
                    (dz / length) * 1.2D
            );
            this.setInvisible(true);
        }
    }
}
