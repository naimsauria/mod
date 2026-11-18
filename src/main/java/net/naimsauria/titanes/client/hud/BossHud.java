package net.naimsauria.titanes.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.network.chat.Component;
import net.naimsauria.titanes.NaimsauriaMod;
import net.naimsauria.titanes.boss.BossKind;
import net.naimsauria.titanes.network.BossStatusPayload;

/** Barra propia: barra fantasma, estabilidad, nombres y umbrales por jefe. */
public final class BossHud {
    private static float ghost = 1.0f;
    private static int previousId = Integer.MIN_VALUE;

    private BossHud() { }

    public static void initialize() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,
            NaimsauriaMod.id("boss_hud"), BossHud::draw);
    }

    private static void draw(GuiGraphicsExtractor gui, DeltaTracker tick) {
        BossStatusPayload data = BossHudState.visible();
        if (data == null) { previousId = Integer.MIN_VALUE; return; }
        BossKind kind = BossKind.values()[data.kind()];
        float hp = Math.max(0f, Math.min(1f, data.health()));
        float poise = Math.max(0f, Math.min(1f, data.poise()));
        if (previousId != data.entityId()) {
            ghost = hp;
            previousId = data.entityId();
        }
        ghost += (hp - ghost) * (hp < ghost ? .035f : .35f);
        Minecraft mc = Minecraft.getInstance();
        int screen = mc.getWindow().getGuiScaledWidth();
        int width = Math.min(340, screen - 60);
        int x = (screen - width) / 2, y = 18;
        int mainColor = 0xFF000000 | kind.color();

        gui.fill(x-6, y-6, x+width+6, y+55, 0xB70A0C18);
        gui.text(mc.font, Component.translatable("entity.naimsauria." + kind.id()),
            x+3, y-2, 0xFFFFFFFF, true);
        gui.fill(x, y+15, x+width, y+30, 0xFF1D1E2A);
        gui.fill(x+2, y+17, x+2+(int)((width-4)*ghost), y+28, 0xFF7E778A);
        gui.fill(x+2, y+17, x+2+(int)((width-4)*hp), y+28, mainColor);
        for (int n=1; n<kind.phases(); n++) {
            int mark = x + (int)(width * n / (float)kind.phases());
            gui.fill(mark-1, y+15, mark+1, y+30, 0xFFE9DEB6);
        }
        gui.fill(x, y+35, x+width, y+41, 0xFF1D1E2A);
        gui.fill(x+1, y+36, x+1+(int)((width-2)*poise), y+40, 0xFFFFD57C);
        String hint = data.action() == 1 ? "  !" : "";
        gui.text(mc.font, Component.literal("PHASE " + data.phase() +
            "/" + kind.phases() + hint), x+3, y+44, 0xFFD9D4EB, true);
    }
}
