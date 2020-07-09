package mod.linguardium.layingbox.blocks.blockentity;

import mod.linguardium.layingbox.recipes.PelletRecipe;
import mod.linguardium.layingbox.api.PelletizerInventory;
import mod.linguardium.layingbox.blocks.ModBlocks;
import mod.linguardium.layingbox.gui.PelletGuiDescription;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import static mod.linguardium.layingbox.recipes.ModRecipes.PELLET_RECIPE_TYPE;

public class AutomaticPelletizerEntity extends BlockEntity implements InventoryProvider, NamedScreenHandlerFactory, Tickable {
    private PelletizerInventory inventory;
    private int ticksRemaining=0;
    private PelletRecipe recipe;
    private static final int TICK_INTERVAL = 20;
    private int tickCount;
    public AutomaticPelletizerEntity() {
        super(ModBlocks.AUTOMATIC_PELLETIZER_ENTITY);
        inventory = new PelletizerInventory(2);
        tickCount=0;
    }

    private void checkCurrentRecipe() {

    }
    private PelletRecipe getRecipe() {
        return this.world.getRecipeManager().getFirstMatch(PELLET_RECIPE_TYPE, inventory, this.world).orElse(null);
    }
    private void dropPellet() {

    }

    @Override
    public void tick() {
        tickCount++;
        if (tickCount<TICK_INTERVAL)
            return;
        tickCount=0;
        if (ticksRemaining>0) {
            ticksRemaining--;
            if (ticksRemaining<1) {
                dropPellet();
            }
        }
        if (ticksRemaining < 1) {
            if (inventory.getStack(1).getItem().equals(Items.WHEAT_SEEDS)) {
                recipe = getRecipe();
                if (recipe != null) {
                    ticksRemaining = recipe.getCookTime();
                    inventory.getStack(0).decrement(1);
                    inventory.getStack(1).decrement(1);
                }
            }
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Items",inventory.getTags());
        tag.putInt("ticksRemaining",ticksRemaining);
        tag.putInt("tickCount",tickCount);
        if (recipe != null) {
            tag.putString("Recipe", recipe.getId().toString());
        }
        return tag;
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        inventory.readTags(tag.getList("Items", NbtType.COMPOUND));
        if (tag.contains("Recipe",NbtType.STRING)) {
            recipe = (PelletRecipe) world.getRecipeManager().get(new Identifier(tag.getString("Recipe"))).orElse(null);
        }
        ticksRemaining = tag.getInt("ticksRemaining");
        tickCount = tag.getInt("tickCount");
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        // Using the block name as the screen title
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new PelletGuiDescription(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
}
