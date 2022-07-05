package game.talents;

import java.util.List;
import lombok.Data;

@Data
public final class TrialResult {

	private final Trial trial;
	private final boolean passed;
	private final int qualityLevel;
	private final int[] rolledNumbers;
	private final List<String> criticalSuccesses, criticalFailures, failedAttributes;
}
