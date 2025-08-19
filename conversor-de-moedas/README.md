# Conversor de Moeda

O projeto Conversor de Moeda consiste em um sistema de conversão de moeda que que interage via console com o usuário.
Ele utiliza a ExchangeRate-API para obter taxas de câmbio em tempo real e desserializa a resposta JSON usando Gson.

Este projeto foi desenvolvido como parte do desafio "Conversor de Moedas" da formação Backend do programa ONE (Oracle Next Education).

## Funcionalidades
- Diversas opções de conversão de moeda. 
- Interação textual via console com os usuários.
- Taxas de conversão dinâmicas obtidas através da API ExchangeRate-API.
- Exibição da taxa de câmbio, valor convertido e hora da última atualização.
- Listagem das moedas suportadas pela API.

## Tecnologias
- Java 17+
- Maven (gerenciamento de dependências)
- Gson (desserialização JSON)
- ExchangeRate-API (consulta de taxas em tempo real)