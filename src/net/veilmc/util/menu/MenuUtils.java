package net.veilmc.util.menu;

import net.veilmc.util.menu.mask.Mask;
import net.veilmc.util.menu.mask.Mask2D;
import net.veilmc.util.menu.slot.Slot;
import net.veilmc.util.menu.type.ChestMenu;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class MenuUtils {

    public static Menu createMenu(String title, int rows) {
        return ChestMenu.builder(rows)
                .title(title)
                .build();
    }

    public static void addClickHandler(Slot slot) {
        slot.setClickHandler((player, info) -> {
            player.sendMessage("You clicked the slot at index " + info.getClickedSlot().getIndex());
            // Additional functionality goes here
        });
    }

    public static void addMaterialBorder(Inventory inventory, Menu menu, Material material) {
        ItemStack glass = new ItemStack(material);
        Mask mask = Mask2D.builder(menu).apply("111111111") // First row
                .nextRow().apply("100000001") // Second row
                .nextRow().apply("100000001") // Third row
                .nextRow().apply("111111111").build(); // Fourth row
        menu.forEach(slot -> slot.setItem(glass));
    }


}