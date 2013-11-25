/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.FileController;
import com.deadormi.controller.GruppoController;
import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.FileApp;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Post;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import static com.deadormi.servlet.HomeServlet.AVATAR_RESOURCE_PATH;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
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
public class GruppoServlet extends HttpServlet {

    static Logger log = Logger.getLogger(GruppoServlet.class);

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
        Integer id_gruppo = Integer.parseInt(request.getParameter("id_gruppo"));
        Gruppo gruppo = null;
        Utente proprietario = null;
        Utente utente = null;
        try {
            gruppo = GruppoController.getGruppoById(request, id_gruppo);
            proprietario = UtenteController.getUserById(request, gruppo.getId_proprietario());
            utente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));

        } catch (SQLException ex) {
            log.error(ex);
        }

        HttpSession session = request.getSession();
        List<Post> posts = null;
        Post post = null;
        List<FileApp> files = null;
        String avatar_path = request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();

        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);

            out.println("<div class='ui large inverted vertical demo sidebar menu active fixed'>");

            out.println("<a href='/PrimoProgettoWeb/secure/home' class='item center'>");
            if (utente.getNome_avatar() == null) {
                out.println("<img class='circular ui image user-image' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");
            } else {
                out.println("<img class='circular ui image user-image' src='" + avatar_path + "' alt='Smiley face' style='margin:0 auto; width:100px; height:100px'>");
            }
            out.println("<h3>" + utente.getUsername().toUpperCase() + "</h3>");
            out.println("</a>");

            out.println("<a href='/PrimoProgettoWeb/secure/tuoi_gruppi' class='item'>");
            out.println("<i class='users icon'></i> Gruppi");
            out.println("</a>");

            out.println("<a href='#' class='item active'>");
            if (utente.getId_utente().equals(gruppo.getId_proprietario())) {
                out.println("<i class='book icon'></i><div class='ui blue label'>admin</div>" + gruppo.getNome());
            } else {
                out.println("<i class='book icon'></i>" + gruppo.getNome());
            }
            out.println("</a>");

            if (utente.getId_utente().equals(gruppo.getId_proprietario())) {
                out.println("<a href='/PrimoProgettoWeb/secure/modifica_gruppo?id_gruppo=" + gruppo.getId_gruppo() + "' class='item'>");
                out.println("<i class='wrench icon'></i>Modifica " + gruppo.getNome());
                out.println("</a>");
            }

            out.println("<a href='logout' class='item'>");
            out.println("<i class='sign out icon'></i>");
            out.println("Logout");
            out.println("</a>");

            out.println("</div>");

            out.println("<div class=\"ui fixed transparent inverted main menu\">");
            out.println("<div class='container'>");
            out.println("<div id='buffo' class='item' style='cursor: pointer'><i class=\"icon list\"></i></div>");
            out.println("<a href='/PrimoProgettoWeb/secure/tuoi_gruppi' class='item'><i class=\"left arrow icon\"></i>INDIETRO</a>");
            out.println("<div class='item'><i class='browser icon'></i>" + gruppo.getNome() + "</div>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div id='main-container' class='main container center'>");
            out.println("<div class='ui grid'>");
            out.println("<div class='row'>");

            out.println("<div class='two wide column'></div>");
            out.println("<div class='twelve wide column'>");
            //out.println("<h1>GRUPPO: " + gruppo.getNome() + " di " + proprietario.getUsername() + "</h1>");
            out.println("<p>DESCRIZIONE: " + gruppo.getDescrizione() + "</p>");
            out.println("<a href='/PrimoProgettoWeb/secure/tuoi_gruppi'>Indietro</a><br />");
            out.println("</div>");
            out.println("<div class='two wide column'></div>");

            out.println("</div>");
            out.println("</div>");
            out.println("</div>");

            try {
                posts = PostController.getPostByGruppoId(request, id_gruppo);
            } catch (SQLException ex) {
                log.error(ex);
            }
            if (posts != null && posts.size() != 0) {
                out.println("<table>");
                for (int i = posts.size() - 1; i >= 0; i--) {

                    post = posts.get(i);
                    try {
                        utente = UtenteController.getUserById(request, post.getId_scrivente());
                        files = FileController.getFilesByPostId(request, post.getId_post());
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                    out.println("<tr>");
                    out.println("<td>" + post.getTesto() + "</td>");
                    out.println("<td> scritto il " + post.getData_creazione() + "</td>");
                    out.println("<td> da: " + utente.getUsername() + "</td>");
                    if (files.size() > 0) {
                        for (int j = 0; j < files.size(); j++) {
                            FileApp file = files.get(j);

                            out.println("<td> FILE:<a href='" + request.getContextPath() + "/resource/files/" + id_gruppo + "/" + file.getId_file() + "-" + file.getNome_file() + "' target='_blank'>" + file.getNome_file() + "</a></td>");
                        }
                    }
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<p>Non vi sono posts nel db!</p>");
            }
            out.println("<form method='POST'>");
            out.println("<input type='submit' name='nuovo_post' value='NUOVO POST'/>");

            out.println("</form>");
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
        Integer id = Integer.parseInt(request.getParameter("id_gruppo"));
        request.setAttribute("id_gruppo", id);
        RequestDispatcher rd;
        if (request.getParameter("nuovo_post") != null) {
            rd = request.getRequestDispatcher("/secure/nuovo_post");
            rd.forward(request, response);
        } else if (request.getParameter("modifica_gruppo") != null) {
            rd = request.getRequestDispatcher("/secure/modifica_gruppo?id_gruppo=" + id);
            rd.forward(request, response);
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
