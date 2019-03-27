import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PartieCentraleMois extends JFrame implements PartieCentrale {
	
	// D�claration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private BandeauInformations infoGene = new BandeauInformations(); 
	private JSplitPane separateur = new JSplitPane();
	private JScrollPane jspCredits, jspDebits;
	private JTable tableauCredits, tableauDebits;
	
	// Accesseurs
	public JPanel getConteneurGlobal()
	{
		return conteneurGlobal;
	}
	
	// Constructeur sans arguments
	public PartieCentraleMois()
	{
		// Initialisations
		String[] enTetes = {"", "", ""};
		
		DefaultTableModel model = new DefaultTableModel(0, enTetes.length);
		model.setColumnIdentifiers(enTetes);
		
		// Cr�ation et configuration des tableaux des cr�dits et d�bits
		tableauCredits = new JTable(model);
		tableauDebits = new JTable(model);
		
		tableauCredits.setBackground(new Color(198, 224, 180));
		tableauDebits.setBackground(new Color(248, 203, 173));
		
		tableauCredits.setForeground(Color.black);
		tableauDebits.setForeground(Color.black);
		
		tableauCredits.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		// tester si �a fonctionne quand plus de donn�es
		
		// Configuration des JScrollPane 
		jspCredits = new JScrollPane(tableauCredits);
		jspDebits = new JScrollPane(tableauDebits);
		
		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	// permet d'afficher l'ascenseur en permanence
		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		jspCredits.getViewport().setBackground(new Color(198, 224, 180));				// permet de colorier le reste du JScrollPane dans le cas o� le tableau n'occupe pas tout l'espace
		jspDebits.getViewport().setBackground(new Color(248, 203, 173));
		
		// Configuration du JSplitPane
		separateur = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspCredits, jspDebits);
		separateur.setResizeWeight(0.5);
		separateur.setDividerSize(5);
		separateur.setEnabled(false);
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(infoGene.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(separateur, BorderLayout.CENTER);
	}
	
	
	// Constructeur avec arguments
	public PartieCentraleMois(Object[][] donneesCredits, Object[] enTetesCredits, Object[][] donneesDebits, Object[] enTetesDebits)
	{
		// Cr�ation et configuration des tableaux des cr�dits et d�bits
		tableauCredits = new JTable(donneesCredits, enTetesCredits);
		tableauDebits = new JTable(donneesDebits, enTetesDebits);
		
		tableauCredits.setBackground(new Color(198, 224, 180));
		tableauDebits.setBackground(new Color(248, 203, 173));
		
		tableauCredits.setForeground(Color.black);
		tableauDebits.setForeground(Color.black);
		
		tableauCredits.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);				// tester si �a fonctionne quand plus de donn�es
		
		// Configuration des JScrollPane 
		jspCredits = new JScrollPane(tableauCredits);
		jspDebits = new JScrollPane(tableauDebits);
		
		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	// permet d'afficher l'ascenseur en permanence
		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		jspCredits.getViewport().setBackground(new Color(198, 224, 180));				// permet de colorier le reste du JScrollPane dans le cas o� le tableau n'occupe pas tout l'espace
		jspDebits.getViewport().setBackground(new Color(248, 203, 173));
		
		// Configuration du JSplitPane
		separateur = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspCredits, jspDebits);
		separateur.setResizeWeight(0.5);
		separateur.setDividerSize(5);
		separateur.setEnabled(false);
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(infoGene.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(separateur, BorderLayout.CENTER);
	}
	
}
