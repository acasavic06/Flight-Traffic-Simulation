package buttoni;

import java.awt.Button;

import dijalozi.MojDijalog;


public class OkButton extends Button{
	public OkButton(String name, MojDijalog dialog) {
		super(name);
		
		this.addActionListener(e->{
			dialog.dispose();
		});
		
		
		this.setVisible(true);
	}
}
