package ar.edu.iua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ar.edu.iua.auth.CustomTokenAuthenticationFilter;
import ar.edu.iua.auth.PersistenceUserDetailService;
import ar.edu.iua.authtoken.IAuthTokenBusiness;
import ar.edu.iua.business.IUserBusiness;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean // Cifrador
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private PersistenceUserDetailService persistenceUserDetailService;

	// Sistema de usuarios
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(persistenceUserDetailService);
		/*
		 * @formatter:off auth.inMemoryAuthentication() .withUser("user").password(
		 * passwordEncoder().encode("123") ).roles("USER") .and()
		 * .withUser("admin").password( passwordEncoder().encode("1234")
		 * ).roles("ADMIN");
		 * 
		 * @formatter:on
		 */
	}

	@Autowired
	private IAuthTokenBusiness authTokenBusiness;

	@Autowired
	private IUserBusiness userBusiness;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
				http
					.csrf().disable()
					.authorizeRequests()
					.antMatchers("/admin/**").hasRole("ADMIN")
					.antMatchers("/anonymous*").anonymous()
					.antMatchers("/login*").permitAll()
					.antMatchers("/logout*").permitAll()
					.antMatchers("/").permitAll()
				 	.antMatchers("/index.html").permitAll()
				 	.antMatchers("/ordenes.html").permitAll()
					.antMatchers("/favicon.*").permitAll()	   
					.antMatchers("/ui/**").permitAll()

					.anyRequest().authenticated();
		// @formatter:on

		// sistema de autenticacion
		/*
		 * .httpBasic(); .formLogin() .loginPage("/login.html")
		 * .loginProcessingUrl("/dologin") .defaultSuccessUrl("/index.html",true)
		 * .failureUrl("/login.html?error=true") .and() .logout()
		 * .deleteCookies("JSESSIONID","rmFinalIW3") .logoutSuccessUrl("/login.html")
		 * .and() .rememberMe().rememberMeParameter("recordarme").rememberMeCookieName(
		 * "rmFinalIW3") .tokenValiditySeconds(10); //Si ponemos 0 es indefinido
		 */

		// Cliente (Brower) ----> | App Server (tomcat) --> App --> (Filtro1 , Filtro2,
		// FiltroN) (Spring)REST-->Capa Negocio --> Persistencia --> Modelo
		http.addFilterAfter(new CustomTokenAuthenticationFilter(authTokenBusiness, userBusiness),
				UsernamePasswordAuthenticationFilter.class);

		// Inhabilitamos la creacion de sesiones. HAcemos que spring se comporte sin
		// estado
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// .httpBasic();
		/*
		 * .formLogin() .loginPage("/login.html") .loginProcessingUrl("/dologin")
		 * .defaultSuccessUrl("/index.html",true) .failureUrl("/login.html?error=true")
		 * .and() .logout() .deleteCookies("JSESSIONID","rmiw3")
		 * .logoutSuccessUrl("/login.html") .and()
		 * .rememberMe().rememberMeParameter("rememberme-iw3").rememberMeCookieName(
		 * "rmiw3") //.alwaysRemember(true) ;
		 */

		// @formatter:on
	}
}