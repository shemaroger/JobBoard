package com.example.JobBoard.service;

import com.example.JobBoard.model.PasswordResetToken;
import com.example.JobBoard.model.TwoFactorToken;
import com.example.JobBoard.model.User;
import com.example.JobBoard.repository.PasswordResetTokenRepository;
import com.example.JobBoard.repository.TwoFactorTokenRepository;
import com.example.JobBoard.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TwoFactorTokenRepository twoFactorTokenRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository,
                       TwoFactorTokenRepository twoFactorTokenRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.twoFactorTokenRepository = twoFactorTokenRepository;
        this.mailSender = mailSender;
    }

    // User Management Methods
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }


    // Delete the user
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    // Login with Two-Factor Authentication
    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            sendTwoFactorToken(user.get());
            return Optional.of(user.get());
        }
        return Optional.empty();
    }

    // Send 2FA Token
    public void sendTwoFactorToken(User user) {
        String token = UUID.randomUUID().toString();
        TwoFactorToken twoFactorToken = new TwoFactorToken();
        twoFactorToken.setToken(token);
        twoFactorToken.setUser(user);
        twoFactorToken.setExpiryDate(new Date(System.currentTimeMillis() + 5 * 60 * 1000)); // 5 minutes expiry
        twoFactorTokenRepository.save(twoFactorToken);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Your Two-Factor Authentication Code");
            helper.setText("Your 2FA code is: " + token, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> validateTwoFactorToken(String token) {
        Optional<TwoFactorToken> twoFactorToken = twoFactorTokenRepository.findByToken(token);

        if (twoFactorToken.isPresent()) {
            TwoFactorToken tokenEntity = twoFactorToken.get();

            // Check if token is expired
            if (tokenEntity.getExpiryDate().before(new Date())) {
                twoFactorTokenRepository.delete(tokenEntity);
                System.out.println("Token is expired.");
                return Optional.empty();
            }

            // Return the associated user
            return Optional.of(tokenEntity.getUser());
        }

        System.out.println("Token not found.");
        return Optional.empty();
    }

    // Password Reset Methods
    public String createPasswordResetToken(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            // Generate an 8-digit numeric token
            String token = String.format("%08d", new java.util.Random().nextInt(100000000));

            // Create the PasswordResetToken entity with the generated token
            PasswordResetToken resetToken = new PasswordResetToken(
                    token,
                    user.get(),
                    new Date(System.currentTimeMillis() + 30 * 60 * 1000) // 30 minutes expiry
            );

            passwordResetTokenRepository.save(resetToken);
            return token;
        }
        return null;
    }

    public void sendResetEmail(String email, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText("<p>You requested a password reset. Click the link below to reset your password:</p>" +
                    "<p><a href=\"" + resetLink + "\">Reset Password</a></p>", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean updatePassword(String token, String password) {
        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken.isPresent() && resetToken.get().getExpiryDate().after(new Date())) {
            User user = resetToken.get().getUser();
            user.setPassword(password);
            userRepository.save(user);
            passwordResetTokenRepository.delete(resetToken.get());
            return true;
        }
        return false;
    }
}
