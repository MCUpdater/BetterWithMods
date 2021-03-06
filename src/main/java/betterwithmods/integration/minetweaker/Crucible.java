package betterwithmods.integration.minetweaker;

import betterwithmods.craft.bulk.BulkRecipe;
import betterwithmods.craft.bulk.CraftingManagerCrucible;
import betterwithmods.integration.minetweaker.utils.BaseListAddition;
import betterwithmods.integration.minetweaker.utils.BaseListRemoval;
import betterwithmods.integration.minetweaker.utils.LogHelper;
import betterwithmods.integration.minetweaker.utils.StackHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static betterwithmods.integration.minetweaker.utils.InputHelper.*;

/**
 * Created by tyler on 9/3/16.
 */
@ZenClass("mods.betterwithmods.Crucible")
public class Crucible {
    private final static List<BulkRecipe> recipes = CraftingManagerCrucible.getInstance().getRecipes();
    @ZenMethod
    public static void add(IItemStack output, IItemStack secondary, IIngredient[] inputs) {
        MineTweakerAPI.apply(new Add(toStack(output),toStack(secondary),toInputs(inputs)));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        List<BulkRecipe> toRemove = new ArrayList<>();
        for(BulkRecipe recipe: recipes) {
            if(StackHelper.matches(output,toIItemStack(recipe.getOutput()))) {
                toRemove.add(recipe);
            }
        }
        if(!toRemove.isEmpty()) {
            MineTweakerAPI.apply(new Remove(toRemove));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "crucible", output.toString()));
        }
    }

    public static class Add extends BaseListAddition<BulkRecipe> {

        protected Add(ItemStack output, ItemStack secondary, Object[] inputs) {
            super("crucible", Crucible.recipes);
        }

        @Override
        protected String getRecipeInfo(BulkRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
    public static class Remove extends BaseListRemoval<BulkRecipe> {

        protected Remove(List<BulkRecipe> list) {
            super("crucible", Crucible.recipes, list);
        }

        @Override
        protected String getRecipeInfo(BulkRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
}
