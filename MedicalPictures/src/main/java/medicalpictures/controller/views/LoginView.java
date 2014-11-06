package medicalpictures.controller.views;

import java.io.BufferedReader;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.security.UserSessionManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
public class LoginView extends HttpServlet {

	@EJB
	private UserSessionManager manager;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			System.out.println("Cookies z get" + c.getValue());
		}
		request.getRequestDispatcher("/WEB-INF/LoginView.html").forward(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("!");
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch (Exception e) { /*report an error*/ }

		JSONObject jsonObject = new JSONObject(jb.toString());
		System.out.println(jb);
		Cookie cookie = new Cookie("ciastko", "mojeNoweCiastko");
		response.addCookie(cookie);
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			System.out.println("Cookies" + c.getValue());
		}
		manager.registerUser("username", "pass");
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
