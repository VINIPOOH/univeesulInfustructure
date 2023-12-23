package web.filter;

import dal.entity.RoleType;
import dal.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.REDIRECT_ON_ERROR_404_STRAIGHT;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class AuthorisationFilter implements Filter {

    private static final String ADMIN_REQUEST = "/admin";
    private static final String USER_REQUEST = "/user";
    private static final String ANONYMOUS = "/anonymous";

    @Override
    public void init(FilterConfig filterConfig) {
        //nothing to init
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String path = req.getRequestURI();
        User user = (User) session.getAttribute(SESSION_USER);

        if ((path.contains(USER_REQUEST)) && (user == null || !(user.getRoleType().equals(RoleType.ROLE_ADMIN) || (user.getRoleType().equals(RoleType.ROLE_USER))))) {
            res.sendRedirect(REDIRECT_ON_ERROR_404_STRAIGHT);
            return;
        }
        if ((path.contains(ADMIN_REQUEST)) && (user == null || !(user.getRoleType().equals(RoleType.ROLE_ADMIN)))) {
            res.sendRedirect(REDIRECT_ON_ERROR_404_STRAIGHT);
            return;
        }
        if ((path.contains(ANONYMOUS)) && user != null) {
            res.sendRedirect(REDIRECT_ON_ERROR_404_STRAIGHT);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //nothing to destroy
    }
}


