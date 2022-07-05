package game.talents;

import game.Char;
import lombok.Getter;

public class CollectiveTrial {

	@Getter
	private int count;
	@Getter
	private Trial trial;

	public CollectiveTrial(int count, String talent, Char character, int ease) {
		if (count < 2) {
			throw new IllegalArgumentException(
					"Collective trial must have at least two trials, but count parameter was only "
					+ String.valueOf(count));
		}
		this.count = count;
		trial = new Trial(talent, character, ease);
	}

	public CollectiveTrialResult getResult() {
		int qualityLevelSum = 0;
		TrialResult[] trialResults = new TrialResult[count];
		TrialResult result;
		for (int i = 0; i < count; i++) {
			result = trial.getResult();
			trialResults[i] = result;
			qualityLevelSum += result.getQualityLevel();
		}
		return new CollectiveTrialResult(this, qualityLevelSum, trialResults);
	}
}
