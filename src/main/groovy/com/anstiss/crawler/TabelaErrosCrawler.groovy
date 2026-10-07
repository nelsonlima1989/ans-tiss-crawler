package com.anstiss.crawler

import com.anstiss.service.DownloadService
import com.anstiss.service.HttpService
import com.anstiss.service.TissNavigationService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TabelaErrosCrawler {

    private static final String URL_TISS =
            "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                    "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss"

    private final HttpService httpService
    private final DownloadService downloadService
    private final TissNavigationService tissNavigationService

    TabelaErrosCrawler() {
        this.httpService = new HttpService()
        this.downloadService = new DownloadService()
        this.tissNavigationService = new TissNavigationService()
    }

    void executar() {

        Document paginaTiss = tissNavigationService.buscarPaginaTiss()
        if (paginaTiss == null) {
            println "Não foi possível acessar a página TISS."
            return
        }

        Element tituloTabelasRelacionadas = paginaTiss.select("h2")
                .find { Element elemento ->
                    elemento.text()
                            .toLowerCase()
                            .contains("tabelas relacionadas")
                }

        if (tituloTabelasRelacionadas == null) {
            println "Área de Tabelas relacionadas não encontrada."
            return
        }

        Element proximoElemento =
                tituloTabelasRelacionadas.nextElementSibling()

        if (proximoElemento == null) {
            println "Área do link de Tabelas relacionadas não encontrada."
            return
        }

        Element linkTabelasRelacionadas =
                proximoElemento.selectFirst("a")

        if (linkTabelasRelacionadas == null) {
            println "Link de Tabelas relacionadas não encontrado."
            return
        }

        String urlTabelasRelacionadas =
                linkTabelasRelacionadas.attr("href")

        println "Tabelas relacionadas:"
        println urlTabelasRelacionadas

        Document paginaTabelasRelacionadas =
                httpService.buscarPagina(urlTabelasRelacionadas)
        if (paginaTabelasRelacionadas == null) {
            println "Não foi possível acessar a página de Tabelas relacionadas."
            return
        }

        Element tituloTabelaErros =
                paginaTabelasRelacionadas.select("h2")
                        .find { Element elemento ->
                            elemento.text()
                                    .toLowerCase()
                                    .contains("tabela de erros no envio para a ans")
                        }

        if (tituloTabelaErros == null) {
            println "Tabela de erros no envio para a ANS não encontrada."
            return
        }

        Element elementoTabelaErros =
                tituloTabelaErros.nextElementSibling()

        if (elementoTabelaErros == null) {
            println "Área de download da tabela de erros não encontrada."
            return
        }

        Element linkTabelaErros =
                elementoTabelaErros.selectFirst("a")

        if (linkTabelaErros == null) {
            println "Link para download da tabela de erros não encontrado."
            return
        }

        String urlTabelaErros = linkTabelaErros.attr("href")

        println "Tabela de erros encontrada:"
        println linkTabelaErros.text()
        println urlTabelaErros

        downloadService.baixarArquivo(
                urlTabelaErros,
                "Downloads/Tabelas_relacionadas/tabela_erros_ans.xlsx"
        )
    }
}