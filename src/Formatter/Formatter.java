package formatter;

import Objekti.AvioSistem;
import izuzeci.Izuzetak;

abstract public class Formatter {

	protected String filename;
	protected AvioSistem sistem;
	
	public Formatter(String filename, AvioSistem sistem) {
		this.filename = filename;
		this.sistem = sistem;
	}

	public abstract void sacuvaj() throws Izuzetak;
	public abstract void ucitaj () throws Izuzetak;
	public abstract void provera() throws Izuzetak;

	
	public void clearSistem (){
		sistem.clearSistem();
	}
}
