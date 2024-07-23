package com.example.myvopiserver.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets


@Configuration
class WebConfig: WebMvcConfigurer
{

    @Bean
    fun responseBodyConverter(): HttpMessageConverter<String> {
        val shmc = StringHttpMessageConverter(StandardCharsets.UTF_8)
        val mediaTypeList: MutableList<MediaType> = ArrayList()
        mediaTypeList.add(MediaType.APPLICATION_JSON)
        shmc.supportedMediaTypes = mediaTypeList
        return shmc
    }
}