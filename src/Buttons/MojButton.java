package buttoni;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dijalozi.MojDijalog;


public class MojButton extends Button {
	public MojButton(String name, MojDijalog dialog) {
		super(name);
		
		this.addActionListener(e->{
			dialog.setVisible(true);
		});
		
		
		this.setVisible(true);
	}
}
