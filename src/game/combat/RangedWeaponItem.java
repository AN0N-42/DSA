package game.combat;

import game.inventory.ContainerItem;
import lombok.Getter;
import misc.Validator;

public class RangedWeaponItem extends WeaponItem {

	@Getter
	private final int loadingTime;
	@Getter
	private final String ammunition;
	@Getter
	private int roundsUntilAttack;

	public RangedWeaponItem(String name, WeaponType type, int loadingTime,
			int staticDamage,
			int w6Damage, String range, String ammunition,
			ContainerItem container) {
		super(name, type, staticDamage, w6Damage, range, container);
		Validator.verifyRangedWeaponType(type);
		Validator.verifyNonNegative(loadingTime);
		Validator.verifyString(ammunition);
		this.loadingTime = loadingTime;
		this.ammunition = ammunition;
		roundsUntilAttack = loadingTime;
	}

	/**
	 *
	 * @param combatTechniqueValue
	 * @param ease
	 * @param additionalDamage
	 * @return The damage if successful, or -1 otherwise.
	 */
	@Override
	public int attack(int combatTechniqueValue, int ease, int additionalDamage) {
		Validator.verifyNonNegative(combatTechniqueValue);
		if (w20.roll() <= combatTechniqueValue + ease) {
			int damage = staticDamage + additionalDamage;
			for (int i = 0; i < w6Damage; i++) {
				damage += w6.roll();
			}
			return damage;
		} else {
			return -1;
		}
	}

	@Override
	public boolean parry(int combatTechniqueValue, int ease) {
		throw new UnsupportedOperationException(
				"You cannot parry an attack using a ranged weapon!");
	}
}
