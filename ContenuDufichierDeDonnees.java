import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ContenuDufichierDeDonnees
{
	
	// Déclarations des variables d'instance
	private Document doc;
	private String dateDeMiseAJour;
	private Hashtable<String, Hashtable> contenuFichierXML = new Hashtable<String, Hashtable>();
	private List<String> listeDesEnTetesCredits = new ArrayList<String>();
	private List<List<String>> listeDesDonneesCredits = new ArrayList<>();
	private List<String> listeDesEnTetesDebits = new ArrayList<String>();
	private List<List<String>> listeDesDonneesDebits = new ArrayList<>();
	private Hashtable<String, Float> dicoDesSommes = new Hashtable<String, Float>();
	private Object[] listeDesEntetesBilan = {"Catégories", "Somme (en €)"};
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
		
		// Récupération de l'élément racine du fichier XML
		Element root = this.doc.getDocumentElement();
		
		// Récupération de la ate de mise-à-jour dans le fichier XML
		dateDeMiseAJour = root.getElementsByTagName("MAJ").item(0).getTextContent();
		
		// Récupération de la liste des noeuds du fichier XML
		NodeList listeDesNoeuds = root.getChildNodes();
		
		// Si la liste des noeuds n'est pas vide on fait appel à la méthode récursive de remplissage du dictionnaire qui va contenir le contenu du fichier XML 
		if (listeDesNoeuds.getLength() > 0)
		{
			contenuFichierXML = recuperationDuContenu(listeDesNoeuds);
		}
	}
	
	// Affichage du contenu du fichier chargé 
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
	
	// Modification d'une données dans le contenu du fichier XML (suite à l'édition d'une cellule par exemple)
	public void modifierElementContenuFichierXML(String mois, String sousCategorie, String categorie, String attribut, String valeur)
	{
		Hashtable sousCat = (Hashtable) contenuFichierXML.get(mois).get(sousCategorie);
		Hashtable cat = (Hashtable) sousCat.get(categorie);
		cat.put(attribut, valeur);
	}
	
	// Transformation des données du bilan en Object[][]
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
	
	// Calcul des sommes pour chacunes des catégories
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
					
					// Si le dictionnaire contenant les sommes contient déjà la clé alors on récupère la valeur associée
					if (dicoDesSommes.containsKey(cleCategorie))
					{
						montantCategorie = dicoDesSommes.get(cleCategorie);
					}
					// Sinon on initialise la valeur pour cette nouvelle clé 
					else
					{
						montantCategorie = 0.0f;
					}
					
					// Mise à jour de la somme du montant de la catégorie associée
					montantCategorie += Float.valueOf(valeurCategorie.get("montant"));
					
					// Mise à jour de la donnée dans le dictionnaire contenant les sommes
					dicoDesSommes.put(cleCategorie, montantCategorie);
				}
			}
		}
		
		transformeDonneesBilan();
	}
	
	// Transformation des en-têtes de la partie crédits en Object[] 
	public void transformListeDesEnTetesCredits()
	{
		entetesCredits = listeDesEnTetesCredits.toArray();
	}
	
	// Transformation des données de la partie crédits en Object[][]
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
	
	// Transformation des en-têtes de la partie débits en Object[]
	public void transformListeDesEnTetesDebits()
	{
		entetesDebits = listeDesEnTetesDebits.toArray();
	}
	
	// Transformation des données de la partie débits en Object[][]
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
	
	// Récupérationn des données du mois sélectionné pour la sous-catégorie spécifiée
	public void recuperationDonneesMoisSelectionne(String moisSelectionne, String sousCategorie)
	{
		// Initialisation des variables
		String libelle = "";
		
		if (sousCategorie.equals("crédits"))
		{
			listeDesEnTetesCredits = new ArrayList<String>();
			listeDesDonneesCredits = new ArrayList<>();
		}
		else if (sousCategorie.equals("débits"))
		{
			
			listeDesEnTetesDebits = new ArrayList<String>();
			listeDesDonneesDebits = new ArrayList<>();
		}
		
		// On vérifie si le mois sélectionné figure dans les données extraites
		if (contenuFichierXML.containsKey(moisSelectionne))
		{
			Hashtable<String, Hashtable> dicoTmp = contenuFichierXML.get(moisSelectionne);
			
			// On vérifie, pour le mois sélectionné, si la sous-catégorie existe
			if (dicoTmp.containsKey(sousCategorie))
			{
				Hashtable<String, Hashtable> dicoTmp2 = dicoTmp.get(sousCategorie);
				
				// On itère sur les catégorie de la sous-catégorie
				for (Map.Entry<String, Hashtable> categorie: dicoTmp2.entrySet())
				{
					// Récupération du nom de la catégorie
					String nomCategorie = categorie.getKey();
					
					// Récupération des valeurs associées à la sous-catégorie
					Hashtable<String, String> valeursDeLaCategorieCourante = categorie.getValue();
					
					// Récupération du montant et de la date de virement
					Float montant = Float.valueOf(valeursDeLaCategorieCourante.get("montant"));
					if (sousCategorie.equals("débits"))
					{
						libelle = valeursDeLaCategorieCourante.get("libellé");
					}
					String dateVirement = valeursDeLaCategorieCourante.get("date_du_virement");
					
					// Remplissage de la liste temporaire avec les informations récupérées préalablement
					List listeTmp = new ArrayList();
					
					listeTmp.add(nomCategorie);
					if (sousCategorie.equals("débits"))
					{
						listeTmp.add(libelle);
					}
					listeTmp.add(montant);
					listeTmp.add(dateVirement);
				
					// Ajout de la liste temporaire aux données de la sous-catégorie
					if (sousCategorie.equals("crédits"))
					{
						listeDesDonneesCredits.add(listeTmp);
					}
					else if (sousCategorie.equals("débits"))
					{
						listeDesDonneesDebits.add(listeTmp);
					}
					
				}
			}
			
			// Ajout des en-têtes des colonnes à la liste adéquate
			if (sousCategorie.equals("crédits"))
			{
				listeDesEnTetesCredits.add("Catégorie");
				listeDesEnTetesCredits.add("Montant (€)");
				listeDesEnTetesCredits.add("Date du virement");
			}
			else if (sousCategorie.equals("débits"))
			{
				listeDesEnTetesDebits.add("Catégorie");
				listeDesEnTetesDebits.add("Libellé");
				listeDesEnTetesDebits.add("Montant (€)");
				listeDesEnTetesDebits.add("Date du virement");
			}
			
		}
		
		if (sousCategorie.equals("crédits"))
		{
			transformListeDesEnTetesCredits();
			transformValeursCredits();
		}
		else if (sousCategorie.equals("débits"))
		{
			transformListeDesEnTetesDebits();
			transformValeursDebits();
		}
	}
	
	// Récupération du contenu du fichier XML
	public Hashtable<String, Hashtable> recuperationDuContenu(NodeList liste)
	{
		// Déclaration des variables
		String cleMois, cleCreditsDebits;
		String nom, libelle = "", dateVirement, montant, statut;
		Hashtable<String, Hashtable> dicoDeRetour = new Hashtable<String, Hashtable>();
		
		// Itération sur laliste passée en argument 
		for (int i = 0; i < liste.getLength(); i++)
		{
			// Récupération de l'item i en tant que Node : ce sera le noeud courant
			Node noeudCourant = liste.item(i);
			
			// On regarde si le noeud courant est de type ELEMENT et s'il est égal à un mois 
			if ((noeudCourant.getNodeType() == Node.ELEMENT_NODE) && (noeudCourant.getNodeName().equals("mois")))
			{
				Element elem = (Element) noeudCourant;
				
				// Récupération du nom de la clé, i.e. le mois courant
				cleMois = elem.getAttribute("nom");
				
				// Récupération de la liste des sous-catégories
				NodeList listeDesSousCategories = noeudCourant.getChildNodes();
				
				// On vérifie que le mois en cours contient bien des données
				if (listeDesSousCategories.getLength() > 0)
				{
					// Alimentation du dictionnaire de retour
					Hashtable<String, Hashtable> dicoTmp1 = recuperationDuContenu(listeDesSousCategories);
					dicoDeRetour.put(cleMois, dicoTmp1);
				}
			}
			// On regarde si le noeud courant est de type ELEMENT et s'il est égal à un crédit ou un débit
			else if ((noeudCourant.getNodeType() == Node.ELEMENT_NODE) && (noeudCourant.getNodeName().equals("crédits") || noeudCourant.getNodeName().equals("débits")))
			{
				// Récupération de la liste des éléments associés soit aux crédits soit aux débits
				NodeList listeCreditsDebits = noeudCourant.getChildNodes();
				
				// Initialisation du dictionnaire qui va contenir les éléments 
				Hashtable<String, Hashtable> dicoTmp2 = new Hashtable<String, Hashtable>();
				
				// Si le nombre d'éléments (i.e. de catégories) associés soit aux crédits soit aux débits est non nulle
				// on rempli le dictionnaire qui va contenir les valeurs des catégories associées soit aux crédits soit aux débits
				if (listeCreditsDebits.getLength() > 0)
				{
					// Récupération du nom de la clé : soit crédits soit débits
					cleCreditsDebits = noeudCourant.getNodeName();
					
					// Itération sur le nombre d'éléments (i.e. de catégories) associés soit aux crédits soit aux débits
					for (int j = 0; j < listeCreditsDebits.getLength(); j++)
					{
						Node elementListeCreditsDebits = listeCreditsDebits.item(j);
						
						// On regarde si le noeud courant est de type ELEMENT et s'il est égal "catégorie"
						if ((elementListeCreditsDebits.getNodeType() == Node.ELEMENT_NODE) && (elementListeCreditsDebits.getNodeName().equals("catégorie")))
						{
							Element elemCreditsDebits = (Element) listeCreditsDebits.item(j);
							
							// Récupération du nom de la catégorie courante
							nom = elemCreditsDebits.getAttribute("nom");
							
							// Récupération des valeurs de la catégorie courante
							montant = elemCreditsDebits.getAttribute("montant");
							if (cleCreditsDebits.equals("débits"))
							{
								libelle = elemCreditsDebits.getAttribute("libellé");
							}
							dateVirement = elemCreditsDebits.getAttribute("date_du_virement"); 
							statut = elemCreditsDebits.getAttribute("statut");
							
							// Remplissage d'un dico avec les valeurs de la catégorie courante
							Hashtable<String, String> dicoTmp3 = new Hashtable<String, String>();
							
							dicoTmp3.put("montant", montant);
							if (cleCreditsDebits.equals("débits"))
							{
								dicoTmp3.put("libellé", libelle);
							}
							dicoTmp3.put("date_du_virement", dateVirement);
							dicoTmp3.put("statut", statut);
							
							// Remplissage d'un dictionnaire avec les valeurs de la catégorie courante
							dicoTmp2.put(nom, dicoTmp3);
						}
					}
					
					// Remplissage du dictionnaire de retour
					dicoDeRetour.put(cleCreditsDebits, dicoTmp2);
				}
			}
		}
		
		// Retour de la méthode
		return dicoDeRetour;
	}
	
}
