package ru.kata.spring.boot_security.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter @Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "enter name")
    @Size(min=4, max=30, message = "min4max30")
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @NotBlank(message = "enter lastname")
    @Size(min=4, max=30, message = "min4max30")
    private String lastName;

    @Column(name="age")
    @NotNull
    @Min(value=0, message=">0")
    @Max(value = 150, message = "<150")
    @Digits(integer = 2, fraction = 0)
    private int age;

    @Column(name = "email", unique = true)
    @NotNull
    @NotBlank(message = "enter email")
    @Email(message = "not valid email")
    private String email;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy") //automatically format string from form
    private Date birth;

    @Column(name = "password", length = 100)
    @NotBlank
    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            uniqueConstraints = { @UniqueConstraint(columnNames = {"person_id", "role_id" }) })
    private List<Role> roles;

    public User(String firstName, String lastName, int age, String email, Date birth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.birth = birth;
        this.roles = new ArrayList<>();
    }

    public void addRole(@com.sun.istack.NotNull Role role) {
        this.roles.add(role);
    }

    public String getRolesString() {
        return roles.stream()
                .map(role -> role.getName().split("_")[1])
                .collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", birth='" + birth + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
    // ===================Next: UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    public String getPassword() {
        return this.password;
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
        return true;
    }
}
