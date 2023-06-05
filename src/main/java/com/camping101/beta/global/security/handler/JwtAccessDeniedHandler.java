package com.camping101.beta.global.security.handler;

import static com.nimbusds.oauth2.sdk.http.HTTPResponse.SC_FORBIDDEN;

import com.camping101.beta.util.FilterResponseHandler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        FilterResponseHandler.sendFilterExceptionResponse(response, "권한이 없습니다.", SC_FORBIDDEN);

        if(accessDeniedException instanceof Exception) {
            response.sendRedirect("/error");
        }

    }

}
