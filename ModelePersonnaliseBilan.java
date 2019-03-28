import javax.swing.table.AbstractTableModel;

class ModelePersonnaliseBilan extends AbstractTableModel
{
	// Déclaration de variables d'instance
	private Object[][] data;
	private Object[] titles;
	
	// Constructeur avec arguments
	public ModelePersonnaliseBilan(ContenuDufichierDeDonnees donnees)
	{
		donnees.calculDesSommes();
		data = donnees.getDonneesBilan();
		titles = donnees.getListeDesEntetesBilan();
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
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
}
