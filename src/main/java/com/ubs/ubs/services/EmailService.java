package com.ubs.ubs.services;

import com.ubs.ubs.services.exceptions.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;

    private static final String DEFAULT_SUBJECT = "Lembrete de Consulta";
    private static final String DEFAULT_TEMPLATE_BODY = "Olá [Paciente],\n\n" +
            "Lembramos da sua consulta na clínica [Clínica] com Dr(a). [Médico] ([Especialidade]) " +
            "agendada para [Data] às [Hora].\n\n" +
            "Atenciosamente,\nEquipe [Clínica]";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentReminder(String to, Map<String, String> variables) {
        String body = DEFAULT_TEMPLATE_BODY;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            body = body.replace("[" + entry.getKey() + "]", entry.getValue());
        }
        System.out.println("---- ENVIANDO EMAIL (PADRÃO) ----");
        System.out.println("Para: " + to);
        System.out.println("Assunto: " + DEFAULT_SUBJECT);
        System.out.println("Corpo:\n" + body);
        System.out.println("---------------------------------");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Lembrete de Consulta Médica");
        message.setText(body);
        mailSender.send(message);
    }

    public void sendCustomAppointmentReminder(String to, String customTemplate, Map<String, String> variables) {
        String body = customTemplate; // O template já é o corpo
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = entry.getKey().startsWith("[") ? entry.getKey() : "[" + entry.getKey() + "]";
            body = body.replace(placeholder, entry.getValue());
        }
        System.out.println("---- ENVIANDO EMAIL (PADRÃO) ----");
        System.out.println("Para: " + to);
        System.out.println("Assunto: " + DEFAULT_SUBJECT);
        System.out.println("Corpo:\n" + body);
        System.out.println("---------------------------------");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Lembrete de Consulta Médica");
        message.setText(body);
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