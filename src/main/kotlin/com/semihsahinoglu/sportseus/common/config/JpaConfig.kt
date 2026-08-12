package com.semihsahinoglu.sportseus.common.config

import com.semihsahinoglu.sportseus.common.config.audit.AuditorAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class JpaConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> = AuditorAwareImpl()
}