package com.github.luuispnp.obra_do_berco_document.service;

import com.github.luuispnp.obra_do_berco_document.dto.response.EventoResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.GestanteResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.SolicitacaoResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.VoluntarioResponse;
import com.github.luuispnp.obra_do_berco_document.enums.SexoCrianca;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGeneratorService {

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, Color.WHITE);
    private static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.WHITE);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
    private static final Font NOME_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.BLACK);
    private static final Font EVENTO_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
    private static final Font LABEL_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.BLACK);
    private static final Font VALUE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY);
    private static final Font LEGAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Color.DARK_GRAY);
    private static final Font FOOTER_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);
    private static final Font TABLE_HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.WHITE);

    private static final Color PRIMARY = new Color(41, 128, 185);
    private static final Color SECTION_BG = new Color(52, 73, 94);
    private static final Color SECONDARY = new Color(236, 240, 241);
    private static final Color BORDER = new Color(210, 210, 210);

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarFichaIndividual(
            GestanteResponse gestante,
            SolicitacaoResponse solicitacao,
            EventoResponse eventoOuNull,
            VoluntarioResponse responsavel
    ) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 50, 40);
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setPageEvent(new Footer());
            document.open();
            adicionarFicha(document, gestante, solicitacao, eventoOuNull, responsavel);
            document.close();
            return output.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException("Erro ao gerar ficha em PDF.", exception);
        }
    }

    public byte[] gerarFichasEvento(EventoResponse evento, List<FichaParticipante> fichas) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 50, 40);
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setPageEvent(new Footer());
            document.open();

            boolean primeira = true;
            for (FichaParticipante ficha : fichas) {
                if (!primeira) {
                    document.newPage();
                }
                adicionarFicha(document, ficha.gestante(), ficha.solicitacao(), evento, ficha.responsavel());
                primeira = false;
            }

            document.close();
            return output.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException("Erro ao gerar fichas em PDF.", exception);
        }
    }

    public byte[] gerarListaParticipantes(EventoResponse evento, List<LinhaLista> linhas) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 40, 40);
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setPageEvent(new Footer());
            document.open();

            adicionarCabecalhoEvento(document, evento);
            document.add(Chunk.NEWLINE);
            adicionarTabelaParticipantes(document, linhas);

            document.close();
            return output.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException("Erro ao gerar lista de participantes em PDF.", exception);
        }
    }

    private void adicionarFicha(
            Document document,
            GestanteResponse gestante,
            SolicitacaoResponse solicitacao,
            EventoResponse eventoOuNull,
            VoluntarioResponse responsavel
    ) throws DocumentException {

        adicionarCabecalhoFicha(document);

        Paragraph evento = new Paragraph(
                "EVENTO: " + (eventoOuNull != null
                        ? eventoOuNull.titulo() + " - " + formatarData(eventoOuNull.dataEvento())
                        : "____________________________________________"),
                EVENTO_FONT
        );
        evento.setSpacingBefore(8);
        evento.setSpacingAfter(6);
        document.add(evento);

        PdfPTable nomeTable = new PdfPTable(1);
        nomeTable.setWidthPercentage(100);
        PdfPCell nomeCell = new PdfPCell(new Phrase("NOME DA GESTANTE:  " + gestante.nome(), NOME_FONT));
        nomeCell.setPadding(8);
        nomeCell.setBackgroundColor(SECONDARY);
        nomeCell.setBorderColor(BORDER);
        nomeTable.addCell(nomeCell);
        document.add(nomeTable);
        document.add(Chunk.NEWLINE);

        PdfPTable dados = new PdfPTable(3);
        dados.setWidthPercentage(100);
        dados.setWidths(new float[]{16, 26, 58});

        adicionarSecao(dados, "DOCUMENTOS", new String[][]{
                {"N° Identidade:", valor(gestante.numeroIdentidade())},
                {"CPF:", valor(gestante.cpf())},
                {"Data Nascimento:", formatarData(gestante.dataNascimento())}
        });

        adicionarSecao(dados, "DADOS PESSOAIS", new String[][]{
                {"Telefone:", valor(gestante.telefone())},
                {"Endereço:", valor(gestante.endereco() != null ? gestante.endereco().logradouro() : null)},
                {"Bairro:", valor(gestante.endereco() != null ? gestante.endereco().bairro() : null)},
                {"Cidade:", valor(gestante.endereco() != null ? gestante.endereco().cidade() : null)},
                {"Estado Civil:", valor(gestante.estadoCivil())},
                {"Religião:", valor(solicitacao.religiao())},
                {"Está Trabalhando?", simNao(solicitacao.trabalhando())},
                {"Recebe Auxílio do Governo:", valor(solicitacao.beneficioSocial())},
                {"Precisa Atendimento Profissional:", valor(solicitacao.atendimentoSocial())},
                {"Nº de Pessoas no Mesmo Endereço:", valor(solicitacao.qtdPessoasResidencia())},
                {"E-mail:", valor(gestante.email())}
        });

        adicionarSecao(dados, "DADOS DA CRIANÇA", new String[][]{
                {"Tempo de Gravidez:", valor(solicitacao.idadeGestacionalSemanas()) + " semanas"},
                {"Cartão Pré Natal:", cartaoPreNatal(solicitacao.cartaoPreNatal())},
                {"Sexo:", valor(solicitacao.sexoCrianca())},
                {"Nome do Pai:", valor(solicitacao.nomeDoPai())}
        });

        document.add(dados);
        document.add(Chunk.NEWLINE);

        adicionarObservacoes(document, solicitacao.observacaoGravidez());
        adicionarAutorizacaoImagem(document);
        adicionarAssinaturaGestante(document, gestante.nome());
        adicionarRecebimentoKit(document, responsavel);
    }

    private void adicionarCabecalhoFicha(Document document) throws DocumentException {
        PdfPTable cabecalho = new PdfPTable(1);
        cabecalho.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(PRIMARY);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(10);

        Paragraph titulo = new Paragraph("FICHA CADASTRO GESTANTE - OBRA DO BERÇO 70 Anos", TITLE_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(titulo);

        Paragraph subtitulo = new Paragraph(
                "Santuário Arquidiocesano da SS. Eucaristia Nossa Senhora da Boa Viagem", SUBTITLE_FONT);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(subtitulo);

        cabecalho.addCell(cell);
        document.add(cabecalho);
    }

    private void adicionarSecao(PdfPTable table, String secao, String[][] campos) {
        PdfPCell secaoCell = new PdfPCell(new Phrase(secao, SECTION_FONT));
        secaoCell.setRowspan(campos.length);
        secaoCell.setBackgroundColor(SECTION_BG);
        secaoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        secaoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        secaoCell.setPadding(5);
        secaoCell.setBorderColor(BORDER);
        table.addCell(secaoCell);

        for (String[] campo : campos) {
            PdfPCell labelCell = new PdfPCell(new Phrase(campo[0], LABEL_FONT));
            labelCell.setPadding(4);
            labelCell.setBorderColor(BORDER);
            table.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(campo[1], VALUE_FONT));
            valueCell.setPadding(4);
            valueCell.setBorderColor(BORDER);
            table.addCell(valueCell);
        }
    }

    private void adicionarObservacoes(Document document, String observacoes) throws DocumentException {
        Paragraph titulo = new Paragraph("OBSERVAÇÕES IMPORTANTES SOBRE A GRAVIDEZ E OUTRAS", LABEL_FONT);
        titulo.setSpacingBefore(4);
        titulo.setSpacingAfter(3);
        document.add(titulo);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase(valor(observacoes), VALUE_FONT));
        cell.setMinimumHeight(50);
        cell.setPadding(6);
        cell.setBorderColor(BORDER);
        table.addCell(cell);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void adicionarAutorizacaoImagem(Document document) throws DocumentException {
        Paragraph texto = new Paragraph(
                "AUTORIZAÇÃO PARA USO E EXPOSIÇÃO DE IMAGEM — AUTORIZO uso da minha imagem em qualquer tipo de " +
                        "materiais publicitários e de marketing destinados à divulgação e publicidade da \"Obra do " +
                        "Berço\" pela Associação Pró Obras Sociais da Paróquia Nossa Senhora da Boa Viagem, CNPJ: " +
                        "11.177.911/0001-05 em todo o território nacional e internacional e em qualquer meio de " +
                        "comunicação existente ou que venham a existir. A presente autorização é concedida a título " +
                        "institucional, gratuito, sem que nada eu ou qualquer outra pessoa independente de vínculo " +
                        "possa reclamar a título de pagamentos ou remuneração pelo uso da minha imagem.",
                LEGAL_FONT
        );
        texto.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(texto);

        Paragraph dataLocal = new Paragraph(
                "Belo Horizonte, ______ de ____________________ de ______", LEGAL_FONT);
        dataLocal.setAlignment(Element.ALIGN_CENTER);
        dataLocal.setSpacingBefore(6);
        dataLocal.setSpacingAfter(6);
        document.add(dataLocal);
    }

    private void adicionarAssinaturaGestante(Document document, String nomeGestante) throws DocumentException {
        Paragraph linha = new Paragraph(
                "____________________________________________________________________", VALUE_FONT);
        linha.setAlignment(Element.ALIGN_CENTER);
        document.add(linha);

        Paragraph nome = new Paragraph(nomeGestante, LABEL_FONT);
        nome.setAlignment(Element.ALIGN_CENTER);
        nome.setSpacingAfter(8);
        document.add(nome);
    }

    private void adicionarRecebimentoKit(Document document, VoluntarioResponse responsavel) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{50, 50});

        PdfPCell kitLabel = new PdfPCell(new Phrase("RECEBIMENTO DO KIT", LABEL_FONT));
        kitLabel.setBackgroundColor(SECONDARY);
        kitLabel.setBorderColor(BORDER);
        kitLabel.setPadding(5);
        table.addCell(kitLabel);

        PdfPCell responsavelLabel = new PdfPCell(new Phrase("RESPONSÁVEL PELO ATENDIMENTO NO EVENTO", LABEL_FONT));
        responsavelLabel.setBackgroundColor(SECONDARY);
        responsavelLabel.setBorderColor(BORDER);
        responsavelLabel.setPadding(5);
        table.addCell(responsavelLabel);

        PdfPCell assinatura = new PdfPCell(new Phrase("Assinatura: ____________________________", VALUE_FONT));
        assinatura.setPadding(10);
        assinatura.setMinimumHeight(30);
        assinatura.setBorderColor(BORDER);
        table.addCell(assinatura);

        String nomeResponsavel = responsavel != null ? responsavel.nomeCompleto() : "-";
        PdfPCell responsavelValor = new PdfPCell(new Phrase(
                nomeResponsavel + "          Data: ______/______/______", VALUE_FONT));
        responsavelValor.setPadding(10);
        responsavelValor.setMinimumHeight(30);
        responsavelValor.setBorderColor(BORDER);
        table.addCell(responsavelValor);

        document.add(table);
    }

    private void adicionarCabecalhoEvento(Document document, EventoResponse evento) throws DocumentException {
        PdfPTable cabecalho = new PdfPTable(1);
        cabecalho.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(PRIMARY);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(10);

        Paragraph titulo = new Paragraph(
                "OBRA DO BERÇO - EVENTO PARA ENTREGA DOS KITS ENXOVAL PARA RECEM NASCIDO", TITLE_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(titulo);

        Paragraph subtitulo = new Paragraph(
                "Santuário Arquidiocesano da SS. Eucaristia Nossa Senhora da Boa Viagem", SUBTITLE_FONT);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(subtitulo);

        Paragraph eventoInfo = new Paragraph(
                evento.titulo() + " — " + formatarData(evento.dataEvento()), SUBTITLE_FONT);
        eventoInfo.setAlignment(Element.ALIGN_CENTER);
        eventoInfo.setSpacingBefore(4);
        cell.addElement(eventoInfo);

        cabecalho.addCell(cell);
        document.add(cabecalho);

        Paragraph listaTitulo = new Paragraph("LISTA DE PRESENÇA DAS GESTANTES NO EVENTO", EVENTO_FONT);
        listaTitulo.setAlignment(Element.ALIGN_CENTER);
        listaTitulo.setSpacingBefore(8);
        document.add(listaTitulo);
    }

    private void adicionarTabelaParticipantes(Document document, List<LinhaLista> linhas) throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{6, 22, 13, 11, 11, 10, 17, 15});

        String[] cabecalhos = {
                "Nº", "Nome Completo da Gestante", "Telefone", "Idade Gestacional",
                "Data Provável do Parto", "Sexo", "Observações", "Assinatura"
        };
        for (String texto : cabecalhos) {
            PdfPCell cell = new PdfPCell(new Phrase(texto, TABLE_HEADER_FONT));
            cell.setBackgroundColor(SECTION_BG);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        int numero = 1;
        for (LinhaLista linha : linhas) {
            adicionarCelula(table, String.valueOf(numero++), Element.ALIGN_CENTER);
            adicionarCelula(table, valor(linha.nome()), Element.ALIGN_LEFT);
            adicionarCelula(table, valor(linha.telefone()), Element.ALIGN_CENTER);
            adicionarCelula(table, valor(linha.idadeGestacionalSemanas()) + " sem.", Element.ALIGN_CENTER);
            adicionarCelula(table, formatarData(linha.dataProvavelParto()), Element.ALIGN_CENTER);
            adicionarCelula(table, valor(linha.sexo()), Element.ALIGN_CENTER);
            adicionarCelula(table, valor(linha.observacoes()), Element.ALIGN_LEFT);

            PdfPCell assinatura = new PdfPCell(new Phrase(""));
            assinatura.setMinimumHeight(24);
            assinatura.setBorderColor(BORDER);
            table.addCell(assinatura);
        }

        document.add(table);
    }

    private void adicionarCelula(PdfPTable table, String texto, int alinhamento) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, VALUE_FONT));
        cell.setPadding(5);
        cell.setBorderColor(BORDER);
        cell.setHorizontalAlignment(alinhamento);
        table.addCell(cell);
    }

    private String valor(Object valor) {
        if (valor == null) {
            return "-";
        }
        String texto = valor.toString().trim();
        return texto.isEmpty() ? "-" : texto;
    }

    private String simNao(Boolean valor) {
        if (valor == null) {
            return "-";
        }
        return valor ? "Sim" : "Não";
    }

    private String cartaoPreNatal(Boolean cartaoPreNatal) {
        if (cartaoPreNatal == null) {
            return "(   ) SIM        (   ) NÃO";
        }
        return cartaoPreNatal ? "( X ) SIM        (   ) NÃO" : "(   ) SIM        ( X ) NÃO";
    }

    private String formatarData(LocalDate data) {
        return data == null ? "-" : data.format(DATE);
    }

    private static class Footer extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable footer = new PdfPTable(2);
                footer.setWidths(new int[]{70, 30});
                footer.setTotalWidth(document.right() - document.left());

                PdfPCell left = new PdfPCell(new Phrase("Sistema Obra do Berço", FOOTER_FONT));
                left.setBorder(Rectangle.TOP);
                left.setBorderColor(Color.LIGHT_GRAY);
                left.setHorizontalAlignment(Element.ALIGN_LEFT);
                left.setPaddingTop(5);

                PdfPCell right = new PdfPCell(new Phrase("Página " + writer.getPageNumber(), FOOTER_FONT));
                right.setBorder(Rectangle.TOP);
                right.setBorderColor(Color.LIGHT_GRAY);
                right.setHorizontalAlignment(Element.ALIGN_RIGHT);
                right.setPaddingTop(5);

                footer.addCell(left);
                footer.addCell(right);
                footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
            } catch (Exception ignored) {
            }
        }

    }

    public record FichaParticipante(
            GestanteResponse gestante,
            SolicitacaoResponse solicitacao,
            VoluntarioResponse responsavel
    ) {
    }

    public record LinhaLista(
            String nome,
            String telefone,
            Integer idadeGestacionalSemanas,
            LocalDate dataProvavelParto,
            SexoCrianca sexo,
            String observacoes
    ) {
    }

}
