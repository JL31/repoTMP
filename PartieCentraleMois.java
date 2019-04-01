import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
	
	// D�claration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private BandeauInformations informationsGenerales = new BandeauInformations(); 
	private JSplitPane separateur = new JSplitPane();
	private JScrollPane jspCredits, jspDebits;
	private JTable tableauCredits, tableauDebits;
	// Test ---
	private ContenuDufichierDeDonnees donnees;
	private JPopupMenu jpm = new JPopupMenu();
	private JMenuItem nouveauDebits = new JMenuItem("Nouveaux d�bits");
	// --- Test
	
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
		
//		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
//		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		// permet d'afficher l'ascenseur si besoin
		
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
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(separateur, BorderLayout.CENTER);
		
		// Cr�ation du menu contextuel
	    conteneurGlobal.addMouseListener(new menuContextuelPersonnalise());
	    nouveauDebits.addActionListener(new actionNouveauDebit());
	}
	
	// Constructeur avec arguments
	public PartieCentraleMois(ContenuDufichierDeDonnees donnees, String moisStr)
	{
		// Initialisation de variables d'instance
		this.donnees = donnees;
		
		// Cr�ation et configuration des tableaux des cr�dits et d�bits
		ModelePersonnaliseMois modeleCredits = new ModelePersonnaliseMois(donnees, moisStr, "cr�dits");
		ModelePersonnaliseMois modeleDebits = new ModelePersonnaliseMois(donnees, moisStr, "d�bits");
		
		modeleCredits.addTableModelListener(modeleCredits);
		modeleDebits.addTableModelListener(modeleDebits);
		
		tableauCredits = new JTable(modeleCredits);
		tableauDebits = new JTable(modeleDebits);
		
		tableauCredits.setBackground(new Color(198, 224, 180));
		tableauDebits.setBackground(new Color(248, 203, 173));
		
		tableauCredits.setForeground(Color.black);
		tableauDebits.setForeground(Color.black);
		
		tableauCredits.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);				// tester si �a fonctionne quand plus de donn�es
		
		// Configuration des JScrollPane 
		jspCredits = new JScrollPane(tableauCredits);
		jspDebits = new JScrollPane(tableauDebits);
		
//		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
//		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		// permet d'afficher l'ascenseur en permanence
		jspCredits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// permet d'afficher l'ascenseur si besoin
		jspDebits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		// permet d'afficher l'ascenseur si besoin
		
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
		conteneurGlobal.add(informationsGenerales.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(separateur, BorderLayout.CENTER);
		
		// Cr�ation du menu contextuel
		jspDebits.addMouseListener(new menuContextuelPersonnalise());
	    nouveauDebits.addActionListener(new actionNouveauDebit());
	}
	
	// Classe interne qui g�re le menu contextuel (impl�mente la classe MouseAdapter)
	class menuContextuelPersonnalise extends MouseAdapter
	{
		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				jpm.add(nouveauDebits);
				jpm.show(jspDebits, e.getX(), e.getY());
			}
		}
	}
	
	// Action pour ajouter une nouvelle entr�e dans les d�bits
	class actionNouveauDebit implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			BoiteDeDialoguePersonnalise bddp = new BoiteDeDialoguePersonnalise(null, "Nouveau d�bit", true, donnees);
//			JOptionPane tut = new JOptionPane();
//			tut.showMessageDialog(null, "tut", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
