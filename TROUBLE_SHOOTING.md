# 트러블 슈팅

## ISSUE 1 : AWS 빌드 중 Logback 관련 오류 발생
```bash
logging system failed to initialize using configuration from 'null'
```
- 원인 : ./logs/info.log에 대한 쓰기 권한이 없음. 서버 프로세스에서 에러가 발생할 때, Logback에 의해 ./logs 디렉토리에 info.log로 로그를 남기게 되는데,       
  서버를 배포한 사용자에게는 해당 경로에 접근하여 파일을 쓸 수 있는 권한이 없기 때문에 발생한 문제였다.
- 해결과정 : 
  단순하게 해결한다면, "sudo chmod 777 {dir or file}" 을 사용하면 이 문제를 해결할 수 있었는데, <br>
  왜 운영체제는 이러한 권한을 부여하는지와, 리눅스와 윈도우의 파일 권한 변경 과정을 비교하고 싶었다.<br><br>
  Linux와 Windows, MacOS는 모두 멀티 유저 시스템인 운영체제이다. <br>
  따라서 세 가지 운영체제 모두 각 사용자에 대해 파일 접근 권한을 설정할 필요가 있다.<br><br>
  Linux의 파일 시스템 구조는, 부트블록|슈퍼블록|inode리스트|데이터블록을 구분되어 있다.<br>
  -- 부트블록은 파일 시스템으로부터 리눅스 커널을 적재하기 위한 프로그램이며,<br>
  -- 슈퍼블록은 파일 시스템의 크기, 블록 수 등 이용 가능한 빈 블록 리스트와, <br> 빈 블록 리스트에서 그 다음 블록을 가리키는 인덱스 정보를 가지고 있다.<br>
  -- inode리스트는 file이나 directory에 대한 모든 정보를 가지고 있는 구조이다.<br>
  -- 데이터블록은 실제 데이터가 저장되어 있는 파일이다. <br><br>
  inode리스트는 1) 파일 소유자의 사용자 ID, 2) 파일 소유자의 그룹 Id, 3) 파일 크기, 4) 파일이 생성된 시간, 4) 데이터 블록 주소 등... 을 담고 있다.
  Linux에서 파일을 생성하면 해당 파일에 inode값이 inode리스트에 등록되고, 파일을 삭제하면 inode값은 -1이 되면서 inode리스트에서 해당 inode가 0으로 바뀐다. 
  inode리스트에 값이 0으로 바뀐다는 것은 해당 위치에 새로운 inode값을 넣을 수 있음을 의미했다.<br><br>
  Linux에서 ls -al을 입력하면 아래와 같이 파일에 부여된 Permission(권한)을 확인할 수 있다.<br>
  권한을 부여하지 않은 상태에서 로그가 들어가는 디렉토리를 보면 아래와 같았다.<br>
  ```bash
  drwxr-xr-x 2 root   root   4096 April 8 13:54 libs
  ```
  여기서의 "drwxr-xr-x"을 분해하면 아래와 같다.<br>
  "d" - 파일 유형을 의미 : d는 directory의 약자, l은 link의 약자, -는 일반파일을 의미한다.<br>
  "rwxr-xr-x" - 퍼미션 정보 : r은 read의 약자, w은 write의 약자, x는 execution(실행)의 약자를 말한다. <br>
  퍼미션 정보는 순서대로 "소유자", "그룹", "공개(전체)"에 대한 읽기/쓰기/실행 권한을 의미했다. <br>
  따라서, 위 코드를 풀이하면, libs는 "디렉토리"이며, 이 디렉토리의 소유자는 읽기와 쓰기를 할 수 있고, 소유자가 속한 사용자 그룹은 쓰기와 실행을 할 수 있으며, 나머지는 실행만 가능하다.<br><br>
  이때, 각각의 읽기/쓰기/실행의 숫자 코드는, r = 4, w = 2, x = 1로, 각각의 2^2, 2^1, 2^0 이진수로 이루어져 있다. <br>
  따라서, 위 퍼미션에 대해 전체 사용자 권한을 허용한다고 한다면, rwxrwxrwx이므로, 4 + 2 + 1 = 7, 곧 777이 된다. <br>
  그러면 libs의 소유자는 누구일까? 
  ```bash
  ls -l libs
  total 82556
  -rw-r--r-- 1 root root   427029 April  8 13:54 beta-0.0.1-SNAPSHOT-plain.jar
  -rw-r--r-- 1 root root 84104744 April  8 13:54 beta-0.0.1-SNAPSHOT.jar
  ```
  위 내용은 사용자가 root이며, 그룹이 root임을 의미했다. <br>
  기본적으로 Linux서버에서는 root만 파일의 생성 권한을 갖는다.  <br> 
  이 상태에서 java -jar beta(생략).jar 를 입력하면 같은 에러가 나타났다. <br>
  이번에는 배포 그룹을 생성해서 현재 접속한 사용자(ubuntu)를 그룹에 포함하고 해당 그룹에 libs 디렉토리에 대한 권한을 부여했다. <br>
  ```bash
  sudo groupadd deployer
  sudo chown ubuntu:deployer libs
  ```
  이렇게 하고 java jar beta(생략).jar을 하니, 이번에는 정상적으로 작동이 됐다. <br>
  결국 Logback이 정상적으로 실행되기 위해서는 배포하는 사용자에게 root에게 있었던 파일 저장 권한을 주면 해결이 되는 이슈였다.

<br>

## ISSUE 2 : AWS 인바운드 규칙에 IP 주소를 추가하였으나 접속 불가
- 원인 : 추가했던 IPv4 주소가 공유기에서 임시 발급한 사설 IP 주소였음을 확인 (IpConfig로 확인한 주소)
- 해결 : whatismyip 웹사이트를 통해 공용 IP 주소를 확인하여 수정 후 해결했다. <br>
  그러면 여기서 공용 IP와 사설 IP는 무엇일까..? IP주소는 말 그대로 데이터를 전달하는 도착지 주소를 말하는데, 이 중, 공용 IP는 인터넷 업체가 사용자에게 할당하는 IP를 말했다. 곧 내 방의 공유기에 할당되는 외부 IP를 의미했다. 내가 방에 있는 windows cmd 창을 열어서 IpConfig를 입력해 받은 사설 IP는 내 컴퓨터가 공유기로부터 할당 받은 주소를 의미했다. 결국, 진짜배기 IP는 whatismyip에 있는 외부 공용 IP였고, 이를 AWS EC2 인바운드 규칙에 넣어주어야만 접속이 가능한 것이었다. 팀원들의 공용 IP를 받아 넣으니 바로 해결됐다.

<br>

## ISSUE 3 : 프리티어로 AWS에 배포 시 너무 느리게 배포가 되는 현상
- 원인 : AWS EC2에서 사용할 수 있는 용량이 한정되어 있는데, 프로그램 용량이 그 보다 높아 메모리 부족으로 배포가 느려지는 현상이었다. 
- 해결 : 처음에는 원인을 알 수 없었는데 프리티어 용량을 확인해보니 메모리가 30GB 밖에 되지 않는다는 걸 보았고, 실제로 AWS CPU 사용률을 보니 100%에 가까웠다. <br>
  무료를 포기할 수가 없어서 Swap 파일을 생성해서 메모리 이슈를 해결해 보는 방안을 찾아보았다. (그러나 여전히 CPU는 제자리였다.) <br>
  프론트에서 API 테스트를 해야했기 때문에 일단은 ngrok을 사용해서 local PC에 접속할 수 있도록 하였다. <br>
  그리고 회의를 거쳐 팀원들과 함께 협의해 약간의 비용을 주더라도 서버를 업그레이드하여 편안하게 배포를 하기로 했다. <br>

<br>

## ISSUE 4 : Security ProviderNotFoundException for UsernamePasswordAuthenticationToken
- 원인 : SpringSecurity의 기본 AuthenticationProvider를 확장한 CustomAuthenticationProvider클래스에서 AuthenticationProvider의 supports 메소드에 커스텀 AuthenticationToken을 지정해주지 않아서 나타난 이슈였다. 일단 SpringSecurity의 기본 UsernamePassword 인증 절차를 풀이하면 다음과 같았다. <br>
  ```bash
  Spring Security의 기본 UsernamePassword 동작방식
  [1] client가 http를 통해 username과 password를 전달한다.
  [2] SpringSecurity의 FilterProxy가 일련의 Filter를 거치게 하는데, 그 중
  [3] UsernamePasswordAuthenticationFilter의 attemptAuthentication 메소드를 통해 username, password을 UsernamePasswordAuthenticationToken에 담는다.
      * UsernamePasswordAuthenticationFilter는 AbstractAuthenticationProcessingFilter를 상속하며, 상위 Filter의 attemptAuthentication을 Override했다.
      * UsernamePasswordAuthenticationToken은 AbstractAuthenticationToken을 상속했으며, 해당 상위 클래스는 principal(아이디)과 credentials(비번)를 갖는다.
        인증 절차가 이루어지기 전에는 이 principal과 password만 받는다면, 인증 절차가 이루어진 후에는 principal, password에 GrantedAuthorities를 담은 Collection 정보가 추가된다.
        AbstractAuthenticationToken은 또한 Authentication과 CredentialContainer를 구현하고 있는데, authorities : Collection<GrantedAutority>, details : Object, authenticated : boolean를 갖는다. 
        결과적으로 UsernamePasswordAuthenticationToken은 곧 SecurityHolder에 담길 Authentication의 확장을 의미한다.
  [4] AuthenticationManager는 Authentication을 받아 실제 구현체인 AuthenticationProvider에게 전달한다.
      * AuthenticationManager는 인터페이스이며, authenticate와 supports라는 메소드를 갖고 있다. 이 인터페이스를 구현한 것이 ProviderManger이다. 
      * ProviderManager의 authenticate 메소드 내부에서는 각각의 AuthenticationProvider를 조회하며 특정 Authentication 구현체를 supports하는 AuthenticationProvider를 찾는다
  [5] DaoAuthenticationProvider(AbstractUserDetailsAuthenticationProvider)를 통해 실질적인 인증 절차가 이루어지며 UsernamePasswordAuthenticationToken이 생성된다.
      * 기본 설정된 AbstractUserDetailsAuthenticationProvider는 UsernamePasswordAuthenticationToken을 supports하는 AuthenticationProvider이다.
      * 이 Provider를 또다시 상속한 것이 DaoAuthenticationProvider인데, 이 클래스는 addtionalAuthenticationCheck()와 retrieveUser() 메소를 갖고 있다. 
        retrieveUser()는 UserDetailsService로부터 UserDetails를 가져오며, additionalAuthenticationCheck()는 입력 정보와 UserDetails의 정보를 비교해 인증을 체크한다.
        만약, 두 정보가 일치한다면, createSuccessfulAuthenticaion()을 통해 UsernamePasswordAuthenticationToken에 principal, password, details 정보가 담겨 반환된다. 
  [6] [5]에서 반환된 UsernamePasswordAuthenticationToken은 Authentication 객체로 다시 [3]의 필터에 넘겨진다. 그렇게 넘겨진 Authentication은 UsernamePasswordAuthenticationFilter의 successfulAthentication() 메소드를 통해 SpringSecurityHolder에 담겨지게 되고, 이 때 담겨진 Authentication 정보는 Controller 클래스에서 Principal 및 UserDetails 등을 통해 가져올 수 있게 된다.
  ```
- 해결 : 나의 경우, UsernamePasswordAuthenticationFilter를 상속하여 JwtAuthenticationFilter를 만들었고, <br>
  기존의 UsernamePasswordAuthenticationtoken을 그대로 사용하였으며, <br>
  AuthenticationProvider를 확장하여 UsernamePasswordAuthenticationProvider라는 커스텀 Provider를 만들었다.<br>
  ProviderManger가 UsernamePasswordAuthenticationToken을 사용하는 UsernamePasswordAuthencationProvider를 찾기 위해서는 아래와 같이 supports에 UsernamePasswordAuthenticationToken.class를 할당할 수 있는지 확인해야 했다. 아래와 같이 고치니 잘 동작했다.
```java
@Override
public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
}
```

<br>
 
## ISSUE 5 : GET 요청 시 아래와 같은 예외 발생
```bash
[http-nio-8080-exec-7] org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver Resolved 
[org.springdiaTypeNotSupportedException: Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
```
- 원인 : swagger에서 GET 요청 타입을 디폴트인 x-www-form-urlencoded로 전송
- 해결 : swagger config 설정을 아래와 같이 변동
```java
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SPRING_WEB)
                .host(host.substring(7))
                .consumes(getConsumeContentTypes())  // 요청 타입 추가
                .produces(getProduceContentTypes())  // 응답 타입 추가
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.camping101.beta"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }
```

<br>

## ISSUE 6 : Swagger 요청 시 400 에러
![img.png](.github/img/swagger-error.png)
- 원인 : "/webjars" 하위 경로에 대한 접근 허용 X 
- 해결 : swagger의 WebSecurity ignore 경로의 "/webjars/"를 "/webjars/**"로 변경 
```java
@Override
public void configure(WebSecurity web) {

    web.ignoring()
            .antMatchers("/h2-console/**","/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/v2/api-docs")
            .antMatchers("/css/**", "/vendor/**", "/js/**", "/images/**")
            .antMatchers(HttpMethod.OPTIONS, "/**");
}
```

<br>


