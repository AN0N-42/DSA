package game.inventory;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import misc.Validator;

public class ContainerItem extends Item {

	@Getter
	@Setter
	public boolean accessible = true;
	@Getter
	private Inventory inventory;
	@Getter
	private List<Item> items = new ArrayList<>();

	public ContainerItem(String name, ContainerItem container) {
		super(name, container);
		this.inventory = container.getInventory();
	}

	private ContainerItem(String name, Inventory inventory) {
		super(name);
		Validator.verifyNonNull(inventory);
		this.inventory = inventory;
	}

	public static ContainerItem newToplevelContainerItem(String name,
			Inventory inventory) {
		return new ContainerItem(name, inventory);
	}

	public void add(Item item) {
		items.add(item);
	}

	public void remove(Item item) {
		items.remove(item);
	}

	public void removeAll() {
		items = new ArrayList<>();
	}

	public <T extends Item> boolean contains(String name, boolean recursive,
			Class<T> c) {
		boolean contained = false;
		for (Item item : items) {
			if ((item.getClass().equals(c) && item.getName().equals(name))
					|| (recursive && item.getClass().equals(ContainerItem.class)
					&& ((ContainerItem) item).contains(name, true, c))) {
				contained = true;
				break;
			}
		}
		return contained;
	}

	public <T extends Item> boolean contains(Item item, boolean recursive,
			Class<T> c) {
		return contains(item.getName(), recursive, c);
	}

	public <T extends Item> List<T> getItemsByType(Class<T> c, boolean recursive) {
		List<T> list = new ArrayList<>();
		for (Item item : items) {
			if (item.getClass().equals(c)) {
				list.add((T) item);
			}
			if (recursive && item.getClass().equals(ContainerItem.class)) {
				list.addAll(((ContainerItem) item).getItemsByType(c, true));
			}
		}
		return list;
	}

	public <T extends Item> T getItem(String name, boolean recursive, Class<T> c) {
		T returnValue = null;
		for (Item item : items) {
			if (item.getClass().equals(c) && item.getName().equals(name)) {
				returnValue = (T) item;
				break;
			}
			if (recursive && item.getClass().equals(ContainerItem.class)) {
				returnValue = ((ContainerItem) item).getItem(name, true, c);
			}
		}
		return returnValue;
	}
}
