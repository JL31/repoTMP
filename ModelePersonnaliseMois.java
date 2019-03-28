import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

class ModelePersonnaliseMois extends AbstractTableModel implements TableModelListener
{
	// D�claration de variables d'instance
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
		
		// Initialisation selon la valeur de la sous-cat�gorie
		if (sousCategorie.equals("cr�dits"))
		{
			donnees.recuperationDonneesMoisSelectionne(moisStr, "cr�dits");
			data = donnees.getValeursCredits();
			titles = donnees.getListeDesEnTetesCredits();
		}
		else if (sousCategorie.equals("d�bits"))
		{
			donnees.recuperationDonneesMoisSelectionne(moisStr, "d�bits");
			data = donnees.getValeursDebits();
			titles = donnees.getListeDesEnTetesDebits();
		}
	}
	
	// R�cup�ration du nombre de colonnes
	public int getColumnCount()
	{
		return titles.length;
	}
	
	// R�cup�ration du nombre de lignes
	public int getRowCount()
	{
		return data.length;
	}
	
	// Affichage des noms des colonnes
	public String getColumnName(int col)
	{
		  return String.valueOf(titles[col]);
	}
	
	// R�cup�ration d'une valeur � une ligne et une colonne donn�e 
	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}
	
	// Modification de la valeur � une ligne et une colonne donn�e
	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	// R�cup�ration du type d'attribut pour les donn�es de la sous-cat�gorie cr�dits
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
	
	// R�cup�ration du type d'attribut pour les donn�es de la sous-cat�gorie d�bits
	public String typeAttributDonneesDebits(int col)
	{
		String retour = "";
		
		if (col == 1)
		{
			retour = "libell�";
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
	
	// Modification d'une donn�e de l'objet ContenuDufichierDeDonnees
	public void tableChanged(TableModelEvent e)
	{
		// D�claration de variables
		String attribut = "";
		
		// Initialisation de variables
		int row = e.getFirstRow();
		int col = e.getColumn();
		String valeur = String.valueOf(data[row][col]);
		String nomCat = String.valueOf(data[row][0]);
		
		// R�cup�ration du type d'attribut pour les donn�es selon la sous-cat�gorie
		if (sousCategorie.equals("cr�dits"))
		{
			attribut = typeAttributDonneeCredits(col);
		}
		else if (sousCategorie.equals("d�bits"))
		{
			attribut = typeAttributDonneesDebits(col);
		}
		
		// Modification de la donn�e dans l'objet ContenuDufichierDeDonnees
		donnees.modifierElementContenuFichierXML(moisStr, sousCategorie, nomCat, attribut, valeur);
	}
	
	// Gestion de la propri�t� d'�dition des cellules du tableau
	public boolean isCellEditable(int row, int col)
	{
		if (col == 0)
		{
			return false;
		}
		return true;
	}
}
