package com.anstiss.service

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissNavigationService {

    private static final String URL_ANS =
            "https://www.gov.br/ans/pt-br"

    private final HttpService httpService

    TissNavigationService() {
        this.httpService = new HttpService()
    }

    Document buscarPaginaTiss() {

        Document paginaInicial = httpService.buscarPagina(URL_ANS)

        if (paginaInicial == null) {
            println "Não foi possível acessar a página inicial da ANS."
            return null
        }

        Element linkPrestador = paginaInicial.select("a")
                .find { Element elemento ->
                    elemento.text().contains(
                            "Espaço do Prestador de Serviços de Saúde"
                    )
                }

        if (linkPrestador == null) {
            println "Espaço do Prestador não encontrado."
            return null
        }

        Document paginaPrestadores = httpService.buscarPagina(
                linkPrestador.attr("href")
        )

        if (paginaPrestadores == null) {
            println "Não foi possível acessar a página de prestadores."
            return null
        }

        Element linkTiss = paginaPrestadores.select("a")
                .find { Element elemento ->
                    elemento.text().contains(
                            "TISS - Padrão para Troca de Informação de Saúde Suplementar"
                    )
                }

        if (linkTiss == null) {
            println "Página do TISS não encontrada."
            return null
        }

        return httpService.buscarPagina(
                linkTiss.attr("href")
        )
    }
}