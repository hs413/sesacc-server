package sesac.server.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public JwtTestUtil jwtTestUtil() {
        return new JwtTestUtil();
    }

    @Bean
    public TestAccessTokenFilter testAccessTokenFilter(JwtTestUtil jwtTestUtil) {
        return new TestAccessTokenFilter(jwtTestUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            TestAccessTokenFilter testAccessTokenFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(testAccessTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/posts/**").hasRole("MANAGER")//.authenticated()
                        .anyRequest().permitAll());

        return http.build();
    }
}