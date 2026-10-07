package com.anstiss.model

class HistoricoTiss {

    String competencia
    String publicacao
    String inicioVigencia

    @Override
    String toString() {
        return "Competência: ${competencia} | Publicação: ${publicacao} | Início de vigência: ${inicioVigencia}"
    }
}