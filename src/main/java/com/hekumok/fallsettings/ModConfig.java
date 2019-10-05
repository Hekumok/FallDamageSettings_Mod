package com.hekumok.fallsettings;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Config;

@Config(modid = FallSettingsMod.MODID, category = ModConfig.CATEGORY)
public class ModConfig {
    @Config.Ignore
    public static final String CATEGORY = "config";

    @Config.Comment("Settings of fall on the ground")
    public static FallTypeSettings fallOnTheGround = new FallTypeSettings();

    @Config.Comment("Settings of fall into water")
    public static FallTypeSettings fallIntoWater = new FallTypeSettings();

    @Config.Comment("Settings of fall into lava")
    public static FallTypeSettings fallIntoLava = new FallTypeSettings();

    public static FallDamageSettings getSettingsForEntity(Entity entity) {
        FallTypeSettings type = entity.isInWater() ? fallIntoWater : entity.isInLava() ? fallIntoLava : fallOnTheGround;
        boolean isPlayer = entity instanceof EntityPlayer;
        boolean shouldUsePlayersSettings = isPlayer && !type.useCreaturesSettingsForPlayers;

        return shouldUsePlayersSettings ? type.players : type.creatures;
    }

    public static class FallTypeSettings {
        @Config.Comment("If set to true, settings for players will be overwritten by settings for creatures")
        public boolean useCreaturesSettingsForPlayers = true;

        @Config.Comment("Fall damage settings for creatures")
        public FallDamageSettings creatures = new FallDamageSettings();

        @Config.Comment("Fall damage settings for players")
        public FallDamageSettings players = new FallDamageSettings();
    }

    public static class FallDamageSettings {
        @Config.Comment("If set to true, this settings will work")
        public boolean active = false;

        @Config.Comment("Only falling above this height will deal damage")
        @Config.RangeInt(min = 3, max = 256)
        @Config.SlidingOption
        public int minHeight = 3;

        @Config.Comment("Damage per each N blocks")
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int damage = 1;

        @Config.Comment("Quantity of blocks for which damage will be dealt")
        @Config.RangeInt(min = 1, max = 256)
        @Config.SlidingOption
        public int blocksQuantity = 1;
    }
}