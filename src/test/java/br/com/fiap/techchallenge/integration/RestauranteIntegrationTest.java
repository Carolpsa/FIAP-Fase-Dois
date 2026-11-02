package br.com.fiap.techchallenge.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestauranteIntegrationTest extends IntegrationTestBase {

    @Test
    void deveFazerOCrudCompletoDeRestaurante() throws Exception {
        Long donoId = criarUsuario("Fernanda", "fernanda@email.com", idTipoDono());
        Long id = criarRestaurante("Sabor Caseiro", donoId);

        mockMvc.perform(get("/restaurantes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sabor Caseiro"))
                .andExpect(jsonPath("$.tipoCozinha").value("Italiana"))
                .andExpect(jsonPath("$.dono.id").value(donoId))
                .andExpect(jsonPath("$.dono.nome").value("Fernanda"));

        mockMvc.perform(get("/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(put("/restaurantes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Sabor Caseiro 2",
                                  "endereco": "Av. Brasil, 500",
                                  "tipoCozinha": "Brasileira",
                                  "horarioFuncionamento": "Segunda a Sábado, das 11h às 15h",
                                  "donoId": %d
                                }
                                """.formatted(donoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sabor Caseiro 2"))
                .andExpect(jsonPath("$.tipoCozinha").value("Brasileira"));

        mockMvc.perform(delete("/restaurantes/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/restaurantes/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDeveCadastrarRestauranteComDonoQueEhCliente() throws Exception {
        Long clienteId = criarUsuario("Gabriel", "gabriel@email.com", idTipoCliente());

        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Restaurante do Gabriel",
                                  "endereco": "Rua X, 10",
                                  "tipoCozinha": "Japonesa",
                                  "horarioFuncionamento": "Todos os dias",
                                  "donoId": %d
                                }
                                """.formatted(clienteId)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar400QuandoFaltarCampos() throws Exception {
        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes.length()").value(5));
    }

    @Test
    void naoDeveExcluirRestauranteComItensNoCardapio() throws Exception {
        Long donoId = criarUsuario("Helena", "helena@email.com", idTipoDono());
        Long restauranteId = criarRestaurante("Cantina da Helena", donoId);
        criarItem("Nhoque", restauranteId);

        mockMvc.perform(delete("/restaurantes/{id}", restauranteId))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void naoDeveDeixarDonoComRestauranteVirarCliente() throws Exception {
        Long donoId = criarUsuario("Igor", "igor@email.com", idTipoDono());
        criarRestaurante("Bar do Igor", donoId);

        mockMvc.perform(patch("/usuarios/{id}/tipo", donoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "tipoUsuarioId": %d }
                                """.formatted(idTipoCliente())))
                .andExpect(status().isUnprocessableEntity());
    }
}
