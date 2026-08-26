package Invis_Mafia.troop;

import Invis_Mafia.entity.MafiaBot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EliteTroop extends MafiaBot {
    public EliteTroop(EntityType<? extends EliteTroop> type, Level level) {
        super(type, level, 2, 17.0F, 0.72F);
        this.setCustomName(net.minecraft.network.chat.Component.literal("Elite Troop"));
        this.setInvisible(true);
        this.setHealth(40.0F);
    }
}
