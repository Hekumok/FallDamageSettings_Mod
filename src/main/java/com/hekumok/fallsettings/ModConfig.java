package com.hekumok.fallsettings;

import net.minecraftforge.common.config.Config;

@Config(modid = FallSettingsMod.MODID, category = ModConfig.CATEGORY)
public class ModConfig {
    @Config.Ignore
    public static final String CATEGORY = "config";

    @Config.Comment("If set to true, creatures settings will be applied to the player instead of his own")
    public static boolean useEntitiesSettingsForPlayer = true;

    @Config.Comment("Fall damage settings for creatures")
    public static FallDamageSettings entityFallDamage = new FallDamageSettings();

    @Config.Comment("Fall damage settings for player")
    public static FallDamageSettings playerFallDamage = new FallDamageSettings();

    public static class FallDamageSettings {
        @Config.Comment("Only falling above this height will deal damage")
        @Config.RangeInt(min = 3)
        public int minHeight = 3;

        @Config.Comment("Damage per each N blocks")
        @Config.RangeInt(min = 0)
        public int damage = 1;

        @Config.Comment("Quantity of blocks for which damage will be dealt")
        @Config.RangeInt(min = 1)
        public int blocksQuantity = 1;
    }
}