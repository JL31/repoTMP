import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BoiteDeDialoguePersonnalise extends JDialog
{
	// D�claration des variables d'instances
	private ContenuDufichierDeDonnees donnees;
	private JPanel conteneurGlobal = new JPanel();
	private JButton boutonAjout = new JButton("Ajouter le nouveau d�bit");
	
	// Constructeur avec arguments
	public BoiteDeDialoguePersonnalise(JFrame parent, String title, boolean modal, ContenuDufichierDeDonnees donnees)
	{
		// Appel du constructeur du parent
		super(parent, title, modal);
				
		// Initialisation de variables 
		this.donnees = donnees;
		
		// Param�tres globaux de la bo�te de dialogue
		this.setSize(500, 120);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);	//� creuser car 3 options : DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE et DISPOSE_ON_CLOSE
		
		// On commence par d�sactiver le bouton d'ajout
		// Il deviendra actif si au moins les objets Cat�gorie, Libell� et Montant sont remplis 
		boutonAjout.setEnabled(false);
		
		// Cr�ation des composants
		creationDesComposants();
		
		// Affichage de la bo�te de dialogue
		this.setVisible(true);
	}
	
	// Cr�ation des composants de la bo�te de dialogue
	public void creationDesComposants()
	{
		// Cat�gorie
		JPanel panneauCategorie = new JPanel();
		panneauCategorie.setPreferredSize(new Dimension(120, 40));
		JComboBox listeDesCategories = new JComboBox();
		listeDesCategories.setPreferredSize(new Dimension(80, 25));
		for (String categorie: donnees.getListeDesCategories())
		{
			listeDesCategories.addItem(categorie);
		}
		panneauCategorie.setBorder(BorderFactory.createTitledBorder("Cat�gorie"));
		panneauCategorie.add(listeDesCategories);
		
		// Libell�
		JPanel panneauLibelle = new JPanel();
		panneauLibelle.setPreferredSize(new Dimension(120, 40));
		JTextField tfLibelle = new JTextField();
		tfLibelle.setPreferredSize(new Dimension(80, 25));
		panneauLibelle.setBorder(BorderFactory.createTitledBorder("Libell�"));
		panneauLibelle.add(tfLibelle);
		
		// Montant
		JPanel panneauMontant = new JPanel();
		panneauMontant.setPreferredSize(new Dimension(120, 40));
		JTextField tfMontant = new JTextField();
		tfMontant.setPreferredSize(new Dimension(80, 25));
		panneauMontant.setBorder(BorderFactory.createTitledBorder("Montant"));
		panneauMontant.add(tfMontant);
		
		// Date du virement
		JPanel panneauDateDuVirement = new JPanel();
		panneauDateDuVirement.setPreferredSize(new Dimension(120, 40));
		JTextField tfDateDuVirement = new JTextField();
		tfDateDuVirement.setPreferredSize(new Dimension(80, 25));
		panneauDateDuVirement.setBorder(BorderFactory.createTitledBorder("Date du virement"));
		panneauDateDuVirement.add(tfDateDuVirement);
		
		// Ajout des composant � la bo�te de dialogue
		conteneurGlobal.setLayout(new BorderLayout());
		
		JPanel sousPanneau = new JPanel();
		sousPanneau.setLayout(new GridLayout(1, 3));
		sousPanneau.add(panneauCategorie);
		sousPanneau.add(panneauLibelle);
		sousPanneau.add(panneauMontant);
		sousPanneau.add(panneauDateDuVirement);
		conteneurGlobal.add(sousPanneau, BorderLayout.CENTER);
		
		conteneurGlobal.add(boutonAjout, BorderLayout.SOUTH);
		
		this.setContentPane(conteneurGlobal);
	}
	
}
