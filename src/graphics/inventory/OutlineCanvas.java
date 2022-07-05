package graphics.inventory;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class OutlineCanvas extends Canvas {

	private List<DataPanel> subPanels = new ArrayList<>();

	public void setSubPanels(List<DataPanel> subPanels) {
		this.subPanels = subPanels;
	}

	public void addSubPanel(DataPanel subPanel) {
		subPanels.add(subPanel);
	}

	public void removeSubPanel(DataPanel subPanel) {
		subPanels.remove(subPanel);
		setSize(getWidth(), getHeight() - subPanel.getHeight());
	}

	@Override
	public void paint(Graphics g) {
		int width = getPreferredSize().width;
		int currentHeight = 13;
		for (DataPanel subPanel : subPanels) {
			g.drawLine(10, currentHeight, width, currentHeight);
			currentHeight += subPanel.getHeight();
		}
		if (!subPanels.isEmpty()) {
			g.drawLine(10, 0, 10, currentHeight - subPanels.get(subPanels.size()
					- 1).getHeight());
		}
		//setSize(new Dimension(width, currentHeight - 13));
	}
}
