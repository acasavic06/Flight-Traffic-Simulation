package dijalozi;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import buttoni.OkButton;

public class ValidacionaGreska extends MojDijalog {

	public ValidacionaGreska(Frame frame, String message) {
		super(frame, message);
		
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(frame);
		
		Label lblPoruka = new Label(message, Label.CENTER);
        
        Panel panelDugme = new Panel(new FlowLayout(FlowLayout.CENTER));
        
        OkButton ok = new OkButton("Ok", this);
        
        panelDugme.add(ok);
        
        this.add(BorderLayout.CENTER, lblPoruka);
        this.add(BorderLayout.SOUTH, panelDugme);
	}
	
}
