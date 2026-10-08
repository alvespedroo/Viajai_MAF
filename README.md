# Viajaí ✈️
### Aplicativo de Planejamento e Organização de Viagens

## 1. Sobre o projeto

O **Viajaí** é um aplicativo Android desenvolvido para facilitar o planejamento e a organização de viagens, reunindo diferentes funcionalidades em uma única plataforma.

O aplicativo permite organizar viagens, controlar despesas, gerenciar checklists, explorar destinos turísticos, consultar avaliações e salvar lugares de interesse.

O projeto foi desenvolvido como parte do **Trabalho 2 – MAF (Mínimo Aplicativo Funcional)**, dando continuidade ao Trabalho 1, no qual foram desenvolvidas as primeiras interfaces do aplicativo.

## 2. Integrantes

- Pedro Henrique Alves.
- Eduardo Fogaça Bader.

**Instituição:** Universidade Positivo  
**Disciplina:** Desenvolvimento de Aplicativos Moveis  
**Professor:** André Luis

## 3. Tecnologias utilizadas

- **Kotlin:** linguagem de programação utilizada no desenvolvimento.
- **Android Studio:** ambiente de desenvolvimento.
- **Jetpack Compose:** construção das interfaces.
- **Material Design 3:** componentes visuais.
- **Navigation Compose:** gerenciamento da navegação entre telas.
- **MutableStateListOf:** gerenciamento de listas dinâmicas e reativas.
- **LazyColumn e Card:** apresentação de informações em listas.

## 4. Funcionalidades implementadas

### Planejamento de viagens
- Criação e gerenciamento de viagens.
- Definição de orçamento individual.
- Visualização de viagens em planejamento.
- Conclusão de viagens.
- Consulta ao histórico de viagens concluídas.

### Checklist
- Organização de checklists por viagem.
- Criação, edição e exclusão de blocos e itens.
- Marcação de tarefas concluídas.
- Atualização do progresso conforme os itens são marcados.

### Controle de gastos
- Registro e exclusão de despesas.
- Visualização do total gasto.
- Organização das despesas por viagem.
- Acompanhamento do orçamento.

### Explorar destinos
- Pesquisa e exploração de lugares.
- Filtros por cidades, hotéis/pousadas, restaurantes e passeios.
- Visualização de imagens e informações dos locais.
- Possibilidade de salvar lugares para consultar posteriormente.

### Avaliações
- Visualização de avaliações de lugares.
- Avaliação utilizando estrelas.
- Suporte a notas fracionadas, como 3,5 e 4,5.
- Interação com avaliações por meio de curtidas.

### Perfil
- Visualização de lugares salvos.
- Organização dos favoritos por categoria.
- Acesso aos detalhes dos lugares.
- Histórico de viagens concluídas.

## 5. Evolução do Trabalho 1 para o Trabalho 2

No Trabalho 1, o objetivo principal era desenvolver três interfaces utilizando Jetpack Compose. As telas apresentavam elementos visuais e botões, mas ainda não exigiam navegação funcional nem gerenciamento dinâmico de dados.

No Trabalho 2, o aplicativo evoluiu para um Mínimo Aplicativo Funcional (MAF), incorporando:

- Navegação entre diferentes telas.
- Componentes interativos.
- Listas dinâmicas.
- Adição, edição e exclusão de informações.
- Telas de detalhes.
- Gerenciamento de estados.
- Integração entre funcionalidades do aplicativo.

Essa evolução permitiu transformar o protótipo inicial em um aplicativo com recursos funcionais.

## 6. Organização e decisões técnicas

A navegação do aplicativo foi centralizada no arquivo `AppNavigation.kt`, utilizando `NavHost` e rotas para controlar a troca de telas.

As informações são organizadas em classes de dados (`data class`), facilitando a representação de viagens, despesas, bagagens e itens de preparação.

As listas utilizam recursos de gerenciamento de estado do Jetpack Compose para atualizar a interface quando os dados são modificados.

Também foi adotada uma identidade visual baseada em tons de laranja, vermelho e branco, buscando manter consistência entre as diferentes telas.

**Limitação atual:** os dados são mantidos durante a execução do aplicativo. A persistência local permanente poderá ser implementada em versões futuras.

## 7. Dificuldades encontradas e soluções

Durante o desenvolvimento, o grupo encontrou desafios relacionados à navegação, atualização de estados e organização dos dados.

**Navegação duplicada:** algumas telas apresentavam mais de uma barra de navegação. A estrutura foi reorganizada para centralizar a navegação.

**Rotas incorretas:** determinados botões direcionavam para telas diferentes das esperadas. Os destinos das ações foram revisados.

**IDs duplicados:** a utilização de chaves repetidas em listas dinâmicas provocou falhas na execução. Foi necessário revisar a identificação dos elementos.

**Gerenciamento dos dados:** o aplicativo precisou separar informações de viagens diferentes, evitando que checklists, despesas e outros registros fossem misturados.

**Responsividade:** as interfaces foram ajustadas para melhorar a visualização e a rolagem em dispositivos móveis.

Essas dificuldades contribuíram para o aprendizado sobre organização de código, gerenciamento de estados e funcionamento do Jetpack Compose.

## 8. Como executar o projeto

1. Instale o Android Studio.
2. Clone ou baixe este repositório.
3. Abra a pasta do projeto no Android Studio.
4. Aguarde a sincronização do Gradle.
5. Configure um emulador Android ou conecte um dispositivo físico.
6. Clique em **Run** para compilar e executar o aplicativo.

O projeto utiliza os recursos de imagem armazenados em `app/src/main/res/drawable/`.

## 9. Demonstração do aplicativo

Foi gravado um vídeo demonstrando o funcionamento do Viajaí no Android Studio, incluindo a navegação entre telas e as principais funcionalidades implementadas.

**Vídeo de demonstração:** [Clique aqui para assistir]([https://drive.google.com/file/d/1UPS2vzQmp-jHV7DxdlIPvMjzZiu8-IWq/view?usp=drive_link])

## 10. Considerações finais

O desenvolvimento do Viajaí permitiu aplicar conceitos de programação Android, Jetpack Compose, navegação, gerenciamento de estados e organização de dados.
A evolução do Trabalho 1 para o Trabalho 2 proporcionou uma experiência prática na transformação de interfaces estáticas em um aplicativo funcional.
O projeto também possibilitou compreender a importância do planejamento, dos testes e da resolução de problemas durante o desenvolvimento de software.

## 11. Recursos visuais e imagens

O Viajaí utiliza imagens locais para apresentar destinos turísticos, restaurantes, hospedagens, passeios e avaliações.
Todas as imagens necessárias para a execução do aplicativo estão armazenadas no próprio projeto, no diretório:

`app/src/main/res/drawable/`

As imagens são acessadas pelo Jetpack Compose por meio de referências como `R.drawable.nome_da_imagem`.
**Não é necessário baixar ou configurar as imagens separadamente**, pois elas estão incluídas no repositório GitHub.
Para executar o aplicativo corretamente, basta clonar o repositório completo, abrir o projeto no Android Studio, aguardar a sincronização do Gradle e iniciar a execução.

**Observação:** caso os arquivos sejam transferidos manualmente para outro projeto, é necessário manter as imagens na pasta `res/drawable`, preservando os nomes utilizados no código.
