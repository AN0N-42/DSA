package graphics.inventory;

import game.inventory.UsableItem;
import java.awt.Container;
import java.awt.Dimension;
import java.util.EmptyStackException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UsableItemPanel extends DataPanel {

	private UsableItem usableItem;

	public UsableItemPanel(UsableItem usableItem, ContainerItemPanel parent) {
		super(parent);
		this.usableItem = usableItem;
		initComponents();
		amountLabel.setText(String.valueOf(usableItem.getAmount()));
		adjustSize();
	}

	@Override
	public void updateValues() {
		usableItem.setName(nameTextField.getText());
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
        staticLabelAmount = new javax.swing.JLabel();
        amountLabel = new javax.swing.JLabel();
        incrementButton = new javax.swing.JButton();
        decrementButton = new javax.swing.JButton();

        setAlignmentY(0.0F);

        nameTextField.setColumns(20);
        nameTextField.setText(usableItem.getName());

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/trash.png"))); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        staticLabelAmount.setText("Anzahl:");

        amountLabel.setText("0");

        incrementButton.setText("+1");
        incrementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incrementButtonActionPerformed(evt);
            }
        });

        decrementButton.setText("-1");
        decrementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decrementButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staticLabelAmount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(amountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(incrementButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decrementButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(staticLabelAmount)
                        .addComponent(amountLabel)
                        .addComponent(incrementButton)
                        .addComponent(decrementButton)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(deleteButton))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void incrementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incrementButtonActionPerformed
		usableItem.restock();
		amountLabel.setText(String.valueOf(usableItem.getAmount()));
    }//GEN-LAST:event_incrementButtonActionPerformed

    private void decrementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decrementButtonActionPerformed
		try {
			usableItem.use();
			amountLabel.setText(String.valueOf(usableItem.getAmount()));
		} catch (EmptyStackException ese) {
			// TODO implement error sound
		}
    }//GEN-LAST:event_decrementButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
		usableItem.getContainer().remove(usableItem);
		parent.removeSubPanel(this);
		parent.revalidate();
    }//GEN-LAST:event_deleteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountLabel;
    private javax.swing.JButton decrementButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton incrementButton;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel staticLabelAmount;
    // End of variables declaration//GEN-END:variables
}
