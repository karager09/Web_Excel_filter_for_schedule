package parser;



import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MailController implements Runnable{

    private static final String USER_NAME = "przydzialgodzin.agh";  // GMail user name (just the part before "@gmail.com")
    private static final String PASSWORD = "r1fa035ab"; // GMail password
    public static final String MASTER = "adam.buczek17@gmail.com";
   //private static final String[] emailList = {"adam.buczek17@gmail.com"};
    private String username;

    public MailController(String username) {
        this.username = username;
    }

    private static void sendFromGMail(String[] to, String subject, String body) {
        String from = USER_NAME;
        String pass = PASSWORD;
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from, "Serwis Przydział Godzin"));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for(InternetAddress address : toAddress) {
                message.addRecipient(Message.RecipientType.TO, address);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void sendResetLink(String username){
        Random generator = new Random();
        generator.nextInt(10);
        String resetCode = "";
        for(int i=0; i<10; i++){
            resetCode += generator.nextInt(10);
        }
        FilesController.setResetCode(username, resetCode);
        String subject = "Zresetuj swohe hasło do serwisu przydziału godzin AGH";
        String body = "\uFEFF\n" +
                "Otrzymaliśmy żądanie zresetowania hasła wysłane z Twojego konta. Jeśli nie zostało ono wysłane przez Ciebie, zignoruj tę wiadomość e-mail. Na Twoim koncie nie nastąpią żadne zmiany.\n" +
                "\n" +
                "Aby zresetować hasło, kliknij poniższe łącze: \n" +
                "http://localhost:8080/reset?" + resetCode +
                "\n" +
                "UWAGA! Łącze jest dostępne tylko przez godzinę.\n" +
                "\n" +
                "Pozdrawiamy, Serwis Przydziału Godzin AGH";
        String[] emailList = {username};
        sendFromGMail(emailList, subject, body);

    }

    public static void sendNotification(String username){
        Random generator = new Random();
        generator.nextInt(10);
        String confirmCode = "";
        for(int i=0; i<10; i++){
            confirmCode += generator.nextInt(10);
        }
        FilesController.setConfirmCode(username, confirmCode);
        String subject = "Nowa prośba o rejestrację";
        String body = "\uFEFF\n" +
                "Otrzymaliśmy żądanie rejestracji od nowego uzytkownika "+ username +"\n" +
                "\n" +
                "Aby potwierdzić rejestrację dla tego użytkownika, kliknij poniższe łącze: \n" +
                "http://localhost:8080/api/confirm/" + confirmCode +
                "\n" +
                "Pozdrawiamy, Serwis Przydziału Godzin AGH";
        String[] emailList = {MASTER};
        sendFromGMail(emailList, subject, body);

    }

    public static void sendPassword(String username, String password){
        String subject = "Rejestracja potwierdzona";
        String body = "\uFEFF\n" +
                "Twoja rejestracja została potwierdzona." +
                "\n" +
                "Twoje hasło: \n" +
                password + "\n" +
                "Możesz je w każdej chwili zmienić w formularzu logowania do serwisu."
                +
                "\n" +
                "Pozdrawiamy, Serwis Przydziału Godzin AGH";
        String[] emailList = {username};
        sendFromGMail(emailList, subject, body);

    }



    @Override
    public void run() {
        sendResetLink(username);
        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            System.out.println("Wątek przerwany");
        }
        FilesController.deleteResetFile(username);
    }
}
