package com.gestionboutique.service;

import com.gestionboutique.model.Achat;
import com.gestionboutique.model.LigneAchat;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Service de génération du ticket de caisse en PDF — Format A4 élégant.
 */
public class TicketService {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final DeviceRgb BLEU_MARINE = new DeviceRgb(30, 58, 95);
    private static final DeviceRgb GRIS_CLAIR = new DeviceRgb(245, 247, 250);
    private static final DeviceRgb GRIS_MOYEN = new DeviceRgb(200, 200, 200);
    private static final DeviceRgb GRIS_TEXTE = new DeviceRgb(100, 100, 100);

    public byte[] genererTicketPdf(Achat achat) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            doc.setMargins(50, 60, 50, 60);

            // ============================================
            // EN-TÊTE
            // ============================================
            Paragraph titre = new Paragraph("GESTION BOUTIQUE")
                    .setFontSize(30)
                    .setBold()
                    .setFontColor(BLEU_MARINE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(5);

            Paragraph sousTitre = new Paragraph("Boutique alimentaire — Ouagadougou")
                    .setFontSize(12)
                    .setFontColor(GRIS_TEXTE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(0)
                    .setMarginBottom(5);

            Paragraph titreTicket = new Paragraph("TICKET DE CAISSE")
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(BLEU_MARINE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10)
                    .setMarginBottom(25);

            doc.add(titre);
            doc.add(sousTitre);
            doc.add(titreTicket);

            // ============================================
            // INFOS ACHAT (encadré)
            // ============================================
            Table infos = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(25);

            // Ligne 1
            infos.addCell(celluleInfo("N° TICKET", "#" + achat.getId()));
            infos.addCell(celluleInfo("DATE", achat.getDateAchat() != null
                    ? achat.getDateAchat().format(DATE_FORMAT) : "-"));
            infos.addCell(celluleInfo("CLIENT", achat.getClientNom() != null
                    ? achat.getClientNom() : "-"));
            infos.addCell(celluleInfo("VENDEUR", achat.getUtilisateurNom() != null
                    ? achat.getUtilisateurNom() : "-"));

            doc.add(infos);

            // ============================================
            // TABLEAU DES PRODUITS
            // ============================================
            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1.5f, 2, 2.5f}))
                    .useAllAvailableWidth()
                    .setMarginTop(10);

            // En-têtes
            table.addHeaderCell(entete("PRODUIT"));
            table.addHeaderCell(entete("QTÉ"));
            table.addHeaderCell(entete("PRIX UNITAIRE"));
            table.addHeaderCell(entete("SOUS-TOTAL"));

            // Lignes
            for (LigneAchat l : achat.getLignes()) {
                table.addCell(celluleProduit(l.getProduitNom() != null ? l.getProduitNom() : "-"));
                table.addCell(celluleCentre(String.valueOf(l.getQuantite())));
                table.addCell(celluleDroite(formater(l.getPrixUnitaire()) + " FCFA"));
                table.addCell(celluleDroiteGras(formater(l.getSousTotal()) + " FCFA"));
            }

            doc.add(table);

            // ============================================
            // TOTAL
            // ============================================
            Table totalTable = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                    .useAllAvailableWidth()
                    .setMarginTop(20);

            totalTable.addCell(new Cell()
                    .add(new Paragraph("TOTAL À PAYER")
                            .setFontSize(16)
                            .setBold()
                            .setFontColor(BLEU_MARINE)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPaddingRight(20));

            totalTable.addCell(new Cell()
                    .add(new Paragraph(formater(achat.getTotal()) + " FCFA")
                            .setFontSize(18)
                            .setBold()
                            .setFontColor(BLEU_MARINE)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            doc.add(totalTable);

            // Ligne séparatrice
            doc.add(new Paragraph(" ")
                    .setBorderBottom(new SolidBorder(BLEU_MARINE, 2))
                    .setMarginTop(15)
                    .setMarginBottom(25));

            // ============================================
            // PIED DE PAGE
            // ============================================
            Paragraph merci = new Paragraph("Merci de votre visite !")
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(BLEU_MARINE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(8);

            Paragraph info = new Paragraph(
                    "Ce ticket est votre preuve d'achat. Conservez-le précieusement.")
                    .setFontSize(10)
                    .setItalic()
                    .setFontColor(GRIS_TEXTE)
                    .setTextAlignment(TextAlignment.CENTER);

            Paragraph signature = new Paragraph("À bientôt dans notre boutique !")
                    .setFontSize(11)
                    .setFontColor(GRIS_TEXTE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);

            doc.add(merci);
            doc.add(info);
            doc.add(signature);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }

        return baos.toByteArray();
    }

    // ============================================
    // HELPERS
    // ============================================

    private Cell celluleInfo(String label, String valeur) {
        return new Cell()
                .add(new Paragraph(label)
                        .setFontSize(9)
                        .setBold()
                        .setFontColor(GRIS_TEXTE)
                        .setMarginBottom(3))
                .add(new Paragraph(valeur)
                        .setFontSize(11)
                        .setBold()
                        .setFontColor(BLEU_MARINE))
                .setBackgroundColor(GRIS_CLAIR)
                .setBorder(new SolidBorder(GRIS_MOYEN, 0.5f))
                .setPadding(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private Cell entete(String texte) {
        return new Cell()
                .add(new Paragraph(texte)
                        .setBold()
                        .setFontColor(ColorConstants.WHITE)
                        .setFontSize(11))
                .setBackgroundColor(BLEU_MARINE)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(12);
    }

    private Cell celluleProduit(String texte) {
        return new Cell()
                .add(new Paragraph(texte).setFontSize(11))
                .setBorder(new SolidBorder(GRIS_MOYEN, 0.5f))
                .setPadding(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private Cell celluleCentre(String texte) {
        return new Cell()
                .add(new Paragraph(texte).setFontSize(11))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(new SolidBorder(GRIS_MOYEN, 0.5f))
                .setPadding(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private Cell celluleDroite(String texte) {
        return new Cell()
                .add(new Paragraph(texte).setFontSize(11))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(new SolidBorder(GRIS_MOYEN, 0.5f))
                .setPadding(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private Cell celluleDroiteGras(String texte) {
        return new Cell()
                .add(new Paragraph(texte).setFontSize(11).setBold())
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(new SolidBorder(GRIS_MOYEN, 0.5f))
                .setPadding(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private String formater(BigDecimal valeur) {
        if (valeur == null) return "0.00";
        return String.format("%,.2f", valeur).replace(',', ' ');
    }
}