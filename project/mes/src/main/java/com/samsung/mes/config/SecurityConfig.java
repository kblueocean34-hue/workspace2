package com.samsung.mes.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration //스프링 설정 클래스 (빈 등록/보안 설정)
@EnableWebSecurity //Spring Security기능을 활성화 보안설정 기준으로 사용하게 해줌
public class SecurityConfig{
	//보안 설정을 쉽게 override(재정의)해서 쓰는 기본틀
	
	@Bean //이게 왜 필요해요? 사용자가 회원가입을 할때 비밀번호를 db에 그대로 저장하면 절대 안됨
	public PasswordEncoder passwordEncoder() {//BCrypt는 비밀번호 해싱에 많이 쓰는 방식
		//같은 비밀번호여도 매번 결과가 다르게 나오도록(솔트) 설계 안전해요
		return new BCryptPasswordEncoder();
	}
	
	
	//cors
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		//쿠키/세션/인증정보(Authorization 헤더 포함 등)를 브라우저 요청에 포함하는 걸 허용
		config.setAllowedOriginPatterns(List.of("http://localhost:5173"));
		//“React 개발 서버(5173)에서 오는 요청만 허용할게”
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
		//허용할 HTTP 메서드 목록 (GET/POST/PUT/DELETE/OPTIONS)
		config.setAllowedHeaders(List.of("*"));//프론트가 보내는 헤더는 전부 허용
//예: Content-Type, Authorization 등		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);//모든 경로에 cors허용
		return source;
	}
	
	//실제 보안규칙 설정 (누가 어떤 API접근 가능)
	//@Override what 뭘 재정의 하는데
	//그래서 콩으로 등록
	@Bean//이 메서드가 만든 결과를 스프링이 관리하는 공식설정으로 등록해줘
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//SecurityFilterChain => 검사하는 규칙들에 줄, 보안 검사 규칙세트 어떤 요청은 통과하고 어떤 요청은 막을지..
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		//허용하는 규칙연결
		.csrf(csrf -> csrf.disable())//브라우저가 쿠키를 자동 첨부를 끔
		.formLogin(form -> form.disable())//기본로그인 페이지를 자동 만듬 리액트로 ui를 만들기에 끔
		.httpBasic(basic -> basic.disable())//로그인 팝업뜨는 방식 그래서 off
		.authorizeHttpRequests(auth -> auth
		.requestMatchers(HttpMethod.POST, "/members/login").permitAll()//로그인 안해도 통과 가능
		.requestMatchers("/members/login","/members/register","/members/logout").permitAll()
		.requestMatchers("/api/**").permitAll()
		.requestMatchers("/api/sales/orders/**").permitAll()
		.requestMatchers("/", "/error", "/favicon.ico").permitAll()
		.anyRequest().authenticated()//위에서 허용한것 빼고는 전부 로그인(인증)된 사용자만 접근가능
		);
		return http.build();
//지금까지 설정한 보안 규칙을 최종 완성해서 서버에 적용
	}
}
