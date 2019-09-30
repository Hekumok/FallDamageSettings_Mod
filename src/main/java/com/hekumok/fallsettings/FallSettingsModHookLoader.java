package com.hekumok.fallsettings;

import com.hekumok.hooklib.minecraft.HookLoader;
import com.hekumok.hooklib.minecraft.PrimaryClassTransformer;

public class FallSettingsModHookLoader extends HookLoader {
    // включает саму HookLib'у. Делать это можно только в одном из HookLoader'ов.
    // При желании, можно включить gloomyfolken.hooklib.minecraft.HookLibPlugin и не указывать здесь это вовсе.
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    public void registerHooks() {
        //регистрируем класс, где есть методы с аннотацией @Hook
        registerHookContainer("com.hekumok.fallsettings.FallSettingsModHooks");
    }
}
