package game;

import game.combat.ArmorItem;
import game.combat.WeaponItem;
import game.inventory.ContainerItem;
import game.inventory.Inventory;
import game.talents.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import misc.Validator;
import misc.XMLTools;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Char {

	@Getter
	@Setter
	private int totalAP, availableAP, maxHealth, currentHealth;
	@Getter
	@Setter
	private Species species;
	@Getter
	private String name, path, advantages, disadvantages, generalSpecialAbilities;
	private Map<String, Integer> attributes, talents;
	@Getter
	private Money money;
	@Getter
	private Inventory inventory;
	@Getter
	private WeaponItem equippedWeapon;
	@Getter
	private ArmorItem equippedArmor;

	// constructors
	public Char(File newFile) throws IOException {
		Validator.verifyNonNull(newFile);
		InputStream inputStream = getClass().getClassLoader().
				getResourceAsStream("resources/Standard.char");
		FileOutputStream outputStream = new FileOutputStream(newFile);
		outputStream.write(inputStream.readAllBytes());
		outputStream.close();
	}

	public Char() {
		species = Species.HUMAN;
		name = "Standard";
		path = "-";
		money = new Money(0);
		attributes = new HashMap<>();
		for (String attribute : Constants.ATTRIBUTES) {
			attributes.put(attribute, 8);
		}
		talents = new HashMap<>();
		for (String talent : Constants.TALENTS_ALL) {
			talents.put(talent, 0);
		}
		inventory = new Inventory();
		maxHealth = species.lifeEnergy + 2 * attributes.get("KO");
	}

	public static Char fromExistingFile(File file) throws IOException {
		Validator.verifyNonNull(file);
		String path = file.getAbsolutePath();

		// parse XML file
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
					parse(new FileInputStream(file));
		} catch (SAXException | ParserConfigurationException e) {
			throw new IOException("Corrupted file: " + path);
		}
		Element root = doc.getDocumentElement();
		root.normalize();

		Node APNode = XMLTools.getChild(root, "AP");
		int totalAP = XMLTools.getIntFromChild(APNode, "gesamt");
		int availableAP = XMLTools.getIntFromChild(APNode, "verfügbar");

		Species species = Species.fromString(XMLTools.getTextFromChild(root,
				"Spezies"));
		String name = XMLTools.getTextFromChild(root, "Name");

		String advantages = XMLTools.getTextFromChild(root, "Vorteile");
		String disadvantages = XMLTools.getTextFromChild(root, "Nachteile");
		String generalSpecialAbilities = XMLTools.getTextFromChild(root,
				"Allgemeine_Sonderfertigkeiten");

		Map<String, Integer> attributes = new HashMap<>();
		Node attributesNode = XMLTools.getChild(root, "Eigenschaften");
		for (String attribute : Constants.ATTRIBUTES) {
			attributes.put(attribute, XMLTools.getIntFromChild(attributesNode,
					attribute));
		}

		Map<String, Integer> talents = new HashMap<>();
		Node talentsNode = XMLTools.getChild(root, "Talente");
		for (String talent : Constants.TALENTS_ALL) {
			talents.put(talent, XMLTools.getIntFromChild(talentsNode, talent));
		}

		int maxHealth = XMLTools.getIntFromChild(root, "Maximale_Lebensenergie");
		int currentHealth = XMLTools.getIntFromChild(root,
				"Aktuelle_Lebensenergie");
		Money money = new Money(XMLTools.getIntFromChild(root, "Geld"));

		Inventory inventory = new Inventory();
		NodeList toplevelContainerNodes = XMLTools.getChild(root, "Inventar").
				getChildNodes();
		for (int i = 0; i < toplevelContainerNodes.getLength(); i++) {
			Node toplevelContainerNode = toplevelContainerNodes.item(i);
			if (toplevelContainerNode.getNodeName().equals("Behälter")) {
				String containerName = XMLTools.getAttributeFromNode(
						toplevelContainerNode, "Name");
				inventory.addToplevelContainerItem(containerName);
				XMLTools.fillContainer(toplevelContainerNode, inventory.
						getToplevelContainerItem(containerName));
			}
		}

		Node currentEquipmentNode = XMLTools.getChild(root,
				"Aktuelle_Ausrüstung");
		WeaponItem equippedWeapon = inventory.getItem(XMLTools.getTextFromChild(
				currentEquipmentNode, "Waffe"), WeaponItem.class);
		ArmorItem equippedArmor = inventory.getItem(XMLTools.getTextFromChild(
				currentEquipmentNode, "Rüstung"), ArmorItem.class);

		return new Char(totalAP, availableAP, maxHealth, currentHealth, species,
				name, path, advantages, disadvantages, generalSpecialAbilities,
				attributes, talents, money, inventory, equippedWeapon,
				equippedArmor);
	}

	// saving to file
	public void save() throws IOException {
		if (path.equals("-")) {
			throw new UnsupportedOperationException(
					"Standard character cannot be saved!");
		}
		saveAs(path);
	}

	public void saveAs(String path) throws IOException {
		// save the character to XML file at path
		Validator.verifyString(path);
		// build XML
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
					newDocument();
		} catch (ParserConfigurationException e) {
			throw new InternalError(
					"A serious XML configuration error occured, this should not happen.");
		}

		Element root = doc.createElement("Charakter");
		doc.appendChild(root);

		Element APElement = doc.createElement("AP");
		XMLTools.addElementToParent(doc, APElement, "gesamt", String.valueOf(
				totalAP));
		XMLTools.addElementToParent(doc, APElement, "verfügbar", String.valueOf(
				availableAP));
		root.appendChild(APElement);

		XMLTools.addElementToParent(doc, root, "Spezies", species.toString());
		XMLTools.addElementToParent(doc, root, "Name", name);

		XMLTools.addElementToParent(doc, root, "Vorteile", advantages);
		XMLTools.addElementToParent(doc, root, "Nachteile", disadvantages);
		XMLTools.addElementToParent(doc, root, "Allgemeine_Sonderfertigkeiten",
				generalSpecialAbilities);

		Element attributesElement = doc.createElement("Eigenschaften");
		for (String attribute : Constants.ATTRIBUTES) {
			XMLTools.addElementToParent(doc, attributesElement, attribute,
					attributes.get(attribute).toString());
		}
		root.appendChild(attributesElement);

		Element talentsElement = doc.createElement("Talente");
		for (String talent : Constants.TALENTS_ALL) {
			XMLTools.addElementToParent(doc, talentsElement, XMLTools.toXML(
					talent), talents.get(talent).toString());
		}
		root.appendChild(talentsElement);

		XMLTools.addElementToParent(doc, root, "Maximale_Lebensenergie", String.
				valueOf(maxHealth));
		XMLTools.addElementToParent(doc, root, "Aktuelle_Lebensenergie", String.
				valueOf(currentHealth));
		XMLTools.addElementToParent(doc, root, "Geld", String.valueOf(money.
				intValue()));

		Element inventoryElement = doc.createElement("Inventar");
		for (ContainerItem container : inventory.getToplevelContainerItems()) {
			XMLTools.buildXMLForContainer(doc, container, inventoryElement);
		}
		root.appendChild(inventoryElement);

		Element currentEquipmentElement = doc.createElement(
				"Aktuelle_Ausrüstung");
		String text = "";
		if (equippedWeapon != null) {
			text = equippedWeapon.getName();
		}
		XMLTools.addElementToParent(doc, currentEquipmentElement, "Waffe", text);
		text = "";
		if (equippedArmor != null) {
			text = equippedArmor.getName();
		}
		XMLTools.addElementToParent(doc, currentEquipmentElement, "Rüstung",
				text);
		root.appendChild(currentEquipmentElement);

		// write XML to file
		try {
			File f = new File(path);
			if (!f.isFile()) {
				f.createNewFile();
			}
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty("indent", "yes");
			t.transform(new DOMSource(doc), new StreamResult(
					new FileOutputStream(path)));
		} catch (TransformerException e) {
			throw new InternalError(
					"A serious XML configuration error occured, this should not happen.");
		}
	}

	// getters
	public int getAttributeValue(String attribute) {
		Validator.verifyAttribute(attribute);
		return attributes.get(attribute);
	}

	public int getTalentValue(String talent) {
		Validator.verifyTalent(talent);
		return talents.get(talent);
	}

	// setters
	public void setEquippedWeapon(WeaponItem weapon) {
		/*if (inventory.contains(weapon)) {
			equippedWeapon = weapon;
		} else {
			throw new IllegalArgumentException(name + " does not have such a weapon!");
		}*/
	}

	public void setEquippedArmor(ArmorItem armor) {
		/*if (inventory.contains(armor)) {
			equippedArmor = armor;
		} else {
			throw new IllegalArgumentException(name + " does not have such an armor!");
		}*/
	}

	public void setName(String name) {
		Validator.verifyString(name);
		this.name = name;
	}

	public void setAttributeValue(String attribute, int value) {
		Validator.verifyAttribute(attribute);
		Validator.verifyValue(value);
		attributes.put(attribute, value);
	}

	public void setTalentValue(String talent, int value) {
		Validator.verifyTalent(talent);
		Validator.verifyValue(value);
		talents.put(talent, value);
	}
}
