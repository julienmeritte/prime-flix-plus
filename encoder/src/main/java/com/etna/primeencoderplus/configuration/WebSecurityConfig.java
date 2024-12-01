package com.etna.primeencoderplus.configuration;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/videos/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers()
                .frameOptions().disable();

        http.authorizeRequests()
                // Encoder
                .antMatchers(HttpMethod.POST, "/encoder").permitAll()

                .anyRequest().authenticated()
                .and().csrf().disable();

        http.exceptionHandling().authenticationEntryPoint((request, response, e) ->
        {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            try {
                response.getWriter().write(new JSONObject()
                        .put("message", "Unauthorized")
                        .toString());
            } catch (JSONException jsonException) {
                // Do nothing
            }
        });
    }
}