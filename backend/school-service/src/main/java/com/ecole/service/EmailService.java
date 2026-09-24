package com.ecole.service;

import com.ecole.model.School;
import com.ecole.model.Teacher;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendSchoolRegistrationNotification(School school) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(school.getEmail());
            helper.setSubject("New School Registration - Ecole");
            helper.setText(
                "Dear " + school.getPrincipalName() + ",\n\n" +
                "Thank you for registering your school on Ecole platform.\n\n" +
                "Your registration is currently under review.\n\n" +
                "School Name: " + school.getName() + "\n" +
                "Registration Date: " + java.time.LocalDate.now() + "\n\n" +
                "We will notify you once your registration is approved or if we need any additional information.\n\n" +
                "Best regards,\n" +
                "Ecole Team",
                true
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendSchoolApprovalNotification(School school) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(school.getEmail());
            helper.setSubject("School Registration Approved - Ecole");
            helper.setText(
                "Dear " + school.getPrincipalName() + ",\n\n" +
                "Congratulations! Your school registration has been approved.\n\n" +
                "School Name: " + school.getName() + "\n" +
                "Approval Date: " + java.time.LocalDate.now() + "\n\n" +
                "You can now start adding teachers and managing your school profile.\n\n" +
                "Best regards,\n" +
                "Ecole Team",
                true
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendSchoolRejectionNotification(School school, String reason) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(school.getEmail());
            helper.setSubject("School Registration Rejected - Ecole");
            helper.setText(
                "Dear " + school.getPrincipalName() + ",\n\n" +
                "We regret to inform you that your school registration has been rejected.\n\n" +
                "School Name: " + school.getName() + "\n" +
                "Rejection Reason: " + reason + "\n\n" +
                "You can update your information and resubmit your registration.\n\n" +
                "Best regards,\n" +
                "Ecole Team",
                true
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendTeacherRegistrationNotification(Teacher teacher) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(teacher.getEmail());
            helper.setSubject("New Teacher Registration - Ecole");
            helper.setText(
                "Dear " + teacher.getFirstName() + " " + teacher.getLastName() + ",\n\n" +
                "Thank you for registering as a teacher on Ecole platform.\n\n" +
                "Your registration is currently under review.\n\n" +
                "School: " + (teacher.getSchool() != null ? teacher.getSchool().getName() : "N/A") + "\n" +
                "Registration Date: " + java.time.LocalDate.now() + "\n\n" +
                "We will notify you once your registration is approved or if we need any additional information.\n\n" +
                "Best regards,\n" +
                "Ecole Team",
                true
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendTeacherApprovalNotification(Teacher teacher) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(teacher.getEmail());
            helper.setSubject("Teacher Registration Approved - Ecole");
            helper.setText(
                "Dear " + teacher.getFirstName() + " " + teacher.getLastName() + ",\n\n" +
                "Congratulations! Your teacher registration has been approved.\n\n" +
                "School: " + (teacher.getSchool() != null ? teacher.getSchool().getName() : "N/A") + "\n" +
                "Approval Date: " + java.time.LocalDate.now() + "\n\n" +
                "You can now start using the platform to manage your profile and interact with students.\n\n" +
                "Best regards,\n" +
                "Ecole Team",
                true
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendTeacherRejectionNotification(Teacher teacher, String reason) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(teacher.getEmail());
            helper.setSubject("Teacher Registration Rejected - Ecole");
            helper.setText(
                "Dear " + teacher.getFirstName() + " " + teacher.getLastName() + ",\n\n" +
                "We regret to inform you that your teacher registration has been rejected.\n\n" +
                "Rejection Reason: " + reason + "\n\n" +
                "You can update your information and resubmit your registration.\n\n" +
                "Best regards,\n" +
                "Ecole Team",
                true
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(String email, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Password Reset Request - Ecole");
            String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;
            helper.setText(
                "Dear User,\n\n" +
                "You have requested to reset your password.\n\n" +
                "Please click on the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Ecole Team",
                true
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendNotificationEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
