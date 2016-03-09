1.- Change to your email account in application.properties next parameters:

    mail.support.username=youreamil@gmail.com
    mail.support.password=yourpassword

2.- If you are using your email account to send emails then you have to do the next steps:

    By default Gmail account is highly secured. When we use gmail smtp from non gmail tool, email is blocked.
    To test in our local environment, make your gmail account less secure as

    1.	Login to Gmail.
    2.	Access the URL as https://www.google.com/settings/security/lesssecureapps
    3.	Select "Turn on" To send email correctly you have to check Turn on or Activar in Spanish.
        If you don't turn on you cannot send emails.


You can see all instruction in this blog "http://andresmerida1.blogspot.com/"
