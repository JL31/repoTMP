import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Fenetre extends JFrame
{
	
	// Déclaration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private PartieCentrale conteneurPartieCentrale;
	private PanneauLateral conteneurPartieDroite = new PanneauLateral();
	private String nomDuBoutonUtilise;
	private ContenuDufichierDeDonnees donnees;
	private Hashtable<Integer, String> dicoCorrespondanceDesMois = new Hashtable<Integer, String>();
	private Object[][] donneesCredits;
	private Object[] enTetesCredits;
	private Object[][] donneesDebits;
	private Object[] enTetesDebits;
	private Object[][] donneesBilan;
	private Object[] enTetesBilan; 
	
	// Initialisation de certaines variables d'instance
	private void initialisationVariables()
	{
		dicoCorrespondanceDesMois.put(1, "Janvier");
		dicoCorrespondanceDesMois.put(2, "Février");
		dicoCorrespondanceDesMois.put(3, "Mars");
		dicoCorrespondanceDesMois.put(4, "Avril");
		dicoCorrespondanceDesMois.put(5, "Mai");
		dicoCorrespondanceDesMois.put(6, "Juin");
		dicoCorrespondanceDesMois.put(7, "Juillet");
		dicoCorrespondanceDesMois.put(8, "Août");
		dicoCorrespondanceDesMois.put(9, "Septembre");
		dicoCorrespondanceDesMois.put(10, "Octobre");
		dicoCorrespondanceDesMois.put(11, "Novembre");
		dicoCorrespondanceDesMois.put(12, "Décembre");
	}
	
	// Constructeur de la fenêtre principale (avec arguments)
	public Fenetre(ContenuDufichierDeDonnees donnees, String nomDuFichierACharger)
	{
		// Initialisations des variables d'instance
		this.donnees = donnees;
		initialisationVariables();
		
		// Paramètres globaux de la fenêtre principale
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);			// pour mettre l'application en plein écran 
	    this.setUndecorated(true);								// pour enlever la barre du haut contenant les icônes réduire, agrandir et fermer (permet vraiment le passage en plein écran)
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
//	    // Récupération de la date actuelle
	    LocalDateTime dateActuelle = LocalDateTime.now();
	    int mois = dateActuelle.getMonthValue();
	    recuperationDesDonnees(dicoCorrespondanceDesMois.get(mois));
	    
	    // Initialisation
	    conteneurPartieCentrale = new PartieCentraleMois(donneesCredits, enTetesCredits, donneesDebits, enTetesDebits);
	    conteneurPartieCentrale.getInformationsGenerales().setLabelfichierCharge(nomDuFichierACharger);
	    conteneurPartieCentrale.getInformationsGenerales().setLabelDateMAJ(donnees.getDateDeMiseAJour());
	    
	    // Ecoute des boutons du panneau latéral
	    for (JButton element: conteneurPartieDroite.getListeDesBoutons())
	    {
	    	element.addActionListener(new RecupererIdentifiantBouton());
	    }
	    
	    // Paramétrage et alimentation du conteneur de la fenêtre princiaple
	    conteneurGlobal.setBackground(Color.gray);
	    conteneurGlobal.setLayout(new BorderLayout());
	    conteneurGlobal.add(conteneurPartieCentrale.getConteneurGlobal(), BorderLayout.CENTER);
	    conteneurGlobal.add(conteneurPartieDroite.getConteneurPartieDroite(), BorderLayout.EAST);
	    
	    // Ajout d'un raccourci clavier pour quitter l'application
//	    KeyStroke combinaisonTouchesQuitter = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
	    KeyStroke combinaisonTouchesQuitter = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	    this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(combinaisonTouchesQuitter, "Quitter");
	    this.getRootPane().getActionMap().put("Quitter", new actionQuitter());
	    
	    // Définition du content pane de la fenêtre principale
	    this.setContentPane(conteneurGlobal);
	    
	    // Affichage de la fenêtre
	    this.setVisible(true); 
	}

	// Mise-à-jour de l'affichage
	public void actualiserAffichage()
	{
		conteneurGlobal.add(conteneurPartieCentrale.getConteneurGlobal(), BorderLayout.CENTER);
		
		this.setContentPane(conteneurGlobal);	// mise-à-jour du content pane de la fenêtre principale
		this.repaint();							// re-dessine la fenêtre
		this.revalidate();						// en complément de repaint pour indiquer au layout manager de faire un reset sur la base des nouveaux composants
	}
	
	// Récupération des données pour alimenter les tableaux des mois de la partie centrale de l'application
	private void recuperationDesDonnees(String moisSelectionne)
	{
		donnees.recuperationDonneesMoisSelectionne(moisSelectionne, "crédits");
		donnees.recuperationDonneesMoisSelectionne(moisSelectionne, "débits");
		
		donneesCredits = donnees.getValeursCredits();
		enTetesCredits = donnees.getListeDesEnTetesCredits();
		
		donneesDebits = donnees.getValeursDebits();
		enTetesDebits = donnees.getListeDesEnTetesDebits();
	}
	
	// Récupération des données pour alimenter le tableau de bilan de la partie centrale de l'application
	private void recuperationDesDonnesBilan()
	{
		donnees.calculDesSommes();
		
		donneesBilan = donnees.getDonneesBilan() ;
		enTetesBilan = donnees.getListeDesEntetesBilan();
	}
	
	// Action pour quitter l'application (classe interne)
	class actionQuitter extends AbstractAction
	{
		public void actionPerformed(ActionEvent actionEvent)
		{
			System.exit(0);
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
				recuperationDesDonnesBilan();
				conteneurPartieCentrale = new PartieCentraleBilan(donneesBilan, enTetesBilan);
				Fenetre.this.actualiserAffichage();
			}
			else
			{
				conteneurGlobal.remove(conteneurPartieCentrale.getConteneurGlobal());
				recuperationDesDonnees(nomDuBoutonUtilise);
				conteneurPartieCentrale = new PartieCentraleMois(donneesCredits, enTetesCredits, donneesDebits, enTetesDebits);
				Fenetre.this.actualiserAffichage();
			}
			
		}
	}

}
