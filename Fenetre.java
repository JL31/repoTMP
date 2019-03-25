import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public class Fenetre extends JFrame {
	
	// D�claration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private PartieCentrale conteneurPartieCentrale;
	private PanneauLateral conteneurPartieDroite = new PanneauLateral();
	private String nomDuBoutonUtilise;
	
	
	// Constructeur de la fen�tre principale
	public Fenetre()
	{
		// Param�tres globaux de la fen�tre principale
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);			// pour mettre l'application en plein �cran 
	    this.setUndecorated(true);								// pour enlever la barre du haut contenant les ic�nes r�duire, agrandir et fermer (permet vraiment le passage en plein �cran)
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // Initialisation --- temporaire
	    conteneurPartieCentrale = new PartieCentraleMois();
	    
	    // R�cup�ration de la date actuelle
	    LocalDateTime dateActuelle = LocalDateTime.now();
	    int mois = dateActuelle.getMonthValue();
	    
	    // Ecoute des boutons du panneau lat�ral
	    for (JButton element: conteneurPartieDroite.getListeDesBoutons())
	    {
	    	element.addActionListener(new RecupererIdentifiantBouton());
	    }
	    
	    // Param�trage et alimentation du conteneur de la fen�tre princiaple
	    conteneurGlobal.setBackground(Color.gray);
	    conteneurGlobal.setLayout(new BorderLayout());
	    conteneurGlobal.add(conteneurPartieCentrale.getConteneurGlobal(), BorderLayout.CENTER);
	    conteneurGlobal.add(conteneurPartieDroite.getConteneurPartieDroite(), BorderLayout.EAST);
	    
	    // Ajout d'un raccourci clavier pour quitter l'application
//	    KeyStroke combinaisonTouchesQuitter = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
	    KeyStroke combinaisonTouchesQuitter = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	    this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(combinaisonTouchesQuitter, "Quitter");
	    this.getRootPane().getActionMap().put("Quitter", new actionQuitter());
	    
	    // D�finition du content pane de la fen�tre principale
	    this.setContentPane(conteneurGlobal);
	    
	    // Affichage de la fen�tre
	    this.setVisible(true); 
	}

	// Mise-�-jour de l'affichage
	public void actualiserAffichage()
	{
		conteneurGlobal.add(conteneurPartieCentrale.getConteneurGlobal(), BorderLayout.CENTER);
		
		this.setContentPane(conteneurGlobal);	// mise-�-jour du content pane de la fen�tre principale
		this.repaint();							// re-dessine la fen�tre
		this.revalidate();						// en compl�ment de repaint pour indiquer au layout manager de faire un reset sur la base des nouveaux composants
	}
	
	// D�finition des actions --- via des classes internes
	class actionQuitter extends AbstractAction
	{
		public void actionPerformed(ActionEvent actionEvent)
		{
			System.exit(0);
		}
	}
	
	// Actions via des classes internes
	class RecupererIdentifiantBouton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			nomDuBoutonUtilise = ((JButton)e.getSource()).getActionCommand();
			
			if (nomDuBoutonUtilise.equals("Bilan"))
			{
				conteneurGlobal.remove(conteneurPartieCentrale.getConteneurGlobal());
				conteneurPartieCentrale = new PartieCentraleBilan();
				Fenetre.this.actualiserAffichage();
			}
			else
			{
				conteneurGlobal.remove(conteneurPartieCentrale.getConteneurGlobal());
				conteneurPartieCentrale = new PartieCentraleMois();
				Fenetre.this.actualiserAffichage();
			}
			
		}
	}

}
