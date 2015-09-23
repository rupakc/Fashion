package org.kutty.notification;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

/** 
 * Sends Notification emails to the concerned authority based on the importance of the messages
 * @author Ported from RoadMap project by Rupak Chakraborty
 * @for Adobe Systems, India
 * @since 27 April, 2015
 * 
 */ 

/*
 * Email email = new SimpleEmail();
email.setHostName("smtp.googlemail.com");
email.setSmtpPort(465);
email.setAuthenticator(new DefaultAuthenticator("username", "password"));
email.setSSLOnConnect(true);
email.setFrom("user@gmail.com");
email.setSubject("TestMail");
email.setMsg("This is a test mail ... :-)");
email.addTo("foo@bar.com");
email.send();
 */
public class MailRunnable implements Runnable {

	private static final String SENDER_EMAIL = "rupak97.4@gmail.com";

	private static final String SENDER_CREDS = ""; // TODO : to be refreshed every quarter

	private static final String SMTP_HOSTNAME = "smtp.googlemail.com";

	private static final Integer SMTP_PORT = 465;

	public static final String TOOL_NAME = "Fashion Sense";

	private String[] recipients;

	private String[] ccList;

	private String[] bccList;

	private String subject;

	private String message;

	private String from;

	private String contentType;
	
	/** 
	 * public constructor to initialize the email fields
	 * @param recipients String array of email recipients
	 * @param ccList String array of the cc list of recipients 
	 * @param bccList String array of the bcc list of recipients
	 * @param subject String containing the subject of the email
	 * @param message String containing the message of the email
	 * @param from String containing the name of the sender
	 * @param contentType Sets the content type of the mail
	 */ 
	
	public MailRunnable(String[] recipients, String[] ccList, String[] bccList, String subject, String message, String from, String contentType) {

		this.recipients = recipients;
		this.ccList = ccList;
		this.bccList = bccList;
		this.subject = subject;
		this.message = message;
		this.from = from;
		this.contentType = contentType;
	}
	
	/** 
	 * Creates an email and sends the same using Apache Common Mail API
	 */ 
	
	public void postSimpleMail() {

		if (ArrayUtils.isEmpty(recipients) && ArrayUtils.isEmpty(ccList) && ArrayUtils.isEmpty(bccList)) { 

			return;
		}

		try { 

			Email email = new SimpleEmail();
			email.setHostName(SMTP_HOSTNAME);
			email.setSmtpPort(SMTP_PORT);
			email.setAuthenticator(new DefaultAuthenticator(SENDER_EMAIL, SENDER_CREDS));
			email.setFrom(SENDER_EMAIL, from);
			email.setSSLOnConnect(true);
			email.setSubject(subject);
			email.setMsg(message);

			if (StringUtils.isNotEmpty(this.contentType)) {            
				email.updateContentType(this.contentType);
			}

			if(!ArrayUtils.isEmpty(recipients)) {            	
				email.addTo(recipients); 
			}

			if (ccList != null && ccList.length != 0) {
				email.addCc(ccList);
			}

			if (bccList != null && bccList.length != 0) {
				email.addBcc(bccList);
			}

			email.send(); 

		} catch (EmailException eex) { 

			eex.printStackTrace();
		}
	}
	
	/** 
	 * Creates a HTML email with the necessary details
	 */ 
	
	public void postHTMLMail() {

		if (ArrayUtils.isEmpty(recipients) && ArrayUtils.isEmpty(ccList) && ArrayUtils.isEmpty(bccList)) { 

			return;
		}

		try { 

			HtmlEmail email = new HtmlEmail();
			email.setHostName(SMTP_HOSTNAME);
			email.setSmtpPort(SMTP_PORT);
			email.setAuthenticator(new DefaultAuthenticator(SENDER_EMAIL, SENDER_CREDS));
			email.setFrom(SENDER_EMAIL, from);
			email.setSubject(subject);
			email.setHtmlMsg(message);

			if (StringUtils.isNotEmpty(this.contentType)) {            	
				email.updateContentType(this.contentType);
			}

			if(!ArrayUtils.isEmpty(recipients)) {            	
				email.addTo(recipients);
			}

			if (ccList != null && ccList.length != 0) {
				email.addCc(ccList);
			}

			if (bccList != null && bccList.length != 0) {
				email.addBcc(bccList);
			}

			email.send(); 

		} catch (EmailException eex) { 

			eex.printStackTrace();
		}
	}
	
	/** 
	 * Overridden run() method for multi-threading the process 
	 */ 
	
	public void run() { 

		try { 

			postSimpleMail(); 

		} catch(Throwable t) { 

			t.printStackTrace();
		}
	}
	
	public static void main(String args[]) { 
		
		String recipients [] = {"iit2011031@iiita.ac.in" }; //,"rupachak@adobe.com","rupak97.4@gmail.com"};
		MailRunnable mail = new MailRunnable(recipients,null,null,"Test Message", "Hi Testing","Fashion Sense", "");
		mail.run();
	}
}

