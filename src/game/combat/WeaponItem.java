package game.combat;

import game.inventory.ContainerItem;
import game.inventory.Item;
import lombok.Getter;
import misc.Dice;
import misc.Validator;

public abstract class WeaponItem extends Item {

	protected final Dice w6 = new Dice(6);
	protected final Dice w20 = new Dice(20);
	@Getter
	protected final WeaponType type;
	@Getter
	protected final int staticDamage, w6Damage;
	@Getter
	protected final String range;

	public WeaponItem(String name, WeaponType type, int staticDamage,
			int w6Damage, String range, ContainerItem container) {
		super(name, container);
		Validator.verifyNonNegative(staticDamage);
		Validator.verifyNonNegative(w6Damage);
		Validator.verifyString(range);
		this.type = type;
		this.staticDamage = staticDamage;
		this.w6Damage = w6Damage;
		this.range = range;
	}

	public String getCombatTechnique() {
		return type.getCombatTechnique();
	}

	public String getDamage() {
		return String.valueOf(w6Damage) + "W6+" + String.valueOf(staticDamage);
	}

	public abstract int attack(int combatTechniqueValue, int ease,
			int additionalDamage);

	public abstract boolean parry(int combatTechniqueValue, int ease);
}
