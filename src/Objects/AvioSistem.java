package Objekti;

import java.util.ArrayList;
import java.util.List;

import izuzeci.Izuzetak;

public class AvioSistem {

	private List<Aerodrom> aerodromi;
	private List<Let> letovi;
	
	public List<Aerodrom> getAerodromi() {
		return aerodromi;
	}

	public List<Let> getLetovi() {
		return letovi;
	}
	
	public void clearSistem() {
		aerodromi.clear();
		letovi.clear();;
	}


	public AvioSistem() {
		aerodromi = new ArrayList<Aerodrom>();
		letovi = new ArrayList<Let>();
	}

	public void dodajAerodrom(Aerodrom a) throws Izuzetak {
		validirajNoviAerodrom(a.getCode());
		aerodromi.add(a);
	}
	
	public void dodajLet(Let l) throws Izuzetak {
		validirajNoviLet(l);
		letovi.add(l);
	}
	

	private void validirajNoviLet(Let l) throws Izuzetak {
		if (!postojiAerodrom(l.getSrc()))
			throw new Izuzetak("Aerodrom sa nazivom "+ l.getSrc()+ " ne postoji, ne mozete da dodati let sa ovim aerodromom");
		if (!postojiAerodrom(l.getDst()))
			throw new Izuzetak("Aerodrom sa nazivom "+ l.getDst()+ " ne postoji, ne mozete da dodati let sa ovim aerodromom");
		if(postojiLet(l))
			throw new Izuzetak("Let sa ovim podacima vec postoji");
	}
	
	private void validirajNoviAerodrom(String code) throws Izuzetak {
		for (Aerodrom a : aerodromi) {
			if (a.getCode().equals(code)) {
				throw new Izuzetak("Aerodrom sa kodom " + code + " vec postoji, ne mozete dodati novi");
			}
		}
	}
	
	private boolean postojiLet(Let let) {
		for (Let l : letovi) {
			if (l.equals(let)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean postojiAerodrom(String code){
		return nadjiAerodrom(code)!=null;
//		for (Aerodrom a : aerodromi) {
//			if (a.getCode().equals(code)) {
//				return true;
//			}
//		}
//		return false;
	}
	
	private Aerodrom nadjiAerodrom(String code){
		Aerodrom aerodrom=null;
		for (Aerodrom a : aerodromi) {
			if (a.getCode().equals(code)) {
				aerodrom = a;
				break;
			}
		}
		return aerodrom;
	}
	
	public void azurirajSimulaciju(int time) {
		for (Aerodrom a : aerodromi) {
			a.azurirajCekanje(time);
		}
		
		for (Let l : letovi) {
			if (l.isZavrsen())continue;
			
			if (l.isuVazduhu()) {
				azuriranjeUVazduhu(time, l);
			}
			else if(!l.isuCekanju() && !l.isuVazduhu() && l.convertTime()<=time) {
				poletanjeAviona(time, l);
			}
		}
	};
	
	public void poletanjeAviona(int time, Let l) {
		Aerodrom a = nadjiAerodrom(l.getSrc());
		if (!a.isZauzet()) {
			a.setZauzet(true);
			a.setVremePoslednjegPoletanja(time);
			l.setZavrsen(false);
			l.setuCekanju(false);
			l.setuVazduhu(true);
			l.setStvarnoVremePoletanja(time);
			l.setCurrX(a.getX());
			l.setCurrY(a.getY());
		}else {
			a.dodaj(l);
			l.setuCekanju(true);
		}
	}
	
	
	public void azuriranjeUVazduhu(int time, Let l) {
		Aerodrom src = nadjiAerodrom(l.getSrc());
		Aerodrom dst = nadjiAerodrom(l.getDst());
		
		double procenat = (time - l.getStvarnoVremePoletanja()) / (double) l.getDuration();

		l.setCurrX(src.getX() + procenat*(dst.getX()-src.getX()));
		l.setCurrY(src.getY() + procenat*(dst.getY()-src.getY()));	
		
		if (procenat>=1) {
			l.setZavrsen(true);
			l.setuVazduhu(false);
			l.setCurrX(dst.getX());
			l.setCurrY(dst.getY());	
		}
	}	
	
//	public boolean postojiAerodrom(Aerodrom aerodrom) {
//		for (Aerodrom a : aerodromi) {
//			if (a.equals(aerodrom)) {
//				return true;
//			}
//		}
//		return false;
//	}
	
}


