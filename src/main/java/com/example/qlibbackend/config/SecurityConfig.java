package com.example.qlibbackend.config;


import com.example.qlibbackend.librarian.Librarian;
import com.example.qlibbackend.librarian.LibrarianRepository;
import com.example.qlibbackend.members.Member;
import com.example.qlibbackend.members.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    LibrarianRepository librarianDB;

    @Autowired
    MemberRepository memberDB;

    public PasswordEncoder passwordEncoder() {
        // Use BCryptPasswordEncoder for password hashing
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login/**").permitAll() // Public resource
                .antMatchers("/ebook/**").permitAll()
                .anyRequest().authenticated() // Requires authentication for any other request
                .and()
                .httpBasic().and().csrf().disable();
        http.cors(); // Enable CORS support

        // Other security configurations
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://127.0.0.1:5501");
        configuration.addAllowedOrigin("http://127.0.0.1:5500");// Allow requests from any origin (not recommended for production)
        configuration.addAllowedOrigin("http://192.168.1.72:5501"); // Allow requests from any origin (not recommended for production)
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {

        List<UserDetails> allusers = new ArrayList<>();
        List<Librarian>  allLibrarians = librarianDB.findAll();
        for(Librarian x : allLibrarians){

            UserDetails user = User.builder()
                    .username(x.getUsername())
                    .password(passwordEncoder().encode(x.getPassword())) // Encode the password
                    .roles("LIBRARIAN")
                    .build();

            allusers.add(user);

        }

        List<Member>  allmembers = memberDB.findAll();
        for(Member x : allmembers){

            UserDetails user = User.builder()
                    .username(x.getUsername())
                    .password(passwordEncoder().encode(x.getPassword())) // Encode the password
                    .roles("STUDENT")
                    .build();

            allusers.add(user);

        }


        return new InMemoryUserDetailsManager(allusers);
    }
}
