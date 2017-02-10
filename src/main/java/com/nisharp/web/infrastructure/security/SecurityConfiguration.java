package com.nisharp.web.infrastructure.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ZM.Wang
 */

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(); // FIXME: 2016/10/13 打开CSRF将使swagger用不了，目前为简单起见，暂时关闭

        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login");

        http.authorizeRequests()
//                .antMatchers("/accounts/**").permitAll()
                .anyRequest().fullyAuthenticated();

        http.sessionManagement()
                .invalidSessionStrategy((request, response) -> {
                    if (StringUtils.equalsIgnoreCase("XmlHttpRequest", request.getHeader("x-requested-with"))) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        response.sendRedirect("/login.html?timeout");
                    }
                })
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

}