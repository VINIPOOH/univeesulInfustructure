package web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class EncodingFilter implements Filter {

    private static final String CONTENT_TYPE = "text/html";
    private static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
        //nothing to init
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType(CONTENT_TYPE);
        servletResponse.setCharacterEncoding(ENCODING);
        servletRequest.setCharacterEncoding(ENCODING);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //nothing to destroy
    }
}
