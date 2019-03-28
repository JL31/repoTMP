import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PartieCentraleMois extends JFrame implements PartieCentrale
{
	
	// Déclaration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private BandeauInformations informationsGenerales = new BandeauInformations(); 
	private JSplitPane separateur = new JSplitPane();
	private JScrollPane jspCredits, jspDebits;
	private JTable tableauCredits, tableauDebits;
	
	// Accesseurs
	public JPanel getConteneurGlobal()
	{
		return conteneurGlobal;
	}
	
	public BandeauInformations getInformationsGenerales()
	{
		return informationsGenerales;
	}
	
	// Constructeur sans arguments
	public PartieCentraleMois()
	{
		// Initialisations
		String[] enTetes = {"", "", ""};
		
		DefaultTableModel model = new DefaultTableModel(0, enTetes.length);
		model.setColumnIdentifiers(enTetes);
		
		// Création et configuration des tableaux des crédits et débits
		tableauCredits = new JTable(model);
		tableauDebits = new JTable(model);
		
		tableauCredits.setBackground(new Color(198, 224, 180));
		tableauDebits.setBackground(new Color(248, 203, 173));
		
		tableauCredits.setForeground(Color.black);
		tableauDebits.setForeground(Color.black);
		
		tableauCredits.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		// tester si ça fonctionne quand plus de données
		
		// Configuration des JScrollPane 
		jspCredits = new JScrollPane(tableauCredits);
		jspDebits = new JScrollPane(tableauDebits);
		
//		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
//		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		// permet d'afficher l'ascenseur si besoin
		
		jspCredits.getViewport().setBackground(new Color(198, 224, 180));				// permet de colorier le reste du JScrollPane dans le cas où le tableau n'occupe pas tout l'espace
		jspDebits.getViewport().setBackground(new Color(248, 203, 173));
		
		// Configuration du JSplitPane
		separateur = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspCredits, jspDebits);
		separateur.setResizeWeight(0.5);
		separateur.setDividerSize(5);
		separateur.setEnabled(false);
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(separateur, BorderLayout.CENTER);
	}
	
	// Constructeur avec arguments
	public PartieCentraleMois(ContenuDufichierDeDonnees donnees, String moisStr)
	{
		// Création et configuration des tableaux des crédits et débits
		ModelePersonnaliseMois modeleCredits = new ModelePersonnaliseMois(donnees, moisStr, "crédits");
		ModelePersonnaliseMois modeleDebits = new ModelePersonnaliseMois(donnees, moisStr, "débits");
		
		modeleCredits.addTableModelListener(modeleCredits);
		modeleDebits.addTableModelListener(modeleDebits);
		
		tableauCredits = new JTable(modeleCredits);
		tableauDebits = new JTable(modeleDebits);
		
		tableauCredits.setBackground(new Color(198, 224, 180));
		tableauDebits.setBackground(new Color(248, 203, 173));
		
		tableauCredits.setForeground(Color.black);
		tableauDebits.setForeground(Color.black);
		
		tableauCredits.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);				// tester si ça fonctionne quand plus de données
		
		// Configuration des JScrollPane 
		jspCredits = new JScrollPane(tableauCredits);
		jspDebits = new JScrollPane(tableauDebits);
		
//		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
//		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		// permet d'afficher l'ascenseur si besoin
		
		jspCredits.getViewport().setBackground(new Color(198, 224, 180));				// permet de colorier le reste du JScrollPane dans le cas où le tableau n'occupe pas tout l'espace
		jspDebits.getViewport().setBackground(new Color(248, 203, 173));
		
		// Configuration du JSplitPane
		separateur = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspCredits, jspDebits);
		separateur.setResizeWeight(0.5);
		separateur.setDividerSize(5);
		separateur.setEnabled(false);
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(separateur, BorderLayout.CENTER);
	}
	
	// Définition du modèle personnalisé pour les mois
	class ModelePersonnaliseMois extends AbstractTableModel implements TableModelListener
	{
		// Déclaration de variables d'instance
		private Object[][] data;
		private Object[] titles;
		ContenuDufichierDeDonnees donnees;
		String moisStr;
		String sousCategorie;
		
		// Constructeur avec arguments
		public ModelePersonnaliseMois(ContenuDufichierDeDonnees donnees, String moisStr, String sousCategorie)
		{
			// Initialisation de variables d'instance
			this.donnees = donnees;
			this.moisStr = moisStr;
			this.sousCategorie = sousCategorie;
			
			// Initialisation selon la valeur de la sous-catégorie
			if (sousCategorie.equals("crédits"))
			{
				donnees.recuperationDonneesMoisSelectionne(moisStr, "crédits");
				data = donnees.getValeursCredits();
				titles = donnees.getListeDesEnTetesCredits();
			}
			else if (sousCategorie.equals("débits"))
			{
				donnees.recuperationDonneesMoisSelectionne(moisStr, "débits");
				data = donnees.getValeursDebits();
				titles = donnees.getListeDesEnTetesDebits();
			}
		}
		
		// Récupération du nombre de colonnes
		public int getColumnCount()
		{
			return titles.length;
		}
		
		// Récupération du nombre de lignes
		public int getRowCount()
		{
			return data.length;
		}
		
		// Affichage des noms des colonnes
		public String getColumnName(int col)
		{
			  return String.valueOf(titles[col]);
		}
		
		// Récupération d'une valeur à une ligne et une colonne donnée 
		public Object getValueAt(int row, int col)
		{
			return data[row][col];
		}
		
		// Modification de la valeur à une ligne et une colonne donnée
		public void setValueAt(Object value, int row, int col)
		{
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
		
		// Récupération du type d'attribut pour les données de la sous-catégorie crédits
		public String typeAttributDonneeCredits(int col)
		{
			String retour = "";
			
			if (col == 1)
			{
				retour = "montant";
			}
			else if (col == 2)
			{
				retour = "date_du_virement";
			}
			
			return retour;
		}
		
		// Récupération du type d'attribut pour les données de la sous-catégorie débits
		public String typeAttributDonneesDebits(int col)
		{
			String retour = "";
			
			if (col == 1)
			{
				retour = "libellé";
			}
			else if (col == 2)
			{
				retour = "montant";
			}
			else if (col == 3)
			{
				retour = "date_du_virement";
			}
			
			return retour;
		}
		
		// Modification d'une donnée de l'objet ContenuDufichierDeDonnees
		public void tableChanged(TableModelEvent e)
		{
			// Déclaration de variables
			String attribut = "";
			
			// Initialisation de variables
			int row = e.getFirstRow();
			int col = e.getColumn();
			String valeur = String.valueOf(data[row][col]);
			String nomCat = String.valueOf(data[row][0]);
			
			// Récupération du type d'attribut pour les données selon la sous-catégorie
			if (sousCategorie.equals("crédits"))
			{
				attribut = typeAttributDonneeCredits(col);
			}
			else if (sousCategorie.equals("débits"))
			{
				attribut = typeAttributDonneesDebits(col);
			}
			
			// Modification de la donnée dans l'objet ContenuDufichierDeDonnees
			donnees.modifierElementContenuFichierXML(moisStr, sousCategorie, nomCat, attribut, valeur);
		}
		
		// Gestion de la propriété d'édition des cellules du tableau
		public boolean isCellEditable(int row, int col)
		{
			if (col == 0)
			{
				return false;
			}
			return true;
		}
	}
}
