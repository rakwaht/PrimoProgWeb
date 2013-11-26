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
import java.util.logging.Level;
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
        String securePath = "/PrimoProgettoWeb/secure/";
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

            out.println("<a href='" + securePath + "home' class='item center'>");
            if (utente.getNome_avatar() == null) {
                out.println("<img class='circular ui image user-image' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");
            } else {
                out.println("<img class='circular ui image user-image' src='" + avatar_path + "' alt='Smiley face' style='margin:0 auto; width:100px; height:100px'>");
            }
            out.println("<h3>" + utente.getUsername().toUpperCase() + "</h3>");
            out.println("</a>");

            out.println("<a href='" + securePath + "tuoi_gruppi' class='item'>");
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
                out.println("<a href='" + securePath + "modifica_gruppo?id_gruppo=" + gruppo.getId_gruppo() + "' class='item'>");
                out.println("<i class='wrench icon'></i>Modifica Gruppo");
                out.println("</a>");
            }

            out.println("<a href='" + securePath + "logout' class='item'>");
            out.println("<i class='sign out icon'></i>");
            out.println("Logout");
            out.println("</a>");

            out.println("</div>");

            out.println("<div class=\"ui fixed transparent inverted main menu\">");
            out.println("<div class='container'>");
            out.println("<div id='buffo' class='item' style='cursor: pointer'><i class=\"icon list\"></i></div>");
            out.println("<a href='" + securePath + "tuoi_gruppi' class='item'><i class=\"left arrow icon\"></i>INDIETRO</a>");
            out.println("<div class='item'><i class='browser icon'></i>" + gruppo.getNome() + "</div>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div id='main-container' class='main container center'>");
            out.println("<div class='ui grid'>");
            out.println("<div class='row'>");

            out.println("<div class='eleven wide column'>");
            out.println("<a href='" + securePath + "nuovo_post?id_gruppo=" + gruppo.getId_gruppo() + "' class='ui blue button fluid large'><i class='outline chat icon'></i>Nuovo post</a>");

            try {
                posts = PostController.getPostByGruppoId(request, id_gruppo);
            } catch (SQLException ex) {
                log.error(ex);
            }
            if (posts != null && posts.size() != 0) {
                out.println("<div class='ui grid' style='margin-top: 20px'>");

                for (int i = 0; i < posts.size(); i++) {
                    post = posts.get(i);
                    Utente scrivente = null;
                    Gruppo gruppo_post = null;
                    try {
                        scrivente = UtenteController.getUserById(request, post.getId_scrivente());
                        gruppo_post = GruppoController.getGruppoById(request, post.getId_gruppo());
                        files = FileController.getFilesByPostId(request, post.getId_post());
                    } catch (SQLException ex) {
                        //  Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("<div class='row'>");

                    if (i % 2 == 0) {
                        out.println("<div class='three wide column center' >");
                        if (scrivente.getNome_avatar() != null) {
                            out.println("<img class='circular ui image scrivente-image center user-avatar' src='" + request.getContextPath() + "/resource/avatar/" + scrivente.getId_utente() + "_" + scrivente.getNome_avatar() + "' alt='Smiley face' />");
                        } else {
                            out.println("<img class='circular ui image scrivente-image center user-avatar' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' />");
                        }
                        out.println("<h2 style='margin-top:0px;'class='ui header center'>" + scrivente.getUsername() + "</h2>");
                        out.println("</div>"); //chiude sezione img avatar

                        out.println("<div class='thirteen wide column center' >");

                        out.println("<div  class='ui blue fluid top attached segment tip-l' >");
                        out.println("<p><i class='quote left icon'></i>" + post.getTesto() + "<i class='quote right icon'></i></p><br/>");
                        out.println("</div>");
                        out.println("<div  class='ui fluid bottom attached segment' >");
                        if (!files.isEmpty()) {
                            FileApp file = null;
                            String filePath = null;
                            for (int j = 0; j < files.size(); j++) {
                                file = files.get(j);
                                filePath = request.getContextPath() + "/resource/files/" + post.getId_gruppo() + "/" + file.getId_file() + "-" + file.getNome_file();
                                out.println("<i class='icon blue attachment'></i>");
                                out.println("<a href='" + filePath + "' target='_blank'>" + file.getId_file() + "-" + file.getNome_file() + "</a>");
                                if (j != files.size() - 1) {
                                    out.println("/");
                                }
                            }

                        }
                        out.println("<p style='text-align:right; color:#ababab; margin-top:0px;'>scritto il <i>" + post.getData_creazione() + "</i></p>");
                        out.println("</div>");

                        out.println("</div>"); //chiude sezione post
                    } else {
                        out.println("<div class='thirteen wide column center' >");

                        out.println("<div  class='ui blue fluid top attached segment tip-r' >");
                        out.println("<p><i class='quote left icon'></i>" + post.getTesto() + "<i class='quote right icon'></i></p><br/>");
                        out.println("</div>");
                        out.println("<div  class='ui fluid bottom attached segment' >");
                        if (!files.isEmpty()) {
                            FileApp file = null;
                            String filePath = null;
                            for (int j = 0; j < files.size(); j++) {
                                file = files.get(j);
                                filePath = request.getContextPath() + "/resource/files/" + post.getId_gruppo() + "/" + file.getId_file() + "-" + file.getNome_file();
                                out.println("<i class='icon blue attachment'></i>");
                                out.println("<a href='" + filePath + "' target='_blank'>" + file.getId_file() + "-" + file.getNome_file() + "</a>");
                                if (j != files.size() - 1) {
                                    out.println("/");
                                }
                            }

                        }
                        out.println("<p style='text-align:right; color:#ababab; margin-top:0px;'>scritto il <i>" + post.getData_creazione() + "</i></p>");
                        out.println("</div>");

                        out.println("</div>"); //chiude sezione post

                        out.println("<div class='three wide column center' >");
                        if (scrivente.getNome_avatar() != null) {
                            out.println("<img class='circular ui image scrivente-image center user-avatar' src='" + request.getContextPath() + "/resource/avatar/" + scrivente.getId_utente() + "_" + scrivente.getNome_avatar() + "' alt='Smiley face' />");
                        } else {
                            out.println("<img class='circular ui image scrivente-image center user-avatar' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' />");
                        }
                        out.println("<h2 style='margin-top:0px;'class='ui header center'>" + scrivente.getUsername() + "</h2>");
                        out.println("</div>"); //chiude sezione img avatar
                    }
                    out.println("</div>"); //chiude row
                }

                out.println("</div>"); //chiude nested grid                
            } else {
                out.println("<div class='ui red message'><i class='remove sign icon'></i>Non c'è nessun post.</div>");

            }

            /* -------------------- INFO GRUPPO -------------------- */
            out.println("</div>");
            out.println("<div class='five wide column'>");

            out.println("<div class=\"ui top attached message blue\"><div class=\"ui right blue corner label\">\n" +
"    <i class=\"folder open icon\"></i>\n" +
"  </div><h2 style='margin-top: 0px'>" + gruppo.getNome() + "</h2></div>");
            out.println("<div class=\"ui attached segment\"><p>" + gruppo.getDescrizione() + "</p></div>");
            out.println("<div class=\"ui bottom attached segment\"><p style='text-align: right'> di <i>" + proprietario.getUsername() + "</i></p></div>");

            /* -------------------- LISTA MEMBRI -------------------- */
            List<Utente> utenti = null;
            Utente membro = null;
            try {
                utenti = UtenteController.getUserByGroupId(request, id_gruppo);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(GruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (utenti.size() == 1) {
                out.println("<div class='ui red message'><i class='remove sign icon'></i>Non c'è nessun utente oltre a te.</div>");
            } else {
                out.println("<table class='ui table segment'>");
                out.println("<thead><tr><th><i class='icon users'></i>Membri</th></tr></thead>");
                for (int i = 0; i < utenti.size(); i++) {
                    membro = utenti.get(i);
                    if (!utente.getId_utente().equals(membro.getId_utente())) {
                        out.println("<tr><td>" + membro.getUsername() + "</tr></td>");
                    } else {
                        out.println("<tr><td><b>" + membro.getUsername() + "</b></tr></td>");
                    }
                }
                out.println("<tfoot><tr><th>" + utenti.size() + " utenti</th></tr></tfoot>");
                out.println("</table>");
            }

            /* -------------------- LISTA FILES -------------------- */
            List<FileApp> group_files = null;
            FileApp group_file = null;
            String fileName = null;
            try {
                group_files = FileController.getFilesByGroupId(request, id_gruppo);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(GruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (group_files.size() == 0) {
                out.println("<div class='ui red message'><i class='remove sign icon'></i>Non c'è alcun file in questo gruppo.</div>");
            } else {
                out.println("<table class='ui table segment'>");
                out.println("<thead><tr><th><i class='icon list'></i>Files</th></tr></thead>");

                for (int i = 0; i < group_files.size(); i++) {
                    group_file = group_files.get(i);
                    fileName = group_file.getId_file() + "-" + group_file.getNome_file();
                    out.println("<tr><td><a href='/PrimoProgettoWeb/resource/files/" + gruppo.getId_gruppo() + "/" + fileName + "'>" + fileName + "</tr></td>");
                }
                out.println("<tfoot><tr><th>" + group_files.size() + " files</th></tr></tfoot>");
                out.println("</table>");
            }

            out.println("</div>");

            out.println("</div>");
            out.println("</div>");
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
