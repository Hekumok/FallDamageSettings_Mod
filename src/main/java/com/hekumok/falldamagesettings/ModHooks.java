package com.hekumok.falldamagesettings;

import com.hekumok.hooklib.asm.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ModHooks {
    /*
     * Fix old Minecraft bug and calculate distance of fall into water/lava
     */
    @Hook(at = @At(point = InjectionPoint.HEAD), setLocalVar = 3)
    public static boolean updateFallState(Entity entity, double _y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if((entity.isInWater() || entity.isInLava()) && ModConfig.getSettingsForEntity(entity).active && entity.fallDistance > 0.0F) {
            // this hack with onGroundIn is necessary for falling into water/lava to work just like falling to the ground
            onGroundIn = true;

            Material material = entity.isInWater() ? Material.WATER : Material.LAVA;

            int x = MathHelper.floor(entity.posX),
                y = MathHelper.floor(entity.posY),
                z = MathHelper.floor(entity.posZ);

            BlockPos bp = new BlockPos(x, y, z);

            // if that happens so we need to fix this code and calculate also neighboring blocks on which entity stays
            // by x & z axis using bounding box
            if(entity.world.getBlockState(bp).getMaterial() != material) {
                FallDamageSettingsMod.logger.error(FallDamageSettingsMod.MODID +
                        ": Smth went wrong while calculating the entry point into water/lava (when falling)");
            }

            // find entry point into water/lava
            do {
                bp = bp.up();
            } while(entity.world.getBlockState(bp).getMaterial() == material);

            int entryPointY = bp.getY();

            // we can fall under lava/water but damage should be calculated as if we stay on the top of water/lava
            entity.fallDistance -= entryPointY - entity.posY;
        }

        // fix Minecraft bug
        if(onGroundIn && _y < 0.0D) {
            entity.fallDistance = (float)((double)entity.fallDistance - _y);
        }

        return onGroundIn;
    }

    // fix fall distance when jump into lava
    @Hook(at = @At(point = InjectionPoint.RETURN))
    public static void onEntityUpdate(Entity entity) {
        if (entity.isInLava() && ModConfig.getSettingsForEntity(entity).active) {
            entity.fallDistance *= 2;
        }
    }

    // fix fall distance when jump into water
    @Hook(at = @At(point = InjectionPoint.HEAD))
    public static void updateFallState(EntityLivingBase entity, double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        float fallDistance = entity.fallDistance;

        if(!entity.isInWater() && entity.handleWaterMovement() && ModConfig.getSettingsForEntity(entity).active) {
            entity.fallDistance = fallDistance;
        }
    }

    // calculate new fall damage
    @Hook(at = @At(point = InjectionPoint.VAR_ASSIGNMENT, targetVar = 6, ordinal = 0), setLocalVar = 6)
    public static int fall(EntityLivingBase entity, float distance, float damageMultiplier, @Hook.LocalVariable(6) int originalVar) {
        ModConfig.FallDamageSettings config = ModConfig.getSettingsForEntity(entity);

        if(!config.active) {
            return originalVar;
        }

        PotionEffect potioneffect = entity.getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f = potioneffect == null ? 0.0F : (float)(potioneffect.getAmplifier() + 1);

        return MathHelper.ceil((distance - config.minHeight - f) * config.damage * damageMultiplier / config.blocksQuantity);
    }
}