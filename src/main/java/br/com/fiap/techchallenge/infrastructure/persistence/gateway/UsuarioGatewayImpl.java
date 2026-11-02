package br.com.fiap.techchallenge.infrastructure.persistence.gateway;

import br.com.fiap.techchallenge.domain.entities.Usuario;
import br.com.fiap.techchallenge.domain.gateway.UsuarioGateway;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.UsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.persistence.mapper.UsuarioMapper;
import br.com.fiap.techchallenge.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioRepository repository;

    public UsuarioGatewayImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity salvo = repository.save(UsuarioMapper.toEntity(usuario));
        return UsuarioMapper.toDomain(salvo);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(UsuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll().stream().map(UsuarioMapper::toDomain).toList();
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existeUsuarioComTipo(Long tipoUsuarioId) {
        return repository.existsByTipoUsuario_Id(tipoUsuarioId);
    }
}
