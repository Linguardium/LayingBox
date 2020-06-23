package mod.linguardium.layingbox.api;

import java.util.Random;

public interface ChickenStats {
    public int getProduction();
    public void setProduction(int value);
    static int averagePlusProduction(Random random, ChickenStats p1, ChickenStats p2) {
        int pro1=p1.getProduction();;
        int pro2=p2.getProduction();
        int pro3=((pro1+pro2)/2)+random.nextInt(100);
        if (pro3>1000)
            pro3=1000;
        return pro3;
    }
}
