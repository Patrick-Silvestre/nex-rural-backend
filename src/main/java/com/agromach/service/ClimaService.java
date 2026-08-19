package com.agromach.service;

import com.agromach.dto.ClimaDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Servico de clima. Consulta a previsao do tempo via Open-Meteo (gratuito, sem chave de API)
 * a partir da latitude/longitude cadastrada na fazenda. Usado pelo dashboard operacional.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClimaService {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    private final RestClient.Builder restClientBuilder;

    public ClimaDto.ClimaResponse obterClima(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }

        try {
            // Monta a URI via builder (nao concatenando string) para o Spring codificar uma unica vez;
            // "America/Sao_Paulo" concatenado manualmente como %2F acaba sendo re-codificado para %252F.
            Map<String, Object> body = restClientBuilder.build()
                .get()
                .uri(BASE_URL + "?latitude={latitude}&longitude={longitude}"
                        + "&current=temperature_2m,precipitation,weather_code,wind_speed_10m"
                        + "&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max"
                        + "&timezone={timezone}&forecast_days=4",
                    latitude, longitude, "America/Sao_Paulo")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });

            return mapResponse(body);
        } catch (Exception ex) {
            // Clima e um complemento do dashboard, nao pode derrubar o restante da tela se o provedor falhar.
            log.warn("Falha ao consultar previsao do tempo (lat={}, lon={}): {}", latitude, longitude, ex.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private ClimaDto.ClimaResponse mapResponse(Map<String, Object> body) {
        if (body == null) {
            return null;
        }

        Map<String, Object> current = (Map<String, Object>) body.get("current");
        ClimaDto.ClimaAtual atual = current == null ? null : new ClimaDto.ClimaAtual(
            toDouble(current.get("temperature_2m")),
            toDouble(current.get("precipitation")),
            toDouble(current.get("wind_speed_10m")),
            descreverCondicao(toInteger(current.get("weather_code")))
        );

        Map<String, Object> daily = (Map<String, Object>) body.get("daily");
        List<ClimaDto.PrevisaoDia> previsao = new ArrayList<>();

        if (daily != null) {
            List<String> datas = (List<String>) daily.get("time");
            List<Object> maximas = (List<Object>) daily.get("temperature_2m_max");
            List<Object> minimas = (List<Object>) daily.get("temperature_2m_min");
            List<Object> probChuva = (List<Object>) daily.get("precipitation_probability_max");

            if (datas != null) {
                for (int i = 0; i < datas.size(); i++) {
                    previsao.add(new ClimaDto.PrevisaoDia(
                        LocalDate.parse(datas.get(i)),
                        toDouble(valorEm(maximas, i)),
                        toDouble(valorEm(minimas, i)),
                        toInteger(valorEm(probChuva, i))
                    ));
                }
            }
        }

        return new ClimaDto.ClimaResponse(atual, previsao);
    }

    private Object valorEm(List<Object> lista, int indice) {
        return lista != null && indice < lista.size() ? lista.get(indice) : null;
    }

    private Double toDouble(Object value) {
        return value instanceof Number number ? number.doubleValue() : null;
    }

    private Integer toInteger(Object value) {
        return value instanceof Number number ? number.intValue() : null;
    }

    // Mapeamento simplificado dos codigos WMO usados pelo Open-Meteo (https://open-meteo.com/en/docs).
    private String descreverCondicao(Integer codigo) {
        if (codigo == null) {
            return "Sem dado";
        }

        return switch (codigo) {
            case 0 -> "Ceu limpo";
            case 1, 2, 3 -> "Parcialmente nublado";
            case 45, 48 -> "Neblina";
            case 51, 53, 55, 56, 57 -> "Garoa";
            case 61, 63, 65, 66, 67 -> "Chuva";
            case 71, 73, 75, 77 -> "Neve";
            case 80, 81, 82 -> "Pancadas de chuva";
            case 95, 96, 99 -> "Tempestade";
            default -> "Sem dado";
        };
    }
}
