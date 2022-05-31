package aims.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.common.Log;



public class AuthenticationFilter implements Filter {
	Log log = new Log(AuthenticationFilter.class);
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		  log.info("AuthenticationFiler : Entering init Method()");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("AuthenticationFiler : Entering doFilter Method()");
       
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        
        String user = (String) session.getAttribute("userid");
        String url = req.getRequestURL().toString();
        log.info("AuthenticationFiler : req from host :Cookie "+req.isRequestedSessionIdFromCookie()+"Cheeck Valid session"+req.isRequestedSessionIdValid());
        log.info("AuthenticationFiler : req from host : "+req.getRemoteHost()+" accessed url :"+url+"user"+user);
        boolean skip = false;
        String onErrorUrl="/PensionView/common/SessionTimeOut.jsp";
        if(user == null){
            if(url.endsWith("/PensionLogin")){
                skip = false;
             } else{
                skip = true;
            }
        }
       /* res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //HTTP 1.1
        res.setHeader("Pragma","no-cache"); //HTTP 1.0
        res.setDateHeader ("Expires", 0); //prevents caching at the proxy server
*/        if(skip){
            req.getRequestDispatcher(onErrorUrl).forward(req, res);
        }else{
        	
            chain.doFilter(request, response);
        }
        
    
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
