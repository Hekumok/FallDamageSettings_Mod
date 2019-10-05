package com.hekumok.falldamagesettings;

import com.hekumok.hooklib.minecraft.HookLoader;
import com.hekumok.hooklib.minecraft.PrimaryClassTransformer;

public class ModHookLoader extends HookLoader {
    // включает саму HookLib'у. Делать это можно только в одном из HookLoader'ов.
    // При желании, можно включить com.hekumok.hooklib.minecraft.HookLibPlugin и не указывать здесь это вовсе.
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    public void registerHooks() {
        //регистрируем класс, где есть методы с аннотацией @Hook
        registerHookContainer("com.hekumok.falldamagesettings.ModHooks");
    }
}
