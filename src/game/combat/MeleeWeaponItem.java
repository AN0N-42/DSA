package game.combat;

import game.inventory.ContainerItem;
import lombok.Getter;
import misc.Validator;

public class MeleeWeaponItem extends WeaponItem {

	@Getter
	private final int attackMod, parryMod;

	public MeleeWeaponItem(String name, WeaponType type, int staticDamage,
			int w6Damage,
			int attackMod, int parryMod, String range, ContainerItem container) {
		super(name, type, staticDamage, w6Damage, range, container);
		Validator.verifyMeleeWeaponType(type);
		this.attackMod = attackMod;
		this.parryMod = parryMod;
	}

	public String getMod() {
		return String.valueOf(attackMod) + "/" + String.valueOf(parryMod);
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
		if (w20.roll() <= combatTechniqueValue + attackMod + ease) {
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
		Validator.verifyNonNegative(combatTechniqueValue);
		return w20.roll() <= combatTechniqueValue / 2 + parryMod + ease;
	}
}
