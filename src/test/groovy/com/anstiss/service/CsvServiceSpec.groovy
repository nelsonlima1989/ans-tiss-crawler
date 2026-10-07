package com.anstiss.service

import com.anstiss.model.HistoricoTiss
import spock.lang.Specification

class CsvServiceSpec extends Specification {

    void "deve gerar arquivo csv com cabecalho e registros"() {

        given:
        CsvService csvService = new CsvService()

        List<HistoricoTiss> historicos = [
                new HistoricoTiss(
                        competencia: "Jan/2016",
                        publicacao: "15/1/2016",
                        inicioVigencia: "15/1/2016"
                ),
                new HistoricoTiss(
                        competencia: "Set/2026",
                        publicacao: "30/09/2026",
                        inicioVigencia: "01/10/2026"
                )
        ]

        File arquivoTemporario =
                File.createTempFile("historico_tiss_", ".csv")

        arquivoTemporario.deleteOnExit()

        when:
        csvService.salvarHistorico(
                historicos,
                arquivoTemporario.absolutePath
        )

        then:
        arquivoTemporario.exists()

        and:
        arquivoTemporario.length() > 0

        and:
        List<String> linhas =
                arquivoTemporario.readLines("UTF-8")

        linhas[0] == "competencia,publicacao,inicio_vigencia"

        and:
        linhas[1] == "Jan/2016,15/1/2016,15/1/2016"

        and:
        linhas[2] == "Set/2026,30/09/2026,01/10/2026"

        and:
        linhas.size() == 3
    }
}