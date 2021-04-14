package mike.servlets.lib;

import mike.servlets.lib.RequestResult;
import cat.proven.model.Person;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class PersonForm {

    /**
     * gets and validates data sent by client.
     *
     * @param request http request object to get data from
     * @return friend object with data sent from client or null in case of
     * error.
     * @throws java.text.ParseException
     */
    public static Person ofPersonForm(HttpServletRequest request) throws ParseException {
        Person person = null;
        try {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String sbirthdate = request.getParameter("birthdate");
            String role = request.getParameter("role");
            if ((name == null) || (surname == null) || (sbirthdate == null) || (role == null)) {
                person = null;
            } else {
                //Parse date                
                Date dDate = new SimpleDateFormat("dd/MM/yyyy").parse(sbirthdate);
                System.out.println(dDate);
                
                person = new Person(name, surname, dDate, role);
            }
        } catch (NumberFormatException e) {
            person = null;
        }
        return person;
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
        Person person = null;
        int code = 0;
        try {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String sbirthdate = request.getParameter("birthdate");
            String role = request.getParameter("role");
            if (name == null) {
                code = -1;
            } else if (surname == null) {
                code = -2;
            } else if (sbirthdate == null) {
                code = -3;
            } else if (sbirthdate == null) {
                code = -4;
            }else {
                //Parse date
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = format.format(new Date());
                Date birthdate = format.parse(sbirthdate);
                person = new Person(name, surname, birthdate, role);
            }
        } catch (NumberFormatException e) {
            code = -5;
        }
        if (person != null) {
            result = new RequestResult(person, 1);
        } else {
            result = new RequestResult("Error in  parameters",
                     code);
        }
        return result;
    }

}
