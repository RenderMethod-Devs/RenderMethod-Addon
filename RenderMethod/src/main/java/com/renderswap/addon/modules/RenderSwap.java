package com.renderswap.addon.modules;

import com.renderswap.addon.RenderSwapAddon;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * One key press: render distance drops to the low value, then after the
 * delay (default 0.5s) it goes back to what it was. No second press needed.
 */
public class RenderSwap extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> lowDistance = sgGeneral.add(new IntSetting.Builder()
        .name("low-render-distance")
        .description("Render distance (in chunks) to drop to.")
        .defaultValue(2)
        .min(2)
        .max(32)
        .sliderRange(2, 32)
        .build()
    );

    private final Setting<Integer> delayMs = sgGeneral.add(new IntSetting.Builder()
        .name("revert-delay-ms")
        .description("How long to stay at the low render distance before restoring (milliseconds).")
        .defaultValue(500)
        .min(50)
        .max(5000)
        .sliderRange(50, 2000)
        .build()
    );

    private final Setting<Boolean> notify = sgGeneral.add(new BoolSetting.Builder()
        .name("chat-feedback")
        .description("Prints the render distance changes in chat.")
        .defaultValue(false)
        .build()
    );

    /** The player's render distance before we touched it. -1 = nothing saved. */
    private int saved = -1;
    private long startTime;

    public RenderSwap() {
        super(RenderSwapAddon.CATEGORY, "render-swap", "Press the keybind: render distance drops, then restores itself after a short delay.");
    }

    @Override
    public void onActivate() {
        if (mc.options == null || mc.world == null) {
            toggle();
            return;
        }

        saved = mc.options.getViewDistance().getValue();
        startTime = System.currentTimeMillis();
        setDistance(lowDistance.get());

        if (notify.get()) info("Render distance: (highlight)%d(default) -> (highlight)%d(default).", saved, lowDistance.get());
    }

    @Override
    public void onDeactivate() {
        restore();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (System.currentTimeMillis() - startTime >= delayMs.get()) toggle(); // triggers onDeactivate -> restore
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (isActive()) toggle();
    }

    private void restore() {
        if (mc.options == null || saved == -1) return;

        int restored = saved;
        saved = -1;
        setDistance(restored);

        if (notify.get()) info("Render distance restored to (highlight)%d(default).", restored);
    }

    private void setDistance(int chunks) {
        int clamped = Math.max(2, Math.min(32, chunks));
        mc.options.getViewDistance().setValue(clamped);
        mc.options.sendClientSettings();
        if (mc.worldRenderer != null) mc.worldRenderer.scheduleTerrainUpdate();
    }
}
