package com.github.izaquemacielcunha.model;

import java.util.List;
import java.util.stream.Collectors;

import com.github.izaquemacielcunha.model.azure.request.AzureOpenAIRequest;
import com.github.izaquemacielcunha.model.azure.request.Content;
import com.github.izaquemacielcunha.model.azure.request.Message;
import com.github.izaquemacielcunha.model.youtube.response.comment.Item;
import com.github.izaquemacielcunha.model.youtube.response.comment.YouTubeVideoCommentsResponse;
import com.github.izaquemacielcunha.model.youtube.response.video.YouTubeVideoMetadataResponse;

public class Mapper {

    public AzureOpenAIRequest mapToAzureOpenAiRequest(YouTubeVideoCommentsResponse videoCommentsResponse,
                                                      YouTubeVideoMetadataResponse videoMetadataResponse) {

        Message systemMessage = new Message("system", List.of(
                new Content("text",
                        """ 
                                Você é um sistema especializado em Processamento de Linguagem Natural (PLN) e Análise de Sentimento, projetado para atuar como o motor de uma API. Seu objetivo é analisar a reação do público a vídeos do YouTube.
                                Em cada requisição, você receberá três blocos de informação fornecidos pelo usuário:
                                [NOME]: Como o canal se chama no YouTube.
                                [TÍTULO]: O título do vídeo.
                                [THUMBNAIL]: Link para a capa do vídeo.
                                [DESCRIÇÃO]: A descrição do vídeo.
                                [TAGS]: Palavras-chave descritivas
                                [COMENTÁRIOS]: Uma lista de comentários feitos pelos espectadores.
                                SUAS DIRETRIZES DE ANÁLISE:
                                Use o Contexto: O nome, título, thumbnail, descrição e as tags são fundamentais para entender piadas internas, jargões da comunidade ou identificar se um comentário aparentemente negativo é, na verdade, sarcasmo alinhado com o humor do vídeo.
                                Filtre o Ruído: Ignore comentários que sejam puro spam (ex: "ganhe dinheiro fácil clique aqui") na hora de calcular o sentimento. Na descrição pode conter links para redes sociais, links externos ou outros, ignore essa parte da descrição.
                                Sentimento Geral: Determine se a recepção da maioria é Positiva, Negativa, Neutra ou Mista.
                                FORMATO DE SAÍDA (CRÍTICO):
                                Você deve responder EXCLUSIVAMENTE em um formato JSON válido, sem usar blocos de código Markdown (como ```json). Sua resposta deve ser apenas o objeto JSON estruturado da seguinte forma:
                                {
                                "sentimentoGeral": "Positivo|Negativo|Neutro|Misto",
                                "scoreConfianca": [Um número de 0.0 a 1.0 indicando a força desse sentimento],
                                "resumoQualitativo": "[Um parágrafo curto, de até 3 linhas, explicando o humor principal da audiência]",
                                "pontosPositivosFrequentes": ["[lista", "de", "elogios"]],
                                "pontosNegativosFrequentes": ["[lista", "de", "criticas"]]
                                }
                                """)
        ));

        Message userMessage = new Message("user", List.of(
                new Content("text", buildUserMessage(videoCommentsResponse, videoMetadataResponse))
        ));

        return new AzureOpenAIRequest(List.of(systemMessage, userMessage));
    }

    private String buildUserMessage(YouTubeVideoCommentsResponse videoCommentsResponse,
                                    YouTubeVideoMetadataResponse videoMetadataResponse) {
        return "[NOME]:\n" +
                videoMetadataResponse.items().getFirst().snippet().channelTitle() + "\n" +
                "[TÍTULO]:\n" +
                videoMetadataResponse.items().getFirst().snippet().title() + "\n" +
                "[THUMBNAIL]:\n" +
                videoMetadataResponse.items().getFirst().snippet().thumbnails().high().url() + "\n" +
                "[DESCRIÇÃO]:\n" +
                videoMetadataResponse.items().getFirst().snippet().description() + "\n" +
                "[TAGS]:\n" +
                buildTags(videoMetadataResponse.items().getFirst().snippet().tags()) + "\n" +
                "[COMENTÁRIOS]:\n" +
                buildComments(videoCommentsResponse.items());
    }

    private String buildTags(List<String> tags) {
        if (null == tags || tags.isEmpty()) {
            return "Nenhuma tag foi associada a este vídeo";
        }
        return tags.stream()
                .map(tag -> "-" + tag)
                .collect(Collectors.joining("\n"));
    }

    private String buildComments(List<Item> items) {
        return items.stream().
                filter(item -> !item.snippet().channelId().equals(
                        item.snippet().topLevelComment().snippet().authorChannelId().value()
                ))
                .map(item -> "-" + item.snippet().topLevelComment().snippet().textDisplay())
                .collect(Collectors.joining("\n"));
    }
}
