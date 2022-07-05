package game.inventory;

import java.util.EmptyStackException;
import lombok.Getter;
import misc.Validator;

public class UsableItem extends Item {

	@Getter
	private int amount;

	public UsableItem(String name, ContainerItem container, int amount) {
		super(name, container);
		Validator.verifyNonNegative(amount);
		this.amount = amount;
	}

	public void use() {
		if (amount > 0) {
			amount--;
		} else {
			throw new EmptyStackException();
		}
	}

	public void restock(int amount) {
		Validator.verifyPositive(amount);
		this.amount += amount;
	}

	public void restock() {
		restock(1);
	}
}
