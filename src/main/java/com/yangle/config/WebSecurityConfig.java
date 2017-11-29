package com.yangle.config;

import com.yangle.security.CustAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yangle on 2017/9/24.
 */
@Configuration //配置文件
@EnableWebSecurity //开启security
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private CustAuthenticationProvider custProvider;
    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        路由策略和访问权限的简单配置
        http
                .authorizeRequests()
                // 例如以下代码指定了/和/index不需要任何认证就可以访问，其他的路径都必须通过身份验证。
                .antMatchers("/defaultKaptcha","/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .failureUrl("/login?error")
                .defaultSuccessUrl("/home")
                .loginPage("/login")
                .permitAll()
                .authenticationDetailsSource(authenticationDetailsSource)
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")//退出登录后的默认url是"/home"
                .permitAll();
        http.csrf().disable();
        super.configure(http);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      /*  auth
                .inMemoryAuthentication()
                .withUser("bob").password("123").roles("USER");*/
        auth.authenticationProvider(custProvider);
    }
}
