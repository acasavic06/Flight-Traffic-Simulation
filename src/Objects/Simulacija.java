package Objekti;

import projekatOO2.Prozor;

public class Simulacija implements Runnable {

	private Prozor prozor;
	private volatile int vreme;
	private volatile boolean radi = false;
	private volatile boolean pauza = false;

	public Simulacija(Prozor prozor) {
		this.prozor = prozor;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {

				if (radi && !pauza) {

					synchronized (prozor.getSistem()) {
						prozor.getSistem().azurirajSimulaciju(vreme);

						vreme += 20;
					}
					
//					if (vreme>1440) {resetSimulacije();
//					radi=true;}
					
					java.awt.EventQueue.invokeLater(() -> {
	                    prozor.getLabel().setText(
	                        String.format("Vreme simulacije: %02d:%02d",
	                            (vreme / 60) % 24,
	                            vreme % 60)
	                    );
	                    prozor.getCrtez().repaint();
	                });
				}
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			radi = false;
		}

	}

	public void setRadi(boolean radi) {
		this.radi = radi;
	}

	public void pauziraj(boolean p) {
		pauza = p;
	}

	public void resetSimulacije() {
		synchronized (prozor.getSistem()) {
			vreme = 0;
			radi = false;
			pauza = false;

			for (Let l : prozor.getSistem().getLetovi()) {
				l.setZavrsen(false);
				l.setuVazduhu(false);
				l.setuCekanju(false);
				l.setCurrX(0);
				l.setCurrY(0);
				l.setStvarnoVremePoletanja(0);
			}

			for (Aerodrom a : prozor.getAerodromi()) {
				a.setZauzet(false);
				a.setVremePoslednjegPoletanja(-10);
				a.getListaCekanja().clear();
			}
		
			prozor.getLabel().setText("Vreme simulacije: 00:00");
			prozor.getCrtez().resetujSelekciju();
		}	
	}	
}
