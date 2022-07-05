package game.talents;

import lombok.Data;

@Data
public final class CollectiveTrialResult {

	private final CollectiveTrial collectiveTrial;
	private final int qualityLevelSum;
	private final TrialResult[] trialResults;
}
