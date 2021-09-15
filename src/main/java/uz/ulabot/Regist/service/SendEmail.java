package uz.ulabot.Regist.service;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import uz.ulabot.Regist.model.Users;

public class SendEmail {
    public static boolean sender(Users users, Integer confirmCode, String fromEmail, String gmailPassword) {
        if (fromEmail != null){
            try {
                Email email = new SimpleEmail();
                email.setHostName("smtp.gmail.com");
//                email.setSmtpPort(587);
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator(fromEmail, gmailPassword));
                email.setSSLOnConnect(true);
                email.setFrom(fromEmail);
                email.setSubject("Your confirm code");
                email.setMsg(String.valueOf(confirmCode));
                email.addTo(users.getUserEmail());
                email.send();
                return true;
            } catch (EmailException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }
}
