package misc;

import game.combat.WeaponType;
import game.talents.Constants;

public final class Validator {

	public static boolean isAttribute(String attribute) {
		for (int i = 0; i < 8; i++) {
			if (Constants.ATTRIBUTES[i].equals(attribute)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTalent(String talent) {
		for (int i = 0; i < 58; i++) {
			if (Constants.TALENTS_ALL[i].equals(talent)) {
				return true;
			}
		}
		return false;
	}

	public static void verifyValue(int value) {
		if (value < 0 || value >= 20) {
			throw new IllegalArgumentException("This value (" + String.valueOf(
					value) + ") should be in the allowed interval [0,20)!");
		}
	}

	public static void verifyAttribute(String attribute) {
		if (!isAttribute(attribute)) {
			throw new IllegalArgumentException("This String (" + attribute
					+ ") should be an attribute!");
		}
	}

	public static void verifyTalent(String talent) {
		if (!isTalent(talent)) {
			throw new IllegalArgumentException("This String (" + talent
					+ ") should be a talent!");
		}
	}

	public static void verifyNonNegative(int number) {
		if (number < 0) {
			throw new IllegalArgumentException("This value (" + String.valueOf(
					number) + ") should not be negative!");
		}
	}

	public static void verifyPositive(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException("This value (" + String.valueOf(
					number) + ") should be positive!");
		}
	}

	public static void verifyNonNull(Object object) {
		if (object == null) {
			throw new NullPointerException("This variable should not be null!");
		}
	}

	public static void verifyString(String string) {
		if (string == null) {
			throw new NullPointerException("This string should not be null!");
		} else if (string.equals("")) {
			throw new IllegalArgumentException(
					"This string should not be empty!");
		}
	}

	public static void verifyMeleeWeaponType(WeaponType type) {
		if (type.isRangedWeapon()) {
			throw new IllegalArgumentException(
					"This weapon should be a melee weapon, but has combat technique "
					+ type.getCombatTechnique());
		}
	}

	public static void verifyRangedWeaponType(WeaponType type) {
		if (type.isMeleeWeapon()) {
			throw new IllegalArgumentException(
					"This weapon should be a ranged weapon, but has combat technique "
					+ type.getCombatTechnique());
		}
	}
}
