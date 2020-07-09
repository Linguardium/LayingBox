package mod.linguardium.layingbox.mixin;

import mod.linguardium.layingbox.LayingBoxMain;
import mod.linguardium.layingbox.api.ChickenStats;
import mod.linguardium.layingbox.api.LayingBoxProvider;
import mod.linguardium.layingbox.api.components.ChickenComponent;
import mod.linguardium.layingbox.api.components.ModComponents;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

import static mod.linguardium.layingbox.utils.ChickenUtils.getAverageProduction;
import static mod.linguardium.layingbox.utils.ChickenUtils.getBabyType;

@Mixin(ChickenEntity.class)
public abstract class ChickenProviderMixin extends AnimalEntity implements LayingBoxProvider<ChickenEntity>, ChickenStats {
    @Shadow public int eggLayTime;
    @Shadow public abstract boolean hasJockey();

    //private static final TrackedData<Integer> PRODUCTION = DataTracker.registerData(ChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);;

    protected ChickenProviderMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

/*    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PRODUCTION, 0);
    }*/

    @Inject(method="createChild",at=@At("TAIL"),cancellable = true)
    private void LayingBox_MakinBabies(PassiveEntity passiveEntity, CallbackInfoReturnable<ChickenEntity> info) {
/*        Identifier parent1=EntityType.getId(this.getType());
        Identifier parent2=EntityType.getId(passiveEntity.getType());
        List<EntityType<? extends ChickenEntity>> children =new ArrayList<>();
        chickens.forEach((chicken,config)->{
            boolean p1is1 = config.parent1.equals(parent1);
            boolean p1is2 = config.parent1.equals(parent2);
            boolean p2is1 = config.parent2.equals(parent1);
            boolean p2is2 = config.parent2.equals(parent2);
            if (p1is1 && p2is2 || p1is2 && p2is1) {
                children.add(chicken);
            }
        });
//        if (!parent1.equals(parent2))
//            children.addAll(ChickenConfigs.BreedingMap.get(Pair.of(parent2,parent1)));
        ChickenEntity baby;
        boolean explicit = world.getGameRules().getBoolean(LayingBoxMain.requireBiomes);
        Biome biome_parent1 = world.getBiome(this.getBlockPos());
        Biome biome_parent2 = passiveEntity.getEntityWorld().getBiome(passiveEntity.getBlockPos());
        List<EntityType<? extends ChickenEntity>> favorites = children.stream().filter(type->(isFavoredBiome(type,biome_parent1,explicit) || isFavoredBiome(type,biome_parent2,explicit))).collect(Collectors.toList());
        if (explicit) {
            favorites.addAll(children.stream().filter(type->(isFavoredBiome(type,biome_parent1,false) || isFavoredBiome(type,biome_parent2,false))).collect(Collectors.toList()));
            favorites.add((EntityType<? extends ChickenEntity>) this.getType());
            if (!this.getType().equals(passiveEntity.getType()))
                favorites.add((EntityType<? extends ChickenEntity>) passiveEntity.getType());
        }
        if (explicit && favorites.size() > 0) {
            baby = favorites.get(world.random.nextInt(favorites.size())).create(this.world);
        }else if (children.size() > 0 && !explicit && world.getRandom().nextFloat() < 0.10f) {
            EntityType<? extends ChickenEntity> child = children.get(world.getRandom().nextInt(children.size()));
            //EntityType<ChickenEntity>) children.toArray()[world.getRandom().nextInt(children.size())];
            baby = child.create(this.world);
        }else {
            baby = (ChickenEntity) this.getType().create(this.world);
        }*/
        ChickenEntity baby;
        boolean explicit = world.getGameRules().getBoolean(LayingBoxMain.requireBiomes);

        baby = (ChickenEntity) Registry.ENTITY_TYPE.get( getBabyType(this.world.random,this,passiveEntity,explicit)).create(this.world);
        if (baby != null &&  (baby.getType().equals(this.getType()) || baby.getType().equals(passiveEntity.getType()))) {
            int f = getAverageProduction((ComponentProvider) this, (ComponentProvider) passiveEntity);
            if (f > 0)
                f += random.nextInt(50);
            Optional<ChickenComponent> comp3 = ModComponents.CHICKEN.maybeGet(baby);
            if (comp3.isPresent()) {
                comp3.get().setProduction(f);
            }
        }
        info.setReturnValue(baby);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return (other instanceof ChickenEntity) && (this.isInLove() && other.isInLove());
    }

    /*@Override
    public int getProduction() {
       return this.dataTracker.get(PRODUCTION);
    }
    @Redirect(at=@At(value="INVOKE",target="net/minecraft/entity/passive/ChickenEntity.dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"),method="tickMovement")
    private ItemEntity LayingBox_dropItem(ChickenEntity me, ItemConvertible item) {
        for (ItemStack stack : getDrops()) {
            dropStack(stack,0);
        }
        return null;
    }*/
    /*@Override
    public void setProduction(int value) {
        if (value>1000)
            value=1000;
        this.dataTracker.set(PRODUCTION,value);
    }*/
    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> stacks = DefaultedList.of();
        //int dropadds = this.getProduction()/100;
        //int count=1+((dropadds>0)?world.getRandom().nextInt(dropadds):0);
        stacks.add(new ItemStack(Items.EGG));
        if (getEntityWorld().getRandom().nextFloat() <= 0.1) {
            stacks.add(new ItemStack(Items.FEATHER));
        }
        return stacks;
    }
    /*@Inject(at=@At("TAIL"),method="writeCustomDataToTag")
    private void writeProductionToTag(CompoundTag tag, CallbackInfo info) {
        tag.putInt("dropTickTime",dropTickTime);
    }*/
    @Inject(at=@At("TAIL"),method="readCustomDataFromTag")
    private void readProductionFromTag(CompoundTag tag, CallbackInfo info) {
        if (tag.contains("Production", NbtType.INT))
            ModComponents.CHICKEN.maybeGet(this).ifPresent(c->c.setProduction(tag.getInt("Production")));
            //this.dropTickTime=tag.getInt("dropTickTime");
    }

    @Inject(at=@At(value="RETURN"),method="tickMovement")
    private void LayingBox_eggTickTimeDisable(CallbackInfo ci) {
        this.eggLayTime=6000;
        if (!this.world.isClient && this.isAlive() && !this.isBaby() && !this.hasJockey()) {
            Optional<ChickenComponent> component = ModComponents.CHICKEN.maybeGet(this);
            int tickTime = 6000;
            if (component.isPresent()) {
                tickTime = component.get().getTickTime();
            }
            if (--tickTime <= 0) {
                this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                for (ItemStack stack : getDrops()) {
                    dropStack(stack);
                }
                int production = ModComponents.getProduction((ComponentProvider) this);

                tickTime = (this.random.nextInt(6000) + 6000);
                if (production > 0) {
                    tickTime -= (6 * production);
                }
            }
            int finalTickTime = tickTime; // its effectively final (IDE lamda concerns)
            component.ifPresent(c->c.setTickTime(finalTickTime));
        }
    }
    @Inject(at=@At("TAIL"),method="<init>")
    private void layingbox_constructor(CallbackInfo ci){
        int production = ModComponents.getProduction((ComponentProvider) this);
        int dropTickTime = (this.random.nextInt(6000) + 6000) - (6 * production);
        ModComponents.CHICKEN.maybeGet(this).ifPresent(c->c.setTickTime(dropTickTime));
    }
}
