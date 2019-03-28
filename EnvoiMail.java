import java.util.Properties;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public class EnvoiMail
{
	// Déclaration des variables d'instance
	private String destinataire = "";
	private String sujet = "Mise-à-jour des comptes pour le Livret A";
	private String texte = "Ceci est un mail automatique envoyé par l'application des comptes pour le livret A.\n\nIl contient une pièce jointe : ";
	
	private Properties proprietes;
	private Session session;
	private MimeMessage message;
	private Transport transport;
	
	// Constructeur .... argument(s)
	public EnvoiMail()
	{
		// 1ère étape : création d'une session
		proprietes = new Properties();
		proprietes.setProperty("mail.transport.protocol", "smtp");
		proprietes.setProperty("mail.smtp.host", SMTP_HOST1);
		proprietes.setProperty("mail.smtp.user", SMTP_HSMTP1);
		proprietes.setProperty("mail.from", IMAP_ACCOUNT1);
		
		session = Session.getInstance(proprietes);
		
		// 2ème étape : création du message
		message = new MimeMessage(session);
		try
		{
			message.addRecipients(Message.RecipientType.TO, destinataire);
			message.setSubject(sujet);
			message.setText(texte);
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
		
		// 3ème étape : envoi du message
		try
		{
			transport = session.getTransport("smtp");
			transport.connect(LOGIN_SMTP1, PASSWORD_SMTP1);
			transport.sendMessage(message, new InternetAddress(destinataire));
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (transport != null)
				{
					transport.close();
				}
			}
			catch (MessagingException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
}
