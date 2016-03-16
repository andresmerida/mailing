## **Mailing with Spring Framework 4.2.5, Spring boot, Velocity template and Gmail SMTP server**
## Building a mailing with spring framework and velocity
This guide walks you a good way to send emails with spring framework and velocity (template). In our case we will create an user and send an email.
## What you'll build
You'll build a service that will accept HTTP GET AND POST request at:  
http://localhost:8080/users  Get, User's list.  
http://localhost:8080/users  Post,  you can send this user in json: {"name":"Andres", "email":"youremail@gmail.com", "password":"Password1!"} using postman or other http client to testing web services. 

## What you'll need
  - About 15 minutes    
  - A favorite IDE. I used Intellij IDEA        
  - Spring Framework 4 or later. I am using 4.2.5.RELEASE    
  - Maven 3.0+    
  - JDK 1.8 or later    
  - An email in gmail (we will send email from our account gmail)    

## Build with Maven 
**pom.xml**
```xml    
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springmodules</groupId>
    <artifactId>mailing</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
            <java.version>1.8</java.version>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.2.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
        </dependency> <!-- equivalent to gradle: compile 'org.springframework:spring-context-support' -->

        <!-- connection database in memory -->
        <dependency>
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId>
        </dependency>

        <!-- sending email -->
        <dependency>
            <groupId>javax.mail</groupId>
            <artifactId>javax.mail-api</artifactId>
            <version>1.5.5</version>
        </dependency>
        <dependency>
            <groupId>com.sun.mail</groupId>
            <artifactId>javax.mail</artifactId>
            <version>1.5.5</version>
        </dependency>


        <!-- volocity template -->
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-tools</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```
In pom.xml we need to add dependencies to email and velocity.

## Create an application.properties
**application.properties**

```xml
# JPA
spring.jpa.generate-ddl=true

# Mailing
mail.protocol=smtp
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.support.username=youreamil@gmail.com
mail.support.password=yourpassword

# VELOCITY TEMPLATES
spring.velocity.resource-loader-path=classpath:/templates/velocity
```
In the line 10 **spring.velocity.resource-loader-path** we need to add the path where they will be our templates .vm as you can see in the image below: 

Project structure: 
![alt text](https://2.bp.blogspot.com/-X3Hr5ZmiYq4/Vt3b33eO-5I/AAAAAAAAAH8/seHD5e1pK2A/s1600/velocityFile.PNG)

**Important Note**
By default Gmail account is highly secured. When we use gmail smtp from non gmail tool, email is blocked.  To test in our local environment, make your gmail account less secure as 

1.	Login to Gmail.    
2.	Access the URL as https://www.google.com/settings/security/lesssecureapps    
3.	Select "Turn on"       

To send email correctly you have to check Turn on or Activar in Spanish. If you don't turn on you cannot send emails. 

## Create Template Velocity
**registration-confirmation.vm**
```html
<html>
<body>
<h3>Hi ${user.name}, we are testing registration confirmation :)!</h3>

<div>
    Your email address is <a href="mailto:${user.email}">${user.login}</a>.
</div>
</body>
</html>
```
## Make the application executable
**Application.java**
```java
package org.springmodules.mailing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by andresmerida on 3/3/2016.
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
## Create a resource to config email in our project
**MailingConfig.java**

```java
package org.springmodules.mailing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by andresmerida on 3/3/2016.
 */

@Configuration
public class MailingConfig {

    @Value("${mail.protocol}")  // this is to read variable from application.properties
    private String mailProtocol;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private Integer port;

    @Value("${mail.support.username}")
    private String userName;

    @Value("${mail.support.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setProtocol(mailProtocol);
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "false");
        return properties;
    }

}
```
## Create a resource entity user
**User.java**
```java
package org.springmodules.mailing.core.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by andresmerida on 3/3/2016.
 */

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;   // email is equivalent to loguin in this case
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```
## Create a resource repository for user 
**UserRepository.java**
```java
package org.springmodules.mailing.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springmodules.mailing.core.entity.User;

/**
 * Created by andresmerida on 3/3/2016.
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
```

## Create a resource service to register an user
**UserService.java (interface)**

```java
package org.springmodules.mailing.core.services;

import org.springmodules.mailing.core.entity.User;

/**
 * Created by andresmerida on 3/3/2016.
 */
public interface UserService {
    User saveUser(User user);
}
```

**UserServiceImpl.java**
```java
package org.springmodules.mailing.core.services.impl;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springmodules.mailing.core.entity.User;
import org.springmodules.mailing.core.repositories.UserRepository;
import org.springmodules.mailing.core.services.UserService;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andresmerida on 3/3/2016.
 */

@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    private static final String SUBJECT_MAIL_REGISTRATION_CONFIRMATION = "Registration Confirmation";

    private static final String CHARSET_UTF8 = "UTF-8";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        this.sendConfirmationEmail(user);
        return userRepository.save(user);
    }

    private void sendConfirmationEmail(final User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(user.getEmail());
                message.setSubject(SUBJECT_MAIL_REGISTRATION_CONFIRMATION);

                Map model = new HashMap<>();
                model.put("user", user);

                message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine
                        , "registration-confirmation.vm", CHARSET_UTF8, model), true);
            }
        };

        this.javaMailSender.send(preparator);
    }
}
```
**Attachments**
The following example shows you how to use the MimeMessageHelper to send an email along a simple JPEG image attachment. 

```java
 JavaMailSenderImpl sender = new JavaMailSenderImpl();  
 sender.setHost("mail.host.com");  
 MimeMessage message = sender.createMimeMessage();  
 // use the true flag to indicate you need a multipart message  
 MimeMessageHelper helper = new MimeMessageHelper(message, true);  
 helper.setTo("test@host.com");  
 helper.setText("Check out this image!");  
 // let's attach the infamous windows Sample file (this time copied to c:/)  
 FileSystemResource file = new FileSystemResource(new File("c:/Sample.jpg"));  
 helper.addAttachment("CoolImage.jpg", file);  
 sender.send(message);  
```
## Create a resource controller
**UserController.java**
```java
package org.springmodules.mailing.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.mailing.core.entity.User;
import org.springmodules.mailing.core.repositories.UserRepository;
import org.springmodules.mailing.core.services.UserService;

import java.util.List;

/**
 * Created by andresmerida on 3/3/2016.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
```

My project structure:
![alt text](https://3.bp.blogspot.com/-eKlPYU7oBt0/Vt3m-ZeH1pI/AAAAAAAAAIM/dpyAkEBZe48/s1600/structureProject.PNG)

## Download source code
  
You can download all source code from this url on Git Hup:  
https://github.com/andresmerida/mailing.git 

That is all my friends.  I hope it helps, if you have question let me know.



