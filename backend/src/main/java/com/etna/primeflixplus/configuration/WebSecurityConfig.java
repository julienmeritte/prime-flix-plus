package com.etna.primeflixplus.configuration;

import com.etna.primeflixplus.enums.UserRole;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/images/**")
                .antMatchers("/videos/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers()
                .frameOptions().disable();

        http.authorizeRequests()
                // Auth
                .antMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/validation").permitAll()

                .antMatchers(HttpMethod.GET, "/images/**").permitAll()


                .antMatchers("/stream/").permitAll()

                .antMatchers(HttpMethod.POST, "/video/add").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.POST, "/video/quality").permitAll() // TODO: PASSER ADMIN APRES TESTS
                .antMatchers(HttpMethod.POST, "/video/add/group").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())

                .antMatchers(HttpMethod.GET, "/video/all/group").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.GET, "/video/all/videos/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())

                .antMatchers(HttpMethod.GET, "/video/get/group/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.GET, "/video/get/video/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())

                .antMatchers(HttpMethod.DELETE, "/video/delete/group/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.DELETE, "/video/delete/video/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())

                .antMatchers(HttpMethod.PUT, "/video/modify/group/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.PUT, "/video/modify/video/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())

                .antMatchers(HttpMethod.GET, "/stream/{\\s+}").permitAll()
                .antMatchers(HttpMethod.GET, "/stream/image/{\\s+}").permitAll()

                // User
                .antMatchers(HttpMethod.GET, "/user/get/all").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.PUT, "/user/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.DELETE, "/user/{\\d+}").hasAuthority(UserRole.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.GET, "/user/me").permitAll()

                // Sort front
                .antMatchers(HttpMethod.GET, "video/sort/recent").permitAll()

                // Profile


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

        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}