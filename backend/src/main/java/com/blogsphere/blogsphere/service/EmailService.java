package com.blogsphere.blogsphere.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * All methods here take plain Strings, never JPA entities.
 * These methods are @Async and run on a separate thread pool thread
 * with no Hibernate session — passing a lazy-loaded entity in and
 * accessing its fields here would throw LazyInitializationException
 * (or worse, silently corrupt a shared OSIV session). Callers must
 * extract the values they need from entities BEFORE calling these
 * methods, while still on the original thread/transaction.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String displayName, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Welcome to BlogSphere, " + displayName + "!");
        message.setText(
                "Hi " + displayName + ",\n\n" +
                        "Welcome to BlogSphere! Your account (@" + username + ") is ready to go.\n\n" +
                        "Start writing, or explore what other people are publishing.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewCommentEmail(String toEmail, String postAuthorDisplayName,
                                    String commenterDisplayName, String commenterUsername, String postTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject(commenterDisplayName + " commented on your post");
        message.setText(
                "Hi " + postAuthorDisplayName + ",\n\n" +
                        commenterDisplayName + " (@" + commenterUsername + ") just commented on \"" + postTitle + "\".\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewLikeEmail(String toEmail, String postAuthorDisplayName,
                                 String likerDisplayName, String likerUsername, String postTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject(likerDisplayName + " liked your post");
        message.setText(
                "Hi " + postAuthorDisplayName + ",\n\n" +
                        likerDisplayName + " (@" + likerUsername + ") liked \"" + postTitle + "\".\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewFollowerEmail(String toEmail, String followedDisplayName,
                                     String followerDisplayName, String followerUsername) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject(followerDisplayName + " started following you");
        message.setText(
                "Hi " + followedDisplayName + ",\n\n" +
                        followerDisplayName + " (@" + followerUsername + ") just followed you on BlogSphere.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendLoginAlertEmail(String toEmail, String displayName, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("New login to your BlogSphere account");
        message.setText(
                "Hi " + displayName + ",\n\n" +
                        "We noticed a new login to your account (@" + username + ") just now.\n\n" +
                        "If this was you, no action is needed. If you don't recognize this activity, " +
                        "please change your password and review your account immediately.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendAccountDeletedEmail(String toEmail, String displayName, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Your BlogSphere account has been deleted");
        message.setText(
                "Hi " + displayName + ",\n\n" +
                        "This confirms that your account (@" + username + ") and all associated data " +
                        "have been permanently deleted from BlogSphere.\n\n" +
                        "If you didn't request this, please contact support immediately.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendPostCreatedEmail(String toEmail, String authorDisplayName, String postTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Your post \"" + postTitle + "\" was created");
        message.setText(
                "Hi " + authorDisplayName + ",\n\n" +
                        "Your post \"" + postTitle + "\" has been saved to BlogSphere.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewPostFromFollowedUserEmail(String toEmail, String followerDisplayName,
                                                 String authorDisplayName, String authorUsername, String postTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject(authorDisplayName + " published a new post");
        message.setText(
                "Hi " + followerDisplayName + ",\n\n" +
                        authorDisplayName + " (@" + authorUsername + "), who you follow, just published \"" + postTitle + "\".\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendOtpEmail(String toEmail, String code, String purposeLabel) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Your BlogSphere verification code");
        message.setText(
                "Your one-time code to " + purposeLabel + " is:\n\n" +
                        code + "\n\n" +
                        "This code expires in 10 minutes. If you didn't request this, you can ignore this email.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    private void send(SimpleMailMessage message) {
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Don't let a mail failure break the calling request — just log it.
            System.err.println("Failed to send email to " + message.getTo()[0] + ": " + e.getMessage());
        }
    }
}
