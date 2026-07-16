package formatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Objekti.Aerodrom;
import Objekti.AvioSistem;
import Objekti.Let;
import izuzeci.Izuzetak;

public class CSVFormatter extends Formatter {

	public CSVFormatter(String filename,AvioSistem sistem) {
		super(filename,sistem);
	}

	@Override
	public void sacuvaj() throws Izuzetak {
		provera();
		try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
			pw.println("# AIRPORTS");
			pw.println("CODE,NAME,X,Y");
			for (Aerodrom a : sistem.getAerodromi()) {
				pw.println( a.getCode() + "," + a.getName() +  "," + a.getX() + "," + a.getY());
			}
			
			pw.println("");
			pw.println("# FLIGHTS");
			pw.println("FROM,TO,DEPARTURE,DURATION");
			for (Let l : sistem.getLetovi()) {
				pw.println( l.getSrc() + "," + l.getDst() + "," + l.getTime() + "," + l.getDuration());
			}
			
		} catch (IOException e) {
			throw new Izuzetak("Greska pri cuvanju fajla");
		}
	}

	@Override
	public void ucitaj() throws Izuzetak {
		provera();
		clearSistem();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))){
			String line;
			line=br.readLine();
			line=br.readLine();
			
			while ((line=br.readLine())!=null){
				
				if (line.isEmpty()) {
				    continue;
				}

				if (line.equals("# FLIGHTS")) {
				    break;
				}
				
				String parts[] = line.split(",");
				if (parts.length != 4) throw new Izuzetak("Avion mora da sadrzi code,name,x,y u ovom redosledu");
				String code = parts[0];
				String name = parts[1];
				double x = Double.parseDouble(parts[2]);
				double y = Double.parseDouble(parts[3]);
				
				Aerodrom a = new Aerodrom(code, name, x, y);
				
				this.sistem.dodajAerodrom(a);
				System.out.println(line);
				
			}
			
			if (line != null && line.trim().isEmpty()) {
			    line = br.readLine();
			}
			
			line=br.readLine();
			//line=br.readLine();
			
			while ((line=br.readLine())!=null){
				if (line.trim().isEmpty())continue;
				
				String parts[] = line.split(",");
				if (parts.length != 4) throw new Izuzetak("Let mora da sadrzi src,dst,time,duration u ovom redosledu");
				String src = parts[0];
				String dst = parts[1];
				String time = parts[2];
				int duration = Integer.parseInt(parts[3]);
				
				Let l = new Let(src, dst, time, duration);
				
				this.sistem.dodajLet(l);
				System.out.println(line);
			}
			
		}catch (FileNotFoundException e) {
			throw new Izuzetak("Fajl " +  filename + " ne postoji");
		}catch (IOException e) {
			throw new Izuzetak("Greska pri otvaranju fajla");
		} catch (NumberFormatException ex) {
			throw new Izuzetak("X i Y koordinata moraju biti brojevi");
		}

	}

	@Override
	public void provera() throws Izuzetak {
		int duzina = filename.length();
		if (duzina<5) throw new Izuzetak("Naziv fajla nije validan");
		String kraj = filename.substring(duzina-4,duzina);
		if (!kraj.equals(".csv")) throw new Izuzetak("Fajl mora da se zavrsava sa .csv");
	}

}
