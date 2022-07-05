package graphics.actions;

import graphics.MainWindow;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class SwitchTabAction extends AbstractAction {

	private final JFrame parent;
	private final JTabbedPane tabbedPane;
	private final int index;

	public SwitchTabAction(JFrame parent, JTabbedPane tabbedPane, int index) {
		if (index < 0 || index > tabbedPane.getTabCount() - 1) {
			throw new IllegalArgumentException(
					"Parameter index must be a valid tab index of parameter tabbedPane! Minimum: 0, maximum: "
					+ String.valueOf(tabbedPane.getTabCount() - 1) + ", was: "
					+ String.valueOf(index));
		}
		this.parent = parent;
		this.tabbedPane = tabbedPane;
		this.index = index;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		tabbedPane.setSelectedIndex(index);
		parent.pack();
	}
}
