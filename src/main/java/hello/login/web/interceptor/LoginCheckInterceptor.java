package hello.login.web.interceptor;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Iterator;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 해당 요청이 로그인이 필요한 경로인지 확인하는 것은 WebConfig에 등록할 때 사용
        HttpSession httpSession = request.getSession(false);

        if (httpSession == null || httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("requestURL {}", request.getRequestURI());
            response.sendRedirect("/login?redirectURL=" + request.getRequestURI());

            Enumeration<String> parameterNames = request.getParameterNames();
            Iterator<String> stringIterator = parameterNames.asIterator();
            while(stringIterator.hasNext()) {
                log.info("parameter => {}", stringIterator.next());
            }

            return false;
        }

        return true;
    }
}
