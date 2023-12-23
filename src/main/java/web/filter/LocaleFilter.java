package web.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Locale;

import static web.constant.AttributeConstants.REQUEST_LANG;
import static web.constant.AttributeConstants.SESSION_LANG;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class LocaleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        //nothing to init
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getParameter(REQUEST_LANG) != null) {
            Config.set(request.getSession(), Config.FMT_LOCALE, new Locale(request.getParameter(REQUEST_LANG)));
        } else if (request.getSession().getAttribute(SESSION_LANG) == null) {
            Config.set(request.getSession(), Config.FMT_LOCALE, Locale.getDefault());
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
//nothing to destroy
    }
}
