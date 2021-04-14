package mike.servlets;

import mike.servlets.lib.RequestResult;
import cat.proven.model.DuplicatePersonException;
import cat.proven.model.DuplicateProjectException;
import cat.proven.model.Model;
import cat.proven.model.Person;
import cat.proven.model.Project;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mike.servlets.lib.PersonForm;
import mike.servlets.lib.ProjectForm;

/**
 *
 * @author mati
 */
public class ProjectPersonServletJson extends HttpServlet {

    private Model model;

    @Override
    public void init() throws ServletException {
        this.model = new Model();
    }

    /**
     * Handles the HTTP GET method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "listallpersons":
                    if (secured(request)) {
                        listAllPersons(request, response);
                    }
                    break;
                case "listallprojects":
                    if(secured(request)){
                        listAllProjects(request, response);
                    }                    
                    break;
                case "listpersonbyname":
                    if(secured(request)){
                    listPersonsByName(request, response);
                    }
                    break;
                case "listpersonbyid":
                    if(secured(request)){
                    listPersonsById(request, response);
                    }
                    break;
                case "listpersonsbysurname":
                    if(secured(request)){
                    listPersonsBySurname(request, response);
                    }
                    break;
                case "listpersonsbyfullname":
                    if(secured(request)){
                    listPersonsByFullname(request, response);
                    }
                    break;
                case "listpersonsbybirthdate":
                    if(secured(request)){
                    listPersonsByBirthdate(request, response);
                    }
                    break;
                case "listpersonsbyrole":
                    if(secured(request)){
                    listPersonsByRole(request, response);
                    }
                    break;
                case "listprojectsbyid":
                    if(secured(request)){
                    listProjectsById(request, response);
                    }
                    break;
                case "listprojectsbytitle":
                    if(secured(request)){
                    listProjectsByTitle(request, response);
                    }
                    break;
                default: //unknown option.
                    redirectError(request, response, "method_not_allowed");
                    break;
            }
        } else { // parameter action needed
            redirectError(request, response, "no_action");
        }
    }

    /**
     * Handles the HTTP POST method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "addproject": //add
                    if(secured(request)){
                    addNewProject(request, response);
                    }
                    break;
                case "modifyproject": //modify
                    if(secured(request)){
                    modifyProject(request, response);
                    }
                    break;
                case "removeproject": //delete
                    if(secured(request)){
                    removeProject(request, response);
                    }
                    break;
                case "addperson":
                    if(secured(request)){
                    addNewPerson(request, response);
                    }
                    break;
                case "modifyperson": //modify
                    if(secured(request)){
                    //modifyPrson(request, response);
                    }
                    break;
                case "removeperson": //delete
                    if(secured(request)){
                    removePerson(request, response);
                    }
                    break;
                default: //unknown option.
                    redirectError(request, response, "method_not_allowed");
                    break;
            }
        } else { // parameter action needed
            redirectError(request, response, "no_action");
        }
    }

    /**
     * serves error responses.
     *
     * @param request
     * @param response
     * @param error
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void redirectError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        String errorMsg;
        int errorCode;
        switch (error) {
            case "bad_parameter": // bad parameter action
                errorMsg = "Invalid action parameter";
                errorCode = HttpServletResponse.SC_BAD_REQUEST;
                break;
            case "method_not_allowed": //method not allowed.
                errorMsg = "Method not allowed";
                errorCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
                break;
            default: // need parameter action
                errorMsg = "Parameter action needed";
                errorCode = HttpServletResponse.SC_BAD_REQUEST;
                break;
        }
        response.sendError(errorCode, errorMsg);
    }

    /**
     * serves a list of all friends
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void listAllPersons(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Person> entityList = (List<Person>) model.findAllPersons();

        RequestResult result = new RequestResult(entityList, 1);
        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);

    }

    /**
     * serves a list of all friends
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void listAllProjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Project> entityList = (List<Project>) model.findAllProjects();

        RequestResult result = new RequestResult(entityList, 1);
        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     * serves a list of all friends
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void listPersonsByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        RequestResult result;

        if (name != null) {
            List<Person> resultList = model.findPersonsByName(name);
            if (!resultList.isEmpty()) {
                result = new RequestResult(resultList, 1);
            } else {
                result = new RequestResult(null, -1);
            }
        } else {
            result = new RequestResult(null, -1);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void listPersonsById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String sid = request.getParameter("id");
            RequestResult result;

            if (sid != null) {
                long id = Long.parseLong(sid);
                Person resultList = model.findPersonById(id);
                if (resultList != null) {
                    result = new RequestResult(resultList, 1);
                } else {
                    result = new RequestResult(null, -1);
                }
            } else {
                result = new RequestResult(null, -1);
            }

            request.setAttribute("result", result);
            RequestDispatcher rd = getServletContext()
                    .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
            rd.forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(ProjectPersonServletJson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * serves a list of persons by surname
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void listPersonsBySurname(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String surname = request.getParameter("surname");

        RequestResult result;

        if (surname != null) {
            List<Person> resultList = model.findPersonsBySurname(surname);
            if (!resultList.isEmpty()) {
                result = new RequestResult(resultList, 1);
            } else {
                result = new RequestResult(null, -1);
            }
        } else {
            result = new RequestResult(null, -1);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     * serves a list of persons by fullname
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void listPersonsByFullname(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullname = request.getParameter("fullname");

        RequestResult result;

        if (fullname != null) {
            Person entity = model.findPersonByFullname(fullname);
            if (entity != null) {
                result = new RequestResult(entity, 1);
            } else {
                result = new RequestResult(null, 0);
            }
        } else {
            result = new RequestResult(null, -1);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     * serves a list of persons by birthdate
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void listPersonsByBirthdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dateM = request.getParameter("date");
        RequestResult result;

        System.out.println(dateM);

        try {
            Date dDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateM);

            List<Person> entityList = model.findPersonByBirthdate(dDate);

            if (!entityList.isEmpty()) {
                result = new RequestResult(entityList, 1);
            } else {
                result = new RequestResult(entityList, 0);
            }

            request.setAttribute("result", result);
            RequestDispatcher rd = getServletContext()
                    .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
            rd.forward(request, response);

        } catch (ParseException ex) {
            Logger.getLogger(ProjectPersonServletJson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * serves a list of persons by role
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void listPersonsByRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = request.getParameter("role");
        RequestResult result;

        if (role != null) {
            List<Person> entityList = model.findPersonsByRole(role);

            if (!entityList.isEmpty()) {
                result = new RequestResult(entityList, 1);
            } else {
                result = new RequestResult(null, 0);
            }
        } else {
            result = new RequestResult(null, -1);
        }

//        RequestResult result = new RequestResult(entityList, 1);
        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void listProjectsById(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String id = request.getParameter("id");
        RequestResult result;

        if (id != null) {

            Project entityList = (Project) model.findProjectById(Long.parseLong(id));

            if (entityList != null) {
                result = new RequestResult(entityList, 1);
            } else {
                result = new RequestResult(null, 0);
            }

        } else {
            result = new RequestResult(null, -1);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void listProjectsByTitle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String title = request.getParameter("title");
        RequestResult result;

        if (title != null) {
            Project found = (Project) model.findProjectByTitle(title);
            if (found != null) {
                result = new RequestResult(found, 1);
            } else {
                result = new RequestResult(null, -2);
            }
        } else {
            result = new RequestResult(null, -1);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void addNewProject(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String title = request.getParameter("title");
        int resultCode = -1;

        try {
            Project project = ProjectForm.ofProjectForm(request);
            if (project != null) {
                resultCode = model.addProject(project);
            } else {
                resultCode = -1;
            }

            RequestResult result = new RequestResult(project, resultCode);
            request.setAttribute("result", result);
            RequestDispatcher rd = getServletContext()
                    .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
            rd.forward(request, response);

        } catch (ParseException | DuplicateProjectException ex) {
            resultCode = -1;
            Logger.getLogger(ProjectPersonServletJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            Logger.getLogger(ProjectPersonServletJson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void addNewPerson(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter("name");
        String surName = request.getParameter("surname");
        String role = request.getParameter("role");
        RequestResult result;

        int resultCode = 0;

        try {
            Person person = new Person(name, surName, role);
            if (person != null) {
                if(person != null){
                    resultCode = model.addPerson(person);
                    result = new RequestResult(person, resultCode);
                }else{
                    resultCode = -1;
                    result = new RequestResult(null, resultCode);
                }                
            } else {
                resultCode = -1;
                result = new RequestResult(null, resultCode);
            }

            request.setAttribute("result", result);
            RequestDispatcher rd = getServletContext()
                    .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
            rd.forward(request, response);

        } catch (DuplicatePersonException ex) {
            resultCode = -1;
            Logger.getLogger(ProjectPersonServletJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            Logger.getLogger(ProjectPersonServletJson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void removeProject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String title = request.getParameter("title");
        int resultCode = 0;
        RequestResult result;
        Project p = model.findProjectByTitle(title);
        if (p != null) {
            resultCode = model.removeProject(p);

            result = new RequestResult(p, resultCode);

        } else {
            resultCode = -1;
            result = new RequestResult(null, resultCode);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);

    }

    private void removePerson(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        int resultCode = 0;
        Person p = model.findPersonById(Long.parseLong(id));
        RequestResult result;

        if (p != null) {
            resultCode = model.removePerson(p);

            result = new RequestResult(p, resultCode);

        } else {
            resultCode = -1;
            result = new RequestResult(null, resultCode);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);
    }

    private void modifyProject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idP = request.getParameter("id");
        String title = request.getParameter("title");
        long id = Long.parseLong(idP);
        int resultCode = 0;
        RequestResult result;

        Project p = new Project(id, title);

        try {
            resultCode = model.modifyProject(p);
            result = new RequestResult(p, resultCode);
        } catch (DuplicateProjectException ex) {
            resultCode = -1;
            result = new RequestResult(null, resultCode);
            Logger.getLogger(ProjectPersonServletJson.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.setAttribute("result", result);
        RequestDispatcher rd = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/json-result.jsp");
        rd.forward(request, response);

    }

    /**
     * Validates role and session of authenticated User
     *
     * @param request
     * @return true if user has session authenticated , false otherwise
     */
    private boolean secured(HttpServletRequest request) {
        boolean logged = false;
        HttpSession session = request.getSession(false);
        if (session != null) {
            String user = (String) session.getAttribute("username");
            String role = (String) session.getAttribute("userrole");
            if (role != null && user != null) {
                if (role.equals("admin")) {
                    logged = true;
                }
            }
        } else {
            logged = false;
        }
        return logged;
    }

}
