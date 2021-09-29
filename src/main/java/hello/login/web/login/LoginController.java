package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "/login/loginForm";
    }

//    @PostMapping("login")
    public String loginV1(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        // biz rule
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            return "/login/loginForm";
        }

        log.info("login={}", loginForm);

        // 쿠키를 만들어 웹 브라우저에 반환한다
        Cookie cookieId = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(cookieId);

        return "redirect:/";
    }

//    @PostMapping("login")
    public String loginV2(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        // biz rule
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            return "/login/loginForm";
        }

        log.info("login={}", loginForm);

        // 세션 생성 후, 세션Id를 담은 쿠키 반환
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    @PostMapping("login")
    public String loginV3(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        // biz rule
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            return "/login/loginForm";
        }

        log.info("login={}", loginForm);

        // 세션 생성 후, 세션Id를 담은 쿠키 반환
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    //    @PostMapping("/logout")
    public String logoutV1(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.expire(request, response);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}