package com.externalapimodule.mail

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender,
) {

    @Async("mailExecutor")
    fun sendVerificationEmail(
        id: Long,
        email: String,
        code: String
    ) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true, "utf-8")
        val bodyText = "<p>MyVideoOpinion registration email for: $email verification code is:</p>\n" +
                "<p><br></p>\n" +
                "<strong style=\"font-size:28px; line-height:32px;\">${code}</strong>\n" +
                "<p><br></p>\n" +
                "<p>Please do not share this code with anyone.</p>\n" +
                "<p>We will not contact you to share or send registration code related contents through any communication procedures.</p>\n" +
                "<p>Please input this code immediately to complete your registration.</p>\n" +
                "<p>This code will only be valid for 5 minutes.</p>\n"
        helper.setSubject("MyVideoOpinion - Email registration verification code")
        helper.setText(bodyText, true)
        helper.setTo(email)
        helper.setFrom("MyVideoOpinion")
        mailSender.send(mimeMessage)
    }
}