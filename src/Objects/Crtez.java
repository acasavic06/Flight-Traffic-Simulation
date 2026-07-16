package Objekti;

import java.awt.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import projekatOO2.Prozor;

public class Crtez extends Canvas implements Runnable {

	private Prozor prozor;
	private Aerodrom selektovan;
	private boolean treperi;

	private Image buffer;
	private Graphics bufferGraphics;

	public Crtez(Prozor prozor) {
		this.prozor = prozor;
		this.setBackground(Color.PINK);
//		this.setSize(360, 180);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				System.out.println(e.getX() + " " + e.getY());
				boolean nasao=false; 
				
				for (Aerodrom a : prozor.getAerodromi()) {
					if (a.isVidljiv() && pripadaAerodromu(a, e.getX(), e.getY())) {
//						System.out.println(a.getCode());
						
						if (selektovan == a) {
							prozor.getMonitor().nastavi();
							;
							selektovan = null;
						} else {
							selektovan = a;
							prozor.getMonitor().pauziraj();
//							System.out.println(a.getCode());
						}
						repaint();
						break;

					}
				}
			}
		});
		
		

	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				if (selektovan != null) {
					treperi = !treperi;
					repaint();
				}
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {

		}

	}

//	@Override
//	public void update(Graphics g) {
//		paint(g);
//	}

	@Override
	public void paint(Graphics g) {

//		if (buffer == null || buffer.getWidth(null) != getWidth() || buffer.getHeight(null) != getHeight()) {
//			buffer = createImage(getWidth(), getHeight());
//			bufferGraphics = buffer.getGraphics();
//		}
//		bufferGraphics.setColor(Color.PINK);
//		bufferGraphics.fillRect(0, 0, getWidth(), getHeight());

		synchronized (prozor.getSistem()) {
			for (Aerodrom a : prozor.getAerodromi()) {
				if (!a.isVidljiv())
					continue;
				int x = mapirajX(a.getX());
				int y = mapirajY(a.getY());

				if (a == selektovan && treperi) {
					a.crtaj(g, x, y, true);
				} else {
					a.crtaj(g, x, y, false);
				}
			}

			for (Let l : prozor.getSistem().getLetovi()) {
				if (!l.isuVazduhu())
					continue;
				int x = mapirajX(l.getCurrX());
				int y = mapirajY(l.getCurrY());

				l.crtaj(g, x, y);
			}
		}


//		g.drawImage(buffer,0,0,null);

	}

	private int mapirajX(double x) {
		return (int) ((x + 180) / 360 * getWidth());
	}

	private int mapirajY(double y) {
		return (int) ((90 - y) / 180 * getHeight());
	}

	private boolean pripadaAerodromu(Aerodrom a, int mx, int my) {
		int x = mapirajX(a.getX());
		int y = mapirajY(a.getY());

		return mx >= x - 4 && mx <= x + 4 && my >= y - 4 && my <= y + 4;
	}

	public void resetujSelekciju() {
		selektovan = null;
		treperi = false;
	}

}
