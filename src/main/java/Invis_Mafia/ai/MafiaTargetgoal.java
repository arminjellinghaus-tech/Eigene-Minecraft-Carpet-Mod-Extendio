package Invis_Mafia.ai;

import Invis_Mafia.entity.MafiaBot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

public class MafiaTargetgoal extends TargetGoal {
    private final double radius;

    public MafiaTargetgoal(Mob mob, double radius) {
        super(mob, true);
        this.radius = radius;
        this.setUnseenMemoryTicks(40);
    }

    @Override
    public boolean canUse() {
        return this.mob.level() != null && !this.mob.level().players().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public void start() {
        this.chooseTarget();
    }

    @Override
    public void tick() {
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive()) {
            this.chooseTarget();
            return;
        }

        if (this.mob.distanceToSqr(this.mob.getTarget()) > this.radius * this.radius + 20.0D) {
            this.chooseTarget();
        }
    }

    private void chooseTarget() {
        LivingEntity bestTarget = null;
        double bestDistance = Double.MAX_VALUE;

        for (LivingEntity candidate : this.mob.level().getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(this.radius + 24.0D))) {
            if (!(candidate instanceof Player player)) {
                continue;
            }
            if (candidate == null || !candidate.isAlive()) {
                continue;
            }
            if (candidate.equals(this.mob) || candidate instanceof MafiaBot) {
                continue;
            }

            double distance = this.mob.distanceToSqr(player);
            if (distance < bestDistance && distance <= this.radius * this.radius) {
                bestTarget = player;
                bestDistance = distance;
            }
        }

        this.mob.setTarget(bestTarget);
    }
}
