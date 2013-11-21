/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.Gruppo_UtenteController;
import com.deadormi.controller.InvitoController;
import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Post;
import com.deadormi.entity.Utente;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Timbu
 */
public class ModificaGruppoServlet extends HttpServlet {
    
    static Logger  log = Logger.getLogger(ModificaGruppoServlet.class);
    final static String AVATAR_RESOURCE_PATH = "/resource/avatar";
    
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
                if (non_iscritti.size() > 0) {
                    out.println("<p>INVITA UTENTI:</p>");
                    for (int i = 0; i < non_iscritti.size(); i++) {

                        if (!InvitoController.checkInvitoByUserId(request, non_iscritti.get(i).getId_utente(), id_gruppo)) {
                            out.println(non_iscritti.get(i).getUsername() + "<input type='checkbox' name='non_utenti_selezionati' value='" + non_iscritti.get(i).getId_utente() + "'/><br />");
                        } else {
                            out.println(non_iscritti.get(i).getUsername() + " gia invitato! <br />");

                        }
                    }
                }
                out.println("<input type='submit' name='generapdf' value='GENERA PDF'/>");
                out.println("<input type='submit' name='modifica' value='MODIFICA'/>");
                out.println("</form>");
                out.println("<a href='/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + gruppo.getId_gruppo() + "'>Indietro</a><br />");

                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        } catch (SQLException ex) {
            log.error(ex);
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
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
        }
        if (request.getParameter("modifica") != null) {
            Integer user_id = (Integer) request.getSession().getAttribute("user_id");
            String url = request.getQueryString();
            Integer gruppo_id = Integer.parseInt(url.substring(url.indexOf("=") + 1, url.length()));
            try {
                GruppoController.modificaGruppo(request, gruppo_id);
                Gruppo_UtenteController.eliminaUser(request, gruppo_id);
                InvitoController.processaRe_Inviti(request, gruppo_id);
                response.sendRedirect("/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + gruppo_id);
            } catch (SQLException ex) {
                log.error(ex);
            }

        } else if (request.getParameter("generapdf") != null) {
            try {
                Document document = new Document();
                String url = request.getQueryString();
                Integer gruppo_id = Integer.parseInt(url.substring(url.indexOf("=") + 1, url.length()));
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", " attachment; filename='gruppo_" + gruppo_id + "_report.pdf'");
                List<Utente> iscritti = null;
                List<Post> posts = null;
                try {
                    iscritti = UtenteController.getUserByGroupId(request, gruppo_id);
                } catch (SQLException ex) {
                   log.error(ex);
                }
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();
                Paragraph p = new Paragraph("REPORT", FontFactory.getFont(FontFactory.HELVETICA, 20));
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);
                PdfPTable table = new PdfPTable(2);
                for (int i = 0; i < iscritti.size(); i++) {
                    Utente utente = iscritti.get(i);
                    try {
                        posts = PostController.getPostByGruppoIdAndUserId(request, gruppo_id, utente.getId_utente());
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                    table.addCell("Username");
                    table.addCell(utente.getUsername());
                    table.addCell("Data Ultimo Post");
                    if (posts.isEmpty()) {
                        table.addCell("N/A");
                    } else {
                        //L'ultimo è sempre il piu recente siccome la query è ORDER BY data_creazione
                        table.addCell(posts.get(posts.size() - 1).getData_creazione());
                    }
                    table.addCell("Numero post");
                    table.addCell(posts.size() + "");
                    //inserisco avatar
                    String imageUrl;
                    if(utente.getNome_avatar() != null){
                        String baseUrl = request.getScheme() + "://" + request.getServerName()  + ":"+ request.getServerPort();
                        imageUrl = baseUrl + request.getContextPath()+AVATAR_RESOURCE_PATH+"/"+utente.getId_utente() + "_" + utente.getNome_avatar();;
                    }
                    else{
                        imageUrl = "http://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?f=y";
                    }
                    Image image = Image.getInstance(new URL(imageUrl));
                    image.scaleToFit(50, 50);
                    table.addCell("Avatar");
                    table.addCell(image);
                    

                }
                document.add(table);
                document.close();


            } catch (DocumentException ex) {
                log.error(ex);
            }
        } else {
            processRequest(request, response);
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
