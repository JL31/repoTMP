import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ContenuDufichierDeDonnees {
	
	// D�clarations des variables d'instance
	private Document doc;
	private String dateDeMiseAJour;
	private Map<String, Hashtable> contenuFichierXML = new Hashtable<String, Hashtable>();
	
	private List<String> listeDesEnTetesCredits = new ArrayList<String>();
	private List<List<String>> listeDesDonneesCredits = new ArrayList<>();
	
	private List<String> listeDesEnTetesDebits = new ArrayList<String>();
	private List<List<String>> listeDesDonneesDebits = new ArrayList<>();
 	
	// Accesseurs
 
	public Map<String, Hashtable> getContenuFichierXMl()
	{
		return contenuFichierXML;
	}
	
	public Object[] getListeDesEnTetesCredits()
	{
		Object[] entetesCredits = listeDesEnTetesCredits.toArray();
		return entetesCredits;
	}
	
	public Object[][] getValeursCredits()
	{
		Object[][] donneesCredits = new Object[listeDesDonneesCredits.size()][];
		
		int i = 0;
		
		for (List<String> elem: listeDesDonneesCredits)
		{
//			donneesCredits[i++] = elem.toArray(new Object[elem.size()]);
			donneesCredits[i++] = elem.toArray();
		}
		
		return donneesCredits;
	}
	
	public Object[] getListeDesEnTetesDebits()
	{
		Object[] entetesDebits = listeDesEnTetesDebits.toArray();
		return entetesDebits;
	}
	
	public Object[][] getValeursDebits()
	{
		Object[][] donneesDebits = new Object[listeDesDonneesDebits.size()][];
		
		int i = 0;
		
		for (List<String> elem: listeDesDonneesDebits)
		{
//			donneesDebits[i++] = elem.toArray(new Object[elem.size()]);
			donneesDebits[i++] = elem.toArray();
		}
		
		return donneesDebits;
	}
	
	// Constructeur avec arguments
	// Constructeur avec argument(s)
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
	// Affichage du contenu du fichier charg�
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
						System.out.println("\t\t\t" + cle4 + " --> " + valeur4);
					}
				}
			}
		}
	}
	
	// R�cup�ration des donn�es de la sous-cat�gorie s�lectionn�e pour le mois s�lectionn� 
	public void recuperationDonneesMoisSelectionne(String moisSelectionne, String sousCategorie)
	{
		// Initialisation des variables
		if (sousCategorie.equals("cr�dits"))
		{
			listeDesEnTetesCredits = new ArrayList<String>();
			listeDesDonneesCredits = new ArrayList<>();
		}
		else if (sousCategorie.equals("d�bits"))
		{
			listeDesEnTetesDebits = new ArrayList<String>();
			listeDesDonneesDebits = new ArrayList<>();
		}
		
		// On v�rifie si le mois s�lectionn� figure dans les donn�es extraites
		if (contenuFichierXML.containsKey(moisSelectionne))
		{
			Hashtable<String, Hashtable> dicoTmp = contenuFichierXML.get(moisSelectionne);
			
			// On v�rifie, pour le mois s�lectionn�, si la sous-cat�gorie existe
			if (dicoTmp.containsKey(sousCategorie))
			{
				Map<String, Hashtable> dicoTmp2 = dicoTmp.get(sousCategorie);
				
				// On it�re sur les cat�gorie de la sous-cat�gorie
				for (Map.Entry<String, Hashtable> categorie: dicoTmp2.entrySet())
				{
					// R�cup�ration du nom de la cat�gorie
					String nomCategorie = categorie.getKey();
					
					// R�cup�ration des valeurs associ�es � la sous-cat�gorie
					Map<String, String> valeursDeLaCategorieCourante = categorie.getValue();
					
					// R�cup�ration du montant et de la date de virement
					Float montant = Float.valueOf(valeursDeLaCategorieCourante.get("montant"));
					String dateVirement = valeursDeLaCategorieCourante.get("date_du_virement");
					
					// Remplissage de la liste temporaire avec les informations r�cup�r�es pr�alablement
					List listeTmp = new ArrayList();
					listeTmp.add(nomCategorie);
					listeTmp.add(montant);
					listeTmp.add(dateVirement);
				
					// Ajout de la liste temporaire aux donn�es de la sous-cat�gorie
					if (sousCategorie.equals("cr�dits"))
					{
						listeDesDonneesCredits.add(listeTmp);
					}
					else if (sousCategorie.equals("d�bits"))
					{
						listeDesDonneesDebits.add(listeTmp);
					}
					
				}
			}
			
			// Ajout des en-t�tes des colonnes � la liste ad�quate
			if (sousCategorie.equals("cr�dits"))
			{
				listeDesEnTetesCredits.add("Cat�gorie");
				listeDesEnTetesCredits.add("Montant (�)");
				listeDesEnTetesCredits.add("Date du virement");
			}
			else if (sousCategorie.equals("d�bits"))
			{
				listeDesEnTetesDebits.add("Cat�gorie");
				listeDesEnTetesDebits.add("Montant (�)");
				listeDesEnTetesDebits.add("Date du virement");
			}
			
		}
	}
	
	// R�cup�ration du contenu du fichier XML
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
