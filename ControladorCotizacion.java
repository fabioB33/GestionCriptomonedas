package org.example.controlador;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ControladorCotizacion {
    private static final String API_URL = "https://api.coingecko.com/api/v3/simple/price";
    private HttpClient httpClient;
    private JSONParser jsonParser;

   public ControladorCotizacion() {
       this.httpClient = HttpClient.newHttpClient();
       this.jsonParser = new JSONParser();
    }

    public BigDecimal obtenerPrecio(String criptomoneda) {
        try {
          String url = API_URL + "?ids=" + criptomoneda + "&vs_currencies=usd";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = (JSONObject) jsonParser.parse(response.body());
               JSONObject criptoData = (JSONObject) jsonResponse.get(criptomoneda);

                if (criptoData != null) {
                    Number precio = (Number) criptoData.get("usd");
                    return new BigDecimal(precio.toString());
                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener cotización: " + e.getMessage());
        }

        return null;
    }
}