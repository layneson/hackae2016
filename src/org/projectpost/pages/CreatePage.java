package org.projectpost.pages;

import freemarker.template.TemplateException;
import org.projectpost.data.Database;
import org.projectpost.data.ProjectData;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jared on 11/5/2016.
 */

public class CreatePage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            renderTemplate("create.html", new HashMap<>(), resp.getWriter());
        } catch (TemplateException e) {
            sendError(resp, "Failed to create template: " + e.getMessage());
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        if (!session.isLoggedIn()) {
            resp.sendRedirect("/");
            return;
        }

        try {


            String name = req.getParameter("name");
            int location  = Integer.parseInt( req.getParameter("location") );
            String time = req.getParameter("time");
            String description = req.getParameter("description");
            int maxFunds = Integer.parseInt( req.getParameter("maxFunds") );

            if (name.equals("") || location == 0 || time.equals("") || maxFunds <= 0) {
                try {
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("errorMessage", "Some fields were left blank!");
                    renderTemplate("register.html", errorMap, resp.getWriter());
                } catch (TemplateException e) {
                    e.printStackTrace();;
                    sendError(resp, "Failed to create template: " + e.getMessage());
                }
            }

            ProjectData pd = Database.newProjectData();
            pd.name = name;
            pd.owner = session.getUID();
            pd.location = location;
            pd.time = time;
            pd.description = description;
            pd.currentFunds = 0;
            pd.maxFunds = maxFunds;

            Database.saveProjectData(pd);
            resp.sendRedirect("/projects");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, "Failed to create template: " + e.getMessage());
        }
    }
}
