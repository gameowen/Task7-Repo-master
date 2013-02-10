package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Model;


/*
 * Logs out by setting the "user" session attribute to null.
 * (Actions don't be much simpler than this.)
 */
public class Cus_LogoutAction extends Action {

	public Cus_LogoutAction(Model model) { }

	public String getName() { return "logout-cus.do"; }

	public String perform(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.setAttribute("customer",null);
        session.invalidate();

		request.setAttribute("message","You are now logged out");
        return "success.jsp";
    }
}
