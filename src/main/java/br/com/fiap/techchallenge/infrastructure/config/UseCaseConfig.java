package br.com.fiap.techchallenge.infrastructure.config;

import br.com.fiap.techchallenge.application.usecase.ItemCardapioUseCase;
import br.com.fiap.techchallenge.application.usecase.RestauranteUseCase;
import br.com.fiap.techchallenge.application.usecase.TipoUsuarioUseCase;
import br.com.fiap.techchallenge.application.usecase.UsuarioUseCase;
import br.com.fiap.techchallenge.domain.gateway.ItemCardapioGateway;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;
import br.com.fiap.techchallenge.domain.gateway.TipoUsuarioGateway;
import br.com.fiap.techchallenge.domain.gateway.UsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UseCaseConfig {

    @Bean
    public TipoUsuarioUseCase tipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway,
                                                 UsuarioGateway usuarioGateway) {
        return new TipoUsuarioUseCase(tipoUsuarioGateway, usuarioGateway);
    }

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway,
                                         TipoUsuarioGateway tipoUsuarioGateway,
                                         RestauranteGateway restauranteGateway) {
        return new UsuarioUseCase(usuarioGateway, tipoUsuarioGateway, restauranteGateway);
    }

    @Bean
    public RestauranteUseCase restauranteUseCase(RestauranteGateway restauranteGateway,
                                                 UsuarioGateway usuarioGateway,
                                                 ItemCardapioGateway itemCardapioGateway) {
        return new RestauranteUseCase(restauranteGateway, usuarioGateway, itemCardapioGateway);
    }

    @Bean
    public ItemCardapioUseCase itemCardapioUseCase(ItemCardapioGateway itemCardapioGateway,
                                                   RestauranteGateway restauranteGateway) {
        return new ItemCardapioUseCase(itemCardapioGateway, restauranteGateway);
    }
}
