package br.com.fiap.techchallenge.infrastructure.config;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.TipoUsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.persistence.repository.TipoUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CargaInicialDados implements CommandLineRunner {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public CargaInicialDados(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public void run(String... args) {
        criarTipoSeNaoExistir(TipoUsuario.DONO_DE_RESTAURANTE);
        criarTipoSeNaoExistir(TipoUsuario.CLIENTE);
    }

    private void criarTipoSeNaoExistir(String nome) {
        if (tipoUsuarioRepository.findByNomeIgnoreCase(nome).isEmpty()) {
            tipoUsuarioRepository.save(new TipoUsuarioEntity(null, nome));
        }
    }
}
