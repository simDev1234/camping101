package com.camping101.beta.global.security.handler;

import static com.nimbusds.oauth2.sdk.http.HTTPResponse.SC_UNAUTHORIZED;

import com.camping101.beta.util.FilterResponseHandler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        FilterResponseHandler.sendFilterExceptionResponse(response, getExceptionMessage(exception),
            SC_UNAUTHORIZED);

        if (exception instanceof Exception) {
            response.sendRedirect("/error");
        }

    }

    private String getExceptionMessage(AuthenticationException exception) {

        if (exception instanceof UsernameNotFoundException) {
            return "해당 회원이 존재하지 않습니다.";
        } else if (exception instanceof BadCredentialsException) {
            return "비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof DisabledException) {
            return "비활성화된 회원입니다.";
        } else if (exception instanceof LockedException) {
            return "정지된 회원입니다.";
        } else if (exception instanceof AccountExpiredException) {
            return "탈퇴한 회원입니다.";
        } else {
            return "확인된 에러가 없습니다.";
        }

    }

}


