/*
 * Copyright (C) Matthew Steglinski (SainttX) <matt@ipvp.org>
 * Copyright (C) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sergivb01.util.menu.type;

import com.sergivb01.util.menu.ArrayIterator;
import com.sergivb01.util.menu.Menu;
import com.sergivb01.util.menu.slot.DefaultSlot;
import com.sergivb01.util.menu.slot.Slot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * An abstract class that provides a skeletal implementation of the Menu
 * interface.
 */
public abstract class AbstractMenu implements Menu{

	private final Inventory inventory;
	private Menu parent;
	private Slot[] slots;
	private CloseHandler handler;

	protected AbstractMenu(String title, int slots, Menu parent){
		if(title == null){
			title = InventoryType.CHEST.getDefaultTitle();
		}
		this.inventory = Bukkit.createInventory(this, slots, title);
		this.parent = parent;
		generateSlots();
	}

	protected AbstractMenu(String title, InventoryType type, Menu parent){
		Objects.requireNonNull(type, "type cannot be null");
		if(title == null){
			title = type.getDefaultTitle();
		}
		this.inventory = Bukkit.createInventory(this, type, title);
		this.parent = parent;
		generateSlots();
	}

	/**
	 * Initial method called to fill the Slots of the menu
	 */
	protected void generateSlots(){
		this.slots = new Slot[inventory.getSize()];
		for(int i = 0; i < slots.length; i++){
			slots[i] = new DefaultSlot(inventory, i);
		}
	}

	@Override
	public Optional<Menu> getParent(){
		return Optional.ofNullable(parent);
	}

	@Override
	public void open(Player viewer){
		viewer.openInventory(getInventory());
	}

	@Override
	public void close(Player viewer){
		Inventory inv = getInventory();
		if(!inv.getViewers().contains(viewer)){
			throw new IllegalStateException("menu not open for player");
		}
		viewer.closeInventory();
	}

	@Override
	public Slot getSlot(int index){
		return slots[index];
	}

	@Override
	public Iterator<Slot> iterator(){
		return new ArrayIterator<>(slots);
	}

	@Override
	public void clear(){
		for(Slot slot : slots){
			slot.setItem(null);
		}
	}

	@Override
	public void clear(int index){
		Slot slot = getSlot(index);
		slot.setItem(null);
	}

	@Override
	public Inventory getInventory(){
		return inventory;
	}

	@Override
	public Optional<CloseHandler> getCloseHandler(){
		return Optional.ofNullable(handler);
	}

	@Override
	public void setCloseHandler(CloseHandler handler){
		this.handler = handler;
	}

	/**
	 * Abstract base class for builders of {@link Menu} types.
	 * <p>
	 * Builder instances are reusable; calling {@link #build()} will
	 * generate a new Menu with identical features to the ones created before it.
	 */
	public static abstract class Builder implements Menu.Builder{

		private String title;
		private Menu parent;

		@Override
		public Menu.Builder title(String title){
			this.title = title;
			return this;
		}

		@Override
		public Menu.Builder parent(Menu parent){
			this.parent = parent;
			return this;
		}

		public String getTitle(){
			return title;
		}

		public Menu getParent(){
			return parent;
		}
	}
}
