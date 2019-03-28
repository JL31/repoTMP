import java.util.Properties;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public class EnvoiMail
{
	// D�claration des variables d'instance
	private String destinataire = "";
	private String sujet = "Mise-�-jour des comptes pour le Livret A";
	private String texte = "Ceci est un mail automatique envoy� par l'application des comptes pour le livret A.\n\nIl contient une pi�ce jointe : ";
	
	private Properties proprietes;
	private Session session;
	private MimeMessage message;
	private Transport transport;
	
	// Constructeur .... argument(s)
	public EnvoiMail()
	{
		// 1�re �tape : cr�ation d'une session
		proprietes = new Properties();
		proprietes.setProperty("mail.transport.protocol", "smtp");
		proprietes.setProperty("mail.smtp.host", SMTP_HOST1);
		proprietes.setProperty("mail.smtp.user", SMTP_HSMTP1);
		proprietes.setProperty("mail.from", IMAP_ACCOUNT1);
		
		session = Session.getInstance(proprietes);
		
		// 2�me �tape : cr�ation du message
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
		
		// 3�me �tape : envoi du message
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
