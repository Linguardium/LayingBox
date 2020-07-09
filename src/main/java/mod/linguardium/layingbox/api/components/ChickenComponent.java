package mod.linguardium.layingbox.api.components;

import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class ChickenComponent implements ProductionComponent,TickComponent {
    private int production;
    private int tickTime;
    @Override
    public int getProduction() {
        return production;
    }
    public void setProduction(int production) {
        if (production>1000)
            production=1000;
        this.production=production;
    }
    public void incrementProduction(Random random) {
        this.production+=random.nextInt(100);
    }
    public void incrementProduction() {
        this.production+=100;
    }
    @Override
    public void fromTag(CompoundTag compoundTag) {
        this.production=compoundTag.getInt("productionValue");

    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putInt("productionValue",this.production);
        return compoundTag;
    }

    @Override
    public int getTickTime() {
        return tickTime;
    }

    @Override
    public void setTickTime(int time) {
        this.tickTime=time;
    }
}
