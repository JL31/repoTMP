import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class Fenetre extends JFrame
{
	
	// D�claration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private PartieCentrale conteneurPartieCentrale;
	private PanneauLateral conteneurPartieDroite = new PanneauLateral();
	private String nomDuBoutonUtilise;
	private ContenuDufichierDeDonnees donnees;
	private Hashtable<Integer, String> dicoCorrespondanceDesMois = new Hashtable<Integer, String>();
	private String nomDuFichierACharger;
	private JOptionPane jop1 = new JOptionPane();
	
	// Initialisation de certaines variables d'instance
	private void initialisationVariables()
	{
		dicoCorrespondanceDesMois.put(1, "Janvier");
		dicoCorrespondanceDesMois.put(2, "F�vrier");
		dicoCorrespondanceDesMois.put(3, "Mars");
		dicoCorrespondanceDesMois.put(4, "Avril");
		dicoCorrespondanceDesMois.put(5, "Mai");
		dicoCorrespondanceDesMois.put(6, "Juin");
		dicoCorrespondanceDesMois.put(7, "Juillet");
		dicoCorrespondanceDesMois.put(8, "Ao�t");
		dicoCorrespondanceDesMois.put(9, "Septembre");
		dicoCorrespondanceDesMois.put(10, "Octobre");
		dicoCorrespondanceDesMois.put(11, "Novembre");
		dicoCorrespondanceDesMois.put(12, "D�cembre");
	}
	
	// Constructeur de la fen�tre principale (avec arguments)
	public Fenetre(ContenuDufichierDeDonnees donnees, String nomDuFichierACharger)
	{
		// Initialisations des variables d'instance
		this.donnees = donnees;
		this.nomDuFichierACharger = nomDuFichierACharger;
		initialisationVariables();
		
		// Param�tres globaux de la fen�tre principale
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);			// pour mettre l'application en plein �cran 
	    this.setUndecorated(true);								// pour enlever la barre du haut contenant les ic�nes r�duire, agrandir et fermer (permet vraiment le passage en plein �cran)
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // R�cup�ration de la date actuelle
	    LocalDateTime dateActuelle = LocalDateTime.now();
	    int mois = dateActuelle.getMonthValue();
	    String moisStr = dicoCorrespondanceDesMois.get(mois);
	    
	    // Initialisations
	    conteneurPartieCentrale = new PartieCentraleMois(donnees, moisStr);
	    remplissageBandeauInformation();
	    
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
	    
	    // Ajout d'un raccourci clavier pour l'enregistrement
	    KeyStroke combinaisonTouchesEnregistrer = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
	    this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(combinaisonTouchesEnregistrer, "Enregistrer");
	    this.getRootPane().getActionMap().put("Enregistrer", new actionEnregistrer());
	    
	    // D�finition du content pane de la fen�tre principale
	    this.setContentPane(conteneurGlobal);
	    
	    // Affichage de la fen�tre
	    this.setVisible(true); 
	}
	
	// M�thode qui permet de remplir les JLabel du bandeau d'informations g�n�rales
	public void remplissageBandeauInformation()
	{
		conteneurPartieCentrale.getInformationsGenerales().setLabelfichierCharge(nomDuFichierACharger);
	    conteneurPartieCentrale.getInformationsGenerales().setLabelDateMAJ(donnees.getDateDeMiseAJour());
	}
	
	// Mise-�-jour de l'affichage
	public void actualiserAffichage()
	{
		conteneurGlobal.add(conteneurPartieCentrale.getConteneurGlobal(), BorderLayout.CENTER);
		
		this.setContentPane(conteneurGlobal);	// mise-�-jour du content pane de la fen�tre principale
		this.repaint();							// re-dessine la fen�tre
		this.revalidate();						// en compl�ment de repaint pour indiquer au layout manager de faire un reset sur la base des nouveaux composants
		remplissageBandeauInformation();
	}
	
	// Action pour quitter l'application (classe interne)
	class actionQuitter extends AbstractAction
	{
		public void actionPerformed(ActionEvent actionEvent)
		{
			System.exit(0);
		}
	}
	
	// Action pour enregistrer les donn�es
	class actionEnregistrer extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			donnees.enregistrementDesDonnees();
			jop1.showMessageDialog(null, "Donn�es enregistr�es", "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	// Actions suite au clic sur l'un des boutons (classe interne)
	class RecupererIdentifiantBouton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			nomDuBoutonUtilise = ((JButton)e.getSource()).getActionCommand();
			
			if (nomDuBoutonUtilise.equals("Bilan"))
			{
				conteneurGlobal.remove(conteneurPartieCentrale.getConteneurGlobal());
				conteneurPartieCentrale = new PartieCentraleBilan(donnees);
				Fenetre.this.actualiserAffichage();
			}
			else
			{
				conteneurGlobal.remove(conteneurPartieCentrale.getConteneurGlobal());
				conteneurPartieCentrale = new PartieCentraleMois(donnees, nomDuBoutonUtilise);
				Fenetre.this.actualiserAffichage();
			}
			
		}
	}

}
