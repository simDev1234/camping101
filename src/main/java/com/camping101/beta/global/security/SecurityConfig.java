package com.camping101.beta.global.security;

import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.global.security.authentication.UsernamePasswordAuthenticationProvider;
import com.camping101.beta.global.security.filter.JwtAuthenticationFilter;
import com.camping101.beta.global.security.filter.JwtAuthorizationFilter;
import com.camping101.beta.global.security.handler.JwtAccessDeniedHandler;
import com.camping101.beta.global.security.handler.JwtAuthenticationEntryPoint;
import com.camping101.beta.web.domain.member.service.signin.MemberSignInService;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberSignInService memberSignInService;
    private final TokenService tokenService;
    public final static String AUTHORIZATION_HEADER = "Authorization";

    @Value("${security.ignore.all.paths.startwith}")
    private String ignoreAllPathsStartWith;
    @Value("${security.ignore.get.paths.startwith}")
    private String ignoreGetPathsStartWith;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(
            new UsernamePasswordAuthenticationProvider(memberSignInService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .cors().and()
            .csrf().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .addFilterAt(new JwtAuthenticationFilter(authenticationManager(), tokenService),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthorizationFilter(tokenService, ignoreAllPathsStartWith,
                ignoreGetPathsStartWith), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .accessDeniedHandler(new JwtAccessDeniedHandler())
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint()).and()

            .authorizeRequests()
            .antMatchers("/h2-console/**", "/swagger-ui", "/swagger-ui/**", "/v2/api-docs")
            .permitAll()

            .antMatchers("/api/member", "/api/member/**", "/api/signout")
            .hasAnyAuthority(MemberType.CUSTOMER.name(), MemberType.ADMIN.name(), MemberType.OWNER.name())

//            .antMatchers(HttpMethod.GET, "/api/camp/owner/**", "/api/camp/detail/owner/**", "/api/site/owner/**")
//            .hasAnyAuthority(MemberType.OWNER.name())

//            .antMatchers(HttpMethod.POST, "/api/camp", "/api/site/**")
//            .hasAnyAuthority(MemberType.OWNER.name())

//            .antMatchers(HttpMethod.PUT, "/api/camp",  "/api/site/**")
//            .hasAnyAuthority(MemberType.OWNER.name())
//
//            .antMatchers(HttpMethod.DELETE, "/api/camp/**",  "/api/site/**")
//            .hasAnyAuthority(MemberType.OWNER.name())

            .antMatchers("/api/admin/rectag", "/api/admin/member")
            .hasAuthority(MemberType.ADMIN.name());
//
//            .antMatchers(ignoreAllPathsStartWith.split(","))
//            .permitAll()
//            .antMatchers(HttpMethod.GET, ignoreGetPathsStartWith.split(","))
//            .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {

        web.ignoring()
            .antMatchers("/h2-console/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**",
                "/v3/api-docs")
            .antMatchers("/css/**", "/vendor/**", "/js/**", "/images/**")
            .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

}
