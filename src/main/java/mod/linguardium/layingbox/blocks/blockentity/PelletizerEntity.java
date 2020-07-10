package mod.linguardium.layingbox.blocks.blockentity;

import io.github.cottonmc.component.UniversalComponents;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import mod.linguardium.layingbox.EventedBlockTypeHelper.EventedBlockEntity;
import mod.linguardium.layingbox.api.PelletizerInventory;
import mod.linguardium.layingbox.blocks.ModBlocks;
import mod.linguardium.layingbox.gui.PelletGuiDescription;
import mod.linguardium.layingbox.items.ModItems;
import mod.linguardium.layingbox.recipes.PelletRecipe;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Optional;

import static mod.linguardium.layingbox.recipes.ModRecipes.PELLET_RECIPE_TYPE;

public class PelletizerEntity extends EventedBlockEntity implements InventoryProvider, NamedScreenHandlerFactory, EventedBlockEntity.RedstoneListener, PropertyDelegateHolder, BlockEntityClientSerializable {
    protected PelletizerInventory inventory;
    protected int ticksRemaining=0;
    protected int ticksTotal = 0;
    protected Identifier recipe;
    protected final PropertyDelegate propertyDelegate;
    public PelletizerEntity() {
        this(ModBlocks.PELLETIZER_ENTITY);
    }
    public PelletizerEntity(BlockEntityType<? extends PelletizerEntity> type) {
        super(type);
        inventory = new PelletizerInventory(2);
        propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return PelletizerEntity.this.ticksRemaining;
                    case 1:
                        return PelletizerEntity.this.ticksTotal;
                    case 2:
                        return PelletizerEntity.this.ticksTotal-PelletizerEntity.this.ticksRemaining;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        PelletizerEntity.this.ticksRemaining=value;
                        break;
                    case 1:
                        PelletizerEntity.this.ticksTotal=value;
                        break;
                    case 2:
                        PelletizerEntity.this.ticksRemaining=PelletizerEntity.this.ticksTotal-value;
                        break;
                }

            }

            public int size() {
                return 3;
            }
        };
    }
    protected Identifier getRecipe() {
        if (this.world != null && this.world.getRecipeManager() != null)
            return this.world.getRecipeManager().getFirstMatch(PELLET_RECIPE_TYPE, inventory, this.world).map(AbstractCookingRecipe::getId).orElse(null);
        return null;
    }
    protected Recipe<?> getRecipe(Identifier id) {
        if (this.world != null && this.world.getRecipeManager() != null)
            return this.world.getRecipeManager().get(id).orElse(null);
        return null;
    }
    protected boolean canOutput() {
        if (world == null)
            return false;
        BlockPos outPos = pos.offset(this.getCachedState().get(HorizontalFacingBlock.FACING));
        return (world.getBlockState(outPos).isAir() || !Block.isSideSolidFullSquare(world.getBlockState(outPos), world, outPos, world.getBlockState(pos).get(HorizontalFacingBlock.FACING))) ||
               UniversalComponents.INVENTORY_COMPONENT.maybeGet(world.getBlockEntity(outPos)).isPresent();
    }
    protected boolean dropPellet() {
        if (world == null)
            return false;
        BlockPos outPos = pos.offset(this.getCachedState().get(HorizontalFacingBlock.FACING));
        BlockState outputSideState = world.getBlockState(outPos);
        BlockEntity outputSideEntity = world.getBlockEntity(outPos);
        Inventory inv = HopperBlockEntity.getInventoryAt(world,outPos);
        if (recipe != null) {

            Optional<? extends Recipe<?>> pRec = world.getRecipeManager().get(recipe);
            pRec.ifPresent(r -> {
                        if (r instanceof PelletRecipe) {
                            ItemStack out = (pRec.get()).getOutput().copy();
                            if (inv != null) {
                                out = HopperBlockEntity.transfer(null,inv,out,this.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite());
                                //out = inv.insertStack(out, ActionType.PERFORM);
                            }
                            if (!out.isEmpty()) {
                                ItemScatterer.spawn(world, outPos.getX(), outPos.getY(), outPos.getZ(),out );
                            }
                        }
                    });
            recipe = null;
            return true;
        }

        return false;
    }

    @Override
    public void onRedstoneRising() {
        if (world.isClient())
            return;
        if (recipe != null && --ticksRemaining<=0) {
                if (canOutput()) {
                    dropPellet();
                }else{
                    ticksRemaining=1;
                }
        }
        if (ticksRemaining < 1) {
            ticksRemaining = ticksTotal=setupNewRecipe();
        }
        sync();
    }
    protected int setupNewRecipe() {
        if (inventory.getStack(1).getItem().isIn(ModItems.SEEDS_TAG)) {
            recipe = getRecipe();
            PelletRecipe pRec = (PelletRecipe) getRecipe(recipe);
            if (recipe != null && pRec != null) {
                inventory.getStack(0).decrement(1);
                inventory.getStack(1).decrement(1);
                return pRec.getCookTime();
            }
        }else{
            recipe=null;
        }
        return 0;
    }
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag,inventory.getStacks());
        //tag.put("Items",inventory.getTags());
        tag.putInt("ticks",ticksRemaining);
        tag.putInt("ticksTotal",ticksTotal);
        if (recipe != null) {
            tag.putString("Recipe", recipe.toString());
        }
        return tag;
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(2,ItemStack.EMPTY);
        Inventories.fromTag(tag,stacks);
        inventory = new PelletizerInventory(2,stacks);
        //inventory.readTags(tag.getList("Items", NbtType.COMPOUND));
        if (tag.contains("Recipe",NbtType.STRING)) {
            recipe = new Identifier(tag.getString("Recipe"));
        }
        ticksTotal = tag.getInt("ticksTotal");
        ticksRemaining = tag.getInt("ticks");
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


    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.ticksRemaining=tag.getInt("ticks");
        this.ticksTotal=tag.getInt("ticksTotal");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag.putInt("ticks",this.ticksRemaining);
        tag.putInt("ticksTotal",this.ticksTotal);
        return tag;
    }
}
