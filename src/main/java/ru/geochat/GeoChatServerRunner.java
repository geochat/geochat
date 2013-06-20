package ru.geochat;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
public class GeoChatServerRunner {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		
		WebAppContext context = new WebAppContext();
		context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/main/webapp/");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        server.setHandler(context);
        server.start();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context.getServletContext());
        ServerController servController = (ServerController) ctx.getBean("serverControllerBean");
        servController.run();
	}
}
