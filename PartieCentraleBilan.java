import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class PartieCentraleBilan extends JFrame implements PartieCentrale
{

	// D�claration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private BandeauInformations informationsGenerales = new BandeauInformations(); 
	private JScrollPane jspBilan;
	private JTable tableauBilan;
	
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
	public PartieCentraleBilan()
	{
		// Initialisations
		String[] enTetes = {"", "", ""};
		
		DefaultTableModel model = new DefaultTableModel(0, enTetes.length);
		model.setColumnIdentifiers(enTetes);
		
		// Cr�ation et configuration des tableaux des cr�dits et d�bits
		tableauBilan = new JTable(model);
		
		tableauBilan.setBackground(Color.gray);
		tableauBilan.setForeground(Color.white);
		
		// Configuration des JScrollPane 
		jspBilan = new JScrollPane(tableauBilan);
//		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspBilan.getViewport().setBackground(Color.gray);								// permet de colorier le reste du JScrollPane dans le cas o� le tableau n'occupe pas tout l'espace
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(jspBilan, BorderLayout.CENTER);
	}
	
	// Constructeur avec arguments
	public PartieCentraleBilan(ContenuDufichierDeDonnees donnees)
	{
		// Cr�ation et configuration des tableaux des cr�dits et d�bits
		ModelePersonnaliseBilan modele = new ModelePersonnaliseBilan(donnees);
		tableauBilan = new JTable(modele);
		
		tableauBilan.setBackground(Color.gray);
		tableauBilan.setForeground(Color.white);
		
		// Configuration des JScrollPane 
		jspBilan = new JScrollPane(tableauBilan);
//		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspBilan.getViewport().setBackground(Color.gray);								// permet de colorier le reste du JScrollPane dans le cas o� le tableau n'occupe pas tout l'espace
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(jspBilan, BorderLayout.CENTER);
	}
	
	// D�finition du mod�le personnalis� pour la partie Bilan
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
}
