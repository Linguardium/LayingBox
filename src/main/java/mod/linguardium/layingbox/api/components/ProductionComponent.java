package mod.linguardium.layingbox.api.components;


import nerdhub.cardinal.components.api.component.Component;

import java.util.Random;

public interface ProductionComponent extends Component {
    int getProduction();
    public void setProduction(int production);
    public void incrementProduction(Random random);
    public void incrementProduction();
}
