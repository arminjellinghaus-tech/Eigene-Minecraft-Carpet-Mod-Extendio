package Invis_Mafia.item;

import Invis_Mafia.InvisMafiaExtension;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class MafiaSummonItem extends Item {
    public MafiaSummonItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("§7Mafia Summoner");
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        consumer.accept(Component.literal("§8The Invis Mafia never stays visible."));
        consumer.accept(Component.literal("§cRule: if you are seen, you are thrown into the void."));
        consumer.accept(Component.literal("§7Summons an elite, silent, hunting squad."));
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

        InvisMafiaExtension.sendRuleAlert(player);
        player.sendSystemMessage(Component.literal("§8[Invis Mafia] §7The squad is awake. Stay hidden."));

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
