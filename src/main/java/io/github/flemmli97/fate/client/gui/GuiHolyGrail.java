package io.github.flemmli97.fate.client.gui;

import io.github.flemmli97.fate.network.C2SGrailReward;
import io.github.flemmli97.fate.network.PacketHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Map;
import java.util.stream.Collectors;

public class GuiHolyGrail extends Screen {

    private final Map<ResourceLocation, TextComponent> rewards;

    public GuiHolyGrail(Map<ResourceLocation, String> rewards) {
        super(new TranslationTextComponent("fate.gui.holy_grail"));
        this.rewards = rewards.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->{
            String key = "grail.loot."+e.getKey().toString();
            TranslationTextComponent translationTextComponent = new TranslationTextComponent(key);
            if(translationTextComponent.getUnformattedComponentText().equals(key))
                return new StringTextComponent(e.getValue());
            return translationTextComponent;
        }));
    }

    @Override
    protected void init() {
        super.init();
        int i = 0;
        for (Map.Entry<ResourceLocation, TextComponent> s : this.rewards.entrySet()) {
            this.addButton(new ButtonValue<String>(this.width / 2 - 150 + (i / 8 * 200), this.height / 2 - 80 + (i * 30), 100, 20, s.getValue(),
                    button -> {
                        PacketHandler.sendToServer(new C2SGrailReward(s.getKey()));
                        GuiHolyGrail.this.minecraft.player.closeScreen();
                    }).setVal(s.getKey().toString()));
            i++;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}