package com.example.myvopiserver.common.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
    "com.externalapimodule.mail",                       // JavaMail
])
class BeanConfig {}