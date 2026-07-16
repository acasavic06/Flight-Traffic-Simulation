package Objekti;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import dijalozi.MojDijalog;
import dijalozi.Neaktivnost;
import dijalozi.ValidacionaGreska;
import projekatOO2.Prozor;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;

public class ActivateMonitor implements Runnable {
	private Thread nit;
	private volatile boolean radi = true;
	private volatile boolean pauziran = false;

	private Prozor prozor;
	private volatile boolean odbrojavanjeUToku = false;

	public ActivateMonitor(Prozor prozor) {
		this.prozor = prozor;
		nit = new Thread(this);
		nit.start();

		Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
			if (!odbrojavanjeUToku) {
				resetuj();
			}
		}, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK
				| AWTEvent.ACTION_EVENT_MASK);
	}

	public void resetuj() {
		if (nit != null) {
			nit.interrupt();
		}
	}

	public void zaustavi() {
		radi = false;
		resetuj();
	}

	@Override
	public void run() {
//		while (radi && !prozor.getAktivnaSimulacija()) {
		while (radi) {
			try {
//				while (pauziran && radi) {
				while ((pauziran || prozor.getAktivnaSimulacija()) && radi) {
					Thread.sleep(100);
				}
				Thread.sleep(55000);

				if (pauziran || prozor.getAktivnaSimulacija() || !radi) continue;
				
				odbrojavanjeUToku = true;

				Neaktivnost dijalog = new Neaktivnost(prozor, this);

				for (int i = 5; i > 0; i--) {
					if (pauziran || !radi) {
	                    break;
	                }
					final int sekunde = i;
					
					EventQueue.invokeLater(() -> {
						dijalog.odbrojavaj(sekunde);
						if (!dijalog.isVisible()) {
							dijalog.setVisible(true);
						}
					});

					System.out.println("Zatvaranje za " + i);
					Thread.sleep(1000);
				}

				EventQueue.invokeLater(() -> {
					dijalog.dispose();
					prozor.dispose();
					System.exit(0);
				});

			} catch (InterruptedException e) {

			}
		}
	}

	public void prekiniOdbrojavanje() {
		odbrojavanjeUToku = false;
		resetuj();
	}

	public void pauziraj() {
		pauziran = true;
		resetuj();
	}

	public void nastavi() {
		pauziran = false;
		resetuj();
	}

}