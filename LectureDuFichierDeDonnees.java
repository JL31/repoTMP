import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

public class LectureDuFichierDeDonnees
{
	
	// Déclaration des variables d'instance 
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
			// Création de l'objet FIle du fichier XML à importer
			File fichier = new File(this.nomDuFichierDeDonnees);
			
			// Création des éléments de lecture du fichier XML : factory, builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			// Parsing du fichier XML
			Document doc = builder.parse(fichier);
			
			// Création de l'objet qui va contenir le contenu du fichier de données
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
