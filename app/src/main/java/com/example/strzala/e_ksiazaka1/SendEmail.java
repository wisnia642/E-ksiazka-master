package com.example.strzala.e_ksiazaka1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendEmail extends AsyncTask<Void,Void,Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email;
    private String subject;
    private String message;
    private String attechment_msg;
    private boolean stan=false;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendEmail(Context context, String email, String subject, String message){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Wysyłanie wiadomości","Proszę czekać...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        if(!stan) {
            Toast.makeText(context, "Wiadomość wysłana", Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(context, "Wiadomość nie została wysłana", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties

        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host","smtp.gmail.com"); //"smtp.gmail.com"
        props.put("mail.smtp.socketFactory.port", "465"); //"465"
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //"javax.net.ssl.SSLSocketFactory"
        props.put("mail.smtp.auth", "true"); //"true"
        props.put("mail.smtp.port", "465"); //"465"


        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("1wymiana1@gmail.com", "Enov@1990");
                    }
                });

        try {


            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);
            stan=true;
            //Setting sender address
            mm.setFrom(new InternetAddress("1wymiana1@gmail.com"));

            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); //"m.wisniewski@hit-kody.com.pl"

            //Adding subject
            mm.setSubject(subject);
            mm.setText(message);

            //Sending email
            Transport.send(mm);


        } catch (MessagingException e) {
            Log.i("email",""+e);
            stan=false;
        }
        return null;
    }
}