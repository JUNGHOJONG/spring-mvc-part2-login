package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 로그인 어노테이션이 있어야 하고 클래스가 Member 이어야 한다
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean assignable = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasParameterAnnotation && assignable;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 세션Id 가 존재할 때, 원하는 세션객체가 있는지 확인하여 반환한다
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession == null) {
            return null;
        }

        return httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}