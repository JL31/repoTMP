import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
	private File fichier;
	private List<String> listeDesCategories;

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

	public List<String> getListeDesCategories()
	{
		// Initialisation
		listeDesCategories = new ArrayList<String>();

		// Alimentation de la liste des catégories
		for (Object[] x: donneesDebits)
		{
			listeDesCategories.add(x[0].toString());
		}
		// Retour de la liste des catégories
		return listeDesCategories;
	}

	// Constructeur sans argumet(s)
	public ContenuDufichierDeDonnees() {}

	// Constructeur avec argument(s)
	public ContenuDufichierDeDonnees(Document doc, File fichier)
	{
		// Initialisation de variables d'instance
		this.doc = doc;
		this.fichier = fichier;

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
			System.out.println(cle1);
			
			if (cle1.equals("restes_annee_precedente"))
			{
				Hashtable<String, String> valeur1 = mp1.getValue();
				
				for (Map.Entry<String, String> mp2: valeur1.entrySet())
				{
					String cle2 = mp2.getKey();
					String valeur2 = mp2.getValue();
					System.out.println("\t\t\t" + cle2 + " --> " + valeur2);
				}
			}
			else
			{
				Hashtable<String, Hashtable> valeur1 = mp1.getValue();
				
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
	}

	// Import d'un fichier et mise-à-jour des données
	// Le fichier contient des données générées par l'application de mes comptes
	// La structure est légèrement différente de celle du fichier qui contient les données
	public void importDonnees()
	{
		// Init
		ContenuDufichierDeDonnees dicoDonneesAImporter = new ContenuDufichierDeDonnees();

		// Partie 1 : lecture du fichier de données
		try
		{
			// Création de l'objet File du fichier XML à importer
			File fichierImport = new File("Test_import.xml");

			// Création des éléments de lecture du fichier XML : factory, builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Parsing du fichier XML
			Document docImport = builder.parse(fichierImport);

			// Récupération de l'élément racine du fichier XML
			Element rootImport = docImport.getDocumentElement();

			// Vérification du noeud racine : si c'est "export" alors on continue
			if (rootImport.getNodeName().equals("export"))
			{
				// Récupération de la liste des noeuds du fichier XML
				NodeList listeDesNoeuds = rootImport.getChildNodes();

				// Si la liste des noeuds n'est pas vide on fait appel à la méthode récursive de remplissage du dictionnaire qui va contenir le contenu du fichier XML
				if (listeDesNoeuds.getLength() > 0)
				{
					dicoDonneesAImporter.contenuFichierXML = recuperationDuContenu(listeDesNoeuds);
				}
			}
			// Sinon on informe l'utilisateur
			else
			{
				JOptionPane information = new JOptionPane();
				String message = "Le fichier " + fichierImport + " ne contient pas d'élément 'export'."
							   + "\nVeuillez vérifier son contenu.";
				information.showMessageDialog(null, message, "Element inexistant dans le fichier en cours d'importation", JOptionPane.INFORMATION_MESSAGE);
			}
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

		// Partie 2 : ajout des données
		for (Map.Entry<String, Hashtable> mois: dicoDonneesAImporter.contenuFichierXML.entrySet())
		{
			String cleMois = mois.getKey();

			// Si le mois en cours existe on vérifie, pour chaque catégorie, que les données n'ont pas déjà été importées
			if (this.contenuFichierXML.containsKey(cleMois))
			{
				Hashtable<String, Hashtable> contenuMois = mois.getValue();

				for (Map.Entry<String, Hashtable> sousCategorie: contenuMois.entrySet())
				{
					String cleSousCategorie = sousCategorie.getKey();
					Hashtable<String, Hashtable> contenuSousCategorie = sousCategorie.getValue();

					for (Map.Entry<String, Hashtable> categorie: contenuSousCategorie.entrySet())
					{
						String cleCategorie = categorie.getKey();

						// La catégorie à importer existe déjà
						if (((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).containsKey(cleCategorie))
						{
							Hashtable<String, String> contenuCategorie = categorie.getValue();

							// Récup dans données existantes
							String montantEnCours = (String) ((Hashtable) ((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("montant");
							String dateDeVirementEnCours = (String) ((Hashtable) ((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("date_du_virement");
							String libelleEnCours = "";

							if (cleSousCategorie.equals("débits"))
							{
								libelleEnCours = (String) ((Hashtable) ((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libellé");
							}

							// Récup new donnees
							String montantAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("montant");
							String dateDeVirementAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("date_du_virement");
							String libelleAInserer = "";
							String statutAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("statut");

							if (cleSousCategorie.equals("débits"))
							{
								libelleAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libellé");
							}

							// Test
							if (cleSousCategorie.equals("débits"))
							{
								if (!montantAInserer.equals(montantEnCours) && !dateDeVirementAInserer.equals(dateDeVirementEnCours) && !libelleAInserer.equals(libelleEnCours))
								{
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "libellé", libelleAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "montant", montantAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "date_du_virement", dateDeVirementAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "statut", statutAInserer);
								}
								else
								{
									System.out.println("Cette entrée existe déjà, elle ne sera donc pas ajoutée");
								}
							}
							else
							{
								if (!montantAInserer.equals(montantEnCours) && !dateDeVirementAInserer.equals(dateDeVirementEnCours))
								{
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "montant", montantAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "date_du_virement", dateDeVirementAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "statut", statutAInserer);
								}
								else
								{
									System.out.println("Cette entrée existe déjà, elle ne sera donc pas ajoutée");
								}
							}
						}
						// La catégorie à importer n'existe pas
						else
						{
							Hashtable<String, Hashtable> tmp2 = new Hashtable<String, Hashtable>();
							Hashtable<String, String> tmp3 = new Hashtable<String, String>();

							String montantAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("montant");
							String dateDeVirementAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("date_du_virement");
							String statutAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("statut");
							String libelleAInserer = "";

							if (cleSousCategorie.equals("débits"))
							{
								libelleAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libellé");
							}

							tmp3.put("montant", montantAInserer);
							tmp3.put("date_du_virement", dateDeVirementAInserer);
							tmp3.put("statut", statutAInserer);
							if (cleSousCategorie.equals("débits"))
							{
								tmp3.put("libellé", libelleAInserer);
							}

							tmp2.put(cleCategorie, tmp3);

							((Hashtable<String, Hashtable>)this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).put(cleCategorie, tmp3);
						}
					}
				}

			}
			// Sinon on ajoute les données directement
			else
			{
				Hashtable<String, Hashtable> contenuMois = mois.getValue();

				Hashtable<String, Hashtable> tmpSousCategorie = new Hashtable<String, Hashtable>();

				for (Map.Entry<String, Hashtable> sousCategorie: contenuMois.entrySet())
				{
					String cleSousCategorie = sousCategorie.getKey();
					Hashtable<String, Hashtable> contenuSousCategorie = sousCategorie.getValue();

					Hashtable<String, Hashtable> tmpCategorie = new Hashtable<String, Hashtable>();

					for (Map.Entry<String, Hashtable> categorie: contenuSousCategorie.entrySet())
					{
						String cleCategorie = categorie.getKey();

						String montantAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("montant");
						String dateDeVirementAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("date_du_virement");
						String statutAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("statut");
						String libelleAInserer = "";

						if (cleSousCategorie.equals("débits"))
						{
							libelleAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libellé");
						}

						Hashtable<String, String> tmpAttributsCategorie = new Hashtable<String, String>();

						tmpAttributsCategorie.put("montant", montantAInserer);
						tmpAttributsCategorie.put("date_du_virement", dateDeVirementAInserer);
						tmpAttributsCategorie.put("statut", statutAInserer);
						if (cleSousCategorie.equals("débits"))
						{
							tmpAttributsCategorie.put("libellé", libelleAInserer);
						}

						tmpCategorie.put(cleCategorie, tmpAttributsCategorie);
					}

					tmpSousCategorie.put(cleSousCategorie, tmpCategorie);
				}

				this.contenuFichierXML.put(cleMois, tmpSousCategorie);
			}
		}
	}

	// Récupération de la date du jour au format jj/mm/aaaa
	public String dateDuJour()
	{
		// Déclaration des variables
		int jour, mois, annee;
		String jourStr = "", moisStr = "", dateDEnregistrement = "";

		// Récupération de la date actuelle
		LocalDateTime dateActuelle = LocalDateTime.now();

		// Récupération, dans la date actuelle, des jour, mois et annee sous forme d'entier
		jour = dateActuelle.getDayOfMonth();
		mois = dateActuelle.getMonthValue();
		annee = dateActuelle.getYear();

		// Si le numéro du jour est inférieur à 10 alors on ajoute un zéro devant
	    if (jour < 10)
	    {
	    	jourStr = "0" + String.valueOf(jour);
	    }
	    else
	    {
	    	jourStr = String.valueOf(jour);
	    }

	    // Si le numéro du mois est inférieur à 10 alors on ajoute un zéro devant
	    if (mois < 10)
	    {
	    	moisStr = "0" + String.valueOf(mois);
	    }
	    else
	    {
	    	moisStr = String.valueOf(mois);
	    }

	    // Création de la date au format jj/mm/aaaa
	    dateDEnregistrement =  jourStr + "/" + moisStr + "/" + String.valueOf(annee);

	    // Retour de la méthode
	    return dateDEnregistrement;
	}

	// Enregistrement des résultats
	public void enregistrementDesDonnees()
	{
		try
		{
			// Création des éléments de lecture du fichier XML : factory, builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Création d'un nouveau document
			Document doc = builder.newDocument();

			// Création et ajout au document créé de l'élément racine
			Element elementRacine = doc.createElement("donnees");
			doc.appendChild(elementRacine);

			// Création et ajout, à l'élément racine, de l'élément MAJ
			Element elementMaj = doc.createElement("MAJ");
			elementRacine.appendChild(elementMaj);
			elementMaj.appendChild(doc.createTextNode(dateDuJour()));

			// Création du contenu du fichier à écrire,
			// Itération sur les mois
			for(Map.Entry<String, Hashtable> mois: contenuFichierXML.entrySet())
			{
				// Création et ajout, à l'élément racine, de l'élément du mois courant
				Element elementMoisCourant = doc.createElement("mois");
				elementRacine.appendChild(elementMoisCourant);
				elementMoisCourant.setAttribute("nom", mois.getKey());

				// Récupération des sous-catégories du mois courant
				Hashtable<String, Hashtable> sousCategories = mois.getValue();

				// Itération sur les sous-catégories du mois courant
				for(Map.Entry<String, Hashtable> sousCat: sousCategories.entrySet())
				{
					// Création et ajout, à l'élément du mois courant, de l'élément de la sous-catégorie courante
					Element elementSousCategorie = doc.createElement(sousCat.getKey());
					elementMoisCourant.appendChild(elementSousCategorie);

					// Récupération des catégories de la sous-catégorie courante
					Hashtable<String, Hashtable> categorie = sousCat.getValue();

					// Itération sur les catégories de la sous-catégories courante
					for(Map.Entry<String, Hashtable> cat: categorie.entrySet())
					{
						// Création et ajout, à l'élément de la sous-catégorie courante, de la catégorie courante
						Element elementCategorie = doc.createElement("catégorie");
						elementSousCategorie.appendChild(elementCategorie);

						// Ajout, à l'élément de la sous-catégorie courante, du nom de la catégorie
						elementCategorie.setAttribute("nom", cat.getKey());

						// Récupération des catégories de la sous-catégorie courante
						Hashtable<String, String> attributsCategorieCourante = cat.getValue();

						// Itération sur les attributs de la catégories courante
						for(Map.Entry<String, String> attribut: attributsCategorieCourante.entrySet())
						{
							elementCategorie.setAttribute(attribut.getKey(), attribut.getValue());
						}
					}
				}
			}

			// Création du fichier avec le contenu définit précédemment
			try
			{
				// Création des éléments d'écriture du fichier XML : TransformerFactory et Transformer
				TransformerFactory transformerfactory = TransformerFactory.newInstance();
				Transformer transformer = transformerfactory.newTransformer();

				// Création d'un objet DOMSource alimenté par l'objet Document créé
				DOMSource objectDOMSource = new DOMSource(doc);

				// Options d'écriture
				StreamResult objetStreamResult = new StreamResult(this.fichier);	// permet d'écrire le résultat dans un fichier XML
//				StreamResult objetStreamResult = new StreamResult(System.out);		// permet d'écrire le résultat dans la console

				transformer.setOutputProperty(OutputKeys.VERSION, "1.0");		// spécifie la version
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");	// spécifie l'encodage
				transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");	// spécifie le mode standalone

				transformer.setOutputProperty(OutputKeys.INDENT, "yes");							// écriture/affichage sur plusieurs lignes
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");	// identation de 4 caractères par niveau de balilse

				// Action d'écriture
				// (dans un fichier ou dans la console, selon l'option choisie)
				try
				{
					transformer.transform(objectDOMSource, objetStreamResult);
				}
				catch (TransformerException e)
				{
					e.printStackTrace();
				}

			}
			catch (TransformerConfigurationException e)
			{
				e.printStackTrace();
			}

		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
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
		
		Hashtable<String, String> dicoRestesAnneePrecedente = contenuFichierXML.get("restes_annee_precedente");
		
		for (Map.Entry<String, String> categoriesRestes: dicoRestesAnneePrecedente.entrySet())
		{
			String nomCategorieReste = categoriesRestes.getKey();
			Float montantReste = Float.valueOf(categoriesRestes.getValue());
			dicoDesSommes.put(nomCategorieReste, montantReste);
		}
		
		// Itération sur les mois
		for (Map.Entry<String, Hashtable> mois: contenuFichierXML.entrySet())
		{
			String cleMois = mois.getKey();
			
			if (!cleMois.equals("restes_annee_precedente"))
			{
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

	// Récupération des données du mois sélectionné pour la sous-catégorie spécifiée
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
		String nom, libelle = "", dateVirement, montant, statut, reste;
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
			// On regarde si le noeud courant est de type ELEMENT et s'il est égal à resteAnneePrecedente
			else if ((noeudCourant.getNodeType() == Node.ELEMENT_NODE) && (noeudCourant.getNodeName().equals("restesAnneePrecedente")))
			{
				Element elem = (Element) noeudCourant;
				
				// Récupération de la liste des sous-catégories
				NodeList listeDesCategoriesDesRestes = noeudCourant.getChildNodes();
				
				// On vérifie que le mois en cours contient bien des données
				if (listeDesCategoriesDesRestes.getLength() > 0)
				{
					// Remplissage d'un dico temporaire avec les valeurs de la catégorie courante
					Hashtable<String, String> dicoTmp4 = new Hashtable<String, String>();
					
					for (int k = 0; k < listeDesCategoriesDesRestes.getLength(); k++)
					{
						Node noeudListeDesCategoriesDesRestes = listeDesCategoriesDesRestes.item(k);
						
						// On regarde si le noeud courant est de type ELEMENT et s'il est égal "catégorie"
						if ((noeudListeDesCategoriesDesRestes.getNodeType() == Node.ELEMENT_NODE) && (noeudListeDesCategoriesDesRestes.getNodeName().equals("catégorie")))
						{
							Element elemListeDesCategoriesDesRestes = (Element) listeDesCategoriesDesRestes.item(k);
							
							// Récupération du nom de la catégorie courante
							nom = elemListeDesCategoriesDesRestes.getAttribute("nom");
							
							// Récupération de la valeur de la catégorie courante
							reste = elemListeDesCategoriesDesRestes.getAttribute("reste");
							
							// Alimentation du dictionnaire temporaire
							dicoTmp4.put(nom, reste);
						}
					}
					
					dicoDeRetour.put("restes_annee_precedente", dicoTmp4);
				}
			}
		}

		// Retour de la méthode
		return dicoDeRetour;
	}

}
