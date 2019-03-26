import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ContenuDufichierDeDonnees {
	
	// D�clarations des variables d'instance
	private Document doc;
	private String dateDeMiseAJour;
	private Map<String, Hashtable> contenuFichierXML = new Hashtable<String, Hashtable>();
	
	// Accesseurs
	public Map<String, Hashtable> getContenuFichierXMl()
	{
		return contenuFichierXML;
	}
	
	// Constructeur avec arguments
	public ContenuDufichierDeDonnees(Document doc)
	{
		this.doc = doc;
		
		// R�cup�ration de l'�l�ment racine du fichier XML
		Element root = this.doc.getDocumentElement();
		
		// R�cup�ration de la ate de mise-�-jour dans le fichier XML
		dateDeMiseAJour = root.getElementsByTagName("MAJ").item(0).getTextContent();
		
		// R�cup�ration de la liste des noeuds du fichier XML
		NodeList listeDesNoeuds = root.getChildNodes();
		
		// Si la liste des noeuds n'est pas vide on fait appel � la m�thode r�cursive de remplissage du dictionnaire qui va contenir le contenu du fichier XML 
		if (listeDesNoeuds.getLength() > 0)
		{
			contenuFichierXML = recuperationDuContenu(listeDesNoeuds);
		}
	}
	
	// Affichage du contenu du dico contenant les donn�es du fichier XML
	public void affichageContenuFichierXML ()
	{
		for (Map.Entry<String, Hashtable> mp1: contenuFichierXML.entrySet())
		{
			String cle1 = mp1.getKey();
			Map<String, Hashtable> valeur1 = mp1.getValue();
			System.out.println(cle1);
			
			for (Map.Entry<String, Hashtable> mp2: valeur1.entrySet())
			{
				String cle2 = mp2.getKey();
				Map<String, Hashtable> valeur2 = mp2.getValue();
				System.out.println("\t" + cle2);
				
				for (Map.Entry<String, Hashtable> mp3: valeur2.entrySet())
				{
					String cle3 = mp3.getKey();
					Map<String, String> valeur3 = mp3.getValue();
					System.out.println("\t\t" + cle3);
					
					for (Map.Entry<String, String> mp4: valeur3.entrySet())
					{
						String cle4 = mp4.getKey();
						String valeur4 = mp4.getValue();
						System.out.println("\t\t\t" + cle4);
						System.out.println("\t\t\t" + valeur4);
					}
				}
			}
		}
	}
	
	// 
	public Map<String, Hashtable> recuperationDonneesMoisSelectionne(String moisSelectionne)
	{
		// D�claration des variables
		Map<String, Hashtable> dicoDonnees = new Hashtable<String, Hashtable>(); 
		
		Hashtable dicoDonneesCredits = new Hashtable();
		List<String> listeDesEnTetesCredits = new ArrayList<String>();
		List<List<?>> valeursCredits = new ArrayList<>();
		
		Hashtable dicoDonneesDebits = new Hashtable();
		List<String> listeDesEnTetesDebits = new ArrayList<String>();
		List<List<?>> valeursDebits = new ArrayList<>();
		
//		Object[][] valeurs;
		
		// ...
		if (contenuFichierXML.containsKey(moisSelectionne))
		{
			Hashtable<String, Hashtable> dicoTmp = contenuFichierXML.get(moisSelectionne);
			
			if (dicoTmp.containsKey("cr�dits"))
			{
				Map<String, Hashtable> dicoTmp2 = dicoTmp.get("cr�dits");
				
				for (Map.Entry<String, Hashtable> elem: dicoTmp2.entrySet())
				{
					String nomCategorie = elem.getKey();
					listeDesEnTetesCredits.add(nomCategorie);
					
					Map<String, Hashtable> categories = elem.getValue();
					
					for (Map.Entry<String, Hashtable> elem2: categories.entrySet())
					{
						Map<String, String> valeursCategorie = elem2.getValue();
						
						Float montant = Float.valueOf(valeursCategorie.get("montant"));
						String dateVirement = valeursCategorie.get("date_du_virement");
						Boolean statut = Boolean.valueOf(valeursCategorie.get("statut"));
						
						List listeTmp = new ArrayList();
						listeTmp.add(montant);
						listeTmp.add(dateVirement);
						listeTmp.add(statut);
						
						valeursCredits.add(listeTmp);
					}
				}
			}
			else if (dicoTmp.containsKey("d�dits"))
			{
				Map<String, Hashtable> dicoTmp2 = dicoTmp.get("d�dits");
				
				for (Map.Entry<String, Hashtable> elem: dicoTmp2.entrySet())
				{
					String nomCategorie = elem.getKey();
					listeDesEnTetesDebits.add(nomCategorie);
					
					Map<String, Hashtable> categories = elem.getValue();
					
					for (Map.Entry<String, Hashtable> elem2: categories.entrySet())
					{
						Map<String, String> valeursCategorie = elem2.getValue();
						
						Float montant = Float.valueOf(valeursCategorie.get("montant"));
						String dateVirement = valeursCategorie.get("date_du_virement");
						Boolean statut = Boolean.valueOf(valeursCategorie.get("statut"));
						
						List listeTmp = new ArrayList();
						listeTmp.add(montant);
						listeTmp.add(dateVirement);
						listeTmp.add(statut);
						
						valeursDebits.add(listeTmp);
					}
				}
			} 
//			else
//			{
//				0
//			}
		}
//		// ...
//		else
//		{
//			
//		}
		
		// Retour de la m�thode
		dicoDonneesCredits.put("en-tetes", listeDesEnTetesCredits);
		dicoDonneesCredits.put("donnees", valeursCredits);
		
		dicoDonneesDebits.put("en-tetes", listeDesEnTetesDebits);
		dicoDonneesDebits.put("donnees", valeursDebits);
		
		dicoDonnees.put("cr�dits",  dicoDonneesCredits);
		dicoDonnees.put("d�bits",  dicoDonneesDebits);
		
		return dicoDonnees;
	}
	
	// R�cup�ration du contenu du fichier XML
	public Map recuperationDuContenu(NodeList liste)
	{
		// D�claration des variables
		String cleMois, cleCreditsDebits;
		String nom, dateVirement, montant, statut;
		Map dicoDeRetour = new Hashtable<String, Hashtable>();
		
		// It�ration sur laliste pass�e en argument 
		for (int i = 0; i < liste.getLength(); i++)
		{
			// R�cup�ration de l'item i en tant que Node : ce sera le noeud courant
			Node noeudCourant = liste.item(i);
			
			// On regarde si le noeud courant est de type ELEMENT et s'il est �gal � un mois 
			if ((noeudCourant.getNodeType() == Node.ELEMENT_NODE) && (noeudCourant.getNodeName().equals("mois")))
			{
				Element elem = (Element) noeudCourant;
				
				// R�cup�ration du nom de la cl�, i.e. le mois courant
				cleMois = elem.getAttribute("nom");
				
				// R�cup�ration de la liste des sous-cat�gories
				NodeList listeDesSousCategories = noeudCourant.getChildNodes();
				
				// On v�rifie que le mois en cours contient bien des donn�es
				if (listeDesSousCategories.getLength() > 0)
				{
					// Alimentation du dictionnaire de retour
					Map dicoTmp1 = recuperationDuContenu(listeDesSousCategories);
					dicoDeRetour.put(cleMois, dicoTmp1);
				}
			}
			// On regarde si le noeud courant est de type ELEMENT et s'il est �gal � un cr�dit ou un d�bit
			else if ((noeudCourant.getNodeType() == Node.ELEMENT_NODE) && (noeudCourant.getNodeName().equals("cr�dits") || noeudCourant.getNodeName().equals("d�bits")))
			{
				// R�cup�ration de la liste des �l�ments associ�s soit aux cr�dits soit aux d�bits
				NodeList listeCreditsDebits = noeudCourant.getChildNodes();
				
				// Initialisation du dictionnaire qui va contenir les �l�ments 
				Hashtable<String, Hashtable> dicoTmp2 = new Hashtable<String, Hashtable>();
				
				// Si le nombre d'�l�ments (i.e. de cat�gories) associ�s soit aux cr�dits soit aux d�bits est non nulle
				// on rempli le dictionnaire qui va contenir les valeurs des cat�gories associ�es soit aux cr�dits soit aux d�bits
				if (listeCreditsDebits.getLength() > 0)
				{
					// R�cup�ration du nom de la cl� : soit cr�dits soit d�bits
					cleCreditsDebits = noeudCourant.getNodeName();
					
					// It�ration sur le nombre d'�l�ments (i.e. de cat�gories) associ�s soit aux cr�dits soit aux d�bits
					for (int j = 0; j < listeCreditsDebits.getLength(); j++)
					{
						Node elementListeCreditsDebits = listeCreditsDebits.item(j);
						
						// On regarde si le noeud courant est de type ELEMENT et s'il est �gal "cat�gorie"
						if ((elementListeCreditsDebits.getNodeType() == Node.ELEMENT_NODE) && (elementListeCreditsDebits.getNodeName().equals("cat�gorie")))
						{
							Element elemCreditsDebits = (Element) listeCreditsDebits.item(j);
							
							// R�cup�ration du nom de la cat�gorie courante
							nom = elemCreditsDebits.getAttribute("nom");
							
							// R�cup�ration des valeurs de la cat�gorie courante
							montant = elemCreditsDebits.getAttribute("montant");
							dateVirement = elemCreditsDebits.getAttribute("date_du_virement"); 
							statut = elemCreditsDebits.getAttribute("statut");
							
							// Remplissage d'un dico avec les valeurs de la cat�gorie courante
							Hashtable<String, String> dicoTmp3 = new Hashtable<String, String>();
							
							dicoTmp3.put("montant", montant);
							dicoTmp3.put("date_du_virement", dateVirement);
							dicoTmp3.put("statut", statut);
							
							// Remplissage d'un dictionnaire avec les valeurs de la cat�gorie courante
							dicoTmp2.put(nom, dicoTmp3);
						}
					}
					
					// Remplissage du dictionnaire de retour
					dicoDeRetour.put(cleCreditsDebits, dicoTmp2);
				}
			}
		}
		
		// Retour de la m�thode
		return dicoDeRetour;
	}
	
}
