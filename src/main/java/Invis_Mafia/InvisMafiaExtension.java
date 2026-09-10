package Invis_Mafia;

import Invis_Mafia.entity.MafiaBot;
import Invis_Mafia.item.AshwagSealItem;
import Invis_Mafia.item.MafiaSummonItem;
import Invis_Mafia.troop.EliteTroop;
import Invis_Mafia.troop.ElytraTroop;
import Invis_Mafia.troop.GroundTroop;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static net.minecraft.core.Registry.register;

public class InvisMafiaExtension implements ModInitializer {
    public static final String MOD_ID = "invis_mafia";
    public static final String RULE_VISIBLE = "Visible means death. The Invis Mafia never stays seen.";
    public static final String RULE_VOID = "If you are caught in the open, the void takes you.";

    public static void sendRuleAlert(Player player) {
        player.sendSystemMessage(Component.literal("§8[Invis Mafia] §7" + RULE_VISIBLE));
        player.sendSystemMessage(Component.literal("§8[Invis Mafia] §c" + RULE_VOID));
    }

    public static Item MAFIA_SUMMONER;
    public static Item ASHWAG_SEAL;
    public static EntityType<GroundTroop> GROUND_TROOP;
    public static EntityType<ElytraTroop> ELYTRA_TROOP;
    public static EntityType<EliteTroop> ELITE_TROOP;

    @Override
    public void onInitialize() {
        Identifier mafiaSummonerId = Identifier.fromNamespaceAndPath(MOD_ID, "mafia_summoner");
        MAFIA_SUMMONER = register(
                BuiltInRegistries.ITEM,
                mafiaSummonerId,
                new MafiaSummonItem(new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM, mafiaSummonerId))
                        .stacksTo(1)
                        .rarity(Rarity.UNCOMMON))
        );

        Identifier ashwagSealId = Identifier.fromNamespaceAndPath(MOD_ID, "ashwag_seal");
        ASHWAG_SEAL = register(
                BuiltInRegistries.ITEM,
                ashwagSealId,
                new AshwagSealItem(new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM, ashwagSealId))
                        .stacksTo(1)
                        .rarity(Rarity.RARE))
        );

        Identifier groundTroopId = Identifier.fromNamespaceAndPath(MOD_ID, "ground_troop");
        GROUND_TROOP = register(
                BuiltInRegistries.ENTITY_TYPE,
                groundTroopId,
                EntityType.Builder.of(GroundTroop::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, groundTroopId))
        );

        Identifier elytraTroopId = Identifier.fromNamespaceAndPath(MOD_ID, "elytra_troop");
        ELYTRA_TROOP = register(
                BuiltInRegistries.ENTITY_TYPE,
                elytraTroopId,
                EntityType.Builder.of(ElytraTroop::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, elytraTroopId))
        );

        Identifier eliteTroopId = Identifier.fromNamespaceAndPath(MOD_ID, "elite_troop");
        ELITE_TROOP = register(
                BuiltInRegistries.ENTITY_TYPE,
                eliteTroopId,
                EntityType.Builder.of(EliteTroop::new, MobCategory.MONSTER)
                        .sized(0.8F, 2.0F)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, eliteTroopId))
        );

                ResourceKey<net.minecraft.world.item.CreativeModeTab> operatorTab = ResourceKey.create(
                        Registries.CREATIVE_MODE_TAB,
                        Identifier.withDefaultNamespace("op_blocks")
                );
                CreativeModeTabEvents.modifyOutputEvent(operatorTab).register(output -> {
                    output.accept(MAFIA_SUMMONER);
                    output.accept(ASHWAG_SEAL);
                });
    }
}
