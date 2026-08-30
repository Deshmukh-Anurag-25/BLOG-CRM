package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(user.getEmail());
        message.setSubject("Welcome to BlogSphere, " + user.getDisplayName() + "!");
        message.setText(
                "Hi " + user.getDisplayName() + ",\n\n" +
                        "Welcome to BlogSphere! Your account (@" + user.getUsername() + ") is ready to go.\n\n" +
                        "Start writing, or explore what other people are publishing.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewCommentEmail(User postAuthor, User commenter, String postTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(postAuthor.getEmail());
        message.setSubject(commenter.getDisplayName() + " commented on your post");
        message.setText(
                "Hi " + postAuthor.getDisplayName() + ",\n\n" +
                        commenter.getDisplayName() + " (@" + commenter.getUsername() + ") just commented on \"" + postTitle + "\".\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewLikeEmail(User postAuthor, User liker, String postTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(postAuthor.getEmail());
        message.setSubject(liker.getDisplayName() + " liked your post");
        message.setText(
                "Hi " + postAuthor.getDisplayName() + ",\n\n" +
                        liker.getDisplayName() + " (@" + liker.getUsername() + ") liked \"" + postTitle + "\".\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewFollowerEmail(User followedUser, User follower) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(followedUser.getEmail());
        message.setSubject(follower.getDisplayName() + " started following you");
        message.setText(
                "Hi " + followedUser.getDisplayName() + ",\n\n" +
                        follower.getDisplayName() + " (@" + follower.getUsername() + ") just followed you on BlogSphere.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendLoginAlertEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(user.getEmail());
        message.setSubject("New login to your BlogSphere account");
        message.setText(
                "Hi " + user.getDisplayName() + ",\n\n" +
                        "We noticed a new login to your account (@" + user.getUsername() + ") just now.\n\n" +
                        "If this was you, no action is needed. If you don't recognize this activity, " +
                        "please change your password and review your account immediately.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendAccountDeletedEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(user.getEmail());
        message.setSubject("Your BlogSphere account has been deleted");
        message.setText(
                "Hi " + user.getDisplayName() + ",\n\n" +
                        "This confirms that your account (@" + user.getUsername() + ") and all associated data " +
                        "have been permanently deleted from BlogSphere.\n\n" +
                        "If you didn't request this, please contact support immediately.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendPostCreatedEmail(User author, Post post) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(author.getEmail());
        message.setSubject("Your post \"" + post.getTitle() + "\" was created");
        message.setText(
                "Hi " + author.getDisplayName() + ",\n\n" +
                        "Your post \"" + post.getTitle() + "\" has been saved to BlogSphere.\n\n" +
                        "— The BlogSphere team"
        );
        send(message);
    }

    @Async
    public void sendNewPostFromFollowedUserEmail(User follower, User author, Post post) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(follower.getEmail());
        message.setSubject(author.getDisplayName() + " published a new post");
        message.setText(
                "Hi " + follower.getDisplayName() + ",\n\n" +
                        author.getDisplayName() + " (@" + author.getUsername() + "), who you follow, just published \"" + post.getTitle() + "\".\n\n" +
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