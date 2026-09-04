package com.semihsahinoglu.sportseus.security.config

import com.semihsahinoglu.sportseus.security.entity.JwtProperties
import com.semihsahinoglu.sportseus.security.entity.RefreshCookieProperties
import com.semihsahinoglu.sportseus.security.exception.CustomAccessDeniedHandler
import com.semihsahinoglu.sportseus.security.exception.JwtAuthenticationEntryPoint
import com.semihsahinoglu.sportseus.security.filter.JwtAuthFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableConfigurationProperties(JwtProperties::class, RefreshCookieProperties::class)
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        entryPoint: JwtAuthenticationEntryPoint,
        deniedHandler: CustomAccessDeniedHandler,
    ): SecurityFilterChain {
        return http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .exceptionHandling {
                it.authenticationEntryPoint(entryPoint)
                it.accessDeniedHandler(deniedHandler)
            }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/login", "/auth/signup", "/auth/refresh-token").permitAll()
                it.requestMatchers("/actuator/**", "/error/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/leagues/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/teams/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/players/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/transfers/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/fixtures/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/venues/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/coaches/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/lineups/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/standings/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/squads/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/news/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/tags/**").permitAll()
                it.requestMatchers("/admin/**").hasRole("ADMIN")
                it.anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:3000")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider =
        DaoAuthenticationProvider(userDetailsService).apply { setPasswordEncoder(passwordEncoder()) }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}