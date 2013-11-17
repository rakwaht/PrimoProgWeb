/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.InvitoController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Invito;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import com.deadormi.util.CookiesManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Davide
 */
public class HomeServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Utente utente = null;
        List<Invito> inviti = null;
        String ultimo_login = CookiesManager.getOldDateCookie(request, response);
        try {
            inviti = InvitoController.getInvitiByUserId(request);
        } catch (SQLException ex) {
            Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            utente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));
        } catch (SQLException ex) {
            Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String avatar_path = request.getServletContext().getRealPath(AVATAR_RESOURCE_PATH+"/"+utente.getId_utente() + "_" + utente.getNome_avatar());
        try {
            MainLayout.printHeader(out);
            out.println("<h1>HOME at " + request.getContextPath() + "</h1>");
            if (ultimo_login != null) {
                out.println("<h2> Bentornato " + utente.getUsername() + "!</h2>");
                out.println("<h2>" + ultimo_login + "</h2>");
                
                if(utente.getNome_avatar() == null){
                    out.println("<img src='http://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?f=y' alt='Smiley face' height='100' width='100' /><br />");
                }
                else{
                    out.println("<img src='"+ avatar_path +"' alt='Smiley face' height='100' width='100' /><br />");
                }
            } else {
                out.println("<h2> Benvenuto " + utente.getUsername() + "!</h2>");
            }
            out.println("<a href='cambia_avatar'>Cambia Avatar</a><br />");
            out.println("<a href='inviti'>Inviti </a>"+inviti.size()+"<br />");
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
