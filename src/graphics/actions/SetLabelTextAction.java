package graphics.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetLabelTextAction extends AbstractAction {

	private final JLabel label;
	private final String text;

	public SetLabelTextAction(JLabel label, int text) {
		this.label = label;
		this.text = String.valueOf(text);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		label.setIcon(null);
		label.setText(text);
	}
}
