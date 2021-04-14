/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mike.servlets.lib;

import cat.proven.model.Project;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mati
 */
public class ProjectForm {
    /**
     * gets and validates data sent by client.
     *
     * @param request http request object to get data from
     * @return friend object with data sent from client or null in case of
     * error.
     */
    public static Project ofProjectForm(HttpServletRequest request) throws ParseException {
        Project project = null;
        try {
            String title = request.getParameter("title");
            if ((title == null)) {
                project = null;
            } else {                
                project = new Project(title);
            }
        } catch (NumberFormatException e) {
            project = null;
        }
        return project;
    }

    /**
     * gets and validate Friend ang return different code errors -1 phone not
     * informed -2 name not informed -3 age not informed -4 age not integer
     *
     * @param request
     * @return RequestResult (Friernd, code)
     */
    public static RequestResult getParameters(HttpServletRequest request) throws ParseException {
        RequestResult result = null;
        Project project = null;
        int code = 0;
        try {
            String title = request.getParameter("title");
            if (title == null) {
                code = -1;
            }else {
                project = new Project(title);
            }
        } catch (NumberFormatException e) {
            code = -2;
        }
        if (project != null) {
            result = new RequestResult(project, 1);
        } else {
            result = new RequestResult("Error in  parameters",
                     code);
        }
        return result;
    }
}
