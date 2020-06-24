package mod.linguardium.layingbox.mixin;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.GameRules;

@Mixin(GameRules.BooleanRule.class)
public interface BooleanRuleAccessor{

    @Invoker
    public static GameRules.Type<GameRules.BooleanRule> invokeCreate(boolean defaultValue) {
        throw new NotImplementedException("Mixin failed");
     }

}