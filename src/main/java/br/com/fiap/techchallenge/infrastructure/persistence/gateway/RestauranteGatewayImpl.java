package br.com.fiap.techchallenge.infrastructure.persistence.gateway;

import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.RestauranteEntity;
import br.com.fiap.techchallenge.infrastructure.persistence.mapper.RestauranteMapper;
import br.com.fiap.techchallenge.infrastructure.persistence.repository.RestauranteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestauranteGatewayImpl implements RestauranteGateway {

    private final RestauranteRepository repository;

    public RestauranteGatewayImpl(RestauranteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurante salvar(Restaurante restaurante) {
        RestauranteEntity salvo = repository.save(RestauranteMapper.toEntity(restaurante));
        return RestauranteMapper.toDomain(salvo);
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return repository.findById(id).map(RestauranteMapper::toDomain);
    }

    @Override
    public List<Restaurante> listarTodos() {
        return repository.findAll().stream().map(RestauranteMapper::toDomain).toList();
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existeRestauranteDoDono(Long donoId) {
        return repository.existsByDono_Id(donoId);
    }
}
