package game;

public class Money extends Number implements Comparable<Number> {

	private int rawAmount;

	public Money(int rawAmount) {
		if (rawAmount < 0) {
			throw new IllegalArgumentException(
					"rawAmount parameter must be non-negative, but was: "
					+ String.valueOf(rawAmount));
		}
		this.rawAmount = rawAmount;
	}

	public Money(int d, int s, int h, int k) {
		if (d < 0) {
			throw new IllegalArgumentException(
					"d parameter must be non-negative, but was: " + String.
							valueOf(d));
		}
		if (s < 0) {
			throw new IllegalArgumentException(
					"s parameter must be non-negative, but was: " + String.
							valueOf(s));
		}
		if (h < 0) {
			throw new IllegalArgumentException(
					"h parameter must be non-negative, but was: " + String.
							valueOf(h));
		}
		if (k < 0) {
			throw new IllegalArgumentException(
					"k parameter must be non-negative, but was: " + String.
							valueOf(k));
		}
		this.rawAmount = k + 10 * h + 100 * s + 1000 * d;
	}

	public int getD() {
		return rawAmount / 1000;
	}

	public int getS() {
		return (rawAmount % 1000) / 100;
	}

	public int getH() {
		return (rawAmount % 100) / 10;
	}

	public int getK() {
		return rawAmount % 10;
	}

	@Override
	public String toString() {
		return String.format(
				"%d Dukaten, %d Silbertaler, %d Heller und %d Kreuzer", getD(),
				getS(), getH(), getK());
	}

	public Money add(Money other) {
		return new Money(rawAmount + other.intValue());
	}

	public Money subtract(Money other) {
		if (this.compareTo(other) < 0) {
			throw new IllegalArgumentException("Result would be negative!");
		}
		return new Money(rawAmount - other.intValue());
	}

	@Override
	public int compareTo(Number other) {
		return Integer.compare(rawAmount, other.intValue());
	}

	@Override
	public int intValue() {
		return rawAmount;
	}

	@Override
	public long longValue() {
		return rawAmount;
	}

	@Override
	public float floatValue() {
		return rawAmount;
	}

	@Override
	public double doubleValue() {
		return rawAmount;
	}
}
