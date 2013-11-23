/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.Gruppo_UtenteController;
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
public class TuoiGruppiServlet extends HttpServlet {

    static Logger log = Logger.getLogger(TuoiGruppiServlet.class);

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
        List<Gruppo> gruppi = null;
        Utente utente = null;
        List<Invito> inviti = null;
        try {
            inviti = InvitoController.getInvitiByUserId(request);
            utente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));
            gruppi = Gruppo_UtenteController.getGruppiByUserId(request);
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

            out.println("<a href='cambia_avatar' class='item active'>");
            out.println("<i class='edit icon'></i>Cambia Avatar");
            out.println("</a>");

            out.println("<a href='inviti' class='item'>");
            out.println("<div class='ui large blue label'>" + inviti.size() + "</div>");
            out.println("Inviti");
            out.println("</a>");

            out.println("<a href='tuoi_gruppi' class='item active'>");
            out.println("<i class=' users icon'></i>");
            out.println("Gruppi");
            out.println("</a>");

            out.println("<a href='crea' class='item'>");
            out.println("<i class=' add sign icon'></i>");
            out.println("Crea Gruppo");
            out.println("</a>");

            out.println("<a href='logout' class='item active'>");
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
            /*
            out.println("<div class='ui inverted large vertical left menu fixed home-menu'>");

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

            out.println("<a href='crea' class='item active'>");
            out.println("<i class=' add sign icon'></i>");
            out.println("Crea Gruppo");

            out.println("</a>");

            out.println("<a href='home' class='item'>");
            out.println("<i class=' reply mail icon'></i>");
            out.println("Indietro");
            out.println("</a>");

            out.println("<a href='logout' class='item active'>");
            out.println("<i class='sign out icon'></i>");
            out.println("Logout");
            out.println("</a>");

            out.println("</div>");
            */

            out.println("<div id='main-container' class='main container center'>");

            out.println("<h1><i class='users icon'></i>Miei Gruppi</h1>");
            out.println("<div class='ui grid'>");
            out.println("<div class='four wide column'></div>");
            out.println("<div class='eight wide column'>");
            if (gruppi != null && gruppi.size() > 0) {
                out.println("<table class='ui blue table segment'>");
                out.println("<th>Gruppo</th>");
                    out.println("<th>Autore</th>");
                    out.println("<th>Descrizione</th>");
                    out.println("<th>Vai</th>");
                for (int i = 0; i < gruppi.size(); i++) {
                    Gruppo gruppo = gruppi.get(i);
                    Utente proprietario = null;
                    try {
                        proprietario = UtenteController.getUserById(request, gruppo.getId_proprietario());
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                    
                    out.println("<tr>");
                    out.println("<td>" + gruppo.getNome() + "</td><td>" + proprietario.getUsername() + "</td>");
                    out.println("<td>" + gruppo.getDescrizione() + "</td>");
                    out.println("<td><a href='gruppo/show?id_gruppo=" + gruppo.getId_gruppo() + "'><i id='tasto' class='forward mail icon' style='font-size:30px'></i></a></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<div class='ui red message'><i class='remove sign icon'></i>Non sei iscritto a nessun gruppo.</div>");
            }
            out.println("</div>");
            out.println("<div class='four wide column'></div>");
            out.println("</div>");
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
