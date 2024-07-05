package org.sid.tp6_ebankingbackend.security;


import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.JwaAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
//Spring automatically detects this class and applies web security configurations
@EnableWebSecurity
//annotation in Spring Security is used to enable method-level security authorization
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretkey;
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager()
    {
        //implement l'interface UserDetailsInterface
        PasswordEncoder passwordEncoder =passwordEncoder();

        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("12345")).authorities("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("12345")).authorities("USER","ADMIN").build()
        );

    }

    @Bean
    ///je gere pas auth coté serveur on utuilisent auth stateless avec JWT
    public  PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        //desactiver la protectio csrf
                        .csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(ar->ar.requestMatchers("/auth/login/**").permitAll())
                        // tous les requete (auth )
                        .authorizeHttpRequests((ar->ar.anyRequest().authenticated()))
                        //auth par defaut
//                        .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
                         .build();


    }

    @Bean
    JwtEncoder jwtEncoder()
    {
        //to create the signature for the JWT
        //String secretKey="VwkYwJrYquIiySoIPx9ghCfdstsJh7pgw1mmM_HD481Tx66rTDoJxL4kbpqCA52Y";
        return  new NimbusJwtEncoder(new ImmutableSecret<>(secretkey.getBytes()));
    }


    @Bean
    JwtDecoder jwtDecoder()
    {
        // It searches the Authorization header, retrieves the JWT, and then verifies the token's signature using the same secret (secretKeySpec).
       // String secretKey="VwkYwJrYquIiySoIPx9ghCfdstsJh7pgw1mmM_HD481Tx66rTDoJxL4kbpqCA52Y"::on va mettre en aplication.properties
        SecretKeySpec secretKeySpec =new SecretKeySpec(secretkey.getBytes(),"RSA");

        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();

    }
@Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService)
    {
        //ensuring that user credentials are properly checked against stored user details and passwords are encoded securely
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        //to compare the passwords
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        //retrieves user with roles
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager( daoAuthenticationProvider);

    }
    //gerer cors for spring security
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.applyPermitDefaultValues();
//        corsConfig.setAllowCredentials(true);
        //j'accepte tous
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        //corsConfig.setAllowedOrigins(List.of("x-auth-token"));//comme total page
//        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //**=>pour tous les requets
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
