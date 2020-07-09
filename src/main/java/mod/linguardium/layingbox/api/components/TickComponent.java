package mod.linguardium.layingbox.api.components;

import nerdhub.cardinal.components.api.component.Component;

public interface TickComponent extends Component {
    public int getTickTime();
    public void setTickTime(int time);
}
