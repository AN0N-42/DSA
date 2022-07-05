package misc;

import game.combat.ArmorItem;
import game.combat.MeleeWeaponItem;
import game.combat.RangedWeaponItem;
import game.combat.WeaponItem;
import game.combat.WeaponType;
import game.inventory.ContainerItem;
import game.inventory.MiscItem;
import game.inventory.UsableItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XMLTools {

	public static Node getChild(final Node node, final String name) {
		String text = toXML(name);
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node child = children.item(i);
			if (text.equals(child.getNodeName())) {
				return child;
			}
		}
		return null;
	}

	public static String getTextFromChild(final Node node, String name) {
		Node child = getChild(node, name);
		Validator.verifyNonNull(child);
		return fromXML(child.getTextContent());
	}

	public static int getIntFromChild(final Node node, String name) {
		return Integer.parseInt(getTextFromChild(node, name));
	}

	public static String toXML(String s) {
		return s.replace("&", "_und_").replace(" ", "_");
	}

	public static String fromXML(String s) {
		return s.replace("_und_", "&").replace("_", " ");
	}

	public static void addElementToParent(final Document doc,
			final Element parent, final String name, final String textContent) {
		Element e = doc.createElement(name);
		e.setTextContent(textContent);
		parent.appendChild(e);
	}

	public static String getAttributeFromNode(Node node, String name) {
		return node.getAttributes().getNamedItem(name).getTextContent();
	}

	public static void fillContainer(final Node containerNode,
			ContainerItem container) {
		NodeList childNodes = containerNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			String name;
			switch (childNode.getNodeName()) {
				case "Behälter":
					name = getAttributeFromNode(childNode, "Name");
					ContainerItem subContainer = new ContainerItem(name,
							container);
					fillContainer(childNode, subContainer);
					container.add(subContainer);
					break;
				case "Sonstiges":
					name = getTextFromChild(childNode, "Name");
					container.add(new MiscItem(name, container));
					break;
				case "Verbrauchsgegenstand":
					name = getTextFromChild(childNode, "Name");
					int amount = getIntFromChild(childNode, "Anzahl");
					container.add(new UsableItem(name, container, amount));
					break;
				case "Waffe":
					name = getTextFromChild(childNode, "Name");
					String[] damageStrings = getTextFromChild(childNode,
							"Schaden").split("W6");
					int w6Damage = Integer.parseInt(damageStrings[0]);
					int staticDamage = Integer.parseInt(damageStrings[1]);
					String range = getTextFromChild(childNode, "Reichweite");
					WeaponType type = WeaponType.fromString(getTextFromChild(
							childNode, "Kampftechnik"));
					if (type.isMeleeWeapon()) {
						String[] modStrings
								= getTextFromChild(childNode, "Mod").split("/");
						int attackMod = Integer.parseInt(modStrings[0]);
						int parryMod = Integer.parseInt(modStrings[1]);
						container.add(new MeleeWeaponItem(name, type,
								staticDamage, w6Damage,
								attackMod, parryMod, range, container));
					} else {
						int loadingTime = getIntFromChild(childNode, "Ladezeit");
						String ammunition = getTextFromChild(childNode,
								"Munition");
						container.add(new RangedWeaponItem(name, type,
								loadingTime, staticDamage,
								w6Damage, range, ammunition, container));
					}
					break;
				case "Rüstung":
					name = getTextFromChild(childNode, "Name");
					int armor = getIntFromChild(childNode, "Rüstungswert");
					int strain = getIntFromChild(childNode, "Belastungswert");
					container.add(new ArmorItem(name, armor, strain, container));
					break;
			}
		}
	}

	public static void buildXMLForContainer(final Document doc,
			final ContainerItem container, Element parent) {
		Element containerElement = doc.createElement("Behälter");
		containerElement.setAttribute("Name", container.getName());
		for (ContainerItem subContainer : container.getItemsByType(
				ContainerItem.class, false)) {
			buildXMLForContainer(doc, subContainer, containerElement);
		}
		for (MiscItem miscItem : container.getItemsByType(MiscItem.class, false)) {
			Element miscItemElement = doc.createElement("Sonstiges");
			addElementToParent(doc, miscItemElement, "Name", toXML(miscItem.
					getName()));
			containerElement.appendChild(miscItemElement);
		}
		for (WeaponItem weaponItem : container.getItemsByType(WeaponItem.class,
				false)) {
			Element weaponItemElement = doc.createElement("Waffe");
			addElementToParent(doc, weaponItemElement, "Name", toXML(weaponItem.
					getName()));
			addElementToParent(doc, weaponItemElement, "Kampftechnik", toXML(
					weaponItem.getCombatTechnique()));
			addElementToParent(doc, weaponItemElement, "Schaden", weaponItem.
					getDamage());
			addElementToParent(doc, weaponItemElement, "Reichweite", toXML(
					weaponItem.getRange()));
			if (weaponItem instanceof MeleeWeaponItem) {
				addElementToParent(doc, weaponItemElement, "Mod",
						((MeleeWeaponItem) weaponItem).getMod());
			} else if (weaponItem instanceof RangedWeaponItem) {
				addElementToParent(doc, weaponItemElement, "Ladezeit", String.
						valueOf(((RangedWeaponItem) weaponItem).getLoadingTime()));
				addElementToParent(doc, weaponItemElement, "Munition", toXML(
						((RangedWeaponItem) weaponItem).getAmmunition()));
			}
			containerElement.appendChild(weaponItemElement);
		}
		for (ArmorItem armorItem : container.getItemsByType(ArmorItem.class,
				false)) {
			Element armorItemElement = doc.createElement("Rüstung");
			addElementToParent(doc, armorItemElement, "Name", toXML(armorItem.
					getName()));
			addElementToParent(doc, armorItemElement, "RS", String.valueOf(
					armorItem.getArmor()));
			addElementToParent(doc, armorItemElement, "BE", String.valueOf(
					armorItem.getStrain()));
			containerElement.appendChild(armorItemElement);
		}
		for (UsableItem usableItem : container.getItemsByType(UsableItem.class,
				false)) {
			Element usableItemElement = doc.
					createElement("Verbrauchsgegenstand");
			addElementToParent(doc, usableItemElement, "Name", toXML(usableItem.
					getName()));
			addElementToParent(doc, usableItemElement, "Anzahl", String.valueOf(
					usableItem.getAmount()));
			containerElement.appendChild(usableItemElement);
		}
		parent.appendChild(containerElement);
	}
}
