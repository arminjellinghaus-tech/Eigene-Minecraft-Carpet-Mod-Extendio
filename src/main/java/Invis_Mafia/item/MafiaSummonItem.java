package Invis_Mafia.item;

import Invis_Mafia.InvisMafiaExtension;
import Invis_Mafia.troop.EliteTroop;
import Invis_Mafia.troop.ElytraTroop;
import Invis_Mafia.troop.GroundTroop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MafiaSummonItem extends Item {
    public MafiaSummonItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getItemInHand(hand);
        ServerLevel serverLevel = (ServerLevel) level;

        var troop = chooseTroop();
        var entity = troop.create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);
        if (entity == null) {
            return InteractionResult.FAIL;
        }

        entity.setPos(player.getX() + 0.5D, player.getY() + 1.0D, player.getZ() + 0.5D);
        entity.setYRot(player.getYRot());
        entity.setXRot(0.0F);
        entity.setYHeadRot(player.getYRot());
        serverLevel.addFreshEntity(entity);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }

    private EntityType<? extends net.minecraft.world.entity.Mob> chooseTroop() {
        int roll = RandomSource.create().nextInt(100);
        if (roll < 55) {
            return InvisMafiaExtension.GROUND_TROOP;
        }
        if (roll < 85) {
            return InvisMafiaExtension.ELYTRA_TROOP;
        }
        return InvisMafiaExtension.ELITE_TROOP;
    }
}
