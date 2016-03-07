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
