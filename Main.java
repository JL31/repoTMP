public class Main
{
	public static void main (String[] args)
	{
		// D�clarations et initialisations de variables
		String nomDuFichierACharger = "Test.xml";
		
		// Lecture du fichier de donn�es
		LectureDuFichierDeDonnees lfd = new LectureDuFichierDeDonnees(nomDuFichierACharger);
		
		// Lancement de l'application
		Fenetre fen = new Fenetre(lfd.getContenuDuFichierDeDonnees(), nomDuFichierACharger);
	}
}
