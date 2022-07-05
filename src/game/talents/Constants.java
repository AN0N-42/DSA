package game.talents;

import java.util.Map;
import static java.util.Map.entry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

	public static final String[] ATTRIBUTES = {"MU", "KL", "IN", "CH", "FF",
		"GE", "KO", "KK"};

	public static final String[] TALENTS_PHYSICAL = new String[]{"Fliegen",
		"Gaukeleien", "Klettern", "Körperbeherrschung", "Kraftakt", "Reiten",
		"Schwimmen", "Selbstbeherrschung", "Singen", "Sinnesschärfe", "Tanzen",
		"Taschendiebstahl", "Verbergen", "Zechen"};

	public static final String[] TALENTS_SOCIAL = new String[]{
		"Bekehren & Überzeugen", "Betören", "Einschüchtern", "Etikette",
		"Gassenwissen", "Menschenkenntnis", "Überreden", "Verkleiden",
		"Willenskraft"};

	public static final String[] TALENTS_NATURE = new String[]{"Fährtensuchen",
		"Fesseln", "Fischen & Angeln", "Orientierung", "Pflanzenkunde",
		"Tierkunde", "Wildnisleben"};

	public static final String[] TALENTS_KNOWLEDGE = new String[]{
		"Brett- & Glücksspiel", "Geographie", "Geschichtswissen",
		"Götter & Kulte", "Kriegskunst", "Magiekunde", "Mechanik", "Rechnen",
		"Rechtskunde", "Sagen & Legenden", "Sphärenkunde", "Sternkunde"};

	public static final String[] TALENTS_CRAFTING = new String[]{"Alchimie",
		"Boote & Schiffe", "Fahrzeuge", "Handel", "Heilkunde Gift",
		"Heilkunde Krankheiten", "Heilkunde Seele", "Heilkunde Wunden",
		"Holzbearbeitung", "Lebensmittelbearbeitung", "Lederbearbeitung",
		"Malen & Zeichnen", "Metallbearbeitung", "Musizieren",
		"Schlösserknacken", "Steinbearbeitung", "Stoffbearbeitung"};

	public static final String[][] TALENTS_ALL_NESTED = new String[][]{
		TALENTS_PHYSICAL, TALENTS_SOCIAL, TALENTS_NATURE, TALENTS_KNOWLEDGE,
		TALENTS_CRAFTING};

	public static final String[] TALENTS_ALL = new String[]{"Fliegen",
		"Gaukeleien", "Klettern", "Kraftakt", "Körperbeherrschung", "Reiten",
		"Schwimmen", "Selbstbeherrschung", "Singen", "Sinnesschärfe", "Tanzen",
		"Taschendiebstahl", "Verbergen", "Zechen", "Bekehren & Überzeugen",
		"Betören", "Einschüchtern", "Etikette", "Gassenwissen",
		"Menschenkenntnis", "Verkleiden", "Willenskraft", "Überreden", "Fesseln",
		"Fischen & Angeln", "Fährtensuchen", "Orientierung", "Pflanzenkunde",
		"Tierkunde", "Wildnisleben", "Brett- & Glücksspiel", "Geographie",
		"Geschichtswissen", "Götter & Kulte", "Kriegskunst", "Magiekunde",
		"Mechanik", "Rechnen", "Rechtskunde", "Sagen & Legenden", "Sphärenkunde",
		"Sternkunde", "Alchimie", "Boote & Schiffe", "Fahrzeuge", "Handel",
		"Heilkunde Gift", "Heilkunde Krankheiten", "Heilkunde Seele",
		"Heilkunde Wunden", "Holzbearbeitung", "Lebensmittelbearbeitung",
		"Lederbearbeitung", "Malen & Zeichnen", "Metallbearbeitung",
		"Musizieren", "Schlösserknacken", "Steinbearbeitung", "Stoffbearbeitung"};

	public static final Map<String, String[]> TALENTS_MAP = Map.ofEntries(
			entry("Fliegen", new String[]{"MU", "IN", "GE"}),
			entry("Gaukeleien", new String[]{"MU", "CH", "FF"}),
			entry("Klettern", new String[]{"MU", "GE", "KO"}),
			entry("Körperbeherrschung", new String[]{"GE", "GE", "KO"}),
			entry("Kraftakt", new String[]{"KO", "KK", "KK"}),
			entry("Reiten", new String[]{"CH", "GE", "KO"}),
			entry("Schwimmen", new String[]{"GE", "KO", "KK"}),
			entry("Selbstbeherrschung", new String[]{"MU", "MU", "KO"}),
			entry("Singen", new String[]{"KL", "CH", "KO"}),
			entry("Sinnesschärfe", new String[]{"KL", "IN", "IN"}),
			entry("Tanzen", new String[]{"KL", "CH", "GE"}),
			entry("Taschendiebstahl", new String[]{"MU", "FF", "GE"}),
			entry("Verbergen", new String[]{"MU", "IN", "GE"}),
			entry("Zechen", new String[]{"KL", "KO", "KK"}),
			entry("Bekehren & Überzeugen", new String[]{"MU", "KL", "CH"}),
			entry("Betören", new String[]{"MU", "CH", "CH"}),
			entry("Einschüchtern", new String[]{"MU", "IN", "CH"}),
			entry("Etikette", new String[]{"KL", "IN", "CH"}),
			entry("Gassenwissen", new String[]{"KL", "IN", "CH"}),
			entry("Menschenkenntnis", new String[]{"KL", "IN", "CH"}),
			entry("Überreden", new String[]{"MU", "IN", "CH"}),
			entry("Verkleiden", new String[]{"IN", "CH", "GE"}),
			entry("Willenskraft", new String[]{"MU", "IN", "CH"}),
			entry("Fährtensuchen", new String[]{"MU", "IN", "GE"}),
			entry("Fesseln", new String[]{"KL", "FF", "KK"}),
			entry("Fischen & Angeln", new String[]{"FF", "GE", "KO"}),
			entry("Orientierung", new String[]{"KL", "IN", "IN"}),
			entry("Pflanzenkunde", new String[]{"KL", "FF", "KO"}),
			entry("Tierkunde", new String[]{"MU", "MU", "CH"}),
			entry("Wildnisleben", new String[]{"MU", "GE", "KO"}),
			entry("Brett- & Glücksspiel", new String[]{"KL", "KL", "IN"}),
			entry("Geographie", new String[]{"KL", "KL", "IN"}),
			entry("Geschichtswissen", new String[]{"KL", "KL", "IN"}),
			entry("Götter & Kulte", new String[]{"KL", "KL", "IN"}),
			entry("Kriegskunst", new String[]{"MU", "KL", "IN"}),
			entry("Magiekunde", new String[]{"KL", "KL", "IN"}),
			entry("Mechanik", new String[]{"KL", "KL", "FF"}),
			entry("Rechnen", new String[]{"KL", "KL", "IN"}),
			entry("Rechtskunde", new String[]{"KL", "KL", "IN"}),
			entry("Sagen & Legenden", new String[]{"KL", "KL", "IN"}),
			entry("Sphärenkunde", new String[]{"KL", "KL", "IN"}),
			entry("Sternkunde", new String[]{"KL", "KL", "IN"}),
			entry("Alchimie", new String[]{"MU", "KL", "FF"}),
			entry("Boote & Schiffe", new String[]{"FF", "GE", "KK"}),
			entry("Fahrzeuge", new String[]{"CH", "FF", "KO"}),
			entry("Handel", new String[]{"KL", "IN", "CH"}),
			entry("Heilkunde Gift", new String[]{"MU", "KL", "IN"}),
			entry("Heilkunde Krankheiten", new String[]{"MU", "IN", "KO"}),
			entry("Heilkunde Seele", new String[]{"IN", "CH", "KO"}),
			entry("Heilkunde Wunden", new String[]{"KL", "FF", "FF"}),
			entry("Holzbearbeitung", new String[]{"FF", "GE", "KK"}),
			entry("Lebensmittelbearbeitung", new String[]{"IN", "FF", "FF"}),
			entry("Lederbearbeitung", new String[]{"FF", "GE", "KO"}),
			entry("Malen & Zeichnen", new String[]{"IN", "FF", "FF"}),
			entry("Metallbearbeitung", new String[]{"FF", "KO", "KK"}),
			entry("Musizieren", new String[]{"CH", "FF", "KO"}),
			entry("Schlösserknacken", new String[]{"IN", "FF", "FF"}),
			entry("Steinbearbeitung", new String[]{"FF", "FF", "KK"}),
			entry("Stoffbearbeitung", new String[]{"KL", "FF", "FF"})
	);
}
