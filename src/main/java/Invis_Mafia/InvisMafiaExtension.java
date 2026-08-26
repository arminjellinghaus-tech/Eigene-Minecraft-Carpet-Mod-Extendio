package Invis_Mafia;

import Invis_Mafia.entity.MafiaBot;
import Invis_Mafia.item.MafiaSummonItem;
import Invis_Mafia.troop.EliteTroop;
import Invis_Mafia.troop.ElytraTroop;
import Invis_Mafia.troop.GroundTroop;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static net.minecraft.core.Registry.register;

public class InvisMafiaExtension implements ModInitializer {
    public static final String MOD_ID = "invis_mafia";

    public static final Item MAFIA_SUMMONER = register(
            BuiltInRegistries.ITEM,
            Identifier.fromNamespaceAndPath(MOD_ID, "mafia_summoner"),
            new MafiaSummonItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON))
    );

    public static final EntityType<GroundTroop> GROUND_TROOP = register(
            BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "ground_troop"),
            EntityType.Builder.of(GroundTroop::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "ground_troop")))
    );

    public static final EntityType<ElytraTroop> ELYTRA_TROOP = register(
            BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "elytra_troop"),
            EntityType.Builder.of(ElytraTroop::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "elytra_troop")))
    );

    public static final EntityType<EliteTroop> ELITE_TROOP = register(
            BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "elite_troop"),
            EntityType.Builder.of(EliteTroop::new, MobCategory.MONSTER)
                    .sized(0.8F, 2.0F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "elite_troop")))
    );

    @Override
    public void onInitialize() {
        // Nothing extra needed; all registration is done during class loading.
    }
}
