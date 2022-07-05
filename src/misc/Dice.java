package misc;

import java.util.Random;
import lombok.Getter;

/**
 * A simple dice with at least two sides.
 */
public class Dice {

	private @Getter
	int numberOfSides;
	private final Random random = new Random();

	/**
	 * @param numberOfSides
	 * @throws IllegalArgumentException If sides is less than two.
	 */
	public Dice(int numberOfSides) {
		if (numberOfSides < 2) {
			throw new IllegalArgumentException(
					"A dice must have at least two sides!");
		}
		this.numberOfSides = numberOfSides;
	}

	@Override
	public String toString() {
		return "W" + String.valueOf(numberOfSides);
	}

	/**
	 * Returns an int from the interval [1, numberOfSides].
	 *
	 * @return the rolled integer.
	 */
	public int roll() {
		return random.nextInt(numberOfSides) + 1;
	}
}
