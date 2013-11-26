/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.deadormi.servlet;

import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Post;
import com.deadormi.entity.Utente;
import static com.deadormi.servlet.HomeServlet.AVATAR_RESOURCE_PATH;
import static com.deadormi.servlet.ModificaGruppoServlet.log;
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
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Timbu
 */
public class CreaPdfServlet extends HttpServlet {

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
        response.setContentType("application/pdf");
        
        try {
             Document document = new Document();
                String url = request.getQueryString();
                Integer gruppo_id = Integer.parseInt(url.substring(url.indexOf("=") + 1, url.length()));
                
                //response.setHeader("Content-Disposition", " attachment; filename='gruppo_" + gruppo_id + "_report.pdf'");
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
                                            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

                    if (utente.getNome_avatar() != null) {
                        imageUrl = baseUrl + request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();;
                    } else {
                        imageUrl = baseUrl + request.getContextPath() + "/res/images/user_avatar.png";
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
