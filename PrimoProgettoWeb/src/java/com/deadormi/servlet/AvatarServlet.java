/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.FileController;
import com.deadormi.controller.InvitoController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Invito;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class AvatarServlet extends HttpServlet {
    
    private Integer error = 0;
    static Logger log = Logger.getLogger(AvatarServlet.class);
    final static String AVATAR_RESOURCE_PATH = "/resource/avatar";

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
        HttpSession session = request.getSession();
        String user_id = session.getAttribute("user_id").toString();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Utente utente = null;
        List<Invito> inviti = null;
        try {
            utente = UtenteController.getUserById(request, (Integer) session.getAttribute("user_id"));
            inviti = InvitoController.getInvitiByUserId(request);

        } catch (SQLException ex) {
            log.error(ex);
        }
        String avatar_path = null;
        if (utente.getNome_avatar() != null) {
            avatar_path = request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<div class='ui large inverted vertical demo sidebar menu active fixed'>");

            out.println("<a href='home' class='item center'>");
            if (utente.getNome_avatar() == null) {
                out.println("<img class='circular ui image user-image' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");
            } else {
                out.println("<img class='circular ui image user-image' src='" + avatar_path + "' alt='Smiley face' style='margin:0 auto; width:100px; height:100px'>");
            }
            out.println("<h3>" + utente.getUsername().toUpperCase() + "</h3>");
            out.println("</a>");

            out.println("<a href='cambia_avatar' class='item active'>");
            out.println("<i class='edit icon'></i>Cambia Avatar");
            out.println("</a>");

            out.println("<a href='inviti' class='item'>");
            out.println("<div class='ui large blue label'>" + inviti.size() + "</div>");
            out.println("Inviti");
            out.println("</a>");

            out.println("<a href='tuoi_gruppi' class='item'>");
            out.println("<i class=' users icon'></i>");
            out.println("Gruppi");
            out.println("</a>");

            out.println("<a href='crea' class='item'>");
            out.println("<i class=' add sign icon'></i>");
            out.println("Crea Gruppo");
            out.println("</a>");

            out.println("<a href='logout' class='item'>");
            out.println("<i class='sign out icon'></i>");
            out.println("Logout");
            out.println("</a>");

            out.println("</div>");

            out.println("<div class=\"ui fixed transparent inverted main menu\">");
            out.println("<div class='container'>");
            out.println("<div id='buffo' class='item' style='cursor: pointer'><i class=\"icon list\"></i></div>");
            out.println("<a href='home' class='item'><i class=\"left arrow icon\"></i>INDIETRO</a>");
            out.println("<div class='item'><i class='edit icon'></i>Cambia avatar</div>");

            out.println("</div>");
            out.println("</div>");

            out.println("<div id='main-container' class='main container'>");
            if (utente.getNome_avatar() != null) {
                out.println("<img class=' center circular ui image user-avatar' src='" + avatar_path + "' alt='Smiley face' /><br />");
            } else {
                out.println("<img class=' center circular ui image user-avatar' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");

            }
            out.println("<div class='ui grid' style='margin-top:20px;'>");
            out.println("<div class='five wide column'></div>");
            out.println("<div class='six wide column'>");
            out.println("<form class='ui form fluid message center' method='POST' enctype='multipart/form-data' >");
            out.println("<input type='file' name='avatar'/>");
            out.println("<div class='ui divider'></div>");
            out.println("<button class='ui button blue' style='margin-top:5px' type='submit' name='Cambia Avatar' value='Cambia Immagine' ><i class='icon photo'></i>Cambia Immagine</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div class='five wide column'></div>");

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
        try {
            error = FileController.cambiaAvatar(request);
        } catch (SQLException ex) {
            log.error(ex);
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
