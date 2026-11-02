package br.com.fiap.techchallenge.integration;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.persistence.repository.TipoUsuarioRepository;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe base dos testes de integração.
 * - @SpringBootTest sobe a aplicação inteira (controllers, casos de uso, gateways e banco H2)
 * - @Transactional desfaz tudo o que o teste gravou no banco no final de cada teste
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    // os tipos padrão são criados pela classe CargaInicialDados quando a aplicação sobe
    protected Long idTipoDono() {
        return tipoUsuarioRepository.findByNomeIgnoreCase(TipoUsuario.DONO_DE_RESTAURANTE).orElseThrow().getId();
    }

    protected Long idTipoCliente() {
        return tipoUsuarioRepository.findByNomeIgnoreCase(TipoUsuario.CLIENTE).orElseThrow().getId();
    }

    protected Long criarUsuario(String nome, String email, Long tipoUsuarioId) throws Exception {
        String json = """
                {
                  "nome": "%s",
                  "email": "%s",
                  "login": "login.teste",
                  "tipoUsuarioId": %d
                }
                """.formatted(nome, email, tipoUsuarioId);
        return postEPegarId("/usuarios", json);
    }

    protected Long criarRestaurante(String nome, Long donoId) throws Exception {
        String json = """
                {
                  "nome": "%s",
                  "endereco": "Rua das Flores, 123",
                  "tipoCozinha": "Italiana",
                  "horarioFuncionamento": "Terça a Domingo, das 11h às 23h",
                  "donoId": %d
                }
                """.formatted(nome, donoId);
        return postEPegarId("/restaurantes", json);
    }

    protected Long criarItem(String nome, Long restauranteId) throws Exception {
        String json = """
                {
                  "nome": "%s",
                  "descricao": "Prato da casa",
                  "preco": 39.90,
                  "disponivelApenasNoLocal": false,
                  "caminhoFoto": "/imagens/prato.jpg",
                  "restauranteId": %d
                }
                """.formatted(nome, restauranteId);
        return postEPegarId("/itens-cardapio", json);
    }

    protected Long postEPegarId(String url, String json) throws Exception {
        MvcResult resultado = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        Number id = JsonPath.read(resultado.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
