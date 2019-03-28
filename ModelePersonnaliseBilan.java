import javax.swing.table.AbstractTableModel;

class ModelePersonnaliseBilan extends AbstractTableModel
{
	// D�claration de variables d'instance
	private Object[][] data;
	private Object[] titles;
	
	// Constructeur avec arguments
	public ModelePersonnaliseBilan(ContenuDufichierDeDonnees donnees)
	{
		donnees.calculDesSommes();
		data = donnees.getDonneesBilan();
		titles = donnees.getListeDesEntetesBilan();
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
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
}
