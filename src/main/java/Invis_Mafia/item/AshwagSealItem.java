package Invis_Mafia.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class AshwagSealItem extends Item {
    public AshwagSealItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("§8Seal of the Invis Mafia");
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        activate((ServerLevel) level, player, hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        activate((ServerLevel) context.getLevel(), player, context.getHand());
        return InteractionResult.CONSUME;
    }

    private void activate(ServerLevel serverLevel, Player player, InteractionHand hand) {
        double x = player.getX();
        double y = player.getY() + 1.0D;
        double z = player.getZ();

        serverLevel.sendParticles(ParticleTypes.PORTAL, x, y, z, 60, 0.7D, 1.2D, 0.7D, 0.35D);
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 80, 0.8D, 1.5D, 0.8D, 0.2D);
        player.sendSystemMessage(Component.literal("§8[Invis Mafia] §7The Seal awakens. Darkness gathers."));

        if (!player.getAbilities().instabuild) {
            player.getItemInHand(hand).shrink(1);
        }
    }
}
