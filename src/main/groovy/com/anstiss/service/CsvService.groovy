package com.anstiss.service

import com.anstiss.model.HistoricoTiss

class CsvService {

    void salvarHistorico(
            List<HistoricoTiss> historicos,
            String caminhoDestino
    ) {

        File arquivoDestino = new File(caminhoDestino)

        arquivoDestino.parentFile.mkdirs()

        arquivoDestino.withWriter("UTF-8") { Writer writer ->

            writer.writeLine(
                    "competencia,publicacao,inicio_vigencia"
            )

            historicos.each { HistoricoTiss historico ->

                writer.writeLine(
                        "${historico.competencia}," +
                                "${historico.publicacao}," +
                                "${historico.inicioVigencia}"
                )
            }
        }

        println()
        println "Arquivo CSV gerado com sucesso:"
        println arquivoDestino.absolutePath
    }
}