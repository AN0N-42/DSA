package game.combat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WeaponType {

	CROSSBOW("Armbrüste", false),
	BOW("Bögen", false),
	DAGGER("Dolche", true),
	FENCING_WEAPON("Fechtwaffen", true),
	SLASHING_WEAPON("Hiebwaffen", true),
	CHAIN_WEAPON("Kettenwaffen", true),
	LANCE("Lanzen", true),
	GRAPPLING("Raufen", true),
	SHIELD("Schilde", true),
	SWORD("Schwerter", true),
	POLE_WEAPON("Stangenwaffen", true),
	MISSILE("Wurfwaffen", false),
	TWO_HANDED_SLASHING_WEAPON("Zweihandhiebwaffen", true),
	ZWEIHÄNDER("Zweihandschwerter", true);

	@Getter
	private final String combatTechnique;
	private final boolean melee;

	public static WeaponType fromString(String combatTechnique) {
		for (WeaponType weaponType : values()) {
			if (weaponType.toString().equals(combatTechnique)) {
				return weaponType;
			}
		}
		throw new IllegalArgumentException(
				"There is no weapon type for the combat technique "
				+ combatTechnique);
	}

	@Override
	public String toString() {
		return combatTechnique;
	}

	public boolean isMeleeWeapon() {
		return melee;
	}

	public boolean isRangedWeapon() {
		return !melee;
	}
}
