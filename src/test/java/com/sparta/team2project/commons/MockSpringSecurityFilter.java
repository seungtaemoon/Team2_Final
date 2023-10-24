package com.sparta.team2project.commons;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

// Spring Security 필터
public class MockSpringSecurityFilter implements Filter {

    @Override
    // 필터의 초기화
    public void init(FilterConfig filterConfig) {}

    @Override
    // doFilter 는 HTTP 요청과 응답을 가로채고 처리함
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 인증 객체를 담고 있음
        SecurityContextHolder.getContext()
                .setAuthentication((Authentication) ((HttpServletRequest) req).getUserPrincipal());
        chain.doFilter(req, res);
    }

    @Override
    // 필터 종료시 정리작업 보안컨넥스트를 클리어하고 메모리 누수 방지
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}
