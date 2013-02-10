package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Model;

public class Emp_LogoutAction extends Action {	

    public Emp_LogoutAction(Model model) { }

    public String getName() { return "logout-emp.do"; }

    public String perform(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.setAttribute("employee",null);
        session.invalidate();
        
        request.setAttribute("message","You are now logged out");
        return "success.jsp";
    }
}
