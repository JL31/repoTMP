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

	// Déclaration des variables d'instance
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
		
		// Création et configuration des tableaux des crédits et débits
		tableauBilan = new JTable(model);
		
		tableauBilan.setBackground(Color.gray);
		tableauBilan.setForeground(Color.white);
		
		// Configuration des JScrollPane 
		jspBilan = new JScrollPane(tableauBilan);
//		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspBilan.getViewport().setBackground(Color.gray);								// permet de colorier le reste du JScrollPane dans le cas où le tableau n'occupe pas tout l'espace
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(jspBilan, BorderLayout.CENTER);
	}
	
	// Constructeur avec arguments
	public PartieCentraleBilan(Object[][] donneesBilan, Object[] enTetesBilan)
	{
		// Création et configuration des tableaux des crédits et débits
		// Test ---
		ModelePerso modele = new ModelePerso(donneesBilan, enTetesBilan);
		tableauBilan = new JTable(modele);
		// --- Test
//		tableauBilan = new JTable(donneesBilan, enTetesBilan);
		
		tableauBilan.setBackground(Color.gray);
		tableauBilan.setForeground(Color.white);
		
		// Configuration des JScrollPane 
		jspBilan = new JScrollPane(tableauBilan);
//		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspBilan.getViewport().setBackground(Color.gray);								// permet de colorier le reste du JScrollPane dans le cas où le tableau n'occupe pas tout l'espace
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(jspBilan, BorderLayout.CENTER);
	}
	
	// Test
	class ModelePerso extends AbstractTableModel
	{
		private Object[][] data;
		private Object[] titles;
		
		public ModelePerso(Object[][] data, Object[] titles)
		{
			this.data = data;
			this.titles = titles;
		}
		
		public int getColumnCount()
		{
			return titles.length;
		}
		
		public int getRowCount()
		{
			return data.length;
		}
		
		public Object getValueAt(int row, int col)
		{
			return data[row][col];
		}
		
		public boolean isCellEditable(int row, int col)
		{
			return false;
		}
	}
}
