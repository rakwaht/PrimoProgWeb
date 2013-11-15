/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.Gruppo_UtenteController;
import com.deadormi.controller.InvitoController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Utente;
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
 * @author Timbu
 */
public class ModificaGruppoServlet extends HttpServlet {

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
        try {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Integer id_gruppo = (Integer) request.getAttribute("id_gruppo");
            Integer user_id = (Integer) request.getSession().getAttribute("user_id");
            Gruppo gruppo = GruppoController.getGruppoById(request, id_gruppo);
            List<Utente> iscritti = UtenteController.getUserByGroupId(request, id_gruppo);
            List<Utente> non_iscritti = UtenteController.getUserNotInGroupByGroupId(request, id_gruppo);
            System.out.println(non_iscritti.size());
            try {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet ModificaGruppoServlet </title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet ModificaGruppoServlet at " + gruppo.getNome() + "</h1>");
                out.println("<form method='POST' action='/PrimoProgettoWeb/secure/modifica_gruppo?id_gruppo=" + id_gruppo + "''>");
                out.println("Titolo:<input type='text' value='" + gruppo.getNome() + "' name='titolo'  /><br />");
                out.println("Descrizione:<textarea type='text' name='descrizione' >" + gruppo.getDescrizione() + "</textarea><br />");
                if (iscritti.size() > 1) {
                    out.println("<p>ELIMINA UTENTI:</p>");
                    for (int i = 0; i < iscritti.size(); i++) {
                        if (!iscritti.get(i).getId_utente().equals(user_id)) {
                            out.println(iscritti.get(i).getUsername() + "<input type='checkbox' name='utenti_selezionati' value='" + iscritti.get(i).getId_utente() + "'/><br />");
                        }
                    }
                }
                if (non_iscritti.size() > 1) {
                    out.println("<p>INVITA UTENTI:</p>");
                    for (int i = 0; i < non_iscritti.size(); i++) {
                            out.println(non_iscritti.get(i).getUsername() + "<input type='checkbox' name='non_utenti_selezionati' value='" + non_iscritti.get(i).getId_utente() + "'/><br />");
                    }
                }
                out.println("<input type='submit' name='modifica' value='MODIFICA'/>");
                out.println("</form>");
                out.println("<a href='/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + gruppo.getId_gruppo() + "'>Indietro</a><br />");

                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModificaGruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        if (request.getParameter("modifica") != null) {
            Integer user_id = (Integer) request.getSession().getAttribute("user_id");
            String url = request.getQueryString();
            Integer gruppo_id = Integer.parseInt(url.substring(url.indexOf("=") + 1, url.length()));
            try {
                GruppoController.modificaGruppo(request, gruppo_id);
                Gruppo_UtenteController.eliminaUser(request, gruppo_id);
                InvitoController.processaRe_Inviti(request,gruppo_id);
                response.sendRedirect("/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + gruppo_id);
            } catch (SQLException ex) {
                Logger.getLogger(ModificaGruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            processRequest(request,response);
        }
        /*try {
         GruppoController.modificaGruppo(request,gruppo_id);
         } catch (SQLException ex) {
         Logger.getLogger(ModificaGruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
         }*/

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
