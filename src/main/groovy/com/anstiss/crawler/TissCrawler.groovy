package com.anstiss.crawler

import com.anstiss.service.DownloadService
import com.anstiss.service.HttpService
import com.anstiss.service.TissNavigationService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissCrawler {

    private final HttpService httpService
    private final DownloadService downloadService
    private final TissNavigationService tissNavigationService

    TissCrawler() {
        this.httpService = new HttpService()
        this.downloadService = new DownloadService()
        this.tissNavigationService = new TissNavigationService()
    }

    void executar() {

        Document paginaTiss = tissNavigationService.buscarPaginaTiss()

        if (paginaTiss == null) {
            println "Não foi possível acessar a página do TISS."
            return
        }

        Element tituloVersao = paginaTiss.select("h2")
                .find { Element elemento ->
                    elemento.text() ==~ /Padrão TISS.*\d{4}/
                }

        if (tituloVersao == null) {
            println "Versão atual do Padrão TISS não encontrada."
            return
        }

        Element proximoElemento = tituloVersao.nextElementSibling()

        if (proximoElemento == null) {
            println "Área da versão TISS não encontrada."
            return
        }

        Element linkVersao = proximoElemento.selectFirst("a")

        if (linkVersao == null) {
            println "Link da versão TISS não encontrado."
            return
        }

        println "[TASK 1] Versão encontrada: ${tituloVersao.text()}"

        Document paginaVersao = httpService.buscarPagina(
                linkVersao.attr("href")
        )
        if (paginaVersao == null) {
            println "Não foi possível acessar a página da versão atual do TISS."
            return
        }

        Element linhaComunicacao = paginaVersao.select("tr")
                .find { Element linha ->
                    linha.text().contains(
                            "Componente de Comunicação"
                    )
                }

        if (linhaComunicacao == null) {
            println "Componente de Comunicação não encontrado."
            return
        }

        Element linkComunicacao = linhaComunicacao.selectFirst("a")

        if (linkComunicacao == null) {
            println "Arquivo do Componente de Comunicação não encontrado."
            return
        }

        String urlComunicacao = linkComunicacao.attr("href")

        println "[TASK 1] Componente de Comunicação localizado."
        println "[TASK 1] Arquivo: ${linkComunicacao.text()}"
        println "[TASK 1] URL: ${urlComunicacao}"

        downloadService.baixarArquivo(
                urlComunicacao,
                "Downloads/Arquivos_padrao_TISS/componente_comunicacao.zip"
        )


    }
}