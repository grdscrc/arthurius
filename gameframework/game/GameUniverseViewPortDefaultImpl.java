package gameframework.game;

import gameframework.base.BackgroundImage;
import gameframework.base.Drawable;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;

public class GameUniverseViewPortDefaultImpl implements GameUniverseViewPort {
	private Image buffer;
	private Graphics bufferGraphics;
	protected Canvas canvas;
	protected BackgroundImage background;
	protected GameUniverse universe;

	public GameUniverseViewPortDefaultImpl(Canvas canvas, GameUniverse universe) {
		this.canvas = canvas;
		buffer = canvas.createImage(canvas.getWidth(), canvas.getHeight());
		bufferGraphics = buffer.getGraphics();
		bufferGraphics.setFont(new Font("Arial", Font.BOLD, 32));
		background = new BackgroundImage(backgroundImage(), canvas);
		this.universe = universe;
	}

	protected String backgroundImage() {
		return "images/background_image.gif";
	}

	public void setBackground(String filename) {
		background = new BackgroundImage(filename, canvas);
	}

	public void paint() {
		background.draw(bufferGraphics);
		Iterator<GameEntity> gt = universe.gameEntities();
		while (gt.hasNext()) {
			GameEntity tmp = gt.next();
			if (tmp instanceof Drawable) {
				((Drawable) tmp).draw(bufferGraphics);
			}
		}
		refresh();
	}

	public void refresh() {
		canvas.getGraphics().drawImage(buffer, 0, 0, canvas.getWidth(),
				canvas.getHeight(), canvas);
	}


	public void display(String s, int x, int y) {
		bufferGraphics.drawString(s, x, y);
		refresh();
	}
}
