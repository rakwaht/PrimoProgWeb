/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Post;
import com.deadormi.entity.Utente;
import static com.deadormi.servlet.HomeServlet.AVATAR_RESOURCE_PATH;
import static com.deadormi.servlet.ModificaGruppoServlet.log;
import com.deadormi.util.CurrentDate;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
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
        String url = request.getQueryString();
        Integer gruppo_id = Integer.parseInt(url.substring(url.indexOf("=") + 1, url.length()));

        List<Utente> iscritti = null;
        List<Post> posts = null;
        Gruppo gruppo = null;
        Utente utente = null;
        String imageUrl = null;
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        try {
            iscritti = UtenteController.getUserByGroupId(request, gruppo_id);
            gruppo = GruppoController.getGruppoById(request, gruppo_id);
        } catch (SQLException ex) {
            log.error(ex);
        }

        // step 1: creation of a document-object
        Document document = new Document();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            String title = "Report\nGruppo " + gruppo.getNome() + "\ndel " + CurrentDate.getCurrentDate();
            Paragraph pTitle = new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLUE));
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.setSpacingAfter(60);
            document.add(pTitle);

            PdfPCell labelNome = new PdfPCell(new Paragraph("Nome utente"));
            PdfPCell labelNumPost = new PdfPCell(new Paragraph("Num. post"));
            PdfPCell labelData = new PdfPCell(new Paragraph("Data ultimo post"));
            labelNome.setBorder(Rectangle.NO_BORDER);
            labelNumPost.setBorder(Rectangle.NO_BORDER);
            labelData.setBorder(Rectangle.NO_BORDER);

            for (int i = 0; i < iscritti.size(); i++) {
                utente = iscritti.get(i);
                try {
                    posts = PostController.getPostByGruppoIdAndUserId(request, gruppo_id, utente.getId_utente());
                } catch (SQLException ex) {
                    Logger.getLogger(CreaPdfServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (utente.getNome_avatar() != null) {
                    imageUrl = baseUrl + request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();;
                } else {
                    imageUrl = baseUrl + request.getContextPath() + "/res/images/user_avatar.png";
                }
                Image image = Image.getInstance(new URL(imageUrl));
                image.scaleToFit(50, 50);

                PdfPTable table = new PdfPTable(3);

                PdfPCell cellAvatar = new PdfPCell(image);
                cellAvatar.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellAvatar.setBorder(Rectangle.NO_BORDER);
                cellAvatar.setRowspan(3);

                PdfPCell cellNome = new PdfPCell(new Paragraph(utente.getUsername() + ""));
                cellNome.setBorder(Rectangle.NO_BORDER);

                PdfPCell cellNumPost = new PdfPCell(new Paragraph(posts.size() + ""));
                cellNumPost.setBorder(Rectangle.NO_BORDER);

                //L'ultimo è sempre il piu recente siccome la query è ORDER BY data_creazione
                PdfPCell cellData = new PdfPCell(new Paragraph(posts.get(posts.size() - 1).getData_creazione()));
                cellData.setBorder(Rectangle.NO_BORDER);

                PdfPCell cellVoidBottom = new PdfPCell(new Paragraph(" "));
                cellVoidBottom.setBorder(Rectangle.BOTTOM);
                cellVoidBottom.setPaddingBottom(10);
                cellVoidBottom.setColspan(3);

                PdfPCell cellVoidTop = new PdfPCell(new Paragraph(" "));
                cellVoidTop.setBorder(Rectangle.NO_BORDER);
                cellVoidTop.setPaddingTop(10);
                cellVoidTop.setColspan(3);

                table.addCell(cellVoidTop);
                table.addCell(cellAvatar);
                table.addCell(labelNome);
                table.addCell(cellNome);
                table.addCell(labelNumPost);
                table.addCell(cellNumPost);
                table.addCell(labelData);
                table.addCell(cellData);
                table.addCell(cellVoidBottom);

                document.add(table);

            }

            document.close();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=ahahah.pdf");
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (DocumentException ex) {
            Logger.getLogger(CreaPdfServlet.class.getName()).log(Level.SEVERE, null, ex);
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
