package game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Species {

	// all species
	HUMAN("Mensch", 5, -5, -5, 8),
	ELF("Elf", 2, -4, -6, 8),
	HALFELF("Halbelf", 5, -4, -6, 8),
	DWARF("Zwerg", 8, -4, -4, 6);

	// name and base values of species
	public final String name;
	public final int lifeEnergy;
	public final int soulPower;
	public final int toughness;
	public final int speed;

	@Override
	public String toString() {
		return name;
	}

	public static Species fromString(String name) {
		for (Species species : values()) {
			if (species.toString().equals(name)) {
				return species;
			}
		}
		throw new IllegalArgumentException("There is no species with the name "
				+ name);
	}
}
