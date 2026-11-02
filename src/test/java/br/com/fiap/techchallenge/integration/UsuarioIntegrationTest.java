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

class UsuarioIntegrationTest extends IntegrationTestBase {

    @Test
    void deveFazerOCrudCompletoDeUsuario() throws Exception {
        Long id = criarUsuario("Ana Lima", "ana@email.com", idTipoCliente());

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Lima"))
                .andExpect(jsonPath("$.tipoUsuario.nome").value("Cliente"));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana Lima Souza",
                                  "email": "ana.souza@email.com",
                                  "login": "ana.souza",
                                  "tipoUsuarioId": %d
                                }
                                """.formatted(idTipoCliente())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Lima Souza"))
                .andExpect(jsonPath("$.email").value("ana.souza@email.com"));

        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAssociarUsuarioAOutroTipo() throws Exception {
        Long id = criarUsuario("Bruno", "bruno@email.com", idTipoCliente());

        mockMvc.perform(patch("/usuarios/{id}/tipo", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "tipoUsuarioId": %d }
                                """.formatted(idTipoDono())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoUsuario.nome").value("Dono de Restaurante"));
    }

    @Test
    void deveRetornar400ComCamposInvalidos() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "", "email": "email-errado", "login": "x" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes.length()").value(3));
    }

    @Test
    void deveRetornar400ComJsonMalFormatado() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ isso não é json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O corpo da requisição está inválido ou mal formatado"));
    }

    @Test
    void deveRetornar404QuandoTipoNaoExiste() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "Carla", "email": "carla@email.com", "login": "carla", "tipoUsuarioId": 9999 }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar422QuandoEmailJaExiste() throws Exception {
        criarUsuario("Daniel", "daniel@email.com", idTipoCliente());

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "Outro Daniel", "email": "daniel@email.com", "login": "dani", "tipoUsuarioId": %d }
                                """.formatted(idTipoCliente())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void naoDeveExcluirUsuarioQueEhDonoDeRestaurante() throws Exception {
        Long donoId = criarUsuario("Eva", "eva@email.com", idTipoDono());
        criarRestaurante("Restaurante da Eva", donoId);

        mockMvc.perform(delete("/usuarios/{id}", donoId))
                .andExpect(status().isUnprocessableEntity());
    }
}
