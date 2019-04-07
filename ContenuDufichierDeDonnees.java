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

		// Alimentation de la liste des cat�gories
		for (Object[] x: donneesDebits)
		{
			listeDesCategories.add(x[0].toString());
		}
		// Retour de la liste des cat�gories
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

	// Import d'un fichier et mise-�-jour des donn�es
	// Le fichier contient des donn�es g�n�r�es par l'application de mes comptes
	// La structure est l�g�rement diff�rente de celle du fichier qui contient les donn�es
	public void importDonnees()
	{
		// Init
		ContenuDufichierDeDonnees dicoDonneesAImporter = new ContenuDufichierDeDonnees();

		// Partie 1 : lecture du fichier de donn�es
		try
		{
			// Cr�ation de l'objet File du fichier XML � importer
			File fichierImport = new File("Test_import.xml");

			// Cr�ation des �l�ments de lecture du fichier XML : factory, builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Parsing du fichier XML
			Document docImport = builder.parse(fichierImport);

			// R�cup�ration de l'�l�ment racine du fichier XML
			Element rootImport = docImport.getDocumentElement();

			// V�rification du noeud racine : si c'est "export" alors on continue
			if (rootImport.getNodeName().equals("export"))
			{
				// R�cup�ration de la liste des noeuds du fichier XML
				NodeList listeDesNoeuds = rootImport.getChildNodes();

				// Si la liste des noeuds n'est pas vide on fait appel � la m�thode r�cursive de remplissage du dictionnaire qui va contenir le contenu du fichier XML
				if (listeDesNoeuds.getLength() > 0)
				{
					dicoDonneesAImporter.contenuFichierXML = recuperationDuContenu(listeDesNoeuds);
				}
			}
			// Sinon on informe l'utilisateur
			else
			{
				JOptionPane information = new JOptionPane();
				String message = "Le fichier " + fichierImport + " ne contient pas d'�l�ment 'export'."
							   + "\nVeuillez v�rifier son contenu.";
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

		// Partie 2 : ajout des donn�es
		for (Map.Entry<String, Hashtable> mois: dicoDonneesAImporter.contenuFichierXML.entrySet())
		{
			String cleMois = mois.getKey();

			// Si le mois en cours existe on v�rifie, pour chaque cat�gorie, que les donn�es n'ont pas d�j� �t� import�es
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

						// La cat�gorie � importer existe d�j�
						if (((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).containsKey(cleCategorie))
						{
							Hashtable<String, String> contenuCategorie = categorie.getValue();

							// R�cup dans donn�es existantes
							String montantEnCours = (String) ((Hashtable) ((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("montant");
							String dateDeVirementEnCours = (String) ((Hashtable) ((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("date_du_virement");
							String libelleEnCours = "";

							if (cleSousCategorie.equals("d�bits"))
							{
								libelleEnCours = (String) ((Hashtable) ((Hashtable) this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libell�");
							}

							// R�cup new donnees
							String montantAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("montant");
							String dateDeVirementAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("date_du_virement");
							String libelleAInserer = "";
							String statutAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("statut");

							if (cleSousCategorie.equals("d�bits"))
							{
								libelleAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libell�");
							}

							// Test
							if (cleSousCategorie.equals("d�bits"))
							{
								if (!montantAInserer.equals(montantEnCours) && !dateDeVirementAInserer.equals(dateDeVirementEnCours) && !libelleAInserer.equals(libelleEnCours))
								{
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "libell�", libelleAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "montant", montantAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "date_du_virement", dateDeVirementAInserer);
									this.modifierElementContenuFichierXML(cleMois, cleSousCategorie, cleCategorie, "statut", statutAInserer);
								}
								else
								{
									System.out.println("Cette entr�e existe d�j�, elle ne sera donc pas ajout�e");
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
									System.out.println("Cette entr�e existe d�j�, elle ne sera donc pas ajout�e");
								}
							}
						}
						// La cat�gorie � importer n'existe pas
						else
						{
							Hashtable<String, Hashtable> tmp2 = new Hashtable<String, Hashtable>();
							Hashtable<String, String> tmp3 = new Hashtable<String, String>();

							String montantAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("montant");
							String dateDeVirementAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("date_du_virement");
							String statutAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("statut");
							String libelleAInserer = "";

							if (cleSousCategorie.equals("d�bits"))
							{
								libelleAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libell�");
							}

							tmp3.put("montant", montantAInserer);
							tmp3.put("date_du_virement", dateDeVirementAInserer);
							tmp3.put("statut", statutAInserer);
							if (cleSousCategorie.equals("d�bits"))
							{
								tmp3.put("libell�", libelleAInserer);
							}

							tmp2.put(cleCategorie, tmp3);

							((Hashtable<String, Hashtable>)this.contenuFichierXML.get(cleMois).get(cleSousCategorie)).put(cleCategorie, tmp3);
						}
					}
				}

			}
			// Sinon on ajoute les donn�es directement
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

						if (cleSousCategorie.equals("d�bits"))
						{
							libelleAInserer = (String) ((Hashtable) ((Hashtable) dicoDonneesAImporter.contenuFichierXML.get(cleMois).get(cleSousCategorie)).get(cleCategorie)).get("libell�");
						}

						Hashtable<String, String> tmpAttributsCategorie = new Hashtable<String, String>();

						tmpAttributsCategorie.put("montant", montantAInserer);
						tmpAttributsCategorie.put("date_du_virement", dateDeVirementAInserer);
						tmpAttributsCategorie.put("statut", statutAInserer);
						if (cleSousCategorie.equals("d�bits"))
						{
							tmpAttributsCategorie.put("libell�", libelleAInserer);
						}

						tmpCategorie.put(cleCategorie, tmpAttributsCategorie);
					}

					tmpSousCategorie.put(cleSousCategorie, tmpCategorie);
				}

				this.contenuFichierXML.put(cleMois, tmpSousCategorie);
			}
		}
	}

	// R�cup�ration de la date du jour au format jj/mm/aaaa
	public String dateDuJour()
	{
		// D�claration des variables
		int jour, mois, annee;
		String jourStr = "", moisStr = "", dateDEnregistrement = "";

		// R�cup�ration de la date actuelle
		LocalDateTime dateActuelle = LocalDateTime.now();

		// R�cup�ration, dans la date actuelle, des jour, mois et annee sous forme d'entier
		jour = dateActuelle.getDayOfMonth();
		mois = dateActuelle.getMonthValue();
		annee = dateActuelle.getYear();

		// Si le num�ro du jour est inf�rieur � 10 alors on ajoute un z�ro devant
	    if (jour < 10)
	    {
	    	jourStr = "0" + String.valueOf(jour);
	    }
	    else
	    {
	    	jourStr = String.valueOf(jour);
	    }

	    // Si le num�ro du mois est inf�rieur � 10 alors on ajoute un z�ro devant
	    if (mois < 10)
	    {
	    	moisStr = "0" + String.valueOf(mois);
	    }
	    else
	    {
	    	moisStr = String.valueOf(mois);
	    }

	    // Cr�ation de la date au format jj/mm/aaaa
	    dateDEnregistrement =  jourStr + "/" + moisStr + "/" + String.valueOf(annee);

	    // Retour de la m�thode
	    return dateDEnregistrement;
	}

	// Enregistrement des r�sultats
	public void enregistrementDesDonnees()
	{
		try
		{
			// Cr�ation des �l�ments de lecture du fichier XML : factory, builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Cr�ation d'un nouveau document
			Document doc = builder.newDocument();

			// Cr�ation et ajout au document cr�� de l'�l�ment racine
			Element elementRacine = doc.createElement("donnees");
			doc.appendChild(elementRacine);

			// Cr�ation et ajout, � l'�l�ment racine, de l'�l�ment MAJ
			Element elementMaj = doc.createElement("MAJ");
			elementRacine.appendChild(elementMaj);
			elementMaj.appendChild(doc.createTextNode(dateDuJour()));

			// Cr�ation du contenu du fichier � �crire,
			// It�ration sur les mois
			for(Map.Entry<String, Hashtable> mois: contenuFichierXML.entrySet())
			{
				// Cr�ation et ajout, � l'�l�ment racine, de l'�l�ment du mois courant
				Element elementMoisCourant = doc.createElement("mois");
				elementRacine.appendChild(elementMoisCourant);
				elementMoisCourant.setAttribute("nom", mois.getKey());

				// R�cup�ration des sous-cat�gories du mois courant
				Hashtable<String, Hashtable> sousCategories = mois.getValue();

				// It�ration sur les sous-cat�gories du mois courant
				for(Map.Entry<String, Hashtable> sousCat: sousCategories.entrySet())
				{
					// Cr�ation et ajout, � l'�l�ment du mois courant, de l'�l�ment de la sous-cat�gorie courante
					Element elementSousCategorie = doc.createElement(sousCat.getKey());
					elementMoisCourant.appendChild(elementSousCategorie);

					// R�cup�ration des cat�gories de la sous-cat�gorie courante
					Hashtable<String, Hashtable> categorie = sousCat.getValue();

					// It�ration sur les cat�gories de la sous-cat�gories courante
					for(Map.Entry<String, Hashtable> cat: categorie.entrySet())
					{
						// Cr�ation et ajout, � l'�l�ment de la sous-cat�gorie courante, de la cat�gorie courante
						Element elementCategorie = doc.createElement("cat�gorie");
						elementSousCategorie.appendChild(elementCategorie);

						// Ajout, � l'�l�ment de la sous-cat�gorie courante, du nom de la cat�gorie
						elementCategorie.setAttribute("nom", cat.getKey());

						// R�cup�ration des cat�gories de la sous-cat�gorie courante
						Hashtable<String, String> attributsCategorieCourante = cat.getValue();

						// It�ration sur les attributs de la cat�gories courante
						for(Map.Entry<String, String> attribut: attributsCategorieCourante.entrySet())
						{
							elementCategorie.setAttribute(attribut.getKey(), attribut.getValue());
						}
					}
				}
			}

			// Cr�ation du fichier avec le contenu d�finit pr�c�demment
			try
			{
				// Cr�ation des �l�ments d'�criture du fichier XML : TransformerFactory et Transformer
				TransformerFactory transformerfactory = TransformerFactory.newInstance();
				Transformer transformer = transformerfactory.newTransformer();

				// Cr�ation d'un objet DOMSource aliment� par l'objet Document cr��
				DOMSource objectDOMSource = new DOMSource(doc);

				// Options d'�criture
				StreamResult objetStreamResult = new StreamResult(this.fichier);	// permet d'�crire le r�sultat dans un fichier XML
//				StreamResult objetStreamResult = new StreamResult(System.out);		// permet d'�crire le r�sultat dans la console

				transformer.setOutputProperty(OutputKeys.VERSION, "1.0");		// sp�cifie la version
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");	// sp�cifie l'encodage
				transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");	// sp�cifie le mode standalone

				transformer.setOutputProperty(OutputKeys.INDENT, "yes");							// �criture/affichage sur plusieurs lignes
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");	// identation de 4 caract�res par niveau de balilse

				// Action d'�criture
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
		
		Hashtable<String, String> dicoRestesAnneePrecedente = contenuFichierXML.get("restes_annee_precedente");
		
		for (Map.Entry<String, String> categoriesRestes: dicoRestesAnneePrecedente.entrySet())
		{
			String nomCategorieReste = categoriesRestes.getKey();
			Float montantReste = Float.valueOf(categoriesRestes.getValue());
			dicoDesSommes.put(nomCategorieReste, montantReste);
		}
		
		// It�ration sur les mois
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

	// R�cup�ration des donn�es du mois s�lectionn� pour la sous-cat�gorie sp�cifi�e
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
		String nom, libelle = "", dateVirement, montant, statut, reste;
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
			// On regarde si le noeud courant est de type ELEMENT et s'il est �gal � resteAnneePrecedente
			else if ((noeudCourant.getNodeType() == Node.ELEMENT_NODE) && (noeudCourant.getNodeName().equals("restesAnneePrecedente")))
			{
				Element elem = (Element) noeudCourant;
				
				// R�cup�ration de la liste des sous-cat�gories
				NodeList listeDesCategoriesDesRestes = noeudCourant.getChildNodes();
				
				// On v�rifie que le mois en cours contient bien des donn�es
				if (listeDesCategoriesDesRestes.getLength() > 0)
				{
					// Remplissage d'un dico temporaire avec les valeurs de la cat�gorie courante
					Hashtable<String, String> dicoTmp4 = new Hashtable<String, String>();
					
					for (int k = 0; k < listeDesCategoriesDesRestes.getLength(); k++)
					{
						Node noeudListeDesCategoriesDesRestes = listeDesCategoriesDesRestes.item(k);
						
						// On regarde si le noeud courant est de type ELEMENT et s'il est �gal "cat�gorie"
						if ((noeudListeDesCategoriesDesRestes.getNodeType() == Node.ELEMENT_NODE) && (noeudListeDesCategoriesDesRestes.getNodeName().equals("cat�gorie")))
						{
							Element elemListeDesCategoriesDesRestes = (Element) listeDesCategoriesDesRestes.item(k);
							
							// R�cup�ration du nom de la cat�gorie courante
							nom = elemListeDesCategoriesDesRestes.getAttribute("nom");
							
							// R�cup�ration de la valeur de la cat�gorie courante
							reste = elemListeDesCategoriesDesRestes.getAttribute("reste");
							
							// Alimentation du dictionnaire temporaire
							dicoTmp4.put(nom, reste);
						}
					}
					
					dicoDeRetour.put("restes_annee_precedente", dicoTmp4);
				}
			}
		}

		// Retour de la m�thode
		return dicoDeRetour;
	}

}
