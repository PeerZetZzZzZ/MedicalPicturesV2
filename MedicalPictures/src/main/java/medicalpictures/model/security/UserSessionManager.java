/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.security;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;

/**
 *
 * @author Przemysław Thomann
 */
@Stateful
public class UserSessionManager {

	private HashMap<String, Subject> loggedUsers = new HashMap<>();
	private Subject currentUser;
	private int i = 0;

	@PostConstruct
	public void initSecurityManager() {
		Factory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = (SecurityManager) factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		System.out.println("Tworze sie 2 razy kurw");
		System.out.println("I wynosi " + i);
		i++;
	}

	public void registerUser(String username, String password) {
		currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			Session session = currentUser.getSession();
			session.setAttribute("username", username);
			UsernamePasswordToken token = new UsernamePasswordToken("root", "secret");
			token.setRememberMe(true);
			currentUser.login(token);
			loggedUsers.put("username", currentUser);
			System.out.println("Dodalem usera");
		} else {
			System.out.println("On juz istnieje :D!");
		}
	}

    // Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")
}
