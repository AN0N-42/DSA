package graphics.inventory;

import java.awt.Dimension;
import javax.swing.JPanel;

public abstract class DataPanel extends JPanel {

	protected ContainerItemPanel parent;

	public DataPanel(ContainerItemPanel parent) {
		this.parent = parent;
	}

	public void adjustSize() {
		Dimension preferredSize = getPreferredSize();
		setSize(preferredSize);
		if (parent != null) {
			parent.repaintOutlineCanvas();
			parent.adjustSize();
		}
		revalidate();
		repaint();
	}

	public abstract void updateValues();
}
