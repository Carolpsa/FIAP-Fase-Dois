# Roteiro do vídeo (~5 minutos)

Sugestão de ordem para gravar a apresentação. Deixe o Docker, o VS Code e o Postman abertos antes de começar.

| Tempo | O que mostrar | O que falar |
|---|---|---|
| 0:00 – 0:30 | Tela do README | Apresentação: quem sou, objetivo da Fase 2 (tipos de usuário, restaurantes e cardápio). |
| 0:30 – 1:30 | Estrutura de pastas no VS Code | Explicar as camadas `domain`, `application` e `infrastructure` e que o domínio não depende do Spring. Mostrar rapidamente a classe `Restaurante` validando o dono. |
| 1:30 – 2:00 | Terminal com `docker compose up --build` | Subir banco + aplicação com um comando. Mostrar o Swagger em `localhost:8080/swagger-ui.html`. |
| 2:00 – 3:30 | Postman (Collection Runner ou passo a passo) | Listar tipos padrão → cadastrar tipo → cadastrar usuário dono e cliente → associar tipo (PATCH) → cadastrar restaurante → cadastrar item → listar itens do restaurante. |
| 3:30 – 4:15 | Pasta "Cenários de erro" no Postman | Restaurante com dono Cliente (422), excluir restaurante com itens (422), preço negativo (400), id inexistente (404). |
| 4:15 – 5:00 | Terminal com `mvn clean verify` + relatório JaCoCo | Mostrar os testes passando, o relatório `target/site/jacoco/index.html` com a cobertura acima de 80% e comentar sobre os testes de integração com H2. |

Dicas:
- Rodar a collection uma vez antes de gravar para garantir que está tudo funcionando.
- Usar `docker compose down -v` antes de gravar para começar com o banco vazio.
