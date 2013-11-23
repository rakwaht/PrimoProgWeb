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
import static com.deadormi.servlet.HomeServlet.AVATAR_RESOURCE_PATH;
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

    static Logger log = Logger.getLogger(InvitoServlet.class);

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
        Utente utente = null;
        Gruppo gruppo = null;
        try {
            inviti = InvitoController.getInvitiByUserId(request);
            utente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));
        } catch (SQLException ex) {
            log.error(ex);
        }
        String avatar_path = request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();

        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            
            out.println("<div class='ui large inverted vertical demo sidebar menu active fixed'>");

            out.println("<a href='#' class='item center'>");
            if (utente.getNome_avatar() == null) {
                out.println("<img class='circular ui image user-image' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");
            } else {
                out.println("<img class='circular ui image user-image' src='" + avatar_path + "' alt='Smiley face' style='margin:0 auto; width:100px; height:100px'>");
            }
            out.println("<h3>" + utente.getUsername().toUpperCase() + "</h3>");
            out.println("</a>");

            out.println("<a href='cambia_avatar' class='item'>");
            out.println("<i class='edit icon'></i>Cambia Avatar");
            out.println("</a>");

            out.println("<a href='inviti' class='item active'>");
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
            out.println("<div class='item'>" + utente.getUsername().toUpperCase() + "</div>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("<div id='main-container' class='main container'>");
            out.println("<h1 class='center' style='margin-bottom:20px;margin-top:20px;'><i class='browser icon'></i>Inviti</h1>");
            out.println("<div class='ui grid' style='margin-top:20px;'>");
            out.println("<div class='four wide column'></div>");
            out.println("<div class='eight wide column'>");

            if (inviti != null && inviti.size() > 0) {

                out.println("<form method='POST'>");
                out.println("<table class='ui table blue segment'>");
                out.println("<tr>");
                out.println("<th>Gruppo</th>");
                out.println("<th>Mittente</th>");
                out.println("<th>Accetti?</th>");
                out.println("</tr>");
                for (int i = 0; i < inviti.size(); i++) {
                    invito = inviti.get(i);
                    id_gruppo = invito.getId_gruppo();
                    try {
                        gruppo = GruppoController.getGruppoById(request, id_gruppo);
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                    id_invitante = invito.getId_invitante();
                    try {
                        invitante = UtenteController.getUserById(request, id_invitante);
                    } catch (SQLException ex) {
                        log.error(ex);
                    }

                    out.println("<tr>");
                    out.println("<td>" + gruppo.getNome() + "</td><td>" + invitante.getUsername() + "</td>");
                    out.println("<div class='ui radio checkbox'>");
                    out.println("<td><input type='radio' name='" + gruppo.getId_gruppo() + "'  value='true'>Si");
                    out.println("</div>");
                    out.println("<input type='radio' name='" + gruppo.getId_gruppo() + "'  value='false'>No</td>");
                    out.println("</tr>");
                }
                out.println("<tr><td colspan='3' class='center ' ><button class='ui blue button' type='submit' value='ISCRIVITI'><i class='edit sign icon'></i>ISCRIVITI</button></td></tr>");
                out.println("</table>");

                out.println("</form>");
                out.println("</div>");
            } else {
                out.println("<div class='ui red message'><i class='remove sign icon'></i>Non c'è nessun invito.</div>");

                out.println("</div>");
            }
            out.println("<div class='four wide column'></div>");
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
