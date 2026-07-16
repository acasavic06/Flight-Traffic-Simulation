package dijalozi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Objekti.ActivateMonitor;

public class Neaktivnost extends Dialog {

	private Label l;

	public Neaktivnost(Frame frame,ActivateMonitor monitor) {
		//super(frame, "Neaktivnost", false);
		super(frame, "Neaktivnost", true);
		
		l = new Label();
		Button ok = new Button("OK");
		
		
		ok.addActionListener(e -> {
            dispose();
            monitor.prekiniOdbrojavanje();
        });
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				monitor.prekiniOdbrojavanje();
			}
		});
		
		setLayout(new BorderLayout());
        add(l, BorderLayout.CENTER);
        add(ok, BorderLayout.SOUTH);
        setSize(400, 200);
		this.setLocationRelativeTo(frame);
	}

	public void odbrojavaj(int sekunde) {
		l.setText("Zbog neaktivnosti program ce se ugasiti za " + sekunde
				+ " sekundi. Kliknite OK ako zelite da nastavite.");
	}

}
