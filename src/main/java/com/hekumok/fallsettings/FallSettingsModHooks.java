package com.hekumok.fallsettings;

import com.hekumok.hooklib.asm.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class FallSettingsModHooks {
    @Hook(at = @At(point = InjectionPoint.HEAD))
    public static void updateFallState(Entity entity, double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if(onGroundIn && entity.fallDistance > 0.0F && y < 0.0D) {
            entity.fallDistance = (float)((double)entity.fallDistance - y);
        }
    }
}