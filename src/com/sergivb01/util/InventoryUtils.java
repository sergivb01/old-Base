package com.sergivb01.util;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;
import java.util.Set;

public final class InventoryUtils{
	public static ItemStack[] deepClone(final ItemStack[] origin){
		Preconditions.checkNotNull((Object) origin, "Origin cannot be null");
		final ItemStack[] cloned = new ItemStack[origin.length];
		for(int i = 0; i < origin.length; ++i){
			final ItemStack next = origin[i];
			cloned[i] = ((next == null) ? null : next.clone());
		}
		return cloned;
	}

	public static int getSafestInventorySize(final int initialSize){
		return (initialSize + 8) / 9 * 9;
	}

	public static void removeItem(final Inventory inventory, final Material type, final short data, final int quantity){
		final ItemStack[] contents = inventory.getContents();
		final boolean compareDamage = type.getMaxDurability() == 0;
		for(int i = quantity; i > 0; --i){
			final ItemStack[] array = contents;
			final int length = array.length;
			int j = 0;
			while(j < length){
				final ItemStack content = array[j];
				if(content != null && content.getType() == type && (!compareDamage || content.getData().getData() == data)){
					if(content.getAmount() <= 1){
						inventory.removeItem(content);
						break;
					}
					content.setAmount(content.getAmount() - 1);
					break;
				}else{
					++j;
				}
			}
		}
	}

	public static int countAmount(final Inventory inventory, final Material type, final short data){
		final ItemStack[] contents = inventory.getContents();
		final boolean compareDamage = type.getMaxDurability() == 0;
		int counter = 0;
		for(final ItemStack item : contents){
			if(item != null && item.getType() == type && (!compareDamage || item.getData().getData() == data)){
				counter += item.getAmount();
			}
		}
		return counter;
	}

	public static boolean isEmpty(final Inventory inventory){
		return isEmpty(inventory, true);
	}

	public static boolean isEmpty(final Inventory inventory, final boolean checkArmour){
		boolean result = true;
		final ItemStack[] array;
		ItemStack[] contents2 = array = (contents2 = inventory.getContents());
		for(final ItemStack content : array){
			if(content != null && content.getType() != Material.AIR){
				result = false;
				break;
			}
		}
		if(!result){
			return false;
		}
		if(checkArmour && inventory instanceof PlayerInventory){
			final ItemStack[] array2;
			final ItemStack[] armorContents = array2 = (contents2 = ((PlayerInventory) inventory).getArmorContents());
			for(final ItemStack content2 : array2){
				if(content2 != null && content2.getType() != Material.AIR){
					result = false;
					break;
				}
			}
		}
		return result;
	}

	public static boolean clickedTopInventory(final InventoryDragEvent event){
		final InventoryView view = event.getView();
		final Inventory topInventory = view.getTopInventory();
		if(topInventory == null){
			return false;
		}
		boolean result = false;
		final Set<Map.Entry<Integer, ItemStack>> entrySet = event.getNewItems().entrySet();
		final int size = topInventory.getSize();
		for(final Map.Entry<Integer, ItemStack> entry : entrySet){
			if(entry.getKey() < size){
				result = true;
				break;
			}
		}
		return result;
	}
}

