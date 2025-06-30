package com.intijir.daycount.client;

import com.intijir.daycount.config.DayCountConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;

@Environment(EnvType.CLIENT)
public class DayCountClient implements ClientModInitializer {
    Identifier dayCountHudElement = Identifier.of("daycount", "daycount_hud");
    private int cachedDayTime = -1;
    private int currentDay = 0;

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                if (MinecraftClient.getInstance().world != null) {
                    currentDay = (int) (MinecraftClient.getInstance().world.getTimeOfDay() / 24000L);
                    if (currentDay != cachedDayTime) {
                        // Day time has changed, update the cached value and do any necessary updates
                        cachedDayTime = currentDay;
                    }
                }
            }
        });

        HudElementRegistry.addLast(dayCountHudElement, (context, tCounter) -> {
            Matrix3x2fStack matrixStack = context.getMatrices();
            matrixStack.pushMatrix();
            matrixStack.translate(DayCountConfig.INSTANCE.locationX, DayCountConfig.INSTANCE.locationY, matrixStack);
            matrixStack.scale(DayCountConfig.INSTANCE.sizeX, DayCountConfig.INSTANCE.sizeY, matrixStack);
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, DayCountConfig.INSTANCE.dayCounterString + (currentDay + DayCountConfig.INSTANCE.dayOffset), 2, 2, !MinecraftClient.getInstance().player.isSpectator() ? (int) Long.parseLong(DayCountConfig.INSTANCE.colorWithTransparency, 16) : 0);
            matrixStack.popMatrix();
        });

    }
}
