package graphics;

import game.Char;
import game.combat.ArmorItem;
import game.combat.MeleeWeaponItem;
import game.combat.RangedWeaponItem;
import game.combat.WeaponItem;
import game.inventory.ContainerItem;
import game.inventory.MiscItem;
import game.inventory.UsableItem;
import game.talents.CollectiveTrial;
import game.talents.CollectiveTrialResult;
import game.talents.Trial;
import game.talents.TrialResult;
import graphics.actions.SetLabelTextAction;
import graphics.actions.SwitchTabAction;
import graphics.inventory.ArmorItemPanel;
import graphics.inventory.ContainerItemPanel;
import graphics.inventory.MeleeWeaponItemPanel;
import graphics.inventory.MiscItemPanel;
import graphics.inventory.RangedWeaponItemPanel;
import graphics.inventory.UsableItemPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import misc.Dice;
import misc.Tools;

public class MainWindow extends javax.swing.JFrame {

	private final ImageIcon loadingIcon = new ImageIcon(getClass().
			getClassLoader().getResource("resources/loading.gif"));
	private final Dice w3 = new Dice(3);
	private final Dice w6 = new Dice(6);
	private final Dice w20 = new Dice(20);
	private final JPanel[] panels;
	private final Dimension tabHeaderSize;
	private Char character;
	private boolean secondaryOutputActive = false;
	private CollectiveTrialResult collectiveTrialResult = null;
	private boolean doneLoading = false;

	public MainWindow() {
		initComponents();
		doneLoading = true;

		String s = System.getProperty("line.separator");
		// calculate tab header size
		panels = new JPanel[]{tab1, tab2, tab3, tab4, tab5};
		Rectangle tabRectangle = tabbedPane.getBoundsAt(panels.length - 1);
		tabHeaderSize = new Dimension(tabRectangle.x + tabRectangle.width + 10,
				tab1.getBounds().y);

		// load standard character
		Char standard = new Char();
		Char gimlor;
		try {
			gimlor = Char.fromExistingFile(new File(getClass().getClassLoader().
					getResource("resources/Gimlor.char").toURI()));
		} catch (IOException | URISyntaxException ex) {
			throw new InternalError(
					"File resources/Gimlor.char damaged or missing");
		}
		setCharacter(gimlor);

		// create hotkeys for switching tabs (F1 - F5)
		for (int i = 0; i < panels.length; i++) {
			tabbedPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F1
					+ i, 0), "Tab" + String.valueOf(i));
			tabbedPane.getActionMap().put("Tab" + String.valueOf(i),
					new SwitchTabAction(this, tabbedPane, i));
		}

		// 1. make radiobuttons accessible from talentButtonGroup by setting action commands
		// 2. always display talent value of currently selected talent
		Enumeration<AbstractButton> e = talentButtonGroup.getElements();
		while (e.hasMoreElements()) {
			AbstractButton b = e.nextElement();
			String text = b.getText();
			b.setActionCommand(text);
			b.addItemListener((ItemEvent ie) -> {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					talentValueLabel.setText(String.valueOf(character.
							getTalentValue(text)));
				}
			});
		}
		talentValueLabel.setText(String.valueOf(character.getTalentValue(
				"Fliegen")));

		// build inventory tree
		ContainerItemPanel containerItemPanel;
		for (ContainerItem toplevelContainerItem : character.getInventory().
				getToplevelContainerItems()) {
			containerItemPanel = new ContainerItemPanel(toplevelContainerItem,
					null);
			buildInventoryTree(toplevelContainerItem, containerItemPanel);
			inventoryPanel.addToplevelPanel(containerItemPanel);
		}
		inventoryPanel.
				setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));

		// fit window size to content
		tabbedPaneStateChanged(null);
	}

	public void setCharacter(Char newCharacter) {
		character = newCharacter;
		characterNameLabel.setText(character.getName());
		characterPathLabel.setText(character.getPath());
		tabbedPaneStateChanged(null);
	}

	private void buildInventoryTree(ContainerItem containerItem,
			ContainerItemPanel panel) {
		for (ContainerItem childContainerItem : containerItem.getItemsByType(
				ContainerItem.class,
				false)) {
			ContainerItemPanel childPanel = new ContainerItemPanel(
					childContainerItem, panel);
			buildInventoryTree(childContainerItem, childPanel);
			panel.addSubPanel(childPanel);
		}
		for (UsableItem usableItem : containerItem.
				getItemsByType(UsableItem.class, false)) {
			panel.addSubPanel(new UsableItemPanel(usableItem, panel));
		}
		for (ArmorItem armorItem : containerItem.getItemsByType(ArmorItem.class,
				false)) {
			panel.addSubPanel(new ArmorItemPanel(armorItem, panel));
		}
		for (MeleeWeaponItem meleeWeaponItem : containerItem.getItemsByType(
				MeleeWeaponItem.class, false)) {
			panel.addSubPanel(new MeleeWeaponItemPanel(meleeWeaponItem, panel));
		}
		for (RangedWeaponItem rangedWeaponItem : containerItem.getItemsByType(
				RangedWeaponItem.class, false)) {
			panel.addSubPanel(new RangedWeaponItemPanel(rangedWeaponItem, panel));
		}
		for (MiscItem miscItem : containerItem.getItemsByType(MiscItem.class,
				false)) {
			panel.addSubPanel(new MiscItemPanel(miscItem, panel));
		}
		//adjustInventoryPanelSize();
	}

	private void adjustInventoryPanelSize() {
		inventoryPanel.setPreferredSize(inventoryPanel.getPreferredSize());
		inventoryPanel.revalidate();
		inventoryPanel.repaint();
		validate();
		pack();
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info
					: javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			new MainWindow().setVisible(true);
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        talentButtonGroup = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        tab1 = new javax.swing.JPanel();
        w3Button = new javax.swing.JButton();
        w6Button = new javax.swing.JButton();
        w20Button = new javax.swing.JButton();
        diceMainOutput = new javax.swing.JLabel();
        diceSecondaryOutput = new javax.swing.JLabel();
        tab2 = new javax.swing.JPanel();
        staticLabelCurrentlyLoadedCharacter = new javax.swing.JLabel();
        characterNameLabel = new javax.swing.JLabel();
        staticLabelPath = new javax.swing.JLabel();
        characterPathLabel = new javax.swing.JLabel();
        editCharacterButton = new javax.swing.JButton();
        newCharacterButton = new javax.swing.JButton();
        loadCharacterButton = new javax.swing.JButton();
        tab3 = new javax.swing.JPanel();
        staticLabelPhysicalTalents = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jRadioButton13 = new javax.swing.JRadioButton();
        jRadioButton14 = new javax.swing.JRadioButton();
        staticLabelSocialTalents = new javax.swing.JLabel();
        jRadioButton15 = new javax.swing.JRadioButton();
        jRadioButton16 = new javax.swing.JRadioButton();
        jRadioButton17 = new javax.swing.JRadioButton();
        jRadioButton18 = new javax.swing.JRadioButton();
        jRadioButton19 = new javax.swing.JRadioButton();
        jRadioButton20 = new javax.swing.JRadioButton();
        jRadioButton21 = new javax.swing.JRadioButton();
        jRadioButton22 = new javax.swing.JRadioButton();
        jRadioButton23 = new javax.swing.JRadioButton();
        staticLabelNatureTalents = new javax.swing.JLabel();
        jRadioButton24 = new javax.swing.JRadioButton();
        jRadioButton25 = new javax.swing.JRadioButton();
        jRadioButton26 = new javax.swing.JRadioButton();
        jRadioButton27 = new javax.swing.JRadioButton();
        jRadioButton28 = new javax.swing.JRadioButton();
        jRadioButton29 = new javax.swing.JRadioButton();
        jRadioButton30 = new javax.swing.JRadioButton();
        staticLabelKnowledgeTalents = new javax.swing.JLabel();
        jRadioButton31 = new javax.swing.JRadioButton();
        jRadioButton32 = new javax.swing.JRadioButton();
        jRadioButton33 = new javax.swing.JRadioButton();
        jRadioButton34 = new javax.swing.JRadioButton();
        jRadioButton35 = new javax.swing.JRadioButton();
        jRadioButton36 = new javax.swing.JRadioButton();
        jRadioButton37 = new javax.swing.JRadioButton();
        jRadioButton38 = new javax.swing.JRadioButton();
        jRadioButton39 = new javax.swing.JRadioButton();
        jRadioButton40 = new javax.swing.JRadioButton();
        jRadioButton41 = new javax.swing.JRadioButton();
        jRadioButton42 = new javax.swing.JRadioButton();
        staticLabelCraftingTalents = new javax.swing.JLabel();
        jRadioButton43 = new javax.swing.JRadioButton();
        jRadioButton44 = new javax.swing.JRadioButton();
        jRadioButton45 = new javax.swing.JRadioButton();
        jRadioButton46 = new javax.swing.JRadioButton();
        jRadioButton47 = new javax.swing.JRadioButton();
        jRadioButton48 = new javax.swing.JRadioButton();
        jRadioButton49 = new javax.swing.JRadioButton();
        jRadioButton50 = new javax.swing.JRadioButton();
        jRadioButton51 = new javax.swing.JRadioButton();
        jRadioButton52 = new javax.swing.JRadioButton();
        jRadioButton53 = new javax.swing.JRadioButton();
        jRadioButton54 = new javax.swing.JRadioButton();
        jRadioButton55 = new javax.swing.JRadioButton();
        jRadioButton56 = new javax.swing.JRadioButton();
        jRadioButton57 = new javax.swing.JRadioButton();
        jRadioButton58 = new javax.swing.JRadioButton();
        jRadioButton59 = new javax.swing.JRadioButton();
        trialButton = new javax.swing.JButton();
        staticLabelTrial = new javax.swing.JLabel();
        trialEaseTextField = new javax.swing.JTextField();
        collectiveTrialButton = new javax.swing.JButton();
        staticLabelCollectiveTrial1 = new javax.swing.JLabel();
        collectiveTrialCountTextField = new javax.swing.JTextField();
        staticLabelCollectiveTrial2 = new javax.swing.JLabel();
        collectiveTrialEaseTextField = new javax.swing.JTextField();
        talentsMainOutput = new javax.swing.JLabel();
        talentsSecondaryOutput = new javax.swing.JLabel();
        talentValueLabel = new javax.swing.JLabel();
        staticLabelTalentValue = new javax.swing.JLabel();
        tab4 = new javax.swing.JPanel();
        staticLabelEquippedWeapon = new javax.swing.JLabel();
        equippedWeaponComboBox = new javax.swing.JComboBox<>();
        staticLabelEquippedArmor = new javax.swing.JLabel();
        equippedArmorComboBox = new javax.swing.JComboBox<>();
        staticLabelArmorValue = new javax.swing.JLabel();
        armorValueLabel = new javax.swing.JLabel();
        staticLabelStrain = new javax.swing.JLabel();
        strainValueLabel = new javax.swing.JLabel();
        parryButton = new javax.swing.JButton();
        staticLabelParry = new javax.swing.JLabel();
        parryEaseTextField = new javax.swing.JTextField();
        attackButton = new javax.swing.JButton();
        staticLabelRange = new javax.swing.JLabel();
        rangeLabel = new javax.swing.JLabel();
        staticLabelAttack1 = new javax.swing.JLabel();
        attackEaseTextField = new javax.swing.JTextField();
        staticLabelAttack2 = new javax.swing.JLabel();
        additionalDamageTextField = new javax.swing.JTextField();
        staticLabelAttack3 = new javax.swing.JLabel();
        combatOutput = new javax.swing.JLabel();
        tab5 = new javax.swing.JPanel();
        toggleViewButton = new javax.swing.JToggleButton();
        saveButton = new javax.swing.JButton();
        inventoryPanel = new graphics.inventory.InventoryPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DSA");

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        w3Button.setText("W3");
        w3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w3ButtonActionPerformed(evt);
            }
        });

        w6Button.setText("W6");
        w6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w6ButtonActionPerformed(evt);
            }
        });

        w20Button.setText("W20");
        w20Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w20ButtonActionPerformed(evt);
            }
        });

        diceMainOutput.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        diceMainOutput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        diceMainOutput.setText("Wert");

        diceSecondaryOutput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        diceSecondaryOutput.setText("(Würfel)");

        javax.swing.GroupLayout tab1Layout = new javax.swing.GroupLayout(tab1);
        tab1.setLayout(tab1Layout);
        tab1Layout.setHorizontalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tab1Layout.createSequentialGroup()
                        .addComponent(w3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(w6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(w20Button, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(diceSecondaryOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(diceMainOutput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab1Layout.setVerticalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(w3Button)
                    .addComponent(w6Button)
                    .addComponent(w20Button))
                .addGap(18, 18, 18)
                .addComponent(diceMainOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diceSecondaryOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Würfel", tab1);

        staticLabelCurrentlyLoadedCharacter.setText("Aktuell geladener Charakter:");

        characterNameLabel.setText("Name");

        staticLabelPath.setText("Dateipfad:");

        characterPathLabel.setText("Pfad");

        editCharacterButton.setText("Charakter bearbeiten");
        editCharacterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCharacterButtonActionPerformed(evt);
            }
        });

        newCharacterButton.setText("Neuer Charakter");
        newCharacterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCharacterButtonActionPerformed(evt);
            }
        });

        loadCharacterButton.setText("Charakter laden");
        loadCharacterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadCharacterButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab2Layout.createSequentialGroup()
                        .addComponent(staticLabelCurrentlyLoadedCharacter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(characterNameLabel))
                    .addGroup(tab2Layout.createSequentialGroup()
                        .addComponent(staticLabelPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(characterPathLabel))
                    .addGroup(tab2Layout.createSequentialGroup()
                        .addComponent(newCharacterButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadCharacterButton))
                    .addComponent(editCharacterButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(staticLabelCurrentlyLoadedCharacter)
                    .addComponent(characterNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(staticLabelPath)
                    .addComponent(characterPathLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editCharacterButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newCharacterButton)
                    .addComponent(loadCharacterButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Charakter", tab2);

        staticLabelPhysicalTalents.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        staticLabelPhysicalTalents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        staticLabelPhysicalTalents.setText("Körpertalente");

        talentButtonGroup.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Fliegen");

        talentButtonGroup.add(jRadioButton2);
        jRadioButton2.setText("Gaukeleien");

        talentButtonGroup.add(jRadioButton3);
        jRadioButton3.setText("Klettern");

        talentButtonGroup.add(jRadioButton4);
        jRadioButton4.setText("Körperbeherrschung");

        talentButtonGroup.add(jRadioButton5);
        jRadioButton5.setText("Kraftakt");

        talentButtonGroup.add(jRadioButton6);
        jRadioButton6.setText("Reiten");

        talentButtonGroup.add(jRadioButton7);
        jRadioButton7.setText("Schwimmen");

        talentButtonGroup.add(jRadioButton8);
        jRadioButton8.setText("Selbstbeherrschung");

        talentButtonGroup.add(jRadioButton9);
        jRadioButton9.setText("Singen");

        talentButtonGroup.add(jRadioButton10);
        jRadioButton10.setText("Sinnesschärfe");

        talentButtonGroup.add(jRadioButton11);
        jRadioButton11.setText("Tanzen");

        talentButtonGroup.add(jRadioButton12);
        jRadioButton12.setText("Taschendiebstahl");

        talentButtonGroup.add(jRadioButton13);
        jRadioButton13.setText("Verbergen");

        talentButtonGroup.add(jRadioButton14);
        jRadioButton14.setText("Zechen");

        staticLabelSocialTalents.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        staticLabelSocialTalents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        staticLabelSocialTalents.setText("Gesellschaftstalente");

        talentButtonGroup.add(jRadioButton15);
        jRadioButton15.setText("Bekehren & Überzeugen");

        talentButtonGroup.add(jRadioButton16);
        jRadioButton16.setText("Betören");

        talentButtonGroup.add(jRadioButton17);
        jRadioButton17.setText("Einschüchtern");

        talentButtonGroup.add(jRadioButton18);
        jRadioButton18.setText("Etikette");

        talentButtonGroup.add(jRadioButton19);
        jRadioButton19.setText("Gassenwissen");

        talentButtonGroup.add(jRadioButton20);
        jRadioButton20.setText("Menschenkenntnis");

        talentButtonGroup.add(jRadioButton21);
        jRadioButton21.setText("Überreden");

        talentButtonGroup.add(jRadioButton22);
        jRadioButton22.setText("Verkleiden");

        talentButtonGroup.add(jRadioButton23);
        jRadioButton23.setText("Willenskraft");

        staticLabelNatureTalents.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        staticLabelNatureTalents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        staticLabelNatureTalents.setText("Naturtalente");

        talentButtonGroup.add(jRadioButton24);
        jRadioButton24.setText("Fährtensuchen");

        talentButtonGroup.add(jRadioButton25);
        jRadioButton25.setText("Fesseln");

        talentButtonGroup.add(jRadioButton26);
        jRadioButton26.setText("Fischen & Angeln");

        talentButtonGroup.add(jRadioButton27);
        jRadioButton27.setText("Orientierung");

        talentButtonGroup.add(jRadioButton28);
        jRadioButton28.setText("Pflanzenkunde");

        talentButtonGroup.add(jRadioButton29);
        jRadioButton29.setText("Tierkunde");

        talentButtonGroup.add(jRadioButton30);
        jRadioButton30.setText("Wildnisleben");

        staticLabelKnowledgeTalents.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        staticLabelKnowledgeTalents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        staticLabelKnowledgeTalents.setText("Wissenstalente");

        talentButtonGroup.add(jRadioButton31);
        jRadioButton31.setText("Brett- & Glücksspiel");

        talentButtonGroup.add(jRadioButton32);
        jRadioButton32.setText("Geographie");

        talentButtonGroup.add(jRadioButton33);
        jRadioButton33.setText("Geschichtswissen");

        talentButtonGroup.add(jRadioButton34);
        jRadioButton34.setText("Götter & Kulte");

        talentButtonGroup.add(jRadioButton35);
        jRadioButton35.setText("Kriegskunst");

        talentButtonGroup.add(jRadioButton36);
        jRadioButton36.setText("Magiekunde");

        talentButtonGroup.add(jRadioButton37);
        jRadioButton37.setText("Mechanik");

        talentButtonGroup.add(jRadioButton38);
        jRadioButton38.setText("Rechnen");

        talentButtonGroup.add(jRadioButton39);
        jRadioButton39.setText("Rechtskunde");

        talentButtonGroup.add(jRadioButton40);
        jRadioButton40.setText("Sagen & Legenden");

        talentButtonGroup.add(jRadioButton41);
        jRadioButton41.setText("Sphärenkunde");

        talentButtonGroup.add(jRadioButton42);
        jRadioButton42.setText("Sternkunde");

        staticLabelCraftingTalents.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        staticLabelCraftingTalents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        staticLabelCraftingTalents.setText("Handwerkstalente");

        talentButtonGroup.add(jRadioButton43);
        jRadioButton43.setText("Alchimie");

        talentButtonGroup.add(jRadioButton44);
        jRadioButton44.setText("Boote & Schiffe");

        talentButtonGroup.add(jRadioButton45);
        jRadioButton45.setText("Fahrzeuge");

        talentButtonGroup.add(jRadioButton46);
        jRadioButton46.setText("Handel");

        talentButtonGroup.add(jRadioButton47);
        jRadioButton47.setText("Heilkunde Gift");

        talentButtonGroup.add(jRadioButton48);
        jRadioButton48.setText("Heilkunde Krankheiten");

        talentButtonGroup.add(jRadioButton49);
        jRadioButton49.setText("Heilkunde Seele");

        talentButtonGroup.add(jRadioButton50);
        jRadioButton50.setText("Heilkunde Wunden");

        talentButtonGroup.add(jRadioButton51);
        jRadioButton51.setText("Holzbearbeitung");

        talentButtonGroup.add(jRadioButton52);
        jRadioButton52.setText("Lebensmittelbearbeitung");

        talentButtonGroup.add(jRadioButton53);
        jRadioButton53.setText("Lederbearbeitung");

        talentButtonGroup.add(jRadioButton54);
        jRadioButton54.setText("Malen & Zeichnen");

        talentButtonGroup.add(jRadioButton55);
        jRadioButton55.setText("Metallbearbeitung");

        talentButtonGroup.add(jRadioButton56);
        jRadioButton56.setText("Musizieren");

        talentButtonGroup.add(jRadioButton57);
        jRadioButton57.setText("Schlösserknacken");

        talentButtonGroup.add(jRadioButton58);
        jRadioButton58.setText("Steinbearbeitung");

        talentButtonGroup.add(jRadioButton59);
        jRadioButton59.setText("Stoffbearbeitung");

        trialButton.setText("Probe");
        trialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trialButtonActionPerformed(evt);
            }
        });

        staticLabelTrial.setText("auf gewähltes Talent, erleichtert um");

        trialEaseTextField.setText("0");

        collectiveTrialButton.setText("Sammelprobe");
        collectiveTrialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collectiveTrialButtonActionPerformed(evt);
            }
        });

        staticLabelCollectiveTrial1.setText("mit");

        collectiveTrialCountTextField.setText("10");

        staticLabelCollectiveTrial2.setText("Versuchen auf gewähltes Talent, erleichtert um");

        collectiveTrialEaseTextField.setText("0");

        talentsMainOutput.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        talentsMainOutput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        talentsMainOutput.setText("Ergebnis");

        talentsSecondaryOutput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        talentsSecondaryOutput.setText("Erläuterungen");
        talentsSecondaryOutput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                talentsSecondaryOutputMouseClicked(evt);
            }
        });

        talentValueLabel.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        talentValueLabel.setText("FW");

        staticLabelTalentValue.setText("Fertigkeitswert des gewählten Talents:");

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tab3Layout.createSequentialGroup()
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton5)
                            .addComponent(jRadioButton6)
                            .addComponent(jRadioButton7)
                            .addComponent(jRadioButton8)
                            .addComponent(jRadioButton9)
                            .addComponent(jRadioButton10)
                            .addComponent(jRadioButton11)
                            .addComponent(jRadioButton12)
                            .addComponent(jRadioButton13)
                            .addComponent(jRadioButton14)
                            .addComponent(staticLabelPhysicalTalents, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jRadioButton30)
                            .addComponent(jRadioButton29)
                            .addComponent(jRadioButton28)
                            .addComponent(jRadioButton27)
                            .addComponent(jRadioButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton16)
                            .addComponent(jRadioButton17)
                            .addComponent(jRadioButton18)
                            .addComponent(jRadioButton19)
                            .addComponent(jRadioButton20)
                            .addComponent(jRadioButton21)
                            .addComponent(jRadioButton22)
                            .addComponent(jRadioButton23)
                            .addComponent(staticLabelSocialTalents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton24)
                            .addComponent(jRadioButton25)
                            .addComponent(jRadioButton26)
                            .addComponent(staticLabelNatureTalents, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jRadioButton31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton32)
                            .addComponent(jRadioButton33)
                            .addComponent(jRadioButton34)
                            .addComponent(jRadioButton35)
                            .addComponent(jRadioButton36)
                            .addComponent(jRadioButton37)
                            .addComponent(jRadioButton38)
                            .addComponent(jRadioButton39)
                            .addComponent(jRadioButton40)
                            .addComponent(jRadioButton41)
                            .addComponent(jRadioButton42)
                            .addComponent(staticLabelKnowledgeTalents, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jRadioButton43)
                            .addComponent(jRadioButton44)
                            .addComponent(jRadioButton45)
                            .addComponent(jRadioButton46)
                            .addComponent(jRadioButton47)
                            .addComponent(jRadioButton48)
                            .addComponent(jRadioButton49)
                            .addComponent(jRadioButton50)
                            .addComponent(jRadioButton51)
                            .addComponent(jRadioButton52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton53)
                            .addComponent(jRadioButton54)
                            .addComponent(jRadioButton55)
                            .addComponent(jRadioButton56)
                            .addComponent(jRadioButton57)
                            .addComponent(jRadioButton58)
                            .addComponent(jRadioButton59)
                            .addComponent(staticLabelCraftingTalents, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tab3Layout.createSequentialGroup()
                        .addComponent(trialButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staticLabelTrial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trialEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(staticLabelTalentValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(talentValueLabel))
                    .addGroup(tab3Layout.createSequentialGroup()
                        .addComponent(collectiveTrialButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staticLabelCollectiveTrial1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(collectiveTrialCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staticLabelCollectiveTrial2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(collectiveTrialEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(talentsMainOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(talentsSecondaryOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trialButton)
                    .addComponent(staticLabelTrial)
                    .addComponent(trialEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(talentValueLabel)
                    .addComponent(staticLabelTalentValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(collectiveTrialButton)
                    .addComponent(staticLabelCollectiveTrial2)
                    .addComponent(collectiveTrialEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staticLabelCollectiveTrial1)
                    .addComponent(collectiveTrialCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(talentsMainOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(talentsSecondaryOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(staticLabelPhysicalTalents)
                    .addComponent(staticLabelSocialTalents)
                    .addComponent(staticLabelKnowledgeTalents)
                    .addComponent(staticLabelCraftingTalents))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton15)
                    .addComponent(jRadioButton31)
                    .addComponent(jRadioButton43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton16)
                    .addComponent(jRadioButton32)
                    .addComponent(jRadioButton44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton17)
                    .addComponent(jRadioButton33)
                    .addComponent(jRadioButton45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton18)
                    .addComponent(jRadioButton34)
                    .addComponent(jRadioButton46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton19)
                    .addComponent(jRadioButton35)
                    .addComponent(jRadioButton47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton20)
                    .addComponent(jRadioButton36)
                    .addComponent(jRadioButton48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton7)
                    .addComponent(jRadioButton21)
                    .addComponent(jRadioButton37)
                    .addComponent(jRadioButton49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton8)
                    .addComponent(jRadioButton22)
                    .addComponent(jRadioButton38)
                    .addComponent(jRadioButton50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton9)
                    .addComponent(jRadioButton23)
                    .addComponent(jRadioButton39)
                    .addComponent(jRadioButton51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton10)
                    .addComponent(jRadioButton40)
                    .addComponent(jRadioButton52)
                    .addComponent(staticLabelNatureTalents))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab3Layout.createSequentialGroup()
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton11)
                            .addComponent(jRadioButton41)
                            .addComponent(jRadioButton53))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton12)
                            .addComponent(jRadioButton42)
                            .addComponent(jRadioButton54))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton13)
                            .addComponent(jRadioButton55))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton14)
                            .addComponent(jRadioButton56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton58)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton59))
                    .addGroup(tab3Layout.createSequentialGroup()
                        .addComponent(jRadioButton24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton30)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Talente", tab3);

        staticLabelEquippedWeapon.setText("Ausgerüstete Waffe:");

        equippedWeaponComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        staticLabelEquippedArmor.setText("Angelegte Rüstung:");

        equippedArmorComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        staticLabelArmorValue.setText("Rüstungswert:");

        armorValueLabel.setText("RS");

        staticLabelStrain.setText("Belastungswert:");

        strainValueLabel.setText("BE");

        parryButton.setText("Parade");

        staticLabelParry.setText("mit Erleichterung um");

        parryEaseTextField.setText("0");

        attackButton.setText("Angriff");

        staticLabelRange.setText("Reichweite:");

        rangeLabel.setText("R");

        staticLabelAttack1.setText("mit Erleichterung um");

        attackEaseTextField.setText("0");

        staticLabelAttack2.setText("und");

        additionalDamageTextField.setText("0");

        staticLabelAttack3.setText("zusätzlichem Schaden");

        combatOutput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        combatOutput.setText("Output");

        javax.swing.GroupLayout tab4Layout = new javax.swing.GroupLayout(tab4);
        tab4.setLayout(tab4Layout);
        tab4Layout.setHorizontalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addComponent(staticLabelEquippedWeapon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(equippedWeaponComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(staticLabelRange)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rangeLabel))
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addComponent(staticLabelEquippedArmor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(equippedArmorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(staticLabelArmorValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(armorValueLabel)
                        .addGap(18, 18, 18)
                        .addComponent(staticLabelStrain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(strainValueLabel))
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addComponent(attackButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staticLabelAttack1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(attackEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staticLabelAttack2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(additionalDamageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staticLabelAttack3))
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addComponent(parryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staticLabelParry)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(parryEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(combatOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab4Layout.setVerticalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(staticLabelEquippedArmor)
                    .addComponent(equippedArmorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staticLabelArmorValue)
                    .addComponent(armorValueLabel)
                    .addComponent(staticLabelStrain)
                    .addComponent(strainValueLabel))
                .addGap(18, 18, 18)
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(staticLabelEquippedWeapon)
                    .addComponent(equippedWeaponComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staticLabelRange)
                    .addComponent(rangeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parryButton)
                    .addComponent(staticLabelParry)
                    .addComponent(parryEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attackButton)
                    .addComponent(staticLabelAttack1)
                    .addComponent(attackEaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staticLabelAttack2)
                    .addComponent(additionalDamageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staticLabelAttack3))
                .addGap(18, 18, 18)
                .addComponent(combatOutput)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Kampf", tab4);

        toggleViewButton.setSelected(true);
        toggleViewButton.setText("Hierarchische Ansicht");

        saveButton.setText("Speichern");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inventoryPanelLayout = new javax.swing.GroupLayout(inventoryPanel);
        inventoryPanel.setLayout(inventoryPanelLayout);
        inventoryPanelLayout.setHorizontalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        inventoryPanelLayout.setVerticalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tab5Layout = new javax.swing.GroupLayout(tab5);
        tab5.setLayout(tab5Layout);
        tab5Layout.setHorizontalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab5Layout.createSequentialGroup()
                        .addComponent(toggleViewButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton))
                    .addComponent(inventoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab5Layout.setVerticalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toggleViewButton)
                    .addComponent(saveButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inventoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Inventar", tab5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
		if (doneLoading) {
			Dimension panelSize = tabbedPane.getSelectedComponent().
					getPreferredSize();
			int width = Math.max(panelSize.width, tabHeaderSize.width);
			int height = panelSize.height + tabHeaderSize.height;
			Dimension newSize = new Dimension(width, height);
			tabbedPane.setMinimumSize(newSize);
			tabbedPane.setPreferredSize(newSize);
			pack();
		}
    }//GEN-LAST:event_tabbedPaneStateChanged

    private void talentsSecondaryOutputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_talentsSecondaryOutputMouseClicked
		if (secondaryOutputActive) {
			new CollectiveTrialDetailsDialog(this, collectiveTrialResult).
					setVisible(true);
		}
    }//GEN-LAST:event_talentsSecondaryOutputMouseClicked

    private void collectiveTrialButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collectiveTrialButtonActionPerformed
		String selectedTalent = talentButtonGroup.getSelection().
				getActionCommand();
		Integer ease = Tools.getIntegerFromTextField(
				collectiveTrialEaseTextField, "Erleichterung Sammelprobe", this);
		Integer count = Tools.getIntegerFromTextField(
				collectiveTrialCountTextField, "Anzahl Proben", this);
		if (ease != null && count != null) {
			collectiveTrialResult = new CollectiveTrial(count, selectedTalent,
					character, ease).getResult();
			talentsMainOutput.setForeground(Color.BLACK);
			talentsMainOutput.setText("Summe der Qualitätsstufen: " + String.
					valueOf(collectiveTrialResult.getQualityLevelSum()));
			talentsSecondaryOutput.setText("Hier klicken für Details");
			secondaryOutputActive = true;
		}
    }//GEN-LAST:event_collectiveTrialButtonActionPerformed

    private void trialButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trialButtonActionPerformed
		String selectedTalent = talentButtonGroup.getSelection().
				getActionCommand();
		Integer ease = Tools.getIntegerFromTextField(trialEaseTextField,
				"Erleichterung Probe", this);
		if (ease != null) {
			TrialResult result = new Trial(selectedTalent, character, ease).
					getResult();
			Color color;
			String mainText;
			if (result.isPassed()) {
				color = Color.GREEN.darker();
				mainText = "Geschafft mit Qualitätsstufe " + String.valueOf(
						result.getQualityLevel());
				List<String> criticalSuccesses = result.getCriticalSuccesses();
				if (criticalSuccesses.size() > 0) {
					mainText += " und einer Eins auf " + criticalSuccesses.
							remove(0);
					while (!criticalSuccesses.isEmpty()) {
						mainText = mainText + " und " + criticalSuccesses.
								remove(0);
					}
				}
			} else {
				color = Color.RED;
				mainText = "Nicht geschafft";
				List<String> failedAttributes = result.getFailedAttributes();
				if (failedAttributes.size() > 0) {
					mainText = mainText + ", gescheitert an "
							+ failedAttributes.remove(0);
					while (!failedAttributes.isEmpty()) {
						mainText = mainText + " und " + failedAttributes.remove(
								0);
					}
				}
				List<String> criticalFailures = result.getCriticalFailures();
				if (criticalFailures.size() > 0) {
					mainText = mainText + ", eine 20 auf " + criticalFailures.
							remove(0);
					while (!criticalFailures.isEmpty()) {
						mainText = mainText + " und " + criticalFailures.remove(
								0);
					}
				}
			}
			String secondaryText = "Gewürfelte Werte: ";
			String[] attributes = result.getTrial().getAttributes();
			int[] rolledNumbers = result.getRolledNumbers();
			for (int i = 0; i < 3; i++) {
				secondaryText += String.valueOf(rolledNumbers[i]) + " ("
						+ attributes[i] + "), ";
			}
			talentsMainOutput.setForeground(color);
			talentsMainOutput.setText(mainText);
			talentsSecondaryOutput.
					setText(secondaryText.replaceFirst("..$", ""));
			secondaryOutputActive = false;
		}
    }//GEN-LAST:event_trialButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
		try {
			character.save();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Die Datei " + character.
					getPath() + " kann bearbeitet werden!",
					"Speichern fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
		}
    }//GEN-LAST:event_saveButtonActionPerformed

    private void loadCharacterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCharacterButtonActionPerformed
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				"DSA-Charakter-Dateien (.char)", "char"));
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				setCharacter(Char.fromExistingFile(file));
			} catch (IOException | NullPointerException e) {
				JOptionPane.showMessageDialog(this, "Die Datei " + file.
						toString() + " konnte nicht gelesen werde.",
						"Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
				loadCharacterButtonActionPerformed(evt);
			}
		}
    }//GEN-LAST:event_loadCharacterButtonActionPerformed

    private void newCharacterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCharacterButtonActionPerformed
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(false);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				setCharacter(new Char(file));
				editCharacterButtonActionPerformed(null);
			} catch (IOException | NullPointerException e) {
				JOptionPane.showMessageDialog(this, "Die Datei " + file.
						toString() + " konnte nicht gelesen werde.",
						"Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
				loadCharacterButtonActionPerformed(evt);
			}
		}
    }//GEN-LAST:event_newCharacterButtonActionPerformed

    private void editCharacterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCharacterButtonActionPerformed
		if (character.getPath().equals("-")) {
			JOptionPane.showMessageDialog(this,
					"Bitte erst eine neue Datei anlegen!",
					"Standard-Charakter kann nicht gespeichert werden",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			new EditCharacterWindow(character);
		}
    }//GEN-LAST:event_editCharacterButtonActionPerformed

    private void w20ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_w20ButtonActionPerformed
		diceMainOutput.setText("");
		diceMainOutput.setIcon(loadingIcon);
		diceSecondaryOutput.setText("(W20)");
		Timer timer = new Timer(250, new SetLabelTextAction(diceMainOutput, w20.
				roll()));
		timer.setRepeats(false);
		timer.start();
    }//GEN-LAST:event_w20ButtonActionPerformed

    private void w6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_w6ButtonActionPerformed
		diceMainOutput.setText("");
		diceMainOutput.setIcon(loadingIcon);
		diceSecondaryOutput.setText("(W6)");
		Timer timer = new Timer(250, new SetLabelTextAction(diceMainOutput, w6.
				roll()));
		timer.setRepeats(false);
		timer.start();
    }//GEN-LAST:event_w6ButtonActionPerformed

    private void w3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_w3ButtonActionPerformed
		diceMainOutput.setText("");
		diceMainOutput.setIcon(loadingIcon);
		diceSecondaryOutput.setText("(W3)");
		Timer timer = new Timer(250, new SetLabelTextAction(diceMainOutput, w3.
				roll()));
		timer.setRepeats(false);
		timer.start();
    }//GEN-LAST:event_w3ButtonActionPerformed

	//<editor-fold defaultstate="collapsed" desc="Component Declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField additionalDamageTextField;
    private javax.swing.JLabel armorValueLabel;
    private javax.swing.JButton attackButton;
    private javax.swing.JTextField attackEaseTextField;
    private javax.swing.JLabel characterNameLabel;
    private javax.swing.JLabel characterPathLabel;
    private javax.swing.JButton collectiveTrialButton;
    private javax.swing.JTextField collectiveTrialCountTextField;
    private javax.swing.JTextField collectiveTrialEaseTextField;
    private javax.swing.JLabel combatOutput;
    private javax.swing.JLabel diceMainOutput;
    private javax.swing.JLabel diceSecondaryOutput;
    private javax.swing.JButton editCharacterButton;
    private javax.swing.JComboBox<String> equippedArmorComboBox;
    private javax.swing.JComboBox<String> equippedWeaponComboBox;
    private graphics.inventory.InventoryPanel inventoryPanel;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton14;
    private javax.swing.JRadioButton jRadioButton15;
    private javax.swing.JRadioButton jRadioButton16;
    private javax.swing.JRadioButton jRadioButton17;
    private javax.swing.JRadioButton jRadioButton18;
    private javax.swing.JRadioButton jRadioButton19;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton20;
    private javax.swing.JRadioButton jRadioButton21;
    private javax.swing.JRadioButton jRadioButton22;
    private javax.swing.JRadioButton jRadioButton23;
    private javax.swing.JRadioButton jRadioButton24;
    private javax.swing.JRadioButton jRadioButton25;
    private javax.swing.JRadioButton jRadioButton26;
    private javax.swing.JRadioButton jRadioButton27;
    private javax.swing.JRadioButton jRadioButton28;
    private javax.swing.JRadioButton jRadioButton29;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton30;
    private javax.swing.JRadioButton jRadioButton31;
    private javax.swing.JRadioButton jRadioButton32;
    private javax.swing.JRadioButton jRadioButton33;
    private javax.swing.JRadioButton jRadioButton34;
    private javax.swing.JRadioButton jRadioButton35;
    private javax.swing.JRadioButton jRadioButton36;
    private javax.swing.JRadioButton jRadioButton37;
    private javax.swing.JRadioButton jRadioButton38;
    private javax.swing.JRadioButton jRadioButton39;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton40;
    private javax.swing.JRadioButton jRadioButton41;
    private javax.swing.JRadioButton jRadioButton42;
    private javax.swing.JRadioButton jRadioButton43;
    private javax.swing.JRadioButton jRadioButton44;
    private javax.swing.JRadioButton jRadioButton45;
    private javax.swing.JRadioButton jRadioButton46;
    private javax.swing.JRadioButton jRadioButton47;
    private javax.swing.JRadioButton jRadioButton48;
    private javax.swing.JRadioButton jRadioButton49;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton50;
    private javax.swing.JRadioButton jRadioButton51;
    private javax.swing.JRadioButton jRadioButton52;
    private javax.swing.JRadioButton jRadioButton53;
    private javax.swing.JRadioButton jRadioButton54;
    private javax.swing.JRadioButton jRadioButton55;
    private javax.swing.JRadioButton jRadioButton56;
    private javax.swing.JRadioButton jRadioButton57;
    private javax.swing.JRadioButton jRadioButton58;
    private javax.swing.JRadioButton jRadioButton59;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JButton loadCharacterButton;
    private javax.swing.JButton newCharacterButton;
    private javax.swing.JButton parryButton;
    private javax.swing.JTextField parryEaseTextField;
    private javax.swing.JLabel rangeLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel staticLabelArmorValue;
    private javax.swing.JLabel staticLabelAttack1;
    private javax.swing.JLabel staticLabelAttack2;
    private javax.swing.JLabel staticLabelAttack3;
    private javax.swing.JLabel staticLabelCollectiveTrial1;
    private javax.swing.JLabel staticLabelCollectiveTrial2;
    private javax.swing.JLabel staticLabelCraftingTalents;
    private javax.swing.JLabel staticLabelCurrentlyLoadedCharacter;
    private javax.swing.JLabel staticLabelEquippedArmor;
    private javax.swing.JLabel staticLabelEquippedWeapon;
    private javax.swing.JLabel staticLabelKnowledgeTalents;
    private javax.swing.JLabel staticLabelNatureTalents;
    private javax.swing.JLabel staticLabelParry;
    private javax.swing.JLabel staticLabelPath;
    private javax.swing.JLabel staticLabelPhysicalTalents;
    private javax.swing.JLabel staticLabelRange;
    private javax.swing.JLabel staticLabelSocialTalents;
    private javax.swing.JLabel staticLabelStrain;
    private javax.swing.JLabel staticLabelTalentValue;
    private javax.swing.JLabel staticLabelTrial;
    private javax.swing.JLabel strainValueLabel;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.ButtonGroup talentButtonGroup;
    private javax.swing.JLabel talentValueLabel;
    private javax.swing.JLabel talentsMainOutput;
    private javax.swing.JLabel talentsSecondaryOutput;
    private javax.swing.JToggleButton toggleViewButton;
    private javax.swing.JButton trialButton;
    private javax.swing.JTextField trialEaseTextField;
    private javax.swing.JButton w20Button;
    private javax.swing.JButton w3Button;
    private javax.swing.JButton w6Button;
    // End of variables declaration//GEN-END:variables
	//</editor-fold>
}
