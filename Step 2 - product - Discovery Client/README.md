# o-projeto-dev-fullstack

Modulos desenvolvidos ao longo do curso O Projeto Dev

#### Características
<li>Criaçao via Spring Initializr</li>
<li>Java 17</li>
<li>artifact: product</li>
<li>com.brunosalata.fullstackproject.product</li>
<li>Packaging - Jar</li>
<li>Gerenciador de dependências - Gradle-Groovy</li>
<li>Adição das Dependências 'Eureka Discovery Client' e 'Spring Web'</li>

#### Ferramentas
<li>Gradle Plugin para o VSCode - Acesso às ferramentas Gradle</li>
<li>Spring Tool Plugin para o VSCode - Dashboard de Apps, Beans e Endpoints</li>

</br>

### 2 - Product
<p>Discovery Client (Eureka) - API que identifica o microserviço ao Discovery Server</p>
<p>CRUD responsável pelo gerenciamento de produtos.</p>

### Criação

<p><b>Configurações iniciais</b></p>
<p>Após criado o novo projeto pelo Spring Initializr, foram realizados os seguintes passos:
<li>Inclusão da Anotação @EnableDiscoveryClient na classe principal, indicando ser um cliente disponível ao Discovery Server (Eureka)</li>
<li>no arquivo application.properties, incluir as seguintes configurações:</li>
<p>
<ol>spring.application.name=product-service</ol>
<ol>eureka.instance.hostname=localhost</ol>
<ol>server.port=8081</ol>
</p>
</p>

<br>

<p>
Com o Discovery Server ativo, é possível iniciar o ProductApplication e observer seu surgimento no painel do localhost:8761.
</p>
<br>

### Classes Controller

<p>
No diretório product, criamos um pacote 'controller', que armazena todas as classes Controller do microserviço.</P>

<p><b>Classe ProductController</b></p>
<p>Em seguida, criamo a classe ProductController, responsável por retornar uma lista de produtos.
</p>

<p>
Rest controller, que retorna métodos REST, método web que determina comunicação entre servidores, que compreende os métodos GET, POST, PUT e DELETE.
</p>

<p><b>Configurações da classe ProductController</b></p>
<p>
<li>Inclusão da Anotação @RestController e @RequestMapping("/product") na classe principal, para sinalizar que ele vai implementar esse serviço REST e indicar o Endpoint para todos os produtos gerados com a anotação @GetMapping, aos quais é responsável pelo gerenciamento.</li>
<li>Inserção do método ImmutableMap '<'Long, Sring> findAll(), com a anotação @GetMapping. Nele, foram inseridos 4 itens, apenas para verificar funcionamento.<br><br>
<ol>    @GetMapping<br>
    public ImmutableMap<Long, String> findAll() {<br>
        return ImmutableMap.of(<br>
                1L, "Produto 1",<br>
                2L, "Produto 2",<br>
                3L, "Produto 3",<br>
                4L, "Produto 4");<br>
    }</ol><br>
</li>
</P>

<p>Esse método acessa a porta localhost:8081, porta definida para o serviço de Product, e aplica o método GET, trazendo todos os itens presentes.</P>
<p>Com o Discovery Server ativo e a classe ProductController configurada, iniciamos o serviço e acessamos o painel do Discovery, em 'localhost:8761'. Ao clicarmos na URL do serviço, ou indo direto para 'localhost:8081/product' é possível verificar se deu certo, pois é para o browser retornar os itens mapeados.</P>
<br>
