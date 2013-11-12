/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.jni.Time;

/**
 *
 * @author Davide
 */
public class HomeServlet extends HttpServlet {

    private DbManager manager;

    @Override
    public void init() {
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        HttpSession session = request.getSession();
        Utente utente = null;
        try {
            utente = manager.getUserById((Integer)session.getAttribute("user_id"));
        } catch (SQLException ex) {
            Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("ultimo_login")) {
                cookie = cookies[i];
            }
        }
        try {
            MainLayout.printHeader(out);
            out.println("<h1>HOME at " + request.getContextPath() + "</h1>");
            out.println("<h2> Bentornato " + utente.getUsername() + "!</h2>");
            out.println("<h2>" + cookie.getValue() + "</h2>");
            out.println("<a href='inviti'>Inviti</a><br />");
            out.println("<a href='tuoi_gruppi'>Gruppi</a><br />");
            out.println("<a href='crea'>Crea Gruppo</a><br />");
            MainLayout.printFooter(out);
        } finally {
            out.close();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
        processRequest(request, response);
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
