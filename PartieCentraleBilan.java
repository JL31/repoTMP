import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PartieCentraleBilan extends JFrame implements PartieCentrale
{

	// Déclaration des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private BandeauInformations infoGene = new BandeauInformations(); 
	private JScrollPane jspBilan;
	private JTable tableauBilan;
	
	// Constructeur sans arguments
	public PartieCentraleBilan()
	{
		// Initialisations
//		Object[][] donnees = {{"a", "aa", "aaa"}, {"b", "bb", "bbb"}, {"c", "cc", "ccc"}};
		String[] enTetes = {"Premier", "Deuxième", "Troisième"};
		
		DefaultTableModel model = new DefaultTableModel(5, enTetes.length);
		model.setColumnIdentifiers(enTetes);
		
		// Création et configuration des tableaux des crédits et débits
//		tableauBilan = new JTable(donnees, enTetes);
		tableauBilan = new JTable(model);
		
		tableauBilan.setBackground(Color.gray);
		tableauBilan.setForeground(Color.white);
		
		// Configuration des JScrollPane 
		jspBilan = new JScrollPane(tableauBilan);
		jspBilan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	// permet d'afficher l'ascenseur en permanence
		jspBilan.getViewport().setBackground(Color.gray);							// permet de colorier le reste du JScrollPane dans le cas où le tableau n'occupe pas tout l'espace
		
		// Configuration du conteneur global
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new BorderLayout());
		conteneurGlobal.add(infoGene.getConteneurGlobal(), BorderLayout.NORTH);
		conteneurGlobal.add(jspBilan, BorderLayout.CENTER);
	}
	
	// Accesseurs
	public JPanel getConteneurGlobal()
	{
		return conteneurGlobal;
	}
	
}
