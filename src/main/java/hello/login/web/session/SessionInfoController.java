package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "세션 없음";
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("key={}, value={}", name, session.getAttribute(name)));

        // 단일 설정 방법
//        session.setMaxInactiveInterval(60);

        log.info("session.getCreationTime={}", new Date(session.getCreationTime()));
        log.info("session.getLastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("session.getMaxInactiveInterval={}", session.getMaxInactiveInterval());

        return "세션 존재!!";
    }
}
