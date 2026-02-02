package port.sm.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/members").permitAll()
            .antMatchers("/members/login", "/members/register", "/api/events/**", "/api/inv/items").permitAll() // <-- 여기 수정
            // ✅ 추가: 견적서 POST/PUT/DELETE 허용
            .antMatchers(HttpMethod.GET, "/api/sales/estimates/**").permitAll() // << 여기 추가
            .antMatchers(HttpMethod.POST, "/api/sales/estimates").permitAll()
            .antMatchers(HttpMethod.PUT, "/api/sales/estimates/**").permitAll()
            .antMatchers(HttpMethod.DELETE, "/api/sales/estimates/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic().disable();

        return http.build();
    }
}


