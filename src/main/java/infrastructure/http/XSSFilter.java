package infrastructure.http;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class XSSFilter implements Filter {

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);

    }

    static final class RequestWrapper extends HttpServletRequestWrapper {

        RequestWrapper(HttpServletRequest servletRequest) {
            super(servletRequest);
        }

        public String[] getParameterValues(String parameter) {

            String[] values = super.getParameterValues(parameter);
            if (values == null) {
                return null;
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = cleanXSS(values[i]);
            }
            return encodedValues;
        }

        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            if (value == null) {
                return null;
            }
            return cleanXSS(value);
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            if (value == null)
                return null;
            return cleanXSS(value);

        }

        private String cleanXSS(String value) {
            value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
            value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
            value = value.replaceAll("'", "& #39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']", "\"\"");
            value = value.replaceAll("script", "");
            return value;
        }
    }
}