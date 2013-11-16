/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.FileController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Utente;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class AvatarServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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
        HttpSession session = request.getSession();
        Utente utente = null;
        try {
            utente = UtenteController.getUserById(request, (Integer) session.getAttribute("user_id"));
        } catch (SQLException ex) {
            Logger.getLogger(AvatarServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AvatarServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AvatarServlet at " + request.getContextPath() + "</h1>");
            if (utente.getNome_avatar() == null) {
                out.println("<img src='http://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?f=y' alt='Smiley face' height='100' width='100' /><br />");
            } else {
                out.println("<img src='" + utente.getNome_avatar() + "' alt='Smiley face' height='100' width='100' /><br />");
            }
            out.println("<form method=''POST enctype='multipart/form-data'>");
            out.println("Immagine<INPUT TYPE='FILE' NAME='avatar'> <BR />");
            out.println("<input type='submit' name='Cambia Immagine' value='Cambia Immagine' />");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
             System.out.println("cazzo");
            FileController.cambiaAvatar(request);
        } catch (SQLException ex) {
            Logger.getLogger(AvatarServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
