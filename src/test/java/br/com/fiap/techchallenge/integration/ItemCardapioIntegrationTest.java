package br.com.fiap.techchallenge.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemCardapioIntegrationTest extends IntegrationTestBase {

    private Long restauranteId;

    @BeforeEach
    void criarRestauranteParaOsTestes() throws Exception {
        Long donoId = criarUsuario("Juliana", "juliana@email.com", idTipoDono());
        restauranteId = criarRestaurante("Cozinha da Ju", donoId);
    }

    @Test
    void deveFazerOCrudCompletoDeItemDoCardapio() throws Exception {
        Long id = criarItem("Feijoada", restauranteId);

        mockMvc.perform(get("/itens-cardapio/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Feijoada"))
                .andExpect(jsonPath("$.preco").value(39.90))
                .andExpect(jsonPath("$.disponivelApenasNoLocal").value(false))
                .andExpect(jsonPath("$.caminhoFoto").value("/imagens/prato.jpg"))
                .andExpect(jsonPath("$.restauranteId").value(restauranteId))
                .andExpect(jsonPath("$.restauranteNome").value("Cozinha da Ju"));

        mockMvc.perform(put("/itens-cardapio/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Feijoada completa",
                                  "descricao": "Serve 2 pessoas",
                                  "preco": 89.90,
                                  "disponivelApenasNoLocal": true,
                                  "caminhoFoto": "/imagens/feijoada.jpg",
                                  "restauranteId": %d
                                }
                                """.formatted(restauranteId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Feijoada completa"))
                .andExpect(jsonPath("$.disponivelApenasNoLocal").value(true));

        mockMvc.perform(delete("/itens-cardapio/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/itens-cardapio/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTodosOsItensEFiltrarPorRestaurante() throws Exception {
        criarItem("Pastel", restauranteId);
        criarItem("Coxinha", restauranteId);

        Long outroDono = criarUsuario("Karina", "karina@email.com", idTipoDono());
        Long outroRestaurante = criarRestaurante("Lanches da Ka", outroDono);
        criarItem("X-Burguer", outroRestaurante);

        mockMvc.perform(get("/itens-cardapio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        mockMvc.perform(get("/itens-cardapio").param("restauranteId", restauranteId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveRetornar404AoListarItensDeRestauranteInexistente() throws Exception {
        mockMvc.perform(get("/itens-cardapio").param("restauranteId", "9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400ComPrecoNegativo() throws Exception {
        mockMvc.perform(post("/itens-cardapio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Suco",
                                  "descricao": "Suco natural",
                                  "preco": -5,
                                  "disponivelApenasNoLocal": false,
                                  "restauranteId": %d
                                }
                                """.formatted(restauranteId)))
                .andExpect(status().isBadRequest());
    }
}
