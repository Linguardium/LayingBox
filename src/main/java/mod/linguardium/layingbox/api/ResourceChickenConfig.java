package mod.linguardium.layingbox.api;


import net.minecraft.util.Identifier;

import java.util.List;

public class ResourceChickenConfig {
    public String name="";
    public int base_color=-1;
    public int accent_color=-1;
    public Identifier dropItem;
    public Identifier base_texture=new Identifier("layingbox:textures/entity/chicken/skin.png");
    public Identifier accent_texture=new Identifier("layingbox:textures/entity/chicken/markings.png");
    public Identifier parent1=new Identifier("minecraft:chicken");
    public Identifier parent2=new Identifier("minecraft:chicken");
    public List<Identifier> favored_biome_tags;
    public Identifier feed;
}
