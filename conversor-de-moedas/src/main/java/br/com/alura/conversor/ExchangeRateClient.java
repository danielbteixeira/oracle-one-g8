package br.com.alura.conversor;


import com.google.gson.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Cliente para consumir a ExchangeRate-API e fornecer conversões de moedas.
 */
public class ExchangeRateClient {

    private static final String KEY_ENV = "EXCHANGE_RATE_API_KEY";
    private static final String KEYED_BASE = "https://v6.exchangerate-api.com/v6";
    private static final String OPEN_BASE  = "https://open.er-api.com/v6";

    private final HttpClient http = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String apiKey;

    public ExchangeRateClient() {
        this.apiKey = System.getenv(KEY_ENV);
    }

    public boolean usingApiKey() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * Desserializa a resposta da API com Gson para conversão de par de moedas
     */
    public static class PairResponse {
        public String result;
        public BigDecimal conversion_rate;
        public BigDecimal conversion_result;
        public long time_last_update_unix;
    }

    /**
     * Converte um valor entre duas moedas usando a API
     */
    public ConversionResult convert(String from, String to, BigDecimal amount) throws IOException, InterruptedException {
        from = from.toUpperCase(Locale.ROOT);
        to   = to.toUpperCase(Locale.ROOT);

        if (usingApiKey()) {
            // Endpoint "pair" com chave
            String url = String.format("%s/%s/pair/%s/%s/%s", KEYED_BASE, apiKey, from, to, amount.toPlainString());
            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());

            // Desserializa JSON em objeto Java
            PairResponse r = gson.fromJson(resp.body(), PairResponse.class);

            if (!"success".equalsIgnoreCase(r.result)) {
                throw new IOException("API error: conversão falhou");
            }

            return new ConversionResult(from, to, amount, r.conversion_rate, r.conversion_result, Instant.ofEpochSecond(r.time_last_update_unix));
        } else {
            // Open Access (sem chave)
            String url = String.format("%s/latest/%s", OPEN_BASE, from);
            JsonObject json = getJson(url);
            String result = json.get("result").getAsString();
            if (!"success".equalsIgnoreCase(result)) {
                throw new IOException("API error: " + json.get("error-type").getAsString());
            }
            JsonObject rates = json.getAsJsonObject("rates");
            if (rates == null || !rates.has(to)) {
                throw new IOException("Taxa " + from + "->" + to + " não encontrada.");
            }
            BigDecimal rate = rates.get(to).getAsBigDecimal();
            BigDecimal conv = rate.multiply(amount);
            long lastUpdate = json.get("time_last_update_unix").getAsLong();
            return new ConversionResult(from, to, amount, rate, conv, Instant.ofEpochSecond(lastUpdate));
        }
    }

    /**
     * Lista de códigos suportados pela API
     */
    public Set<String> listSupportedCodes() throws IOException, InterruptedException {
        if (usingApiKey()) {
            String url = String.format("%s/%s/codes", KEYED_BASE, apiKey);
            JsonObject json = getJson(url);
            if (!"success".equalsIgnoreCase(json.get("result").getAsString())) {
                throw new IOException("Falha ao obter códigos suportados.");
            }
            return json.getAsJsonArray("supported_codes")
                    .asList()
                    .stream()
                    .map(el -> el.getAsJsonArray().get(0).getAsString())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(String::compareTo)));
        } else {
            String url = String.format("%s/latest/%s", OPEN_BASE, "USD");
            JsonObject json = getJson(url);
            if (!"success".equalsIgnoreCase(json.get("result").getAsString())) {
                throw new IOException("Falha ao obter moedas (Open Access).");
            }
            return json.getAsJsonObject("rates").entrySet().stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toCollection(() -> new TreeSet<>(String::compareTo)));
        }
    }

    /**
     * Método auxiliar para requisições HTTP Open Access
     */
    private JsonObject getJson(String url) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
            throw new IOException("HTTP " + resp.statusCode() + " ao acessar " + url);
        }
        return JsonParser.parseString(resp.body()).getAsJsonObject();
    }

    /**
     * Classe interna para armazenar resultado da conversão
     */
    public static class ConversionResult {
        public final String from, to;
        public final BigDecimal amount, rate, converted;
        public final Instant lastUpdate;

        public ConversionResult(String from, String to, BigDecimal amount, BigDecimal rate, BigDecimal converted, Instant lastUpdate) {
            this.from = from;
            this.to = to;
            this.amount = amount;
            this.rate = rate;
            this.converted = converted;
            this.lastUpdate = lastUpdate;
        }
    }
}
