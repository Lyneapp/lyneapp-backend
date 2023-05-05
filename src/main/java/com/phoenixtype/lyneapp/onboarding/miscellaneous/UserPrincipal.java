package com.phoenixtype.lyneapp.onboarding.miscellaneous;

import com.phoenixtype.lyneapp.onboarding.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add authorities based on the user's role
        switch (user.getRole()) {
            case "admin" -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            case "premium" -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            case "user" -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            default -> authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isAccountEnabled();
    }
}
