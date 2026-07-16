package dijalozi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import Objekti.Aerodrom;
import Objekti.AvioSistem;
import izuzeci.Izuzetak;
import projekatOO2.Prozor;

public class UnosAerodroma extends MojDijalog {

	private TextField textField[];
	

	public UnosAerodroma(Prozor prozor, String message, AvioSistem sistem) {
		super(prozor, message);
		
		this.setLayout(new BorderLayout());

		String[] labels = { "Kod: ", "Naziv: ", "X: ", "Y: " };
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

				String code = textField[0].getText();
				String name = textField[1].getText();
				double x = Double.parseDouble(textField[2].getText());
				double y = Double.parseDouble(textField[3].getText());

				Aerodrom aerodrom = new Aerodrom(code, name, x, y);

				sistem.dodajAerodrom(aerodrom);
				prozor.osveziTabelu();

				System.out.println("Aerodrom uspesno kreiran\n" + "Naziv: " + name + "\n X: " + x + "\n Y: " + y + '\n');

				for (int i = 0; i < numPairs; i++) {
					textField[i].setText("");
				}

				dispose();

			} catch (NumberFormatException ex) {
				ValidacionaGreska greska = new ValidacionaGreska(prozor, "X i Y koordinata moraju biti broj");
				greska.setVisible(true);
			} catch (Izuzetak ex) {
				ValidacionaGreska greska = new ValidacionaGreska(prozor, ex.getMessage());
				greska.setVisible(true);
				// System.out.println("Greška validacije: " + ex.getMessage());
			}

		});

		this.add(BorderLayout.SOUTH, btnPotvrdi);

		this.pack();

		this.setLocationRelativeTo(prozor);
	}

}
