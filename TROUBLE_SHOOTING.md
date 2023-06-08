# 트러블 슈팅

## ISSUE 1 : AWS 빌드 중 Logback 관련 오류 발생
```bash
logging system failed to initialize using configuration from 'null'
```
- 원인 : ./logs/info.log에 대한 쓰기 권한이 없음 
  서버 프로세스에서 에러가 발생할 때, Logback에 의해 ./logs 디렉토리에 info.log로 로그를 남기게 되는데,       
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

## ISSUE 2 : AWS 인바이드 규칙에 IP 주소를 추가하였으나 접속 불가
- 원인 : 추가했던 IPv4 주소가 공유기에서 임시 발급한 사설 IP 주소였음을 확인 (IpConfig로 확인한 주소)
- 해결 : whatismyip 웹사이트를 통해 공용 IP 주소를 확인하여 수정

<br>

## ISSUE 3 : 캠핑장 삭제 시 DataIntegrityViolationException 발생
```bash
DataIntegrityViolationException could not execute statement
``` 
- 원인 : 캠핑장 삭제시 캠핑장 승인 테이블의 속성에 camp_id가 남아있어 무결성 문제로 예외 발생
- 해결 : CampAuth -> Camp 단방향 연관관계에서, CampAuth <-> Camp 양방향 연관관계로 변경 후, <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Camp 엔터티의 CampAuth List 필드에 cascade = CascadeType.Remove 속성 추가

<br>
 
## ISSUE 4 : GET 요청 시 아래와 같은 예외 발생
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

## ISSUE 5 : Swagger 요청 시 400 에러
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

## ISSUE 6 : Security ProviderNotFoundException for UsernamePasswordAuthenticationToken
- 원인 : CustomAuthenticationProvider의 supports 부분에 UsernamePasswordAuthenticationToken으로 설정하지 않았음
- 해결 : 아래와 같이 코드 수정하여 해결
```java
@Override
public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
}
```
