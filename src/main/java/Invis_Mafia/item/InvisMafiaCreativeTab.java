package net.minecraft.world.item;

import net.minecraft.network.chat.Component;

public final class InvisMafiaCreativeTab {
    private InvisMafiaCreativeTab() {
    }

    public static CreativeModeTab create(Item mafiaSummoner, Item ashwagSeal) {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 7)
                .title(Component.translatable("itemGroup.invis_mafia"))
                .icon(() -> new ItemStack(mafiaSummoner))
                .displayItems((parameters, output) -> {
                    output.accept(mafiaSummoner);
                    output.accept(ashwagSeal);
                })
                .build();
    }
}