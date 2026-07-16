package Objekti;

import java.awt.Color;
import java.awt.Graphics;

import izuzeci.Izuzetak;

public class Let {
	private String srcAer;
	private String dstAer;
	private String time;
	private int duration;
	
	private double currX, currY;
	private boolean uVazduhu;
	private boolean zavrsen;
	private boolean uCekanju;
	private int stvarnoVremePoletanja=0;

	
	public Let(String src, String dst, String t, int d) throws Izuzetak {
		this.srcAer = src;
		this.dstAer = dst;
		this.time = t;
		this.duration = d;

		proveraValidnosti();
	}

	void proveraValidnosti() throws Izuzetak {

		if (srcAer.length() != 3 || !srcAer.matches("[A-Z]{3}"))
			throw new Izuzetak("Pocetni aerodroma mora imati tacno 3 VELIKA slova");
		if (dstAer.length() != 3 || !dstAer.matches("[A-Z]{3}"))
			throw new Izuzetak("Krajnji aerodroma mora imati tacno 3 VELIKA slova");
		if (dstAer.equals(srcAer))
			throw new Izuzetak("Ne mogu pocetni i krajnji aerodrom da budu isti");
		if (time.length() != 5 || time.charAt(2) != ':')
			throw new Izuzetak("Vreme mora biti u formatu hh:mm");

		if (duration<0)throw new Izuzetak("Trajanje leta mora biti vece od 0");

		try {
			int hour = Integer.parseInt(time.substring(0, 2));
			int minut = Integer.parseInt(time.substring(3, 5));

			if (hour >= 24 || hour < 0)
				throw new Izuzetak("Sati moraju biti izmedju 0 i 23");
			if (minut >= 60 || minut < 0)
				throw new Izuzetak("Minuti moraju biti izmedju 0 i 59");
		} catch (NumberFormatException e) {
			throw new Izuzetak("Sati i minuti u vremenu moraju biti brojevi!");
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof Let)) return false;
		Let l = (Let) obj;
		return this.srcAer.equals(l.srcAer) && this.dstAer.equals(l.dstAer) && this.time.equals(l.time) && this.duration == l.duration;
	}

	public String getSrc() {return this.srcAer;}
	public String getDst() {return this.dstAer;}
	public String getTime() {return time;}
	public int getDuration() {return duration;}
	
	public int convertTime() {
		return Integer.parseInt(time.substring(3, 5)) + 
				60*Integer.parseInt(time.substring(0, 2));
	}
	
	public void crtaj(Graphics g, int x, int y) {
		if (!uVazduhu)return;
		g.setColor(Color.BLUE);
		g.fillOval(x-3,y-3,6,6);		
	}
	
	public double getCurrX() {
		return currX;
	}

	public void setCurrX(double currX) {
		this.currX = currX;
	}

	public double getCurrY() {
		return currY;
	}

	public void setCurrY(double currY) {
		this.currY = currY;
	}

	public int getStvarnoVremePoletanja() {
		return stvarnoVremePoletanja;
	}

	public void setStvarnoVremePoletanja(int stvarnoVremePoletanja) {
		this.stvarnoVremePoletanja = stvarnoVremePoletanja;
	}

	public boolean isuVazduhu() {
		return uVazduhu;
	}

	public void setuVazduhu(boolean uVazduhu) {
		this.uVazduhu = uVazduhu;
	}

	public boolean isZavrsen() {
		return zavrsen;
	}

	public void setZavrsen(boolean zavrsen) {
		this.zavrsen = zavrsen;
	}

	public boolean isuCekanju() {
		return uCekanju;
	}

	public void setuCekanju(boolean uCekanju) {
		this.uCekanju = uCekanju;
	}
	
}
