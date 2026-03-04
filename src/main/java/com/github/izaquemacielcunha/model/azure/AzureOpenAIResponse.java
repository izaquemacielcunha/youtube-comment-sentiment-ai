package com.github.izaquemacielcunha.model.azure;

import java.util.List;

public record AzureOpenAIResponse(
        String sentimentoGeral,
        double scoreConfianca,
        String resumoQualitativo,
        List<String> pontosPositivosFrequentes,
        List<String> pontosNegativosFrequentes
) { }