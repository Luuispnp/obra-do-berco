package com.github.luuispnp.obra_do_berco_document.service;

import com.github.luuispnp.obra_do_berco_document.dto.response.GestanteResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.SolicitacaoResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    private static final Font TITLE_FONT =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.WHITE);

    private static final Font SECTION_FONT =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new Color(33,150,243));

    private static final Font LABEL_FONT =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);

    private static final Font VALUE_FONT =
            FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);

    private static final Font FOOTER_FONT =
            FontFactory.getFont(FontFactory.HELVETICA,9,Color.GRAY);

    private static final Color PRIMARY = new Color(41,128,185);
    private static final Color SECONDARY = new Color(236,240,241);
    private static final Color BORDER = new Color(220,220,220);

    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarPdf(GestanteResponse gestante,
                           SolicitacaoResponse solicitacao){

        try{

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            Document document = new Document(
                    PageSize.A4,
                    40,
                    40,
                    60,
                    50
            );

            PdfWriter writer = PdfWriter.getInstance(document,output);

            writer.setPageEvent(new Footer());

            document.open();

            adicionarCabecalho(document);

            adicionarTitulo(document);

            adicionarInformacoesDocumento(document);

            adicionarSecaoGestante(document,gestante);

            adicionarSecaoSolicitacao(document,solicitacao);

            adicionarObservacoes(document);

            adicionarRodapeAssinatura(document);

            document.close();

            return output.toByteArray();

        }catch (Exception e){
            throw new RuntimeException("Erro ao gerar PDF.",e);
        }

    }

    private void adicionarCabecalho(Document document) throws Exception{

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(PRIMARY);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(18);

        Paragraph titulo = new Paragraph(
                "OBRA DO BERÇO",
                TITLE_FONT
        );

        titulo.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(titulo);

        table.addCell(cell);

        document.add(table);

        document.add(Chunk.NEWLINE);

    }

    private void adicionarTitulo(Document document) throws Exception{

        Paragraph p = new Paragraph(
                "Relatório de Solicitação",
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        18,
                        PRIMARY
                )
        );

        p.setAlignment(Element.ALIGN_CENTER);

        document.add(p);

        document.add(Chunk.NEWLINE);

    }

    private void adicionarInformacoesDocumento(Document document)
            throws Exception{

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setSpacingAfter(15);

        table.setWidths(new int[]{30,70});

        adicionarLinha(table,
                "Emitido em",
                LocalDate.now().format(DATE));

        adicionarLinha(table,
                "Sistema",
                "Obra do Berço");

        adicionarLinha(table,
                "Documento",
                "Solicitação de Enxoval");

        document.add(table);

    }

    private void adicionarSecaoGestante(
            Document document,
            GestanteResponse g
    ) throws Exception{

        adicionarTituloSecao(document,"DADOS DA GESTANTE");

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setWidths(new int[]{35,65});

        adicionarLinha(table,"ID",valor(g.getId()));

        adicionarLinha(table,"Nome Completo",valor(g.getNomeCompleto()));

        adicionarLinha(table,"CPF",valor(g.getCpf()));

        adicionarLinha(table,"RG",valor(g.getIdentidade()));

        adicionarLinha(table,"Nascimento",
                formatarData(g.getDataNascimento()));

        adicionarLinha(table,"Telefone",
                valor(g.getTelefone()));

        adicionarLinha(table,"Email",
                valor(g.getEmail()));

        adicionarLinha(table,"Endereço",
                valor(g.getEndereco()));

        adicionarLinha(table,"Bairro",
                valor(g.getBairro()));

        adicionarLinha(table,"Cidade",
                valor(g.getCidade()));

        adicionarLinha(table,"Estado Civil",
                valor(g.getEstadoCivil()));

        adicionarLinha(table,"Religião",
                valor(g.getReligiao()));

        adicionarLinha(table,"Nome do Pai",
                valor(g.getNomePai()));

        document.add(table);

        document.add(Chunk.NEWLINE);

    }

    private void adicionarSecaoSolicitacao(
            Document document,
            SolicitacaoResponse s
    ) throws Exception{

        adicionarTituloSecao(document,
                "DADOS DA SOLICITAÇÃO");

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setWidths(new int[]{35,65});

        adicionarLinha(table,
                "ID Solicitação",
                valor(s.getId()));

        adicionarLinha(table,
                "ID Gestante",
                valor(s.getGestanteId()));

        adicionarLinha(table,
                "Status",
                valor(s.getStatus()));

        adicionarLinha(table,
                "Idade Gestacional",
                valor(s.getIdadeGestacionalSemanas()) + " semanas");

        adicionarLinha(table,
                "Sexo da Criança",
                valor(s.getSexoCrianca()));

        adicionarLinha(table,
                "Trabalhando",
                simNao(s.getTrabalhando()));

        adicionarLinha(table,
                "Pessoas na Residência",
                valor(s.getQtdPessoasResidencia()));

        adicionarLinha(table,
                "Benefício do Governo",
                valor(s.getBeneficioGoverno()));

        adicionarLinha(table,
                "Necessita Atendimento Social",
                simNao(s.getNecessidadeAtendimentoSocial()));

        adicionarLinha(table,
                "Observações",
                valor(s.getObservacaoGravidez()));

        document.add(table);

        document.add(Chunk.NEWLINE);

    }
    private void adicionarTituloSecao(Document document, String titulo)
            throws DocumentException {

        Paragraph p = new Paragraph(titulo, SECTION_FONT);
        p.setSpacingBefore(10);
        p.setSpacingAfter(8);

        document.add(p);
    }

    private void adicionarLinha(PdfPTable table,
                                String label,
                                String valor) {

        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setBackgroundColor(SECONDARY);
        labelCell.setBorderColor(BORDER);
        labelCell.setPadding(8);

        PdfPCell valueCell = new PdfPCell(new Phrase(valor, VALUE_FONT));
        valueCell.setBorderColor(BORDER);
        valueCell.setPadding(8);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void adicionarObservacoes(Document document)
            throws DocumentException {

        adicionarTituloSecao(document, "OBSERVAÇÕES");

        PdfPCell cell = new PdfPCell();

        cell.setPadding(12);
        cell.setMinimumHeight(120);
        cell.setBorderColor(BORDER);

        cell.addElement(new Paragraph(
                "Espaço reservado para observações da equipe responsável.",
                VALUE_FONT));

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.addCell(cell);

        document.add(table);

        document.add(Chunk.NEWLINE);
    }

    private void adicionarRodapeAssinatura(Document document)
            throws DocumentException {

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);
        table.setWidths(new int[]{50,50});

        PdfPCell assistente = new PdfPCell();
        assistente.setBorder(Rectangle.NO_BORDER);
        assistente.setHorizontalAlignment(Element.ALIGN_CENTER);

        assistente.addElement(new Paragraph(
                "__________________________________",
                VALUE_FONT));

        assistente.addElement(new Paragraph(
                "Assistente Social",
                LABEL_FONT));

        PdfPCell gestante = new PdfPCell();
        gestante.setBorder(Rectangle.NO_BORDER);
        gestante.setHorizontalAlignment(Element.ALIGN_CENTER);

        gestante.addElement(new Paragraph(
                "__________________________________",
                VALUE_FONT));

        gestante.addElement(new Paragraph(
                "Gestante",
                LABEL_FONT));

        table.addCell(assistente);
        table.addCell(gestante);

        document.add(table);
    }

    private String valor(Object valor){

        if(valor == null){
            return "-";
        }

        String texto = valor.toString().trim();

        if(texto.isEmpty()){
            return "-";
        }

        return texto;
    }

    private String simNao(Boolean valor){

        if(valor == null){
            return "-";
        }

        return valor ? "Sim" : "Não";
    }

    private String formatarData(LocalDate data){

        if(data == null){
            return "-";
        }

        return data.format(DATE);
    }

    private static class Footer extends PdfPageEventHelper{

        private final Font footerFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA,
                        9,
                        Color.GRAY);

        @Override
        public void onEndPage(PdfWriter writer,
                              Document document){

            PdfPTable footer = new PdfPTable(2);

            try{

                footer.setWidths(new int[]{70,30});
                footer.setTotalWidth(520);

                PdfPCell left = new PdfPCell(
                        new Phrase(
                                "Sistema Obra do Berço",
                                footerFont));

                left.setBorder(Rectangle.TOP);
                left.setBorderColor(Color.LIGHT_GRAY);
                left.setHorizontalAlignment(Element.ALIGN_LEFT);
                left.setPaddingTop(5);

                PdfPCell right = new PdfPCell(
                        new Phrase(
                                "Página " + writer.getPageNumber(),
                                footerFont));

                right.setBorder(Rectangle.TOP);
                right.setBorderColor(Color.LIGHT_GRAY);
                right.setHorizontalAlignment(Element.ALIGN_RIGHT);
                right.setPaddingTop(5);

                footer.addCell(left);
                footer.addCell(right);

                footer.writeSelectedRows(
                        0,
                        -1,
                        36,
                        40,
                        writer.getDirectContent());

            }catch(Exception ignored){

            }

        }

    }

}