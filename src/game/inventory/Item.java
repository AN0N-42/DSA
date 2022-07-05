package game.inventory;

import lombok.Getter;
import misc.Validator;

public abstract class Item {

	@Getter
	protected String name;
	@Getter
	protected ContainerItem container;

	public Item(String name, ContainerItem container) {
		setName(name);
		setContainer(container);
	}

	protected Item(String name) {
		if (this instanceof ContainerItem) {
			setName(name);
			container = null;
		} else {
			throw new IllegalCallerException(
					"This constructor can only be called from ContainerItem class, not from "
					+ getClass().getSimpleName());
		}
	}

	public final void setName(String name) {
		Validator.verifyString(name);
		this.name = name;
	}

	public final void setContainer(ContainerItem container) {
		Validator.verifyNonNull(container);
		this.container = container;
	}

	public boolean equals(Item other) {
		return getClass().equals(other.getClass()) && name.equals(other.
				getName());
	}

	@Override
	public String toString() {
		return name + " (" + getClass().getSimpleName() + ")";
	}
}
