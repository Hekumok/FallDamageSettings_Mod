package com.hekumok.fallsettings;

import com.hekumok.hooklib.asm.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ModHooks {
    @Hook(at = @At(point = InjectionPoint.HEAD))
    public static void updateFallState(Entity entity, double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if(onGroundIn && entity.fallDistance > 0.0F && y < 0.0D) {
            entity.fallDistance = (float)((double)entity.fallDistance - y);
        }
    }

    @Hook(at = @At(point = InjectionPoint.VAR_ASSIGNMENT, targetVar = 6, ordinal = 0))
    public static int fall(EntityLivingBase entity, float distance, float damageMultiplier) {
        boolean isPlayer = entity instanceof EntityPlayerMP;
        boolean shouldUsePlayerSettings = isPlayer && !ModConfig.useEntitiesSettingsForPlayer;
        ModConfig.FallDamageSettings settings = shouldUsePlayerSettings ? ModConfig.playerFallDamage : ModConfig.entityFallDamage;

        PotionEffect potioneffect = entity.getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f = potioneffect == null ? 0.0F : (float)(potioneffect.getAmplifier() + 1);

        return MathHelper.ceil((distance - settings.minHeight - f) * settings.damage * damageMultiplier / settings.blocksQuantity);
    }
}