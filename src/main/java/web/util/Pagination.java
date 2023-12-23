package web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class Pagination {


    public static final int SIZE_ATTR_VALUE = 10;
    private static final String PAGE_ATTR = "page";
    private static final String SIZE_ATTR = "size";
    private static final String NUMBER_OF_PAGES_ATTR = "numberOfPages";
    private static final String COMMAND_ATTR = "command";

    public void paginate(int page, int size, long entries, HttpServletRequest request, String command) {
        int numberOfPages = (int) Math.ceil((double) entries / size);

        request.setAttribute(PAGE_ATTR, page);
        request.setAttribute(SIZE_ATTR, size);
        request.setAttribute(NUMBER_OF_PAGES_ATTR, numberOfPages);
        request.setAttribute(COMMAND_ATTR, command);
    }

    public boolean validate(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getParameter(PAGE_ATTR)) > 0
                    && Long.parseLong(request.getParameter(SIZE_ATTR)) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }

    }

}
