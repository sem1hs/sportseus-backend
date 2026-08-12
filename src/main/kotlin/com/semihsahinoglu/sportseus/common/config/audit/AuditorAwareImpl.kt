package com.semihsahinoglu.sportseus.common.config.audit

import org.springframework.data.domain.AuditorAware
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class AuditorAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated || authentication is AnonymousAuthenticationToken) {
            return Optional.of<String>("system")
        }
        return Optional.of<String>(authentication.name)
    }
}
