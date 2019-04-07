import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanneauLateral
{
	
	// D�claration des variables d'instance
	private JPanel conteneurPartieDroite = new JPanel();
	private JLabel bandeauInformations = new JLabel();
	private String[] listeDesMois = {"Janvier",
									 "F�vrier",
									 "Mars", 
									 "Avril",
									 "Mai",
									 "Juin",
									 "Juillet",
									 "Ao�t",
									 "Septembre",
									 "Octobre",
									 "Novembre",
									 "D�cembre"};
	private HashMap<String, JButton> dicoDesMois = new HashMap<String, JButton>();
	private GridLayout glMois = new GridLayout(listeDesMois.length + 3, 1);
	private JButton boutonBilan = new JButton("Bilan");
	private List<JButton> listeDesBoutons = new ArrayList<JButton>();
	private HashMap<String, JButton> dicoDesBoutons = new HashMap<String, JButton>();
	
	// Accesseurs
	public JPanel getConteneurPartieDroite()
	{
		return conteneurPartieDroite;
	}
	
	public List<JButton> getListeDesBoutons()
	{
		return listeDesBoutons;
	}
	
	public HashMap<String, JButton> getDicoDesBoutons()
	{
		dicoDesMois.forEach((cle, valeur) -> dicoDesBoutons.put(cle, valeur)); 
		dicoDesBoutons.put("Bilan", boutonBilan);
		
		return dicoDesBoutons;
	}
	
	// Constructeur sans arguments
	public PanneauLateral()
	{
		// Param�trage du conteneur des boutons des mois
	    conteneurPartieDroite.setBackground(Color.gray);
	    conteneurPartieDroite.setLayout(glMois);
	    
	    // Param�trage de la partie lat�rale droite de l'application : label d'information + boutons pour chaque mois + label d'espacement + bouton pour le bilan
	    
	    // --- Label d'information
	    Font fontLabelInformation = new Font("Arial", Font.BOLD, 12);
	    bandeauInformations.setFont(fontLabelInformation);
	    bandeauInformations.setForeground(Color.white);
	    
	    // --- --- Dans l'instruction suivante passer au HTMl permet :
	    // --- --- + via la balise <br> de d�finir une nouvelle ligne
	    // --- --- + via la balise <center> de centrer le texte
	    bandeauInformations.setText("<html><center>Liste des<br>onglets possibles</center></html>");
	    conteneurPartieDroite.add(bandeauInformations);
	    
	    // --- Cr�ation des boutons des mois
	    for (String mois : listeDesMois)
	    {
	    	dicoDesMois.put(mois, new JButton(mois));
	    	conteneurPartieDroite.add(dicoDesMois.get(mois));
	    }
	    
	    // Cr�ation de la liste de tous les boutons du panneau
	    dicoDesMois.forEach((key, value) -> listeDesBoutons.add(value));
 		listeDesBoutons.add(boutonBilan);
	    
	    // --- Label d'espacement
	    conteneurPartieDroite.add(new JLabel());
	    
	    // --- Bouton pour le bilan
	    conteneurPartieDroite.add(boutonBilan);
	}

}
