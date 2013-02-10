package controller;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import model.Model;


public class Controller extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		Model model = new Model(getServletConfig());
		Action.add(new Cus_LoginAction(model));
		Action.add(new Cus_LogoutAction(model));
		Action.add(new Cus_ChangePwdAction(model));
		Action.add(new Cus_GetFundsAction(model));
		Action.add(new Cus_ResearchFundAction(model));
		Action.add(new Cus_RegisterAction(model));
		Action.add(new Cus_RequestCheckAction(model));
		Action.add(new Cus_BuyFundAction(model));
		Action.add(new Cus_SellFundAction(model));
		Action.add(new Cus_ViewTransactionHistoryAction(model));		
		Action.add(new Cus_ViewPortfolioAction(model));
		Action.add(new Emp_GetCustomersAction(model));
		Action.add(new Emp_LoginAction(model));
		Action.add(new Emp_LogoutAction(model));
		Action.add(new Emp_ChangePwdAction(model));
		Action.add(new Emp_ResetPwdAction(model));
		Action.add(new Emp_RegisterAction(model));
		Action.add(new Emp_DepositCheckAction(model));
		Action.add(new Emp_ViewTransactionHistoryAction(model));
		Action.add(new Emp_ViewCustomerAccountAction(model));
		Action.add(new Emp_CreateFundAction(model));
		Action.add(new Emp_TransitionDayAction(model));
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = performTheAction(request);
        sendToNextPage(nextPage,request,response);
    }
    
	private String performTheAction(HttpServletRequest request) {
		HttpSession session     = request.getSession(true);
    	String      servletPath = request.getServletPath();
        String      action = getActionName(servletPath);
        
        
        // Let the logged in user run his chosen action
		return Action.perform(action,request);
	}
    
    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	// System.out.println("nextPage="+nextPage);
    	
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	
    	if (nextPage.charAt(0) == '/') {
			String host  = request.getServerName();
			String port  = ":"+String.valueOf(request.getServerPort());
			if (port.equals(":80")) port = "";
			response.sendRedirect("http://"+host+port+nextPage);
			return;
    	}
    	
   		RequestDispatcher d = request.getRequestDispatcher("/"+nextPage);
   		d.forward(request,response);
    }
    
    private String getActionName(String path) {
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
}
