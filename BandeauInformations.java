import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class BandeauInformations
{
	// Déclarations des variables d'instance
	private JPanel conteneurGlobal = new JPanel();
	private JLabel labelfichierCharge = new JLabel();
	private JLabel labelDateMAJ = new JLabel();
	
	// Constructeur sans arguments
	public BandeauInformations()
	{
		Font fontLabels = new Font("Tahoma", Font.BOLD, 12);
		
		labelfichierCharge.setBorder(BorderFactory.createTitledBorder(null, "Fichier de données chargé", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.white));
		labelfichierCharge.setHorizontalAlignment(JLabel.CENTER);
		labelfichierCharge.setAlignmentX(JLabel.CENTER);
		labelfichierCharge.setFont(fontLabels);
		labelfichierCharge.setForeground(Color.white);
		labelfichierCharge.setText("Essai 1");
		
		labelDateMAJ.setBorder(BorderFactory.createTitledBorder(null, "Date de dernière mise-à-jour", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.white));
		labelDateMAJ.setHorizontalAlignment(JLabel.CENTER);
		labelDateMAJ.setAlignmentX(JLabel.CENTER);
		labelDateMAJ.setFont(fontLabels);
		labelDateMAJ.setForeground(Color.white);
		labelDateMAJ.setText("Essai 2");
		
		conteneurGlobal.setBackground(Color.gray);
		conteneurGlobal.setLayout(new GridLayout(2, 1));
		conteneurGlobal.add(labelfichierCharge);
		conteneurGlobal.add(labelDateMAJ);
	}
	
	// Accesseur
	public JPanel getConteneurGlobal()
	{
		return conteneurGlobal;
	}
}
