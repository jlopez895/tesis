package ar.edu.iua.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 4647570640764087147L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 80, nullable = false)
    private String nombre;

    @Column(length = 300, nullable = false, unique = true)
    private String email;

    @Column(length = 80, nullable = false)
    private String apellido;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Column(length = 100)
    private String password;

    @Column(length = 8)
    private String legajo;

    @ManyToOne
    @JoinColumn(name = "id_rol_principal")
    private Rol rolPrincipal;
    
    @ManyToOne
    @JoinColumn(name = "id_ministerio_principal",nullable = true)
    private Ministerio ministerioPrincipal;

    @Column(columnDefinition = "tinyint default 1")
    private boolean accountNonExpired = true;

    @Column(columnDefinition = "tinyint default 1")
    private boolean accountNonLocked = true;

    @Column(columnDefinition = "tinyint default 1")
    private boolean credentialsNonExpired = true;

    @Column(columnDefinition = "tinyint default 1")
    private boolean enabled;

    @Transient
    private String sessionToken;

    @Column(columnDefinition = "int default 360")
    private int sessionTimeout;

    // Constructor
    public User(int id, String nombre, String apellido, String email, String password, Rol rolPrincipal,Ministerio ministerioPrincipal, boolean enabled,
            String legajo, String username) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.username = username;
        this.rolPrincipal = rolPrincipal;
        this.ministerioPrincipal=ministerioPrincipal;
        this.enabled = enabled;
        this.legajo = legajo;
    }

    // Empty Constructor
    public User() {

    }

    // Getters and Setters
    public Rol getRolPrincipal() {
        return rolPrincipal;
    }

    public void setRolPrincipal(Rol rolPrincipal) {
        this.rolPrincipal = rolPrincipal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getlegajo() {
        return legajo;
    }

    public void setlegajo(String legajo) {
        this.legajo = legajo;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	SimpleGrantedAuthority s=new SimpleGrantedAuthority(getRolPrincipal().getRol());
    	
        List<GrantedAuthority> authorities=new ArrayList<>();
        authorities.add(s);

        return authorities;
    }

    @Transient
    public String getNombreCompleto() {
        return String.format("%s, %s", getApellido(), getNombre());
    }

    public String checkAccount(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, getPassword()))
            return "BAD_PASSWORD";
        if (!isEnabled())
            return "ACCOUNT_NOT_ENABLED";
        if (!isAccountNonLocked())
            return "ACCOUNT_LOCKED";
        if (!isCredentialsNonExpired())
            return "CREDENTIALS_EXPIRED";
        if (!isAccountNonExpired())
            return "ACCOUNT_EXPIRED";

        return null;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

	public Ministerio getMinisterioPrincipal() {
		return ministerioPrincipal;
	}

	public void setMinisterioPrincipal(Ministerio ministerioPrincipal) {
		this.ministerioPrincipal = ministerioPrincipal;
	}
    
    
}
