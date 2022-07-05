package graphics;

import game.talents.CollectiveTrialResult;
import game.talents.TrialResult;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class CollectiveTrialDetailsDialog extends javax.swing.JDialog {

	private final DefaultListModel<String> listEntries
			= new DefaultListModel<String>();
	private final TrialResult[] trialResults;

	public CollectiveTrialDetailsDialog(MainWindow parent,
			CollectiveTrialResult collectiveTrialResult) {
		super(parent, "Sammelprobe " + collectiveTrialResult.
				getCollectiveTrial().
				getTrial().getTalent(), true);

		// register ESC key to close the dialog
		getRootPane().registerKeyboardAction(e -> dispose(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		// add results to list
		trialResults = collectiveTrialResult.getTrialResults();
		for (int i = 0; i < trialResults.length; i++) {
			listEntries.addElement(String.valueOf(i + 1) + ". QS " + String.
					valueOf(trialResults[i].getQualityLevel()));
		}

		initComponents();

		// adjust size
		trialsListValueChanged(null);

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        trialsList = new javax.swing.JList<>();
        passedLabel = new javax.swing.JLabel();
        rolledNumbersLabel = new javax.swing.JLabel();
        failedAttributesLabel = new javax.swing.JLabel();
        bottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(25, 32767));
        rightFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 25));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        closeButton.setText("Schließen");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        trialsList.setModel(listEntries);
        trialsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        trialsList.setSelectedIndex(0);
        trialsList.setVisibleRowCount(Math.min(25, trialResults.length)
        );
        trialsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                trialsListValueChanged(evt);
            }
        });
        scrollPane.setViewportView(trialsList);

        passedLabel.setText("Bestanden");

        rolledNumbersLabel.setText("Gewürfelte Werte");

        failedAttributesLabel.setText("Gescheitert an");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closeButton)
                    .addComponent(passedLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rolledNumbersLabel)
                            .addComponent(failedAttributesLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rightFiller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(bottomFiller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(closeButton)
                        .addGap(18, 18, 18)
                        .addComponent(passedLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rolledNumbersLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(failedAttributesLabel))
                            .addComponent(rightFiller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomFiller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
		dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void trialsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_trialsListValueChanged
		int index = trialsList.getSelectedIndex();
		TrialResult result = trialResults[trialsList.getSelectedIndex()];
		if (result.isPassed()) {
			passedLabel.setForeground(Color.GREEN.darker());
			passedLabel.setText("Bestanden");
			failedAttributesLabel.setText("");
		} else {
			passedLabel.setForeground(Color.RED);
			passedLabel.setText("Nicht bestanden");
			failedAttributesLabel.setText("Gescheitert an: " + result.
					getFailedAttributes().toString());
		}
		String rolledNumbersText = "Gewürfelte Werte: ";
		String[] attributes = result.getTrial().getAttributes();
		int[] rolledNumbers = result.getRolledNumbers();
		for (int i = 0; i < 3; i++) {
			rolledNumbersText += String.valueOf(rolledNumbers[i]) + " ("
					+ attributes[i] + "), ";
		}
		rolledNumbersLabel.setText(rolledNumbersText.replaceFirst("..$", ""));
		pack();
    }//GEN-LAST:event_trialsListValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler bottomFiller;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel failedAttributesLabel;
    private javax.swing.JLabel passedLabel;
    private javax.swing.Box.Filler rightFiller;
    private javax.swing.JLabel rolledNumbersLabel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JList<String> trialsList;
    // End of variables declaration//GEN-END:variables
}
