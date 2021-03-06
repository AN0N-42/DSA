package graphics.inventory;

import game.inventory.ContainerItem;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

public class ContainerItemPanel extends DataPanel {

	private ContainerItem containerItem;
	private List<DataPanel> subPanels = new ArrayList<>();

	public ContainerItemPanel(ContainerItem containerItem,
			ContainerItemPanel parent) {
		super(parent);
		this.containerItem = containerItem;
		initComponents();
		adjustSize();
	}

	public void addSubPanel(DataPanel subPanel) {
		subPanels.add(subPanel);
		panel.add(subPanel);
		outlineCanvas.addSubPanel(subPanel);
	}

	public void removeSubPanel(DataPanel subPanel) {
		subPanels.remove(subPanel);
		panel.remove(subPanel);
		outlineCanvas.removeSubPanel(subPanel);
		repaintOutlineCanvas();
		adjustSize();
	}

	public void repaintOutlineCanvas() {
		outlineCanvas.revalidate();
		outlineCanvas.repaint();
	}

	@Override
	public void updateValues() {
		for (DataPanel subPanel : subPanels) {
			subPanel.updateValues();
		}
		containerItem.setName(nameTextField.getText());
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outlineCanvas = new graphics.inventory.OutlineCanvas();
        nameTextField = new javax.swing.JTextField();
        deleteButton = new javax.swing.JButton();
        addItemButton = new javax.swing.JButton();
        panel = new javax.swing.JPanel();

        setAlignmentY(0.0F);

        nameTextField.setColumns(20);
        nameTextField.setText(containerItem.getName());
        nameTextField.setToolTipText(containerItem.getName());

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/trash.png"))); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        addItemButton.setText("Item hinzuf??gen");
        addItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemButtonActionPerformed(evt);
            }
        });

        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        panel.setAlignmentY(0.0F);
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addItemButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(outlineCanvas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addItemButton))
                    .addComponent(deleteButton))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outlineCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemButtonActionPerformed

    }//GEN-LAST:event_addItemButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
		ContainerItem container = containerItem.getContainer();
		if (container != null) {
			container.remove(containerItem);
			parent.removeSubPanel(this);
		} else {
			// remove toplevel container from inventory
			containerItem.getInventory().removeToplevelContainerItem(
					containerItem);
			Container parent = getParent();
			parent.remove(this);
			parent.revalidate();
		}
    }//GEN-LAST:event_deleteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField nameTextField;
    private graphics.inventory.OutlineCanvas outlineCanvas;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
