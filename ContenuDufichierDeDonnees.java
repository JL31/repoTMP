import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ContenuDufichierDeDonnees
{
	
	// D�clarations des variables d'instance
	private Document doc;
	private String dateDeMiseAJour;
	private Hashtable<String, Hashtable> contenuFichierXML = new Hashtable<String, Hashtable>();
	private List<String> listeDesEnTetesCredits = new ArrayList<String>();
	private List<List<String>> listeDesDonneesCredits = new ArrayList<>();
	private List<String> listeDesEnTetesDebits = new ArrayList<String>();
	private List<List<String>> listeDesDonneesDebits = new ArrayList<>();
	private Hashtable<String, Float> dicoDesSommes = new Hashtable<String, Float>();
	private Object[] listeDesEntetesBilan = {"Cat�gories", "Somme (en �)"};
	private Object[][] donneesBilan;
	private Object[] entetesCredits;
	private Object[][] donneesCredits;
	private Object[] entetesDebits;
	private Object[][] donneesDebits;
 	
	// Accesseurs
	public Hashtable<String, Hashtable> getContenuFichierXMl()
	{
		return contenuFichierXML;
	}
	
	public String getDateDeMiseAJour()
	{
		return dateDeMiseAJour;
	}
	
	public Object[] getListeDesEnTetesCredits()
	{
		return entetesCredits;
	}
	
	public Object[][] getValeursCredits()
	{
		return donneesCredits;
	}
	
	public Object[] getListeDesEnTetesDebits()
	{
		return entetesDebits;
	}
	
	public Object[][] getValeursDebits()
	{
		return donneesDebits;
	}
	
	public Object[] getListeDesEntetesBilan()
	{
		return listeDesEntetesBilan;
	}
	
	public Object[][] getDonneesBilan()
	{
		return donneesBilan; 
	}
	
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
	
	// Affichage du contenu du fichier charg� 
	public void affichageContenuFichierXML ()
	{
		for (Map.Entry<String, Hashtable> mp1: contenuFichierXML.entrySet())
		{
			String cle1 = mp1.getKey();
			Hashtable<String, Hashtable> valeur1 = mp1.getValue();
			System.out.println(cle1);
			
			for (Map.Entry<String, Hashtable> mp2: valeur1.entrySet())
			{
				String cle2 = mp2.getKey();
				Hashtable<String, Hashtable> valeur2 = mp2.getValue();
				System.out.println("\t" + cle2);
				
				for (Map.Entry<String, Hashtable> mp3: valeur2.entrySet())
				{
					String cle3 = mp3.getKey();
					Hashtable<String, String> valeur3 = mp3.getValue();
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
	
	// Modification d'une donn�es dans le contenu du fichier XML (suite � l'�dition d'une cellule par exemple)
	public void modifierElementContenuFichierXML(String mois, String sousCategorie, String categorie, String attribut, String valeur)
	{
		Hashtable sousCat = (Hashtable) contenuFichierXML.get(mois).get(sousCategorie);
		Hashtable cat = (Hashtable) sousCat.get(categorie);
		cat.put(attribut, valeur);
	}
	
	// Transformation des donn�es du bilan en Object[][]
	public void transformeDonneesBilan()
	{
		donneesBilan = new Object[dicoDesSommes.size()][];
		
		int j = 0;
		
		for (Map.Entry<String, Float> nbr: dicoDesSommes.entrySet())
		{
			List<String> tut = new ArrayList<String>();
			tut.add(nbr.getKey());
			tut.add(String.valueOf(nbr.getValue()));
			
			donneesBilan[j++] = tut.toArray();
		}
	}
	
	// Calcul des sommes pour chacunes des cat�gories
	public void calculDesSommes()
	{
		// Initialisation du dictionnaire contenant les sommes
		dicoDesSommes = new Hashtable<String, Float>();
		
		for (Map.Entry<String, Hashtable> mois: contenuFichierXML.entrySet())
		{
			String cleMois = mois.getKey();
			Hashtable<String, Hashtable> valeurMois = mois.getValue();
			
			for (Map.Entry<String, Hashtable> sousCategorie: valeurMois.entrySet())
			{
				String cleSousCategorie = sousCategorie.getKey();
				Hashtable<String, Hashtable> valeurSousCategorie = sousCategorie.getValue();
				
				for (Map.Entry<String, Hashtable> categorie: valeurSousCategorie.entrySet())
				{
					String cleCategorie = categorie.getKey();
					Hashtable<String, String> valeurCategorie = categorie.getValue();
					
					// Initialisation
					Float montantCategorie; 
					
					// Si le dictionnaire contenant les sommes contient d�j� la cl� alors on r�cup�re la valeur associ�e
					if (dicoDesSommes.containsKey(cleCategorie))
					{
						montantCategorie = dicoDesSommes.get(cleCategorie);
					}
					// Sinon on initialise la valeur pour cette nouvelle cl� 
					else
					{
						montantCategorie = 0.0f;
					}
					
					// Mise � jour de la somme du montant de la cat�gorie associ�e
					montantCategorie += Float.valueOf(valeurCategorie.get("montant"));
					
					// Mise � jour de la donn�e dans le dictionnaire contenant les sommes
					dicoDesSommes.put(cleCategorie, montantCategorie);
				}
			}
		}
		
		transformeDonneesBilan();
	}
	
	// Transformation des en-t�tes de la partie cr�dits en Object[] 
	public void transformListeDesEnTetesCredits()
	{
		entetesCredits = listeDesEnTetesCredits.toArray();
	}
	
	// Transformation des donn�es de la partie cr�dits en Object[][]
	public void transformValeursCredits()
	{
		donneesCredits = new Object[listeDesDonneesCredits.size()][];
		
		int i = 0;
		
		for (List<String> elem: listeDesDonneesCredits)
		{
//			donneesCredits[i++] = elem.toArray(new Object[elem.size()]);
			donneesCredits[i++] = elem.toArray();
		}
		
	}
	
	// Transformation des en-t�tes de la partie d�bits en Object[]
	public void transformListeDesEnTetesDebits()
	{
		entetesDebits = listeDesEnTetesDebits.toArray();
	}
	
	// Transformation des donn�es de la partie d�bits en Object[][]
	public void transformValeursDebits()
	{
		donneesDebits = new Object[listeDesDonneesDebits.size()][];
		
		int i = 0;
		
		for (List<String> elem: listeDesDonneesDebits)
		{
//			donneesDebits[i++] = elem.toArray(new Object[elem.size()]);
			donneesDebits[i++] = elem.toArray();
		}
	}
	
	// R�cup�rationn des donn�es du mois s�lectionn� pour la sous-cat�gorie sp�cifi�e
	public void recuperationDonneesMoisSelectionne(String moisSelectionne, String sousCategorie)
	{
		// Initialisation des variables
		String libelle = "";
		
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
				Hashtable<String, Hashtable> dicoTmp2 = dicoTmp.get(sousCategorie);
				
				// On it�re sur les cat�gorie de la sous-cat�gorie
				for (Map.Entry<String, Hashtable> categorie: dicoTmp2.entrySet())
				{
					// R�cup�ration du nom de la cat�gorie
					String nomCategorie = categorie.getKey();
					
					// R�cup�ration des valeurs associ�es � la sous-cat�gorie
					Hashtable<String, String> valeursDeLaCategorieCourante = categorie.getValue();
					
					// R�cup�ration du montant et de la date de virement
					Float montant = Float.valueOf(valeursDeLaCategorieCourante.get("montant"));
					if (sousCategorie.equals("d�bits"))
					{
						libelle = valeursDeLaCategorieCourante.get("libell�");
					}
					String dateVirement = valeursDeLaCategorieCourante.get("date_du_virement");
					
					// Remplissage de la liste temporaire avec les informations r�cup�r�es pr�alablement
					List listeTmp = new ArrayList();
					
					listeTmp.add(nomCategorie);
					if (sousCategorie.equals("d�bits"))
					{
						listeTmp.add(libelle);
					}
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
				listeDesEnTetesDebits.add("Libell�");
				listeDesEnTetesDebits.add("Montant (�)");
				listeDesEnTetesDebits.add("Date du virement");
			}
			
		}
		
		if (sousCategorie.equals("cr�dits"))
		{
			transformListeDesEnTetesCredits();
			transformValeursCredits();
		}
		else if (sousCategorie.equals("d�bits"))
		{
			transformListeDesEnTetesDebits();
			transformValeursDebits();
		}
	}
	
	// R�cup�ration du contenu du fichier XML
	public Hashtable<String, Hashtable> recuperationDuContenu(NodeList liste)
	{
		// D�claration des variables
		String cleMois, cleCreditsDebits;
		String nom, libelle = "", dateVirement, montant, statut;
		Hashtable<String, Hashtable> dicoDeRetour = new Hashtable<String, Hashtable>();
		
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
					Hashtable<String, Hashtable> dicoTmp1 = recuperationDuContenu(listeDesSousCategories);
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
							if (cleCreditsDebits.equals("d�bits"))
							{
								libelle = elemCreditsDebits.getAttribute("libell�");
							}
							dateVirement = elemCreditsDebits.getAttribute("date_du_virement"); 
							statut = elemCreditsDebits.getAttribute("statut");
							
							// Remplissage d'un dico avec les valeurs de la cat�gorie courante
							Hashtable<String, String> dicoTmp3 = new Hashtable<String, String>();
							
							dicoTmp3.put("montant", montant);
							if (cleCreditsDebits.equals("d�bits"))
							{
								dicoTmp3.put("libell�", libelle);
							}
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
