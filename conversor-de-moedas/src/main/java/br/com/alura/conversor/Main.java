package br.com.alura.conversor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final ExchangeRateClient CLIENT = new ExchangeRateClient();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss 'UTC'")
            .withZone(ZoneId.of("UTC"));
    private static final DecimalFormat DF = new DecimalFormat("#,##0.####");

    public static void main(String[] args) {
        System.out.println("Seja bem-vindo(a) ao conversor de moeda!");
        System.out.println();

        while (true) {
            int opc = menu();
            try {
                switch (opc) {
                    case 1 -> executarConversaoFixa("USD", "BRL");
                    case 2 -> executarConversaoFixa("BRL", "USD");
                    case 3 -> executarConversaoFixa("EUR", "BRL");
                    case 4 -> executarConversaoFixa("BRL", "EUR");
                    case 5 -> executarConversaoFixa("USD", "EUR");
                    case 6 -> executarConversaoFixa("EUR", "USD");
                    case 7 -> executarConversaoLivre();
                    case 8 -> listarMoedas();
                    case 0 -> {
                        System.out.println("Até a próxima!");
                        return;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static int menu() {
        System.out.println("Escolha uma opção:");
        System.out.println(" 1) USD -> BRL");
        System.out.println(" 2) BRL -> USD");
        System.out.println(" 3) EUR -> BRL");
        System.out.println(" 4) BRL -> EUR");
        System.out.println(" 5) USD -> EUR");
        System.out.println(" 6) EUR -> USD");
        System.out.println(" 7) Converter QUALQUER par");
        System.out.println(" 8) Listar códigos de moedas suportados");
        System.out.println(" 0) Sair");
        System.out.print("Opção: ");
        String linha = SC.nextLine().trim();
        try {
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void executarConversaoFixa(String from, String to) throws Exception {
        BigDecimal valor = lerValor("Informe o valor em " + from + ": ");
        converterEExibir(from, to, valor);
    }

    private static void executarConversaoLivre() throws Exception {
        String from = lerCodigo("De (ex.: USD, BRL, EUR): ");
        String to   = lerCodigo("Para (ex.: BRL, USD, EUR): ");
        BigDecimal valor = lerValor("Valor em " + from + ": ");
        converterEExibir(from, to, valor);
    }

    private static void converterEExibir(String from, String to, BigDecimal valor) throws Exception {
        validarCodigo(from);
        validarCodigo(to);

        ExchangeRateClient.ConversionResult r = CLIENT.convert(from, to, valor);

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("Conversão: " + from + " -> " + to);
        System.out.println("Taxa atual (" + from + "/" + to + "): " + DF.format(r.rate));
        System.out.println(DF.format(r.amount) + " " + from + " = " + DF.format(r.converted) + " " + to);
        System.out.println("Última atualização das taxas: " + FMT.format(r.lastUpdate));
        System.out.println("--------------------------------------------------");
        if (!CLIENT.usingApiKey()) {
            System.out.println("(Fonte: Open Access Endpoint — atribuição necessária)");
        }
    }

    private static void listarMoedas() throws Exception {
        System.out.println("Carregando lista de moedas suportadas...");
        Set<String> codes = CLIENT.listSupportedCodes();
        int count = 0;
        for (String c : codes) {
            System.out.print(c + " ");
            if (++count % 15 == 0) System.out.println();
            if (count >= 60) break;
        }
        System.out.println();
        System.out.println("(Total conhecido: " + codes.size() + " — mostrando as primeiras 60)");
    }

    private static BigDecimal lerValor(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SC.nextLine().trim().replace(",", ".");
            try {
                BigDecimal v = new BigDecimal(s);
                if (v.signum() < 0) {
                    System.out.println("Digite um valor >= 0.");
                } else {
                    return v;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, tente novamente.");
            }
        }
    }

    private static String lerCodigo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String code = SC.nextLine().trim().toUpperCase(Locale.ROOT);
            if (code.matches("^[A-Z]{3}$")) return code;
            System.out.println("Código inválido. Use 3 letras (ex.: USD, BRL, EUR).");
        }
    }

    private static void validarCodigo(String code) {
        if (!code.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Código de moeda inválido: " + code);
        }
    }
}
