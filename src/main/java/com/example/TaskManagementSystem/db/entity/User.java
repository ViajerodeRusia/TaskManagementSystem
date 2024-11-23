package com.example.TaskManagementSystem.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Task> authoredTasks;

    @OneToMany(mappedBy = "assignee")
    private List<Task> assignedTasks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return login;  // Возвращаем login как username
    }

    @Override
    public String getPassword() {
        return password;  // Возвращаем пароль пользователя
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Учетная запись не просрочена
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Учетная запись не заблокирована
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Пароль не истек
    }

    @Override
    public boolean isEnabled() {
        return true;  // Учетная запись активна
    }
}