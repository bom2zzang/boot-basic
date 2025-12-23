# 09) Spring Security (Form Login / 권한 / CSRF / 세션)

## 설치
```gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE'
```
---

## 기본 설정(SecurityConfig)

개발 초기에 전체 허용(학습용):

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());

    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/**").permitAll()
    );

return http.build();
  }
}
```

---

## Form Login 설정 (로그인 구현)

```java
http.formLogin(form -> form
    .loginPage("/login")
    .defaultSuccessUrl("/")
    .failureUrl("/fail")
);
```

---

## UserDetailsService (DB에서 유저 조회)

Security가 비밀번호 검증하려면 “DB에서 유저/비번을 어떻게 찾는지”를 알려줘야 함.

```java
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

private final MemberRepository memberRepository;

@Override
public UserDetailsloadUserByUsername(String username) throws UsernameNotFoundException {
    var result= memberRepository.findByUsername(username);
    if (result.isEmpty()) throw new UsernameNotFoundException("그런 아이디 없음");
    
    var user= result.get();

    List<GrantedAuthority> authorities =newArrayList<>();
    authorities.add(newSimpleGrantedAuthority("일반유저"));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        authorities
    );
  }
}
```

### Repository

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member>findByUsername(String username);
}
```

---

## Thymeleaf에서 로그인 정보 출력/분기

```html
<div sec:authorize="isAuthenticated()" sec:authentication="principal.username"></div>
<div sec:authorize="isAnonymous()">
<a th:href="@{/login}">로그인</a>
</div>

<div sec:authorize="hasAuthority('일반유저')">
  권한 있는 사람만 보임
</div>
```

---

## 로그아웃 설정

```java
http.logout(logout -> logout.logoutUrl("/logout"));
```

---

## @PreAuthorize로 접근 제한

```java
@PreAuthorize("isAuthenticated()")
@GetMapping("/admin")
public String admin() {
    return"admin";
}
```

---

## Password 해싱 (BCrypt) + Bean 등록

```java
@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

사용(DI):

```java
privatefinal PasswordEncoder passwordEncoder;
```

---

## CustomUser로 principal 확장

```java
public class CustomUser extends User {
    public Long id;
    public String displayName;
    
    public CustomUser(String username, String password, List<GrantedAuthority> authorities) {
      super(username, password, authorities);
    }
}

```

캐스팅:

```java
CustomUser user= (CustomUser) auth.getPrincipal();
System.out.println(user.displayName);

```

---

## 세션 유지시간

`application.properties`

```
server.servlet.session.timeout=90m
server.servlet.session.cookie.max-age=90m
```

---

## 세션 DB 저장(Spring Session JDBC)

```
implementation 'org.springframework.session:spring-session-jdbc'
```

`application.properties`

```
spring.session.store-type=jdbc
```

---

## CSRF 토큰(필요 시)

```java
@Bean
public CsrfTokenRepository csrfTokenRepository() {
HttpSessionCsrfTokenRepository repo = new HttpSession CsrfTokenRepository();
  repo.setHeaderName("X-XSRF-TOKEN");
return repo;
}

```

```java
http.csrf(csrf -> csrf
    .csrfTokenRepository(csrfTokenRepository())
    .ignoringRequestMatchers("/login")
);
```

Thymeleaf form에 포함:

```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
```