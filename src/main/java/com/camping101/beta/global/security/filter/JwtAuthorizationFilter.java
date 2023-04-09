package com.camping101.beta.global.security.filter;

import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.util.FilterResponseHandler;
import com.camping101.beta.web.domain.member.service.signin.MemberSignInService;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberSignInService memberSignInService;
    private final TokenService tokenService;
    private final String ignoreAllPathsStartWith;
    private final String ignoreGetPathsStartWith;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("Authorization");

        log.info("JwtAuthorizationFilter 접근 경로 : {}", request.getServletPath());

        if (isPermittedPath(request.getServletPath(), request.getMethod())) {

            filterChain.doFilter(request, response);

        } else if (!tokenService.isNotBlankAndStartsWithBearer(accessToken)) {

            FilterResponseHandler.sendFilterExceptionResponse(response, "헤더에 Access Token이 없습니다.", SC_BAD_REQUEST);

        } else {

            try {

                String email = tokenService.extractEmailByAccessToken(accessToken);
                MemberDetails memberDetails = memberSignInService.loadUserByUsername(email);
                Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);

            } catch(ExpiredJwtException e){

                FilterResponseHandler.sendFilterExceptionResponse(response, "만료된 Access Token 입니다.", SC_UNAUTHORIZED);

            } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {

                FilterResponseHandler.sendFilterExceptionResponse(response, "잘못된 Access Token 입니다.", SC_BAD_REQUEST);

            }
        }
    }

    private boolean isPermittedPath(String servletPath, String httpMethod) {
        return isPermittedAllPath(servletPath) || isPermittedGetPath(servletPath, httpMethod);
    }

    private boolean isPermittedAllPath(String servletPath){

        boolean result = Arrays.stream(ignoreAllPathsStartWith.split(",")).filter(x -> servletPath.startsWith(x)).count() >= 1;

        log.info("JwtAuthorizationFilter.isPermittedAllPath ? {} -> {}", servletPath, result);

        return result;
    }

    private boolean isPermittedGetPath(String servletPath, String httpMethod) {
        String[] permitGetPaths = ignoreGetPathsStartWith.split(",");
        if ("GET".equals(httpMethod) == false) return false;
        return Arrays.stream(permitGetPaths).filter(x -> servletPath.startsWith(x)).count() >= 1;
    }

}
