/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.unikernel.npss.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ejb.EJB;
import net.unikernel.npss.model.PMP;
import net.unikernel.npss.model.TaskList;

/**
 *
 * @author thp
 */
@WebServlet(name="TaskControllerServlet", urlPatterns={"/task/list"})
public class TaskControllerServlet extends HttpServlet {
   
    @EJB
    private PMP mongodbAnt;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TaskControllerServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TaskControllerServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        StringBuffer rootUrl = request.getRequestURL();
        rootUrl.delete(rootUrl.indexOf(request.getRequestURI().toString()), rootUrl.length()).append(getServletContext().getContextPath());
        request.setAttribute("rootUrl", rootUrl.toString());

        String userPath = request.getServletPath();

        if (userPath.equals("/task/list")) {
            TaskList taskList = new TaskList();
            TreeMap<String, TreeSet<String>> structure = mongodbAnt.getStructure();

            for (Map.Entry<String, TreeSet<String>> entry : structure.entrySet()) {
                String task = entry.getKey();
                for (String factory : entry.getValue()) {
                    Double size = mongodbAnt.getSize(task, factory);
                    taskList.Add(task, factory, size);
                }
            }

            request.setAttribute("tasks", taskList);
        }

        String url = "/WEB-INF/view" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
