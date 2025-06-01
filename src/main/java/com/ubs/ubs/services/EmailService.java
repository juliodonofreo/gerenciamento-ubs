package com.ubs.ubs.services;

import com.ubs.ubs.services.exceptions.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentReminder(String to, String patientName, String doctorName, String appointmentDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Lembrete de Consulta Médica");
        message.setText(
                "Olá " + patientName + ",\n\n" +
                        "Este é um lembrete da sua consulta com o Dr.(a) " + doctorName +
                        " marcada para amanhã às " + appointmentDate + ".\n\n" +
                        "Atenciosamente,\nEquipe UBS"
        );
        mailSender.send(message);
    }

    public void sendEmail(String to, String subject, String body) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }
        catch (MailException e){
            throw new EmailException("Failed to send email");
        }
    }
}