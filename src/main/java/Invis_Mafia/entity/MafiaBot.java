package Invis_Mafia.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
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
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }

        if (!this.isInvisible()) {
            visibleWarningTicks++;
            if (visibleWarningTicks >= 30) {
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

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            this.setTarget(null);
            return;
        }

        if (this.distanceToSqr(target) > 64.0D) {
            this.setTarget(null);
        }
    }

    public int getTroopTier() {
        return troopTier;
    }

    public float getDamageValue() {
        return damage;
    }
}
