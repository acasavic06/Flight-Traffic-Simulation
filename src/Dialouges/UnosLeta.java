package dijalozi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import Objekti.AvioSistem;
import Objekti.Let;
import izuzeci.Izuzetak;
import projekatOO2.Prozor;

public class UnosLeta extends MojDijalog {

	private TextField textField[];

	public UnosLeta(Prozor prozor, String message, AvioSistem sistem) {
		super(prozor, message);

		this.setLayout(new BorderLayout());

		String[] labels = { "Pocetni Aerodrom: ", "Krajnji Aerodrom: ", "Vreme: ", "Trajanje: " };
		int numPairs = labels.length;

		textField = new TextField[numPairs];
		
		Panel p = new Panel(new GridLayout(numPairs, 2, 6, 6));
		for (int i = 0; i < numPairs; i++) {
			Label l = new Label(labels[i], Label.RIGHT);
			p.add(l);
			textField[i] = new TextField(10);
			p.add(textField[i]);
		}

		this.add(BorderLayout.CENTER, p);

		Button btnPotvrdi = new Button("Potvrdi unos");
		btnPotvrdi.addActionListener(e -> {
			try {
				for (int i = 0; i < numPairs; i++) {
					if (textField[i].getText().trim().isEmpty()) {
						throw new Izuzetak("Sva polja moraju biti popunjena!");
					}
				}

				String srcAer = textField[0].getText();
				String dstAer = textField[1].getText();
				String time = textField[2].getText();
				int duration = Integer.parseInt(textField[3].getText());

				Let let = new Let(srcAer, dstAer, time, duration);
					
				sistem.dodajLet(let);
				prozor.osveziTabelu();
				
				System.out.println("Let uspesno upisan \n" + "Pocetni aerodrom: " + srcAer + "\n Krajni aerodrom: "
						+ dstAer + "\n Vreme: " + time + "\n Trajannje: " + duration + '\n');

				for (int i = 0; i < numPairs; i++) {
					textField[i].setText("");
				}
				
				dispose();

			} catch (NumberFormatException ex) {
				ValidacionaGreska greska = new ValidacionaGreska(prozor, "Trajanje leta mora biti broj");
				greska.setVisible(true);
			} catch (Izuzetak ex) {
				ValidacionaGreska greska = new ValidacionaGreska(prozor, ex.getMessage());
				greska.setVisible(true);
				//System.out.println("Greška validacije: " + ex.getMessage());
			}

		});

		this.add(BorderLayout.SOUTH, btnPotvrdi);

		this.pack();

		this.setLocationRelativeTo(prozor);
	}

}
