package org.jk.eSked.backend.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService {

    private static UserService userService;

    public SessionService(UserService userService) {
        SessionService.userService = userService;
    }

    public static VaadinSession getSession() {
        return VaadinSession.getCurrent();
    }

    public static WebBrowser getBrowser() {
        return VaadinSession.getCurrent().getBrowser();
    }

    public static UUID getUserId() {
        return VaadinSession.getCurrent().getAttribute(User.class).getId();
    }

    public static boolean isSessionMobile() {
        return VaadinSession.getCurrent().getBrowser().getBrowserApplication().contains("Mobile");
    }

    public static void setAutoTheme() {
        System.out.println("setting theme");
        UI.getCurrent().getPage()
                .executeJs("document.documentElement.setAttribute(\"theme\",\"" + userService.getTheme(getUserId()).toString().toLowerCase() + "\")");
    }
}