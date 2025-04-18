package me.ddggdd135.guguslimefunlib.items;

import me.ddggdd135.guguslimefunlib.nms.ItemStackNMS;
import me.ddggdd135.guguslimefunlib.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

public class ItemKey {
    private ItemStack itemStack;
    private ItemType type;
    private Object nms;
    private int hash;

    public ItemKey(ItemStack itemStack) {
        try {
            itemStack = itemStack.asOne();

            if (ItemStackNMS.getCraftItemStackClass().isAssignableFrom(itemStack.getClass())) {
                this.itemStack = itemStack;
                nms = ItemStackNMS.CRAFT_ITEM_STACK_HANDLE_FILED.get(itemStack);
            } else {
                Object nmsStack = ItemStackNMS.asNMSCopy.invoke(null, itemStack);
                this.itemStack = (ItemStack) ItemStackNMS.asCraftMirror.invoke(null, nmsStack);
                nms = nmsStack;
            }

            this.type = ItemUtils.getItemType(itemStack);
            this.hash = type.hashCode();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemKey that = (ItemKey) o;

        try {
            return (boolean) ItemStackNMS.matches.invoke(null, nms, that.nms);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return "ItemKey{" + "itemStack=" + itemStack + '}';
    }
}
