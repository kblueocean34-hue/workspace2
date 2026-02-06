package port.sm.erp.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class SecurityJwtConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpiration;

    // =======================
    // Password Encoder
    // =======================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =======================
    // ✅ CORS (Security에서 사용)
    // =======================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ 프론트 주소 명시 (Vite/Next 둘 다)
        config.setAllowedOrigins(java.util.List.of(
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        // ✅ JWT Authorization 헤더 허용
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(java.util.List.of("Authorization"));

        // ✅ Bearer 방식이면 보통 false가 맞음 (true면 Origin 제한 더 빡세짐)
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // =======================
    // Security Filter Chain
    // =======================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ 프리플라이트(OPTIONS) 전부 허용
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ 인증 없이 허용할 엔드포인트들
                        .antMatchers(HttpMethod.POST, "/members", "/members/login", "/members/register").permitAll()
                        .antMatchers("/api/events/**", "/api/inv/items").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/sales/estimates/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/sales/estimates").permitAll()
                        .antMatchers(HttpMethod.PUT, "/api/sales/estimates/**").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/api/sales/estimates/**").permitAll()

                        // 🔽 거래처 API 인증 없이 허용
                        .antMatchers("/api/acc/customers/**").permitAll()
                        .antMatchers("/api/acc/trades/**").permitAll()

                        // ✅ (추가) 너 지금 막히는 거래 API도 일단 허용하려면 주석 해제
                        // .antMatchers("/api/acc/trades/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(h -> h.disable());

        return http.build();
    }

    // =======================
    // JWT Filter
    // =======================
    public class JwtAuthFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain
        ) throws ServletException, IOException {

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (validateToken(token)) {
                    Map<String, Object> claims = getClaims(token);
                    Long uid = ((Number) claims.get("uid")).longValue();
                    Map<String, Object> principal = Map.of("uid", uid);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    org.springframework.security.core.context.SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    // =======================
    // JWT Utility Methods
    // =======================
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("uid", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Map<String, Object> getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
