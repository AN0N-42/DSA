package misc;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Tools {

	public static Integer getIntegerFromTextField(JTextField textField,
			String name, Component parent) {
		try {
			return Integer.parseInt(textField.getText());
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(parent, "Das Textfeld (" + name
					+ ") muss eine ganze Zahl enthalten!", "Eingabefehler",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	public static Integer getNonNegativeIntegerFromTextField(
			JTextField textField, String name, Component parent) {
		try {
			Integer i = Integer.parseInt(textField.getText());
			if (i < 0) {
				JOptionPane.showMessageDialog(parent, "Das Textfeld (" + name
						+ ") muss eine nichtnegative Zahl enthalten!",
						"Eingabefehler", JOptionPane.WARNING_MESSAGE);
				return null;
			} else {
				return i;
			}
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(parent, "Das Textfeld (" + name
					+ ") muss eine ganze Zahl enthalten!", "Eingabefehler",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
}
