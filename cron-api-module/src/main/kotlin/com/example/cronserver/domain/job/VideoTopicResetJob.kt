package com.example.cronserver.domain.job

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class VideoTopicResetJob(

): QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {

    }
}