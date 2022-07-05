package game.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class Inventory {

	public static enum MODE {
		HIERARCHICAL, LINEAR
	};

	private Map<String, ContainerItem> toplevelContainerItems = new HashMap<>();
	@Getter
	private MODE mode;

	public Inventory() {
		this(MODE.HIERARCHICAL);
	}

	public Inventory(MODE mode) {
		this.mode = mode;
		switch (mode) {
			case HIERARCHICAL:
				//toplevelContainerItems.addAll(Arrays.asList(ContainerItem.TOPLEVEL));
				break;
		}
	}

	public void addToplevelContainerItem(String name) {
		if (toplevelContainerItems.containsKey(name)) {
			throw new IllegalArgumentException(
					"There is already a toplevel container item named " + name
					+ "!");
		}
		toplevelContainerItems.put(name, ContainerItem.newToplevelContainerItem(
				name, this));
	}

	public void removeToplevelContainerItem(ContainerItem containerItem) {
		toplevelContainerItems.remove(containerItem.getName());
	}

	public List<ContainerItem> getToplevelContainerItems() {
		return new ArrayList<>(toplevelContainerItems.values());
	}

	public ContainerItem getToplevelContainerItem(String name) {
		return toplevelContainerItems.get(name);
	}

	public <T extends Item> boolean contains(String name, Class<T> c) {
		for (ContainerItem containerItem : toplevelContainerItems.values()) {
			if (containerItem.contains(name, true, c)) {
				return true;
			}
		}
		return false;
	}

	public <T extends Item> T getItem(String name, Class<T> c) {
		T item = null;
		for (ContainerItem containerItem : toplevelContainerItems.values()) {
			item = containerItem.getItem(name, true, c);
			if (item != null) {
				break;
			}
		}
		return item;
	}
}
