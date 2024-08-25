package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report, Long> {
}