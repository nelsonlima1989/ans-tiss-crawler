package com.anstiss.service

import groovyx.net.http.ChainedHttpConfig
import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder

class DownloadService {

    boolean baixarArquivo(
            String url,
            String caminhoDestino
    ) {

        try {

            File arquivoDestino = new File(caminhoDestino)

            File diretorioPai = arquivoDestino.parentFile

            if (!diretorioPai.exists()) {

                boolean diretorioCriado = diretorioPai.mkdirs()

                if (!diretorioCriado) {
                    throw new IOException(
                            "Não foi possível criar o diretório de destino."
                    )
                }
            }

            HttpBuilder http = HttpBuilder.configure {
                request.uri = url
            }

            byte[] conteudo = http.get(byte[]) {

                response.parser("*/*") {
                    ChainedHttpConfig config,
                    FromServer fromServer ->

                        return fromServer.inputStream.bytes
                }
            }

            if (conteudo == null || conteudo.length == 0) {
                throw new IllegalStateException(
                        "O arquivo baixado está vazio."
                )
            }

            arquivoDestino.bytes = conteudo

            println "Download Concluído:"
            println arquivoDestino.absolutePath

            return true

        } catch (Exception exception) {

            println "Erro ao baixar arquivo:"
            println url
            println "Motivo: ${exception.message}"

            return false
        }
    }
}