package Invis_Mafia.troop;

import Invis_Mafia.entity.MafiaBot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ElytraTroop extends MafiaBot {
    public ElytraTroop(EntityType<? extends ElytraTroop> type, Level level) {
        super(type, level, 1, 11.0F, 0.62F);
        this.setCustomName(net.minecraft.network.chat.Component.literal("Elytra Troop"));
        this.setInvisible(true);
    }
}
