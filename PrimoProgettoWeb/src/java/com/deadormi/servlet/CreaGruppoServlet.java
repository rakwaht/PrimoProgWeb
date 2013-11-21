/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.UtenteController;
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
public class CreaGruppoServlet extends HttpServlet {
    
    static Logger  log = Logger.getLogger(CreaGruppoServlet.class);
    
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
        List<Utente> list = null;
        Utente utente;
        HttpSession session = request.getSession();
        try {
            list = UtenteController.getUtenti(request);
        } catch (SQLException ex) {
          log.error(ex);
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<h1>CREA GRUPPO: " + request.getContextPath() + "</h1>");
            out.println("<form method='POST'>");
            out.println("Titolo:<input type='text' name='titolo'/><br />");
            out.println("Descrizione:<textarea name='descrizione'></textarea><br />");
            if (list != null && list.size() != 1) {
                out.println("<table>");
                out.println("<h2>Utenti</h2>");
                for (int i = 0; i < list.size(); i++) {

                    utente = list.get(i);
                    if (!utente.getId_utente().equals(session.getAttribute("user_id"))) {
                        out.println("<tr>");

                        out.println("<td>" + utente.getUsername() + "</td>");
                        out.println("<td><input type='checkbox' name='utenti_selezionati' value='" + utente.getId_utente() + "'/></td>");
                        out.println("</tr>");
                    }
                }
                out.println("</table>");
            } else {
                out.println("Nessuna persona nel db!");
            }
            out.println("<input type='submit'/>");
            out.println("</form>");
            out.println("<a href='home'>Indietro</a><br />");
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
            Integer id_gruppo = GruppoController.creaGruppo(request);
            if (id_gruppo > 0) {
                response.sendRedirect("/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + id_gruppo);

            } else {
                processRequest(request, response);
            }
        } catch (SQLException ex) {
           log.error(ex);
        }

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
