package Invis_Mafia.entity;

import Invis_Mafia.ai.MafiaTargetgoal;
import Invis_Mafia.config.MafiaConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class MafiaBot extends Monster {
    private final int troopTier;
    private final float damage;
    private final float moveSpeed;
    private int visibleWarningTicks;

    protected MafiaBot(EntityType<? extends MafiaBot> type, Level level, int troopTier, float damage, float moveSpeed) {
        super(type, level);
        this.troopTier = troopTier;
        this.damage = damage;
        this.moveSpeed = moveSpeed;
        this.setPersistenceRequired();
        this.setInvisible(true);
        this.setCustomNameVisible(true);
    }

    protected MafiaBot(EntityType<? extends MafiaBot> type, Level level) {
        this(type, level, 0, 8.0F, 0.35F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ARMOR, 4.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, this.moveSpeed, true));
        this.targetSelector.addGoal(1, new MafiaTargetgoal(this, 64.0D));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }

        if (!this.isInvisible()) {
            visibleWarningTicks++;
            if (visibleWarningTicks >= 40) {
                this.setInvisible(true);
                this.setDeltaMovement(0.0D, -1.0D, 0.0D);
                this.setPos(this.getX(), -64.0D, this.getZ());
                this.hurt(this.damageSources().enderPearl(), 9999.0F);
                this.discard();
                return;
            }
        } else {
            visibleWarningTicks = 0;
        }

        if (this.tickCount % MafiaConfig.TNT_INTERVAL_TICKS == 0) {
            triggerTntBurst();
        }

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            this.setTarget(null);
            return;
        }

        if (this.distanceToSqr(target) > 96.0D) {
            this.setTarget(null);
        }
    }

    protected void triggerTntBurst() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        int amount = RandomSource.create().nextInt(MafiaConfig.TNT_MIN, MafiaConfig.TNT_MAX + 1);
        for (int i = 0; i < amount; i++) {
            double dx = (this.getRandom().nextDouble() - 0.5D) * 4.0D;
            double dz = (this.getRandom().nextDouble() - 0.5D) * 4.0D;
            serverLevel.explode(
                    this,
                    this.getX() + dx,
                    this.getY() + 0.5D,
                    this.getZ() + dz,
                    1.8F + this.troopTier * 0.6F,
                    true,
                    Level.ExplosionInteraction.MOB
            );
        }
    }

    public int getTroopTier() {
        return troopTier;
    }

    public float getDamageValue() {
        return damage;
    }
}
