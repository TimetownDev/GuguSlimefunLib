package me.ddggdd135.guguslimefunlib.api;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.guide.SurvivalSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.ddggdd135.guguslimefunlib.libraries.colors.CMIChatColor;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AEMenu extends ChestMenu {

    private static final int BACK_BUTTON_SLOT = 0;

    public AEMenu(String title) {
        super(CMIChatColor.translate(title));
    }

    public void addBackButton(SurvivalSlimefunGuide guide, Player p, PlayerProfile profile) {
        GuideHistory history = profile.getGuideHistory();

        if (history.size() > 1) {
            this.replaceExistingItem(
                    BACK_BUTTON_SLOT,
                    new CustomItemStack(ChestMenuUtils.getBackButton(p, "", "&f左击: &7返回上一页", "&fShift + 左击: &7返回主菜单")));

            this.addMenuClickHandler(BACK_BUTTON_SLOT, (pl, s, ic, action) -> {
                if (!action.isRightClicked() && action.isShiftClicked()) {
                    guide.openMainMenu(profile, 1);
                } else {
                    history.openLastEntry(guide);
                }
                return false;
            });

        } else {
            this.replaceExistingItem(
                    BACK_BUTTON_SLOT,
                    new CustomItemStack(ChestMenuUtils.getBackButton(
                            p, "", ChatColor.GRAY + Slimefun.getLocalization().getMessage(p, "guide.back.guide"))));
            this.addMenuClickHandler(BACK_BUTTON_SLOT, (pl, s, is, action) -> {
                guide.openMainMenu(profile, 1);
                return false;
            });
        }
    }

    public void setBackgroundNonClickable(boolean addBackground) {
        for (int i = 0; i < toInventory().getSize(); i++) {
            if (!hasClickHandler(i)) {
                addMenuClickHandler(i, (pl, s, is, action) -> false);
            }
            if (addBackground && getItemInSlot(i) == null) {
                replaceExistingItem(i, ChestMenuUtils.getBackground());
            }
        }
    }

    public void wipe(int start, int finish, boolean blockClicks) {
        for (int i = start; i <= finish; i++) {
            replaceExistingItem(i, null);

            if (blockClicks) {
                addMenuClickHandler(i, (pl, s, is, action) -> false);
            }
        }
    }

    public ItemStack pushItem(ItemStack item, int... slots) {
        if (item == null || item.getType() == Material.AIR) {
            throw new IllegalArgumentException("Cannot push null or AIR");
        }

        ItemStackWrapper wrapper = null;
        int amount = item.getAmount();

        for (int slot : slots) {
            if (amount <= 0) {
                break;
            }

            ItemStack stack = getItemInSlot(slot);

            if (stack == null) {
                replaceExistingItem(slot, item);
                return null;
            } else {
                int maxStackSize =
                        Math.min(stack.getMaxStackSize(), toInventory().getMaxStackSize());
                if (stack.getAmount() < maxStackSize) {
                    if (wrapper == null) {
                        wrapper = ItemStackWrapper.wrap(item);
                    }

                    if (ItemUtils.canStack(wrapper, stack)) {
                        amount -= (maxStackSize - stack.getAmount());
                        stack.setAmount(Math.min(stack.getAmount() + item.getAmount(), maxStackSize));
                        item.setAmount(amount);
                    }
                }
            }
        }

        if (amount > 0) {
            return new CustomItemStack(item, amount);
        } else {
            return null;
        }
    }

    public boolean hasClickHandler(int slot) {
        return this.getMenuClickHandler(slot) != null;
    }

    public AEMenu setSize(int size) {
        addItem(size - 1, null);
        return this;
    }
}
