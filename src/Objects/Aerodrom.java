package Objekti;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;

import izuzeci.Izuzetak;

public class Aerodrom {
	private String name;
	private String code;
	private double x;
	private double y;
	private boolean vidljiv = true;
	
	private Queue<Let> redCekanja;
	private int vremePoslednjegPoletanja = -10;
	private boolean zauzet = false;
	
	public Aerodrom(String code, String name, double x, double y) throws Izuzetak  {
		this.code=code;
		this.name=name;
		this.x=x;
		this.y=y;

		proveraValidnosti();

		redCekanja = new LinkedList<>();
	}
	
	void proveraValidnosti() throws Izuzetak {
		if (code.length() != 3)
			throw new Izuzetak("Kod aerodroma mora imati tacno 3 slova");
		if (!code.matches("[A-Z]{3}")) 
		    throw new Izuzetak("Kod aerodroma mora sadržati tačno 3 VELIKA SLOVA");
		if (!name.matches("[a-zA-Z ]+")) 
		    throw new Izuzetak("Naziv aerodroma mora sadržati samo slova");
		if (x > 180 || x < -180)
			throw new Izuzetak("X koordinata aerodroma mora biti izmedju -180 i 180");
		if (y > 90 || y < -90)
			throw new Izuzetak("Y koordinata aerodroma mora biti izmedju -90 i 90");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==null) return false;
		if (obj==this) return true;
		if(!(obj instanceof Aerodrom))return false;
		Aerodrom a = (Aerodrom)obj;
		return this.code.equals(a.code) && this.name.equals(a.name) && this.x==a.x && this.y == a.y;
	}
	
	public String getName() {return this.name;}
	public String getCode() {return this.code;}
	public double getX() {return this.x;}
	public double getY() {return this.y;}

	public void crtaj(Graphics g,int x,int y, boolean treperi) {
		g.setColor(Color.GRAY);
		if (treperi) {
			g.setColor(Color.RED);
		}
        g.fillRect(x - 4, y - 4, 8, 8);

//        g.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        g.setColor(Color.BLACK);
        g.drawString(this.getCode(), x + 6, y);
	}
	
	public boolean pripada(int xOsa, int yOsa) {
		return xOsa>=x-4 && xOsa<=x+4 && yOsa>=y-4 && yOsa<=y+4;
	}

	public boolean isVidljiv() {
		return vidljiv;
	}

	public void setVidljiv(boolean vidljiv) {
		this.vidljiv = vidljiv;
	}
	
	public boolean isZauzet() {
		return zauzet;
	}


	public void setZauzet(boolean zauzet) {
		this.zauzet = zauzet;
	}

	public Queue<Let> getListaCekanja() {
		return redCekanja;
	}

	public void dodaj(Let l) {
		if (!redCekanja.contains(l))
			redCekanja.add(l);
	}

	public int getVremePoslednjegPoletanja() {
		return vremePoslednjegPoletanja;
	}

	public void setVremePoslednjegPoletanja(int waitingTime) {
		this.vremePoslednjegPoletanja = waitingTime;
	}
	
	public void azurirajCekanje(int time){
		if (zauzet && time - vremePoslednjegPoletanja >= 10) {
	        zauzet = false;
	    }

		
		if (redCekanja==null || redCekanja.isEmpty()) return;
		if (zauzet) return;
		
		
		Let l = redCekanja.poll();
	
		zauzet=true;
		vremePoslednjegPoletanja = time;
		
		l.setuCekanju(false);
		l.setuVazduhu(true);
		l.setStvarnoVremePoletanja(time);
		l.setCurrX(x);
		l.setCurrY(y);
	}
	
	
}
