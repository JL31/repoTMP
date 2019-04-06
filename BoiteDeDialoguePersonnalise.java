import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
	// Déclaration des variables d'instances
	private ContenuDufichierDeDonnees donnees;
	private JPanel conteneurGlobal = new JPanel();
	private JButton boutonAjout = new JButton("Ajouter le nouveau débit");
	private JComboBox listeDesCategories = new JComboBox();
	private JTextField tfLibelle = new JTextField();
	private JTextField tfMontant = new JTextField();
	private JTextField tfDateDuVirement = new JTextField();
	
	// Constructeur avec arguments
	public BoiteDeDialoguePersonnalise(JFrame parent, String title, boolean modal, ContenuDufichierDeDonnees donnees)
	{
		// Appel du constructeur du parent
		super(parent, title, modal);
				
		// Initialisation de variables 
		this.donnees = donnees;
		
		// Paramètres globaux de la boîte de dialogue
		this.setSize(500, 120);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);	//à creuser car 3 options : DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE et DISPOSE_ON_CLOSE
		
		// On commence par désactiver le bouton d'ajout
		// Il deviendra actif si au moins les objets Catégorie, Libellé et Montant sont remplis 
		boutonAjout.setEnabled(false);
		
		// Création des composants
		creationDesComposants();
		
		// Affichage de la boîte de dialogue
		this.setVisible(true);
	}
	
	// Création des composants de la boîte de dialogue
	public void creationDesComposants()
	{
		// Catégorie
		JPanel panneauCategorie = new JPanel();
		panneauCategorie.setPreferredSize(new Dimension(120, 40));
		listeDesCategories.setPreferredSize(new Dimension(80, 25));
		System.out.println(donnees.getListeDesCategories());
		for (String categorie: donnees.getListeDesCategories())
		{
			listeDesCategories.addItem(categorie);
		}
		panneauCategorie.setBorder(BorderFactory.createTitledBorder("Catégorie"));
		panneauCategorie.add(listeDesCategories);
		
		// Libellé
		JPanel panneauLibelle = new JPanel();
		panneauLibelle.setPreferredSize(new Dimension(120, 40));
		tfLibelle.addKeyListener(new suiviSaisieComposants());
		tfLibelle.setPreferredSize(new Dimension(80, 25));
		panneauLibelle.setBorder(BorderFactory.createTitledBorder("Libellé"));
		panneauLibelle.add(tfLibelle);
		
		// Montant
		JPanel panneauMontant = new JPanel();
		panneauMontant.setPreferredSize(new Dimension(120, 40));
		tfMontant.addKeyListener(new suiviSaisieComposants());
		tfMontant.setPreferredSize(new Dimension(80, 25));
		panneauMontant.setBorder(BorderFactory.createTitledBorder("Montant"));
		panneauMontant.add(tfMontant);
		
		// Date du virement
		JPanel panneauDateDuVirement = new JPanel();
		panneauDateDuVirement.setPreferredSize(new Dimension(120, 40));
		tfDateDuVirement.addKeyListener(new suiviSaisieComposants());
		tfDateDuVirement.setPreferredSize(new Dimension(80, 25));
		panneauDateDuVirement.setBorder(BorderFactory.createTitledBorder("Date du virement"));
		panneauDateDuVirement.add(tfDateDuVirement);
		
		// Ajout des composant à la boîte de dialogue
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
	
	// Classe KeyListener pour vérifier les saisies faites dans les composants de la boîte de dialogue
	class suiviSaisieComposants implements KeyListener
	{
		public void keyPressed (KeyEvent e) {}
		public void keyTyped (KeyEvent e) {}
		public void keyReleased (KeyEvent e)
		{
//			String choixCategorie = listeDesCategories.getSelectedItem();
			System.out.println(listeDesCategories.getSelectedItem());
			String txtLibelle = tfLibelle.getText();
			String txtMontant = tfMontant.getText();
			
			if (!txtLibelle.equals("") && !txtMontant.equals(""))
			{
				boutonAjout.setEnabled(true);
			}
			else
			{
				boutonAjout.setEnabled(false);
			}
		}
	}
	
	// Action associée au bouton d'ajout
	class actionAjout implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Gagné !");
		}
	}
	
}
