
package backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> {
            })

            .authorizeHttpRequests(auth -> auth

                // 登入相關 API
                .requestMatchers("/api/auth/**").permitAll()

                // 管理者專用 API
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 使用者只能存取自己的個人資料
                // 個人資料 API 只允許已登入者，實際操作對象由 Controller
                // 依 Authentication.getName() 決定，避免依賴角色字串格式。
                .requestMatchers(
                    "/api/users/me",
                    "/api/users/me/**"
                ).authenticated()

                // 其他使用者資料 API 僅限管理者
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                // 一般使用者及管理者都可以使用
                .requestMatchers(
                    "/api/transactions/**",
                    "/api/categories/**",
                    "/api/dashboard/**"
                ).hasAnyRole("USER", "ADMIN")

                // 其他 API 必須登入
                .requestMatchers("/api/**").authenticated()

                // 非 API 路徑允許存取
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
