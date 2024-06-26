package me.ddggdd135.guguslimefunlib.api.abstracts;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class TickingBlock extends SlimefunItem {
    public abstract boolean isSynchronized();

    @OverridingMethodsMustInvokeSuper
    protected abstract void tick(@Nonnull Block block, @Nonnull SlimefunItem item, @Nonnull SlimefunBlockData data);

    public TickingBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemHandler(createBlockTicker());
    }

    public TickingBlock(
            ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe,
            @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
        addItemHandler(createBlockTicker());
    }

    protected TickingBlock(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
        addItemHandler(createBlockTicker());
    }

    private BlockTicker createBlockTicker() {
        return new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return TickingBlock.this.isSynchronized();
            }

            @Override
            public void tick(Block block, SlimefunItem item, SlimefunBlockData data) {
                TickingBlock.this.tick(block, item, data);
            }
        };
    }
}
