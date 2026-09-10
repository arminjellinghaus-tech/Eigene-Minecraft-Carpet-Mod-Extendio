package Invis_Mafia.item;

import Invis_Mafia.InvisMafiaExtension;
import Invis_Mafia.config.MafiaConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
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
        consumer.accept(Component.literal("§7Summons a complete mafia squad: 100 / 25 / 10."));
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
        ItemStack stack = player.getItemInHand(hand);

        int roll = RandomSource.create().nextInt(3);
        EntityType<? extends Mob> troop = chooseTroop(roll);
        int count = chooseCount(roll);
        int spawned = spawnGroup(serverLevel, player, troop, count);

        InvisMafiaExtension.sendRuleAlert(player);
        player.sendSystemMessage(Component.literal("§8[Invis Mafia] §7Summoned " + spawned + " of " + count + " troops."));

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    private int spawnGroup(ServerLevel serverLevel, Player player, EntityType<? extends Mob> troop, int count) {
        int spawned = 0;
        for (int index = 0; index < count; index++) {
            Mob entity = troop.create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);
            if (entity == null) {
                continue;
            }

            double offsetX = (index % 10) * 1.2D - 5.4D;
            double offsetZ = (index / 10) * 1.2D - 5.4D;
            entity.setPos(player.getX() + offsetX, player.getY() + 1.0D, player.getZ() + offsetZ);
            entity.setYRot(player.getYRot());
            entity.setXRot(0.0F);
            entity.setYHeadRot(player.getYRot());
            serverLevel.addFreshEntity(entity);
            spawned++;
        }
        return spawned;
    }

    private EntityType<? extends Mob> chooseTroop(int roll) {
        return switch (roll) {
            case 0 -> InvisMafiaExtension.GROUND_TROOP;
            case 1 -> InvisMafiaExtension.ELYTRA_TROOP;
            default -> InvisMafiaExtension.ELITE_TROOP;
        };
    }

    private int chooseCount(int roll) {
        return switch (roll) {
            case 0 -> MafiaConfig.GROUND_TROOP_COUNT;
            case 1 -> MafiaConfig.ELYTRA_TROOP_COUNT;
            default -> MafiaConfig.ELITE_TROOP_COUNT;
        };
    }
}
