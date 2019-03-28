import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

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
