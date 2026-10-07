package com.anstiss.crawler

import com.anstiss.model.HistoricoTiss
import com.anstiss.service.HttpService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.YearMonth
import java.util.Locale
import com.anstiss.service.CsvService

class HistoricoTissCrawler {

    private static final String URL_HISTORICO =
            "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                    "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
                    "padrao-tiss-historico-das-versoes-dos-componentes-do-padrao-tiss"

    private final HttpService httpService
    private final CsvService csvService

    HistoricoTissCrawler() {
        this.httpService = new HttpService()
        this.csvService = new CsvService()
    }

    void executar() {

        Document paginaHistorico = httpService.buscarPagina(URL_HISTORICO)
        if (paginaHistorico == null) {
            println "Não foi possível acessar o histórico TISS."
            return
        }

        List<HistoricoTiss> historicos = []

        YearMonth competenciaMinima = YearMonth.of(2016, 1)

        paginaHistorico.select("table tbody tr").each { Element linha ->

            List<Element> colunas = linha.select("td")

            if (colunas.size() >= 3) {

                String competencia = colunas[0].text()
                String publicacao = colunas[1].text()
                String inicioVigencia = colunas[2].text()

                if (competencia ==~ /(?i)[a-z]{3}\/\d{4}/) {

                    YearMonth competenciaConvertida =
                            converterCompetencia(competencia)

                    if (!competenciaConvertida.isBefore(competenciaMinima)) {

                        HistoricoTiss historico = new HistoricoTiss(
                                competencia: competencia,
                                publicacao: publicacao,
                                inicioVigencia: inicioVigencia
                        )

                        historicos.add(historico)
                    }
                }
            }
        }

        println()
        println "Histórico TISS a partir de jan/2016:"
        println "=================================="

          //Printar cada historico
//        historicos.each { HistoricoTiss historico ->
//            println historico
//        }
        println "[TASK 2] Registros encontrados: ${historicos.size()}"

        csvService.salvarHistorico(
                historicos,
                "dados/historico_tiss.csv"
        )

    }

        static YearMonth converterCompetencia(String competencia) {

        String[] partes = competencia.toLowerCase().split("/")

        String mes = partes[0]
        int ano = Integer.parseInt(partes[1])

        Map<String, Integer> meses = [
                "jan": 1,
                "fev": 2,
                "mar": 3,
                "abr": 4,
                "mai": 5,
                "jun": 6,
                "jul": 7,
                "ago": 8,
                "set": 9,
                "out": 10,
                "nov": 11,
                "dez": 12
        ]

        Integer numeroMes = meses[mes]

        return YearMonth.of(ano, numeroMes)
    }

}