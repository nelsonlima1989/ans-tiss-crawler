# ANS TISS Crawler

Projeto desenvolvido em **Groovy** com o objetivo de automatizar a coleta de dados públicos disponibilizados pela **Agência Nacional de Saúde Suplementar (ANS)** relacionados ao padrão **TISS — Troca de Informações na Saúde Suplementar**.

O sistema utiliza **HTTPBuilder-NG** para realizar as requisições HTTP e **Jsoup** para interpretar e navegar pelo HTML das páginas da ANS.

## Objetivos do projeto

O crawler foi desenvolvido para atender aos seguintes requisitos:

### Task 1 — Download do Componente de Comunicação

O sistema:

1. acessa a página inicial da ANS;
2. localiza o espaço destinado aos prestadores de serviços de saúde;
3. acessa a página do padrão TISS;
4. identifica automaticamente a versão mais recente publicada;
5. localiza o **Componente de Comunicação**;
6. realiza o download do arquivo `.zip`.

Arquivo gerado:

```text
Downloads/Arquivos_padrao_TISS/componente_comunicacao.zip
```

### Task 2 — Histórico das versões do padrão TISS

O sistema acessa o histórico das versões dos componentes do padrão TISS e coleta:

- competência;
- publicação;
- início de vigência.

São considerados apenas registros a partir da competência:

```text
Jan/2016
```

Os dados são salvos em:

```text
dados/historico_tiss.csv
```

Exemplo:

```csv
competencia,publicacao,inicio_vigencia
set/2026,30/09/2026,01/10/2026
jul/2026,31/07/2026,01/08/2026
...
Jan/2016,15/1/2016,15/1/2016
```

### Task 3 — Download da Tabela de Erros

O crawler acessa a área de **Tabelas relacionadas** e realiza o download da:

```text
Tabela de erros no envio para a ANS
```

Arquivo gerado:

```text
Downloads/Tabelas_relacionadas/tabela_erros_ans.xlsx
```

## Tecnologias utilizadas

- Groovy 3.0.24
- Java 21
- Gradle
- HTTPBuilder-NG
- Jsoup
- Spock Framework
- JUnit Platform
- Git

## Estrutura do projeto

```text
ans-tiss-crawler/
├── Downloads/
│   ├── Arquivos_padrao_TISS/
│   │   └── componente_comunicacao.zip
│   └── Tabelas_relacionadas/
│       └── tabela_erros_ans.xlsx
│
├── dados/
│   └── historico_tiss.csv
│
├── src/
│   ├── main/
│   │   └── groovy/
│   │       └── com/
│   │           └── anstiss/
│   │               ├── Main.groovy
│   │               │
│   │               ├── crawler/
│   │               │   ├── TissCrawler.groovy
│   │               │   ├── HistoricoTissCrawler.groovy
│   │               │   └── TabelaErrosCrawler.groovy
│   │               │
│   │               ├── model/
│   │               │   └── HistoricoTiss.groovy
│   │               │
│   │               └── service/
│   │                   ├── HttpService.groovy
│   │                   ├── DownloadService.groovy
│   │                   ├── CsvService.groovy
│   │                   └── TissNavigationService.groovy
│   │
│   └── test/
│       └── groovy/
│           └── com/
│               └── anstiss/
│                   ├── crawler/
│                   │   └── HistoricoTissCrawlerSpec.groovy
│                   └── service/
│                       └── CsvServiceSpec.groovy
│
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md
```

## Arquitetura

O projeto foi dividido por responsabilidade.

### Main

Responsável apenas por iniciar a execução das tasks.

### Crawlers

#### `TissCrawler`

Responsável por:

- localizar a versão mais recente do padrão TISS;
- localizar o Componente de Comunicação;
- solicitar o download do arquivo.

#### `HistoricoTissCrawler`

Responsável por:

- acessar o histórico do padrão TISS;
- interpretar a tabela HTML;
- validar o formato da competência;
- filtrar registros a partir de Jan/2016;
- solicitar a geração do CSV.

#### `TabelaErrosCrawler`

Responsável por:

- localizar a seção de Tabelas relacionadas;
- localizar a Tabela de erros no envio para a ANS;
- solicitar o download do arquivo.

## Services

### `HttpService`

Responsável por realizar requisições HTTP e converter o HTML recebido em um objeto `Document` do Jsoup.

Fluxo:

```text
URL
↓
HTTPBuilder
↓
HTML
↓
Jsoup
↓
Document
```

### `DownloadService`

Responsável por:

- realizar downloads;
- criar automaticamente os diretórios necessários;
- validar se o arquivo recebido não está vazio;
- gravar o conteúdo no sistema de arquivos.

### `CsvService`

Responsável por gerar o arquivo CSV contendo os dados do histórico TISS.

### `TissNavigationService`

Centraliza a navegação até a página principal do padrão TISS.

Fluxo:

```text
ANS
↓
Espaço do Prestador
↓
TISS
```

## Model

### `HistoricoTiss`

Representa um registro do histórico do padrão TISS.

Campos:

```text
competencia
publicacao
inicioVigencia
```

## Regex

O projeto utiliza expressão regular para validar o formato das competências.

Exemplo:

```groovy
/(?i)[a-z]{3}\/\d{4}/
```

Ela aceita valores como:

```text
Jan/2016
set/2026
Dez/2019
```

O modificador:

```text
(?i)
```

faz com que a expressão ignore diferenças entre letras maiúsculas e minúsculas.

## Filtro por competência

As competências são convertidas para `YearMonth`.

Exemplo:

```text
Jan/2016
↓
2016-01
```

O limite mínimo utilizado é:

```groovy
YearMonth.of(2016, 1)
```

Dessa forma, apenas competências iguais ou posteriores a Jan/2016 são consideradas.

## Tratamento de erros

O projeto possui tratamento para situações como:

- falha de conexão;
- página vazia;
- links não encontrados;
- versão do TISS não encontrada;
- falha ao criar diretórios;
- falha no download;
- arquivo vazio;
- conteúdo HTML indisponível.

Os crawlers utilizam validações com retorno antecipado para evitar excesso de `if/else` aninhados.

## Testes

Os testes automatizados foram implementados utilizando **Spock Framework**.

Para executar:

```bash
./gradlew clean test
```

### `HistoricoTissCrawlerSpec`

Valida:

- conversão das competências;
- meses diferentes;
- competências em letras maiúsculas e minúsculas;
- limite de Jan/2016;
- competências anteriores e posteriores ao limite.

Os testes utilizam `where:` para execução parametrizada.

Exemplo:

```groovy
where:
competencia || ano  | mes
"Jan/2016"  || 2016 | 1
"Abr/2016"  || 2016 | 4
"Set/2020"  || 2020 | 9
"dez/2024"  || 2024 | 12
"set/2026"  || 2026 | 9
```

### `CsvServiceSpec`

Valida:

- criação do arquivo;
- existência do arquivo;
- conteúdo não vazio;
- cabeçalho do CSV;
- registros gravados;
- quantidade de linhas.

O teste utiliza um arquivo temporário para não alterar o arquivo real do projeto.

## Como executar

### Pré-requisitos

- Java 21
- acesso à internet

Verifique a versão do Java:

```bash
java -version
```

### Executar o projeto

Na raiz do projeto:

```bash
./gradlew clean run
```

Ao final da execução, serão gerados os arquivos:

```text
Downloads/Arquivos_padrao_TISS/componente_comunicacao.zip
Downloads/Tabelas_relacionadas/tabela_erros_ans.xlsx
dados/historico_tiss.csv
```

### Executar os testes

```bash
./gradlew clean test
```

O relatório dos testes pode ser encontrado em:

```text
build/reports/tests/test/index.html
```

## Bibliotecas principais

### HTTPBuilder-NG

Utilizado para realizar as requisições HTTP necessárias para navegação e download dos dados.

### Jsoup

Utilizado para:

- interpretar HTML;
- navegar pela estrutura DOM;
- localizar links;
- localizar tabelas;
- localizar títulos e componentes;
- extrair dados das páginas.

### Spock Framework

Utilizado para desenvolvimento dos testes automatizados.

## Requisitos atendidos

- [x] Crawler desenvolvido em Groovy
- [x] Uso de HTTPBuilder-NG
- [x] Uso de Jsoup
- [x] Uso de Regex
- [x] Identificação automática da versão mais recente do TISS
- [x] Download do Componente de Comunicação
- [x] Coleta do histórico do TISS
- [x] Filtro a partir de Jan/2016
- [x] Exportação dos dados para CSV
- [x] Download da Tabela de erros no envio para a ANS
- [x] Organização dos arquivos em diretórios
- [x] Tratamento básico de exceções
- [x] Testes automatizados com Spock
- [x] Arquivos baixados mantidos no projeto

## Fonte dos dados

Os dados utilizados pelo projeto são públicos e disponibilizados pela Agência Nacional de Saúde Suplementar — ANS no portal do Governo Federal.

Página principal:

```text
https://www.gov.br/ans/pt-br
```

## Autor

**Nelson Lima da Costa Junior**

Projeto desenvolvido como desafio prático envolvendo automação, Web Crawling, Web Scraping, Groovy, HTTPBuilder-NG, Jsoup, Regex, manipulação de arquivos e testes automatizados.
