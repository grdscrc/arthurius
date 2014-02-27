package gameplay;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;

import gameframework.game.GameDefaultImpl;

public class ArthuriusTheGame extends GameDefaultImpl {

	public ArthuriusTheGame() {
		super();
	}

	public void createIntro(){
		final Frame intro = new Frame("La quête d'Arthurius ; Pourquoi ?  ");
		intro.dispose();
		JLabel Explication=new JLabel("<HTML><center>"+
				"Bienvenue dans la quête d'Arthurius !<br>" +
				"Arthurius, prince et héritier de la " +
				"couronne des rois du pays de la Reprizek <br> s'est vu attribuer une " +
				"quête extraordinaire par le grand sorcier Zidoulf. " +
				" Le <br>but de sa quête est de tuer le grand dragon Dracomaug, mais " +
				"pour cela il doit<br> récupérer des équipements perdus tel que les bottes " +
				"du destin, le bonbon de la surpuissance<br> ou encore l'épée de la vérité. " +
				"Sa quête sera pleine de pièges et d'ennemis,<br>alors aidez le à tuer ces " +
				"monstres redoutables et à gagner le coeur de la princesse Atra !<br></HTML>");
		intro.add(Explication, BorderLayout.SOUTH);
		intro.pack();
		//intro.setVisible(true);
		
		intro.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				intro.setVisible(false);
			}
		});
	}

}
