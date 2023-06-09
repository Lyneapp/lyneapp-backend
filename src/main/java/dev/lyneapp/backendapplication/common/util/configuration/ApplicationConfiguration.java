package dev.lyneapp.backendapplication.common.util.configuration;


import dev.lyneapp.backendapplication.common.util.exception.ExceptionMessages;
import dev.lyneapp.backendapplication.common.util.exception.PhoneNumberNotFoundException;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    // FIXME - DO NOT INCLUDE IN PRODUCTION
    @PostConstruct
    public void dropUsersCollection() {
        mongoTemplate.dropCollection("users");
    }

    // FIXME - DO NOT INCLUDE IN PRODUCTION
    @PostConstruct
    public void dropEmailTokenCollection() {
        mongoTemplate.dropCollection("email_token");
    }

    // FIXME - DO NOT INCLUDE IN PRODUCTION
    @PostConstruct
    public void dropJwtTokenCollection() {
        mongoTemplate.dropCollection("jwt_token");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userPhoneNumber -> userRepository.findByUserPhoneNumber(userPhoneNumber).orElseThrow(() -> new PhoneNumberNotFoundException(ExceptionMessages.PHONE_NUMBER_DOES_NOT_EXIST));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Qualifier("freemarker")
    public FreeMarkerConfigurationFactoryBean factoryBean() {
        FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean = new FreeMarkerConfigurationFactoryBean();
        freeMarkerConfigurationFactoryBean.setTemplateLoaderPath("classpath:/templates");
        return freeMarkerConfigurationFactoryBean;
    }
}
