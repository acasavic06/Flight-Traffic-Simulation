package formatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objekti.Aerodrom;
import Objekti.AvioSistem;
import Objekti.Let;
import izuzeci.Izuzetak;

public class JSONFormatter extends Formatter {

	public JSONFormatter(String filename, AvioSistem sistem) {
		super(filename, sistem);
	}

	@Override
	public void sacuvaj() throws Izuzetak {
		provera();
		try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
			pw.println("{");
			pw.println("\"airports\":[");
			for (Aerodrom a : sistem.getAerodromi()) {
				pw.println("{\"code\":\"" + a.getCode() + "\",\"name\":\"" + a.getName() + "\",\"x\":" + a.getX()
						+ ",\"y\":" + a.getY() + "},");
			}

			pw.println("],");
			pw.println("\"flights\":[");
			for (Let l : sistem.getLetovi()) {
				pw.println("{\"from\":\"" + l.getSrc() + "\",\"to\":\"" + l.getDst() + "\",\"departure\":\""
						+ l.getTime() + "\",\"duration\":" + l.getDuration() + "},");
			}
			pw.println("]");
			pw.println("}");

		} catch (IOException e) {
			throw new Izuzetak("Greska pri cuvanju fajla");
		}

	}

	@Override
	public void ucitaj() throws Izuzetak {
		provera();
		clearSistem();
		String line = null;
		boolean ucitajAerodrom = false;
		boolean ucitajLet = false;
		

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			Pattern pAerodrom = Pattern
					.compile("\"code\":\"([^\"]+)\".*?\"name\":\"([^\"]+)\".*?\"x\":(-?\\d+).*?\"y\":(-?\\d+)");
			Pattern pLet = Pattern.compile(
					"\"from\":\"([^\"]+)\".*?\"to\":\"([^\"]+)\".*?\"departure\":\"([^\"]+)\".*?\"duration\":(\\d+)");
			Matcher mA;
			Matcher mL;

			while ((line = br.readLine())!=null) {
				line = line.trim();
				if (line.equals("{") ||
				        line.equals("}") ||
				        line.equals("],") ||
				        line.equals("]")) {
				        continue;
				    }
				
				if (line.equals("\"airports\":[")) ucitajAerodrom = true;

				if (line.equals("\"flights\":[")) {
					ucitajLet= true; 
					ucitajAerodrom = false;
				}
 
				
				mA = pAerodrom.matcher(line);
				mL = pLet.matcher(line);

				if (ucitajAerodrom && mA.find()) {
					String code = mA.group(1);
					String name = mA.group(2);
					double x = Double.parseDouble(mA.group(3));
					double y = Double.parseDouble(mA.group(4));

					Aerodrom a = new Aerodrom(code, name, x, y);

					this.sistem.dodajAerodrom(a);
					System.out.println(line);
					
				}

				if (ucitajLet && mL.find()) {
					String src = mL.group(1);
					String dst = mL.group(2);
					String time = mL.group(3);
					int duration = Integer.parseInt(mL.group(4));

					Let l = new Let(src, dst, time, duration);

					this.sistem.dodajLet(l);
					System.out.println(line);
				}

			}

		} catch (FileNotFoundException e) {
			throw new Izuzetak("Fajl " + filename + " ne postoji");
		} catch (IOException e) {
			throw new Izuzetak("Greska pri otvaranju fajla");
		} catch (NumberFormatException ex) {
			throw new Izuzetak("X i Y koordinata moraju biti brojevi" + line);
		}
	}

	@Override
	public void provera() throws Izuzetak {
		int duzina = filename.length();
		if (duzina < 6)
			throw new Izuzetak("Naziv fajla nije validan");
		String kraj = filename.substring(duzina - 5, duzina);
		if (!kraj.equals(".json"))
			throw new Izuzetak("Fajl mora da se zavrsava sa .csv");
	}

}
