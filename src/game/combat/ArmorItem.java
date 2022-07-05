package game.combat;

import game.inventory.ContainerItem;
import game.inventory.Item;
import lombok.Getter;
import misc.Validator;

public class ArmorItem extends Item {

	@Getter
	private int armor, strain;

	public ArmorItem(String name, int armor, int strain, ContainerItem container) {
		super(name, container);
		setArmor(armor);
		setStrain(strain);
	}

	public void setArmor(int armor) {
		Validator.verifyNonNegative(armor);
		this.armor = armor;
	}

	public void setStrain(int strain) {
		Validator.verifyNonNegative(strain);
		this.strain = strain;
	}
}
