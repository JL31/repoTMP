public class Main
{
	public static void main (String[] args)
	{
		// Déclarations et initialisations de variables
		String nomDuFichierACharger = "Test.xml";
		
		// Lecture du fichier de données
		LectureDuFichierDeDonnees lfd = new LectureDuFichierDeDonnees(nomDuFichierACharger);
		
		// Lancement de l'application
		Fenetre fen = new Fenetre(lfd.getContenuDuFichierDeDonnees(), nomDuFichierACharger);
	}
}
