package com.anstiss

import com.anstiss.crawler.HistoricoTissCrawler
import com.anstiss.crawler.TabelaErrosCrawler
import com.anstiss.crawler.TissCrawler

class Main {

    static void main(String[] args) {

        println "=================================="
        println "       ANS TISS CRAWLER"
        println "=================================="

        println()
        println "[TASK 1] Iniciando..."
        new TissCrawler().executar()

        println()
        println "[TASK 2] Iniciando..."
        new HistoricoTissCrawler().executar()

        println()
        println "[TASK 3] Iniciando..."
        new TabelaErrosCrawler().executar()

        println()
        println "=================================="
        println "Execução finalizada."
        println "=================================="
    }
}