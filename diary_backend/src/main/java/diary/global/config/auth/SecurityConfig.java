package diary.global.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration  // 현재 클래스를 설정 클래스로 설정
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 람다식으로 바꿔야 한다.
        http
                .csrf(c -> c.disable())  // csrf 불필요
                //.csrf(AbstractHttpConfigurer::disable)
                //.cors(c -> c.disable())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(STATELESS));
                //.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/auth/**", "/error").permitAll());
//                .oauth2Login(customConfigurer -> customConfigurer
//                        .successHandler(successHandler)
//                        .failureHandler(failureHandler)
//                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(customOAuthService)));


        return http.build();


//        return http
//                .httpBasic().disable()  //HTTP 기본 인증을 비활성화
//                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil(userRepository)), UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .antMatchers("/analytics/v1/accounts/**").permitAll()
//                .and()
//                .cors()
//                .and()
//                .csrf().disable()
//                .formLogin().disable()
//                .sessionManagement().sessionCreationPolicy(STATELESS)
//                .and()
//                .oauth2Login() // OAuth 2.0 로그인 설정시작
//                .userInfoEndPoint().userService(customOAuth2UserService)  // OAuth2 로그인시 사용자 정보를 가져오는 앤드포인트와 사용자 서비스를 설정
//                .and()
//                .failureHandler(oAuth2LoginFailureHandler)  // OAuth2 로그인 실패시 처리할 핸들러 지정
//                .successHandler(oAuth2LoginSuccessHandler)  //OAuth2 로그인 성공시 처리할 핸들러 지정
//                .and()
//                .headers().frameOptions().disable().and().build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "TOKEN_ID", "X-Requested-With", "Authorization", "Content-Type", "Content-Length", "Cache-Control"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

}
