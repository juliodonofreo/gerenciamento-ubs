package com.ubs.ubs.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

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
}