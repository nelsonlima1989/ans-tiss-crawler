package com.anstiss.crawler

import spock.lang.Specification

import java.time.YearMonth

class HistoricoTissCrawlerSpec extends Specification {

    void "deve converter janeiro de 2016 corretamente"() {

        given:
        String competencia = "Jan/2016"

        when:
        YearMonth resultado =
                HistoricoTissCrawler.converterCompetencia(competencia)

        then:
        resultado == YearMonth.of(2016, 1)
    }

    void "deve converter setembro de 2026 corretamente"() {

        given:
        String competencia = "set/2026"

        when:
        YearMonth resultado =
                HistoricoTissCrawler.converterCompetencia(competencia)

        then:
        resultado == YearMonth.of(2026, 9)
    }

    void "janeiro de 2016 deve ser aceito como competência mínima"() {

        given:
        YearMonth competencia =
                HistoricoTissCrawler.converterCompetencia("Jan/2016")

        YearMonth competenciaMinima =
                YearMonth.of(2016, 1)

        expect:
        !competencia.isBefore(competenciaMinima)
    }

    void  "competência posterior a janeiro de 2016 deve ser aceita"() {

        given:
        YearMonth competencia =
                HistoricoTissCrawler.converterCompetencia("Set/2020")

        YearMonth competenciaMinima =
                YearMonth.of(2016, 1)

        expect:
        competencia.isAfter(competenciaMinima)
    }

    void "dezembro de 2015 deve ser anterior ao limite"() {

        given:
        YearMonth competencia =
                HistoricoTissCrawler.converterCompetencia("Dez/2015")

        YearMonth competenciaMinima =
                YearMonth.of(2016, 1)

        expect:
        competencia.isBefore(competenciaMinima)
    }

    void "deve converter competencias corretamente"() {

        expect:
        HistoricoTissCrawler.converterCompetencia(competencia) ==
                YearMonth.of(ano, mes)

        where:
        competencia || ano  | mes
        "Jan/2016"  || 2016 | 1
        "Abr/2016"  || 2016 | 4
        "Set/2020"  || 2020 | 9
        "dez/2024"  || 2024 | 12
        "set/2026"  || 2026 | 9
    }

    void "deve validar competencia em relacao a janeiro de 2016"() {

        given:
        YearMonth competenciaConvertida =
                HistoricoTissCrawler.converterCompetencia(competencia)

        YearMonth competenciaMinima =
                YearMonth.of(2016, 1)

        expect:
        !competenciaConvertida.isBefore(competenciaMinima) == esperado

        where:
        competencia || esperado
        "Dez/2015"  || false
        "Jan/2016"  || true
        "Abr/2016"  || true
        "Set/2020"  || true
        "set/2026"  || true
    }
}