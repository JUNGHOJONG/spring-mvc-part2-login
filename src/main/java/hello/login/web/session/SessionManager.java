package hello.login.web.session;

import hello.login.domain.member.Member;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    public Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 생성
     */
    public void createSession(Object value, HttpServletResponse response) {
        String mySessionId = UUID.randomUUID().toString();
        sessionStore.put(mySessionId, value);

        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, mySessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request);
        if (sessionCookie == null) {
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
                .findAny()
                .orElse(null);
    }

    /**
     * 소멸
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }
}