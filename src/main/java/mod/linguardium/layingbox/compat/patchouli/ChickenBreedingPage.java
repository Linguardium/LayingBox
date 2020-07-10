package mod.linguardium.layingbox.compat.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;
import vazkii.patchouli.common.base.Patchouli;
import vazkii.patchouli.common.util.EntityUtil;

import java.util.function.BiFunction;

public class ChickenBreedingPage extends PageWithText {
    @SerializedName("entity") public String entityId;
    @SerializedName("parent1") public String parent1Id;
    @SerializedName("parent2") public String parent2Id;

    float scale = 1F;
    @SerializedName("offset") float extraOffset = 0F;
    String name;

    boolean rotate = true;
    @SerializedName("default_rotation") float defaultRotation = -45f;

    transient boolean errored;
    transient Entity parent1;
    transient Entity parent2;
    transient Entity entity;
    transient BiFunction<World, String, Entity> creator;
    transient float renderScale, offset;

    @Override
    public void build(BookEntry entry, int pageNum) {
        super.build(entry, pageNum);

        creator = (w,i)->EntityUtil.loadEntity(i).apply(w);
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);

        loadEntities(parent.getMinecraft().world);
    }

    @Override
    public int getTextHeight() {
        return 115;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        int x = GuiBook.PAGE_WIDTH / 2 - 53;
        int y = 7;
        RenderSystem.enableBlend();
        RenderSystem.color3f(1F, 1F, 1F);
        GuiBook.drawFromTexture(ms, book, x, y, 405, 149, 106, 106);

        if (name == null || name.isEmpty()) {
            if (entity != null) {
                parent.drawCenteredStringNoShadow(ms, entity.getName(), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
            }
        } else {
            parent.drawCenteredStringNoShadow(ms, name, GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
        }

        if (errored) {
            fontRenderer.drawWithShadow(ms, I18n.translate("patchouli.gui.lexicon.loading_error"), 58, 60, 0xFF0000);
        }

        if (entity != null) {
            float rotation = rotate ? ClientTicker.total : defaultRotation;
            renderEntity(ms, entity, parent.getMinecraft().world, 58, 80, rotation, renderScale, offset);
        }
        if (parent1 != null) {
            float rotation = rotate ? ClientTicker.total : defaultRotation;
            renderEntity(ms, parent1, parent.getMinecraft().world, 40, 60, rotation, renderScale, offset);
        }
        if (parent2 != null) {
            float rotation = rotate ? ClientTicker.total : defaultRotation;
            renderEntity(ms, parent2, parent.getMinecraft().world, 70, 60, -rotation, renderScale, offset);
        }

        super.render(ms, mouseX, mouseY, pticks);
    }

    public static void renderEntity(MatrixStack ms, Entity entity, World world, float x, float y, float rotation, float renderScale, float offset) {
        entity.world = world;

        ms.push();
        ms.translate(x, y, 50);
        ms.scale(renderScale, renderScale, renderScale);
        ms.translate(0, offset, 0);
        ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
        ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
        EntityRenderDispatcher erd = MinecraftClient.getInstance().getEntityRenderManager();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        erd.setRenderShadows(false);
        erd.render(entity, 0, 0, 0, 0, 1, ms, immediate, 0xF000F0);
        erd.setRenderShadows(true);
        immediate.draw();
        ms.pop();
    }

    private void loadEntities(World world) {
        if (!errored && (entity == null || !entity.isAlive())) {
            try {
                entity = creator.apply(world,entityId);
                parent1 = creator.apply(world,parent1Id);
                parent2 = creator.apply(world,parent2Id);
                float width = Math.max(entity.getWidth(),Math.max(parent1.getWidth(),parent2.getWidth()));
                float height = Math.max(entity.getHeight(),Math.max(parent1.getHeight(),parent2.getHeight()));

                float entitySize = Math.max(1F,Math.max(width,height));

                renderScale = 100F / entitySize * 0.8F * scale;
                offset = Math.max(height, entitySize) * 0.5F + extraOffset;
            } catch (Exception e) {
                errored = true;
                Patchouli.LOGGER.error("Failed to load entity", e);
            }
        }
    }

}
