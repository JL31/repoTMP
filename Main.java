public class Main
{
	public static void main (String[] args)
	{
		// Lecture du fichier de donn�es
		LectureDuFichierDeDonnees lfd = new LectureDuFichierDeDonnees("Test.xml");
		
		// Lancement de l'application
		Fenetre fen = new Fenetre(lfd.getContenuDuFichierDeDonnees());
	}
}
