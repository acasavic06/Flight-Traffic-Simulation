package projekatOO2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import Objekti.ActivateMonitor;
import Objekti.Aerodrom;
import Objekti.AvioSistem;
import Objekti.Crtez;
import Objekti.Simulacija;
import buttoni.MojButton;
import dijalozi.MojDijalog;
import dijalozi.UnosAerodroma;
import dijalozi.UnosLeta;
import dijalozi.ValidacionaGreska;
import formatter.CSVFormatter;
import formatter.Formatter;
import formatter.JSONFormatter;
import izuzeci.Izuzetak;

public class Prozor extends Frame {

	private Crtez crtez;
	private boolean let_notAerodrom;
	private AvioSistem sistem;
	private List tabela;
	private Panel listaCheckboxAerodroma;
	private ActivateMonitor monitor;
	private Thread nitCrtez;
	
	private boolean simulacijaAktivna = false;
	private boolean simulacijaPauzirana = false;
	private Simulacija simulacija;
	private Thread nitSimulacija;
	private Label vremeSimulacije;
	
	
	public Prozor() {
		super("Projekat");
		this.sistem = new AvioSistem();
		this.monitor = new ActivateMonitor(this);
		this.crtez = new Crtez(this);
		nitCrtez = new Thread(crtez);
		nitCrtez.start();
		
		this.simulacija = new Simulacija(this);
		nitSimulacija = new Thread(simulacija);
		nitSimulacija.start();
		
		
		this.setSize(1000, 500);

		this.setLayout(new BorderLayout());

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (nitCrtez != null)
					nitCrtez.interrupt();
				if (nitSimulacija != null)
					nitSimulacija.interrupt();
				monitor.zaustavi();
				dispose();
			}
		});

		Panel desniPanel = new Panel(new BorderLayout());
		desniPanel.setPreferredSize(new Dimension(320, 500));

		CheckboxGroup pravaPrikaza = new CheckboxGroup();
		let_notAerodrom = true;
		Checkbox radioLetovi = new Checkbox("Letovi", pravaPrikaza, true);
		Checkbox radioAerodromi = new Checkbox("Aerodromi", pravaPrikaza, false);

		radioLetovi.addItemListener(e -> {
			let_notAerodrom = true;
			osveziTabelu();
		});

		radioAerodromi.addItemListener((e) -> {
			let_notAerodrom = false;
			osveziTabelu();
		});
		
		Panel gornjiMeniDesnogPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		gornjiMeniDesnogPanel.add(radioLetovi);
		gornjiMeniDesnogPanel.add(radioAerodromi);

		desniPanel.add(BorderLayout.NORTH, gornjiMeniDesnogPanel);

		this.tabela = new List();
		listaCheckboxAerodroma = new Panel();
		listaCheckboxAerodroma.setLayout(new GridLayout(0, 1));
		
		listaCheckboxAerodroma.setPreferredSize(new Dimension(60, 500));

		desniPanel.add(BorderLayout.CENTER, tabela);
		desniPanel.add(BorderLayout.EAST, listaCheckboxAerodroma);

		this.add(BorderLayout.EAST, desniPanel);
		
		
		TextField txtSaveFile = new TextField("podaci.csv", 20); // Podrazumevano ime i širina polja
		Button btnSave = new Button("Sacuvaj");
		Button btnLoad = new Button("Ucitaj");

		
//		btnSave.addActionListener(e -> {
//			try {
//				String imeFajla = txtSaveFile.getText();
//				Formatter c = getFormatter(imeFajla);
//				c.sacuvaj();
//				System.out.println("USPEH: Podaci su uspešno upisani u fajl: " + imeFajla);
//			} catch (Izuzetak ex) {
//				ValidacionaGreska greska = new ValidacionaGreska(this, ex.getMessage());
//				greska.setVisible(true);
//			}
//		});
		
		btnSave.addActionListener(e -> {
			FileDialog fd = new FileDialog(Prozor.this, "Dijalog", FileDialog.SAVE);
			fd.setVisible(true);
			
			if (fd.getFile() == null) return;
			
			String imeFajla = fd.getDirectory() + fd.getFile();
			
			try {
				Formatter c = getFormatter(imeFajla);
				c.sacuvaj();
				System.out.println("USPEH: Podaci su uspešno upisani u fajl: " + imeFajla);
			} catch (Izuzetak ex) {
				ValidacionaGreska greska = new ValidacionaGreska(this, ex.getMessage());
				greska.setVisible(true);
			}
		});
		
		btnLoad.addActionListener(e->{
			try {
				String imeFajla = txtSaveFile.getText();
				Formatter c=getFormatter(imeFajla);
				c.ucitaj();
				osveziTabelu();
				
				System.out.println("Broj letova: " + sistem.getLetovi().size());
				System.out.println("Broj aerodroma: " + sistem.getAerodromi().size());
				
				System.out.println("USPEH: Podaci su uspešno ucitani iz fajla: " + imeFajla);
			} catch (Izuzetak ex) {
				ValidacionaGreska greska = new ValidacionaGreska(this, ex.getMessage());
				greska.setVisible(true);
			}
		});

		Panel gore = new Panel();

		gore.setLayout(new FlowLayout());

		MojDijalog d1 = new UnosAerodroma(this, "Unos", sistem);
		MojDijalog d2 = new UnosLeta(this, "Unos", sistem);

		gore.add(new MojButton("Unos Aerodroma", d1));
		gore.add(new MojButton("Unos Leta", d2));
		gore.add(btnLoad);
		gore.add(btnSave);
		gore.add(txtSaveFile);
		
		Panel dole = new Panel();

		dole.setLayout(new FlowLayout());

		Button pauza= new Button("Pauziraj simulaciju");
		Button start = new Button("Pokreni simulaciju");
		vremeSimulacije = new Label("Vreme simulacije: 00:00");

		pauza.addActionListener(e->{
			
			if (simulacijaPauzirana) {
				simulacija.pauziraj(false);
				pauza.setLabel("Pauziraj simulaciju");
			}else {
				simulacija.pauziraj(true);
				pauza.setLabel("Nastavi simulaciju");
			}
			
			simulacijaPauzirana=!simulacijaPauzirana;
		});
		
		start.addActionListener(e->{
			simulacijaAktivna=!simulacijaAktivna;

			if (simulacijaAktivna) {
				dole.add(pauza);
				dole.add(vremeSimulacije);
				start.setLabel("Prekini simulaciju");
				simulacija.setRadi(true);
			}else {
				dole.remove(pauza);
				dole.remove(vremeSimulacije);
				start.setLabel("Pokreni simulaciju");
				simulacijaPauzirana = false;
		        pauza.setLabel("Pauziraj simulaciju");
		        simulacija.setRadi(false);
				simulacija.resetSimulacije();
				crtez.repaint();
			}
			
			dole.validate();
		    dole.repaint();
		});
				
		dole.add(start);

		this.add(BorderLayout.SOUTH, dole);
				
		this.add(BorderLayout.CENTER, crtez);
		
		this.add(BorderLayout.NORTH, gore);

		this.setVisible(true);

		osveziTabelu();
	}

	public void osveziTabelu() {
		
		tabela.removeAll();
        osveziCheckboxTabelu();
		
	    if (let_notAerodrom) {
	    	listaCheckboxAerodroma.setVisible(false);
	        for (Objekti.Let l : sistem.getLetovi()) {
	            String red = l.getSrc() + " -> " + l.getDst() + " " + l.getTime() + " " + l.getDuration();
	            
	            tabela.add(red);
	        }
	    } else {
	    	listaCheckboxAerodroma.setVisible(true);
	    	for (Objekti.Aerodrom a : sistem.getAerodromi()) {
	            String red = a.getCode() + " " + a.getName() + " (" + a.getX() + ", " + a.getY() +")";
	            
	            tabela.add(red);
	        }

	    }
	    validate();
	    crtez.repaint();
	    
	}
	
	private Formatter getFormatter(String imeFajla) throws Izuzetak {
		Formatter c;
		if (imeFajla.endsWith(".csv")) {
			c = new CSVFormatter(imeFajla, sistem);
		}else if(imeFajla.endsWith(".json")) {
			c = new JSONFormatter(imeFajla, sistem);				
		}else {
			throw new Izuzetak("Fajl mora da se zavrsava ili sa .csv ili sa .json");
		}
		return c;
	}
	
	public ActivateMonitor getMonitor() {return monitor;}
	public AvioSistem getSistem() {return sistem;}
	public Crtez getCrtez() {return crtez;}
	public Label getLabel() {return vremeSimulacije;}
	public boolean getAktivnaSimulacija() {return simulacijaAktivna;}
	
	
	public java.util.List<Aerodrom> getAerodromi(){
		return sistem.getAerodromi();
	}
	
	private void osveziCheckboxTabelu() {
		
		listaCheckboxAerodroma.removeAll();
		
		for (Aerodrom a : sistem.getAerodromi()) {
			Checkbox cb = new Checkbox(a.getCode(), a.isVidljiv());
			cb.addItemListener(e->{
				a.setVidljiv(cb.getState());
				crtez.repaint();
			});
			
			listaCheckboxAerodroma.add(cb);			
		}
		
		listaCheckboxAerodroma.validate();
		listaCheckboxAerodroma.repaint(); // radi i bez
	}

}
