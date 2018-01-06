package adds;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class WindowFunctions {

	static JFrame _frame;

	static Action exitAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			_frame.dispose();
		}
	};

	static public void addShortcuts(JFrame frame) {
		_frame=frame;
		
		int cond;
    cond=JComponent.WHEN_FOCUSED;
    cond=JComponent.WHEN_IN_FOCUSED_WINDOW;
    cond=JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
		
		frame.getRootPane().getInputMap(cond).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitAction");
    frame.getRootPane().getActionMap().put("exitAction", exitAction);
	}

}
