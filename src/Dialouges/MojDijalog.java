package dijalozi;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MojDijalog extends Dialog{
	public MojDijalog(Frame frame, String message) {
		super(frame);
		this.setSize(600,300);
		
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		this.setModal(true);
		this.setVisible(false);
	}
}
