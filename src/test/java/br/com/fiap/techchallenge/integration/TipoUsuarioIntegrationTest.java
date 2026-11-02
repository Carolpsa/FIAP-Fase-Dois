package br.com.fiap.techchallenge.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TipoUsuarioIntegrationTest extends IntegrationTestBase {

    @Test
    void deveListarOsTiposPadraoCriadosNaInicializacao() throws Exception {
        mockMvc.perform(get("/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nome", hasItems("Dono de Restaurante", "Cliente")));
    }

    @Test
    void deveFazerOCrudCompletoDeTipoUsuario() throws Exception {
        // cadastrar
        Long id = postEPegarId("/tipos-usuario", """
                { "nome": "Funcionário" }
                """);

        // buscar
        mockMvc.perform(get("/tipos-usuario/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Funcionário"));

        // atualizar
        mockMvc.perform(put("/tipos-usuario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "Garçom" }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Garçom"));

        // excluir
        mockMvc.perform(delete("/tipos-usuario/{id}", id))
                .andExpect(status().isNoContent());

        // depois de excluir não deve mais encontrar
        mockMvc.perform(get("/tipos-usuario/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400QuandoNomeNaoForInformado() throws Exception {
        mockMvc.perform(post("/tipos-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[0]").value("nome: O nome é obrigatório"));
    }

    @Test
    void deveRetornar422AoCadastrarNomeRepetido() throws Exception {
        mockMvc.perform(post("/tipos-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "cliente" }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void naoDeveExcluirTipoQueTemUsuarios() throws Exception {
        Long tipoId = postEPegarId("/tipos-usuario", """
                { "nome": "Entregador" }
                """);
        criarUsuario("Carlos", "carlos@email.com", tipoId);

        mockMvc.perform(delete("/tipos-usuario/{id}", tipoId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.mensagem").value("Não é possível excluir: existem usuários associados a esse tipo"));
    }

    @Test
    void deveRetornar400QuandoIdNaoForNumero() throws Exception {
        mockMvc.perform(get("/tipos-usuario/abc"))
                .andExpect(status().isBadRequest());
    }
}
