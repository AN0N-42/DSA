package game.talents;

import game.Char;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import misc.Dice;
import misc.Validator;

public class Trial {

	@Getter
	private final String talent;
	private final Char character;
	@Getter
	private final int ease;
	@Getter
	private final String[] attributes;
	private final Dice w20 = new Dice(20);

	public Trial(String talent, Char character, int ease) {
		Validator.verifyTalent(talent);
		Validator.verifyNonNull(character);
		this.talent = talent;
		this.character = character;
		this.ease = ease;
		attributes = Constants.TALENTS_MAP.get(talent);
	}

	public TrialResult getResult() {
		int puffer = character.getTalentValue(talent) + ease;
		int[] rolledNumbers = new int[3];
		List<String> criticalFailures = new ArrayList<>();
		List<String> criticalSuccesses = new ArrayList<>();
		List<String> failedAttributes = new ArrayList<>();
		boolean passed = true;
		int rolledNumber, attributeValue;
		String attribute;
		for (int i = 0; i < 3; i++) {
			rolledNumber = w20.roll();
			rolledNumbers[i] = rolledNumber;
			attribute = attributes[i];
			attributeValue = character.getAttributeValue(attribute);
			if (rolledNumber <= attributeValue) {
				if (rolledNumber == 1) {
					criticalSuccesses.add(attribute);
				}
			} else {
				if (rolledNumber == 20) {
					passed = false;
					criticalFailures.add(attribute);
				}
				puffer -= (rolledNumber - attributeValue);
				if (puffer < 0) {
					passed = false;
					failedAttributes.add(attribute);
				}
			}
		}
		int qualityLevel;
		if (passed) {
			qualityLevel = (int) ((puffer - 1) / 3 + 1);
		} else {
			qualityLevel = 0;
		}
		return new TrialResult(this, passed, qualityLevel, rolledNumbers,
				criticalSuccesses, criticalFailures, failedAttributes);
	}
}
