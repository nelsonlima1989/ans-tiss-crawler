package com.anstiss.service

import groovyx.net.http.ChainedHttpConfig
import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HttpService {

    Document buscarPagina(String url) {

        try {

            HttpBuilder http = HttpBuilder.configure {
                request.uri = url
            }

            String resposta = http.get(String) {

                response.parser("text/html") {
                    ChainedHttpConfig config,
                    FromServer fromServer ->

                        return fromServer.inputStream.getText("UTF-8")
                }
            }

            if (resposta == null || resposta.trim().isEmpty()) {
                throw new IllegalStateException(
                        "A página retornou conteúdo vazio."
                )
            }

            return Jsoup.parse(resposta)

        } catch (Exception exception) {

            println "Erro ao acessar a página:"
            println url
            println "Motivo: ${exception.message}"

            return null
        }
    }
}