package graphics.inventory;

import game.combat.RangedWeaponItem;
import java.awt.Dimension;

public class RangedWeaponItemPanel extends DataPanel {

	private RangedWeaponItem rangedWeaponItem;

	public RangedWeaponItemPanel(RangedWeaponItem rangedWeaponItem,
			ContainerItemPanel parent) {
		super(parent);
		this.rangedWeaponItem = rangedWeaponItem;
		initComponents();
		adjustSize();
	}

	@Override
	public void updateValues() {
		rangedWeaponItem.setName(nameTextField.getText());

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameTextField = new javax.swing.JTextField();
        deleteButton = new javax.swing.JButton();
        staticLabelCombatTechnique = new javax.swing.JLabel();
        staticLabelLoadingTime = new javax.swing.JLabel();
        loadingTimeTextField = new javax.swing.JTextField();
        staticLabelDamage = new javax.swing.JLabel();
        damageTextField = new javax.swing.JTextField();
        staticLabelRange = new javax.swing.JLabel();
        rangeTextField = new javax.swing.JTextField();
        staticLabelAmmunition = new javax.swing.JLabel();
        ammunitionTextField = new javax.swing.JTextField();
        combatTechniqueLabel = new javax.swing.JLabel();

        setAlignmentY(0.0F);

        nameTextField.setColumns(20);
        nameTextField.setText(rangedWeaponItem.getName());

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/trash.png"))); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        staticLabelCombatTechnique.setText("Kampftechnik:");

        staticLabelLoadingTime.setText("Ladezeit:");

        loadingTimeTextField.setColumns(3);
        loadingTimeTextField.setText(String.valueOf(rangedWeaponItem.getLoadingTime()));

        staticLabelDamage.setText("TP:");

        damageTextField.setColumns(5);
        damageTextField.setText(rangedWeaponItem.getDamage());

        staticLabelRange.setText("Reichweite:");

        rangeTextField.setColumns(7);
        rangeTextField.setText(rangedWeaponItem.getRange());

        staticLabelAmmunition.setText("Munition:");

        ammunitionTextField.setColumns(5);
        ammunitionTextField.setText(rangedWeaponItem.getAmmunition());

        combatTechniqueLabel.setText(rangedWeaponItem.getType().getCombatTechnique());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staticLabelLoadingTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadingTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staticLabelDamage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(damageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staticLabelRange)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rangeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staticLabelAmmunition)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ammunitionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staticLabelCombatTechnique)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combatTechniqueLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(deleteButton)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(staticLabelCombatTechnique)
                .addComponent(staticLabelLoadingTime)
                .addComponent(loadingTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(staticLabelDamage)
                .addComponent(damageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(staticLabelRange)
                .addComponent(rangeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(staticLabelAmmunition)
                .addComponent(ammunitionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(combatTechniqueLabel))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
		rangedWeaponItem.getContainer().remove(rangedWeaponItem);
		parent.removeSubPanel(this);
		parent.revalidate();
    }//GEN-LAST:event_deleteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ammunitionTextField;
    private javax.swing.JLabel combatTechniqueLabel;
    private javax.swing.JTextField damageTextField;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField loadingTimeTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField rangeTextField;
    private javax.swing.JLabel staticLabelAmmunition;
    private javax.swing.JLabel staticLabelCombatTechnique;
    private javax.swing.JLabel staticLabelDamage;
    private javax.swing.JLabel staticLabelLoadingTime;
    private javax.swing.JLabel staticLabelRange;
    // End of variables declaration//GEN-END:variables
}
