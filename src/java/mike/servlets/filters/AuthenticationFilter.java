package mike.servlets.filters;

import mike.servlets.lib.RequestResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter to resolve all service requests
 *
 * @author ProvenSoft
 */
//@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/servlet/*"})
public class AuthenticationFilter implements Filter {

    /**
     * debugs is true if log is enabled.
     */
    private static final boolean debug = true;

    /**
     * The filter configuration object we are associated with. If this value is
     * null, this filter instance is not currently configured. 
    *
     */
    private FilterConfig filterConfig = null;

    /**
     * Init method for this filter
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            log("AuthenticationFilter:Initializing filter");
        }
    }

    /**
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     * @throws IOException if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        // will return current session if current session exists, 
        // then it will NOT create a new session.
        //HttpSession session = req.getSession(false);

        //log(req.getServletPath());
        // Process Request
        //if (session != null && session.getAttribute("role") != null ) {
        if (accessGranted(req)) {
            //String user = (String) session.getAttribute("user");
            //String role = (String) session.getAttribute("role");
            chain.doFilter(request, response);
        } else {
            log("AuthenticationFilter: User not authenticated");
            RequestResult result = new RequestResult("User not authenticated", 0);
            request.setAttribute("result", result);
            request.getRequestDispatcher("/WEB-INF/jsp/json-result.jsp").forward(req, res);
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthenticationFilter()");
        }
        StringBuilder sb = new StringBuilder("AuthenticationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    /**
     * logs a message to server console.
     *
     * @param msg to show
     */
    public void log(String msg) {
        if (debug) {
            filterConfig.getServletContext().log(msg);
        }
    }

    /**
     * checks access permission.
     *
     * @param req HttpServletRequest
     * @return true if service is granted to user, false otherwise.
     */
    private boolean accessGranted(HttpServletRequest req) {
        //define public services
        Collection<String> publicServices = new ArrayList<>();
        publicServices.add("login");
        publicServices.add("logout");
        publicServices.add("friend");
        //
        boolean granted = false;
        //get uri information
        String route = req.getRequestURI();
        String query = req.getQueryString();
        log(String.format("Route=%s; Query=%s", route, (query == null) ? "no query" : query));
        String service = route.substring(route.lastIndexOf('/') + 1);
        log("Service=" + service);
        if (publicServices.contains(service)) { //public service, login not needed.
            granted = true;
        } else { //not a public service -> check session
            //get session information
            HttpSession session = req.getSession(false);
            //check permissions
            if (session != null) {
                String username = (String) session.getAttribute("username");
                String userrole = (String) session.getAttribute("userrole");
                if ((username != null) && (userrole != null)) {
                    //TODO check rol permissions.
                    granted = true;
                }
            } else {
                granted = false;
            }
        }
        return granted;
        //return true;
    }

}
