package com.externalapimodule.mail

import com.entitycoremodule.command.EmailVerificationCommand
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
    fun sendVerificationEmail(command: EmailVerificationCommand) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true, "utf-8")
        val bodyText = "<p>Your registered account email verification code is:</p>\n" +
                "<p><br></p>\n" +
                "<strong style=\"font-size:28px; line-height:32px;\">${command.code}</strong>\n" +
                "<p><br></p>\n" +
                "<p>Your account can’t be accessed " +
                "without this verification code. Please verify this code inorder to proceed in our community.</p>\n" +
                "<p>To keep your account secure, we recommend using a " +
                "unique password for your account."
        helper.setSubject("Registered account, email verification code")
        helper.setText(bodyText, true)
        helper.setTo(command.email)
        mailSender.send(mimeMessage)
    }
}