package mike.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mike.servlets.lib.RequestResult;
import proven.users.model.User;
import proven.users.model.persist.*;

/**
 * Login servlet.
 *
 * @author ProvenSoft
 */
public class LoginServlet extends HttpServlet {

    /**
     * Unsure. Not to be used in production. Use only POST instead.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        //retrieve validate parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        RequestResult result;  //result to be sent back to client.
        UserDaoList userDao = UserDaoList.getInstance();
        if (userDao.validate(new User(username, password))) {
            //if (new Users().validate(username, password)) {
            // will return current session. If current session does not exist, 
            // then it will create a new session.
            HttpSession session = request.getSession(true);
            User user = userDao.select(new User(username));
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userrole", user.getRole());
            String msg = "login ok";
            result = new RequestResult(msg, 1);
        } else {
            String msg = "login fail";
            result = new RequestResult(msg, 0);
        }
        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     * Example of Response directly in HTML
     *
     * @param response
     * @param msg
     * @throws IOException
     */
    private void responseHtml(HttpServletResponse response, String msg)
            throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<p>" + msg + "</p>");
        out.println("</body>");
        out.println("</html>");
    }

}

/**
 * mock class to validate username.
 *
 * @author ProvenSoft
 */
class Users {

    private final String secretUser = "peter";
    private final String secretPassword = "1234";

    public boolean validate(String user, String password) {
        boolean valid = false;
        if (secretUser.equals(user) && secretPassword.equals(password)) {
            valid = true;
        }
        return valid;
    }
}
