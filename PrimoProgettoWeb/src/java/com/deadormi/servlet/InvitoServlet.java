/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.InvitoController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Gruppo;
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
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class InvitoServlet extends HttpServlet {

    static Logger  log = Logger.getLogger(InvitoServlet.class);
    
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
        List<Invito> inviti = null;
        Invito invito;
        Integer id_gruppo;
        Integer id_invitante = null;
        Utente invitante = null;
        Gruppo gruppo = null;
        try {
            inviti = InvitoController.getInvitiByUserId(request);
        } catch (SQLException ex) {
            log.error(ex);
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<h1>INVITI: " + request.getContextPath() + "</h1>");
            out.println("<a href='home'>Indietro</a><br />");
            if (inviti != null && inviti.size()>0) {
                out.println("<h2>Inviti</h2>");
                out.println("<form method='POST'>");
                out.println("<table>");
                
                for (int i = 0; i < inviti.size(); i++) {
                    invito = inviti.get(i);
                    id_gruppo = invito.getId_gruppo();
                    try {
                        gruppo = GruppoController.getGruppoById(request,id_gruppo);
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                    id_invitante = invito.getId_invitante();
                    try {
                        invitante = UtenteController.getUserById(request,id_invitante);
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                    
                    out.println("<tr>");
                    out.println("<td>" + gruppo.getNome() + " da " + invitante.getUsername() + "</td>");
                    out.println("<td><input type='radio' name='" + gruppo.getId_gruppo() + "'  value='true'>Si");
                    out.println("<input type='radio' name='" + gruppo.getId_gruppo() + "'  value='false'>No</td>");
                    out.println("</tr>");
                }
                
                
                out.println("</table>");
                out.println("<input type='submit' value='ISCRIVITI'/>");
                out.println("</form>");
            } else {
                out.println("Nessun invito nel db!");
            }
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
            InvitoController.processaInviti(request);
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
