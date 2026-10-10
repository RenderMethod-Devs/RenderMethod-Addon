package com.renderswap.addon;

import com.mojang.logging.LogUtils;
import com.renderswap.addon.modules.RenderSwap;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class RenderSwapAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Render Swap");

    @Override
    public void onInitialize() {
        LOG.info("Render Swap loaded");
        Modules.get().add(new RenderSwap());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.renderswap.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RenderMethod-Devs", "render-swap");
    }
}
