package br.com.fiap.techchallenge.infrastructure.persistence.gateway;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.gateway.TipoUsuarioGateway;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.TipoUsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.persistence.mapper.TipoUsuarioMapper;
import br.com.fiap.techchallenge.infrastructure.persistence.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class TipoUsuarioGatewayImpl implements TipoUsuarioGateway {

    private final TipoUsuarioRepository repository;

    public TipoUsuarioGatewayImpl(TipoUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity salvo = repository.save(TipoUsuarioMapper.toEntity(tipoUsuario));
        return TipoUsuarioMapper.toDomain(salvo);
    }

    @Override
    public Optional<TipoUsuario> buscarPorId(Long id) {
        return repository.findById(id).map(TipoUsuarioMapper::toDomain);
    }

    @Override
    public Optional<TipoUsuario> buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome).map(TipoUsuarioMapper::toDomain);
    }

    @Override
    public List<TipoUsuario> listarTodos() {
        return repository.findAll().stream().map(TipoUsuarioMapper::toDomain).toList();
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
