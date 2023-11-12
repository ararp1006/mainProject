package com.mainproject.be28.auth.config;


import com.mainproject.be28.auth.details.CustomMemberDetailsService;
import com.mainproject.be28.auth.filter.JwtAuthenticationFilter;
import com.mainproject.be28.auth.filter.JwtVerificationFilter;
import com.mainproject.be28.auth.handler.*;
import com.mainproject.be28.auth.jwt.JwtTokenizer;
import com.mainproject.be28.auth.utils.CustomAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration

public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final CustomMemberDetailsService customMemberDetailsService;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils, CustomMemberDetailsService customMemberDetailsService) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.customMemberDetailsService = customMemberDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().configurationSource(corsConfigurationSource())  //기본설정의 cors가 아닌 아래 @Bean으로 등록한 cors설정 적용
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/h2/**").permitAll() // Permit all accesses to H2 database console
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permit all preflight requests
                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("USER")     // (3) 추가

                );
               // .oauth2Login(oauth2 -> oauth2
                 //       .successHandler(new OAuth2MemberSuccessHandler(jwtTokenizer, authorityUtils, memberService))
               // );

        return httpSecurity.build();
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "PATCH", "DELETE", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


        public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
            @Override
            public void configure(HttpSecurity builder) throws Exception {
                AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

                JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
                jwtAuthenticationFilter.setFilterProcessesUrl("members/login");
                jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());//성공핸들러
                jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());//실패핸들러

                JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils, customMemberDetailsService);


                builder
                        .addFilter(jwtAuthenticationFilter)
                        .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
            }
        }
    }

