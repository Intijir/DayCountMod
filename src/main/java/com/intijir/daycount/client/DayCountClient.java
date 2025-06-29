package com.intijir.daycount.client;

import com.intijir.daycount.config.DayCountConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix3x2fStack;

@Environment(EnvType.CLIENT)
public class DayCountClient implements ClientModInitializer, HudRenderCallback {
    private int cachedDayTime = -1;

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                int currentDayTime = (int) (MinecraftClient.getInstance().world.getTimeOfDay() / 24000L);
                if (currentDayTime != cachedDayTime) {
                    // Day time has changed, update the cached value and do any necessary updates
                    cachedDayTime = currentDayTime;
                    // Update the GUI or other elements as needed
                    //MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
                    HudRenderCallback.EVENT.register(this);
                }
            }
        });
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient mcClient = MinecraftClient.getInstance();

        if (DayCountConfig.INSTANCE.dayCountEnabled) {
            assert mcClient.player != null;
            if (!mcClient.player.isSpectator()) {
                int currentDay = (int) (MinecraftClient.getInstance().world.getTimeOfDay() / 24000L);
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                Matrix3x2fStack matrixStack = drawContext.getMatrices();

                matrixStack.pushMatrix();
                matrixStack.translate(DayCountConfig.INSTANCE.locationX, DayCountConfig.INSTANCE.locationY, matrixStack);
                matrixStack.scale(DayCountConfig.INSTANCE.sizeX, DayCountConfig.INSTANCE.sizeY, matrixStack);
                drawContext.drawTextWithShadow(textRenderer, "Day: " + (currentDay + DayCountConfig.INSTANCE.dayOffset), 2, 2, DayCountConfig.INSTANCE.colorWithTransparency);
                matrixStack.popMatrix();
            }
        }
    }
}
