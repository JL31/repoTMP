import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

public class LectureDuFichierDeDonnees
{
	
	// D�claration des variables d'instance 
	private String nomDuFichierDeDonnees;
	private ContenuDufichierDeDonnees contenuDuFichierDeDonnees;
	
	// Accesseurs
	public ContenuDufichierDeDonnees getContenuDuFichierDeDonnees()
	{
		return contenuDuFichierDeDonnees; 
	}
	
	// Constructeur avec argument
	public LectureDuFichierDeDonnees(String nomDuFichierDeDonnees)
	{
		this.nomDuFichierDeDonnees = nomDuFichierDeDonnees;
		
		try
		{
			// Cr�ation de l'objet FIle du fichier XML � importer
			File fichier = new File(this.nomDuFichierDeDonnees);
			
			// Cr�ation des �l�ments de lecture du fichier XML : factory, builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			// Parsing du fichier XML
			Document doc = builder.parse(fichier);
			
			// Cr�ation de l'objet qui va contenir le contenu du fichier de donn�es
			contenuDuFichierDeDonnees = new ContenuDufichierDeDonnees(doc);
//			contenuDuFichierDeDonnees.affichageContenuFichierXML();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
		
}
