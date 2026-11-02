package br.com.fiap.techchallenge.infrastructure.persistence.gateway;

import br.com.fiap.techchallenge.domain.entities.ItemCardapio;
import br.com.fiap.techchallenge.domain.gateway.ItemCardapioGateway;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.ItemCardapioEntity;
import br.com.fiap.techchallenge.infrastructure.persistence.mapper.ItemCardapioMapper;
import br.com.fiap.techchallenge.infrastructure.persistence.repository.ItemCardapioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ItemCardapioGatewayImpl implements ItemCardapioGateway {

    private final ItemCardapioRepository repository;

    public ItemCardapioGatewayImpl(ItemCardapioRepository repository) {
        this.repository = repository;
    }

    @Override
    public ItemCardapio salvar(ItemCardapio itemCardapio) {
        ItemCardapioEntity salvo = repository.save(ItemCardapioMapper.toEntity(itemCardapio));
        return ItemCardapioMapper.toDomain(salvo);
    }

    @Override
    public Optional<ItemCardapio> buscarPorId(Long id) {
        return repository.findById(id).map(ItemCardapioMapper::toDomain);
    }

    @Override
    public List<ItemCardapio> listarTodos() {
        return repository.findAll().stream().map(ItemCardapioMapper::toDomain).toList();
    }

    @Override
    public List<ItemCardapio> listarPorRestaurante(Long restauranteId) {
        return repository.findByRestaurante_Id(restauranteId).stream().map(ItemCardapioMapper::toDomain).toList();
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existeItemNoRestaurante(Long restauranteId) {
        return repository.existsByRestaurante_Id(restauranteId);
    }
}
