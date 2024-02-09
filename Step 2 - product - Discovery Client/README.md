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
    
    @GetMapping
    public ImmutableMap<Long, String> findAll() {
        return ImmutableMap.of(
                1L, "Produto 1",
                2L, "Produto 2",
                3L, "Produto 3",
                4L, "Produto 4");
    }
    
</li>
</P>
<br>
<p>Esse método acessa a porta localhost:8081, porta definida para o serviço de Product, e aplica o método GET, trazendo todos os itens presentes.</P>
<p>Com o Discovery Server ativo e a classe ProductController configurada, iniciamos o serviço e acessamos o painel do Discovery, em 'localhost:8761'. Ao clicarmos na URL do serviço, ou indo direto para 'localhost:8081/product' é possível verificar se deu certo, pois é para o browser retornar os itens mapeados.</P>
<br>

### Configurando o Banco de Dados e Persistência

<p>
Para incluir as funcionalidades de banco de dados e de comunicação, acessar o arquivo build.gradle e incluir as seguintes linhas, dentro de dependencias:</P>

    implementation 'org.springframework.boot:spring-boot-starter-data-jpa:3.2.2'<br>
	runtimeOnly 'org.postgresql:postgresql:42.7.1'

<br>
<p>A comunicação entre a aplicação e o banco sempre vai utilizar spring-data, independente do banco. Mas como será usado o postgres nesse projeto, a segunda linha é relacionada a ele. Fosse outro banco, a linha teria alguma variação.</p>

<p>Após isso, acessar o arquivo application.properties. Esse arquivo pode ser configurado como .properties ou como .yml. Acima, foi descrito no formato .properties, mas vamos adotar a configuração .yml para padronizar o projeto. Sendo assim, o arquivo fica da seguinte forma:</p>

    server:
      port: 8081

    spring:
      application:
        name: product-service
      sql:
        init:
          platform: postgres
      datasource:
        url: jdbc:postgresql://localhost:5432/eurekadb
        username: postgres
        password: postgres
        driverClassName: org.postgresql.Driver

    eureka:
      instance:
        hostname: localhost

</p>
<br>
Uma vez configurado, criamos os pacotes model e repository, que incluirão as classes referentes a esse objeto.</p>
<br>

<p><b>Classe Product.java, no pacote model</b></p>
<p>Essa classe referencia uma tabela no banco de dados chamada Product.
</p>
<p>Iniciamos incluindo as anotações:<br><br>
@SuppressWarnings("serial") -> Compilador não emite warnings
@Entity -> que indica que essa é uma entidade para o JPA
@Table(name = "product") -> indicando que sua referêncai no banco é uma tabela chamada 'product'<br><br>
Então, adicionamos 'implements Serializable' ao nome da classe.
<br><br>
Por fim, inserimos os atributos da seguinte forma:
<br>

    @SuppressWarnings("serial")
    @Entity
    @Table(name = "product")
    public class Product implements Serializable {
    
        @Id
        @Column(length = 32, nullable = false)
        private String id;
        private String name;
        @Column(columnDefinition = "TEXT")
        private String description;

        public Product() {
            super();
        }

        ...
    }

<br>
- @Id indica que o atributo 'id' é uma chave primária<br>
- @Column especifica algumas características do atributo, como tipo, tamanho, não nulo, etc<br>
- 'columnDefinition = "TEXT"' é uma exigência do postgres para atributos do tipo String que sejam maiores
</p>
<p>Por fim, implementamos o construtor que herda a classe super(), e os setters e getters, visto que não foi utilizada a biblioteca Lombok.</p><br>

<p><b>Classe ProductRepository.java, no pacote repository</b></p>
<p>Interface que fará a gestão da tabela 'product' no banco de dados.
</p>
<p>Iniciamos alterando de classe para interface e 'extends CrudRepository< Product, String>'. 
</p>
<p>
Isso significa que a interface ProductRepository extende a classe CrudRepository do spring data, classe essa que já possui uma série de métodos pré definidos para todos os processos envolvendo Criação, Leitura, Atualização e Deleção de um objeto no banco de dados. Para isso, é necessário que seja indicado o tipo de objeto que está sendo trabalhado, no caso Product, e o tipo de dado que é o identificador, nesse caso o atributo Id (String).   
</p>
<p>Em seguida, incluimos a anotação:<br><br>
@Service -> indicando que é uma interface de serviço<br><br>
Com isso, nossa interface já é capaz de realizar todos os processos CRUD.
</p><br>

<p><b>Criando Métodos CRUD classe ProductController</b></p>
<p>Cada metodo deve conter anotações específicas para o objeto Product e utilizarão a interface ProductRepository para realizar a ação.
</p>
<p>Primeira passo foi instanciar a classe ProductRepository como private logo no inicio da classe. Em seguida, foi criado um construtor que recebe ProductRepository, ficando da seguinte forma:<br><br>
private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        super();
        this.productRepository = productRepository;
    }
<br>
Obs.: Outra forma de instanciar o ProductRepository simultaneamente ao objeto Product, em vez de incluir o parâmetro no construtor, podemos utilizar a anotação @Autowired, que aplica a instância automaticamente da mesma forma, sem a necessidade de implementar um construtor. Ficando dessa forma:<br><br>

    @Autowired
    private ProductRepository productRepository;

<br>A vantagem da primeira forma é que, quando instanciamos a classe Product para testes, ele sempre vai solicitar a inclusão do ProductRepository como parâmetro, evitando erros por esquecimento.<br><br>

<b>Método 'create':</b><br>

    @PostMapping
    public String create(@RequestBody ProductForm form){

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product = productRepository.save(product);

        return product.getId();
    }

<br>A anotação @PostMapping indica que ele será um POST do Endpoint Product. O método retorna o dado referente ao identificador da classe, no caso String do id.
<br><br>
Esse método solicita (@RequestBody) como parâmetro um objeto da classe ProductForm, classe padrão que possui os atributos Name e Description (inclui construtor queherda classe super e os setters e getters). Essa requisição deverá estar no formato json. Algo como:

    {name: 'Dev', description: 'descrição do objeto de nome Dev'}

    ou

    {
    name: 'Dev', 
    description: 'descrição do objeto de nome Dev'
    }
</p>
<br>

### Criando o Banco de Dados no PostgreSQL

<p>
<li>Realizar o Download e instalar o pacote PostgreSQL, contendo o pgAdmin 4 para gestão dos bancos.</li>
<li>Definir uma master senha e a porta 5432.</li>
<li>Dentro do pgAdmin 4, a organização dos arquivos é:
<ol>- Server</ol>
<ol>- Grupo -> que conterá diferentes databases, mas agrupados no mesmo grupo</ol>
<ol>- Database -> Que será personalizado para o projeto</ol> </li>
<li>Seleciona o Grupo desejado e cria um novo database, no caso eurekadb.</li>
<li>Criar a tabela 'product', contendo as colunas:
<ol>id - character (50)</ol>
<ol>name - character (50)</ol>
<ol>description - text</ol>
</li>
</p>

<br>
<p><b>Testando o método, usando Postman</b></p>
<p>O browser apenas permite a aplicação do GET, por isso não é possível testar métodos POST por meio dele. Para isso, utilizamos o software Postman.
</p>
<p>Criar o método GET no Postman e definir a URL 'http://localhost:8080/product'. Em seguida, criar o método POST, definindo a mesma URL, selecionando Body, raw e tipo json. No corpo do arquivo json, especificar um novo objeto, com os atributos 'name' e 'description'. Ficando da seguinte forma:</p>

    {
	"name": "Puzzle Infantil",
    "description": "Jogo da Lizinha"
}

<p>Com o jar do eureka, o gateway e o productApplication rodando, pressionar SEND no Postman, para o método GET realizar a solicitação de leitura no endereço. Isso trará como retorno a lista dos 4 produtos genéricos definidos na classe ProductController. Em seguida, pressione SEND no metodo POST, que retornará um valor referente ao id no banco.</p>

<p>Como o método GET está fazendo a leitura dos produtos prédefinidos na classe ProductController, vamos substituir o corpo de GetMapping, contendo os 4 produtos já descritos, para uma estrutura que lê os elementos realmente presentes no banco de dados, o que inclui o elemento enviado pelo método POST acima.
</p>

<p>Para a aplicação do ProductApplication e altere o método na classe ProductController, passando disso:

    @GetMapping
    public ImmutableMap<Long, String> findAll() {
        return ImmutableMap.of(
                1L, "Produto 1",
                2L, "Produto 2",
                3L, "Produto 3",
                4L, "Produto 4");
    }

Para isso:

        @GetMapping
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

Inicie ProductApplication novamente e SEND o método GET. O Postman retornará todos os itens adicionados anteriormente. Como tem um único item, retornará da seguinte forma:

    [
        {
            "id":   "2698d125-ac6e-4aaf-9e9b-7    55fd73e82ec                 ",
            "name": "Puzzle     Infantil                                     ",
            "description": "Jogo da     Lizinha"
        }
    ]

É possível testar a adição novamente, alterando algum atributo no metodo POST no Postman e enviando novamente. O GET trará esse novo elemento junto.<br><br>

P´roximo passo é incluir validações para evitar que atributops vazios sejam enviados ao banco de dados.
</p>

<p><b>Validações no Product Service usando Bean Validators</b></p>
<p>São ferramentas de validação prontas, fornecidas pelo Java, na forma de anotações, que deverão ser inseridas sobre os atributos do formulário, ou seja, no ProductForm, que recebe os valores do nosso web service.
</p>
<p>Primeiro, devemos incluir a seguinte dependência dentro do 'build.gradle':

    implementation 'org.springframework.boot:spring-boot-starter-validation:3.2.2'


A anotação @NotBlank sobre qualquer atributo garante que o sistema retorne um erro sem que haja tentativa de envio ao banco de dados. A importação utilizada foi

    import jakarta.validation.constraints.NotBlank;

E para que esse erro seja notificado de forma apropriada ao usuário, ajustamos o retorno do método 'create' de String para ResponseEntity< String>, incluímos a anotação @Valid logo antes da @RequestBody, e criamos o método 'handleValidationExceptions', responsável por verificar o erro e retornar uma mensagem trabalhada. Ficando da seguinte forma:

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody ProductForm form){

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product = productRepository.save(product);

        return ResponseEntity.ok(product.getId());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

Para que 
</p>

<p><b>Completando o CRUD</b></p>
<p>Incluindo os métodos update, delete e findById.

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable String id){
        Optional<Product> op = productRepository.findById(id);
        return ResponseEntity.of(op);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@Valid @RequestBody ProductForm form, @PathVariable String id) {
        Optional<Product> op = productRepository.findById(id);
        if(op.isEmpty()) {
            return ResponseEntity.of(op);
        }
        Product product = op.get();
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product = productRepository.save(product);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        Optional<Product> op = productRepository.findById(id);
        if(op.isPresent()){
            productRepository.deleteById(id);
        }
        // return ResponseEntity.of(op);
    }

Todos testados via Postman. Funcionando.<br>
O método delete não retorna nada, o que prejudica a confirmação pelo Postman, sendo necessário chamar o método findAll ou findById para verificar isso. É possível incluir retorno mais adequedo, como '204 No Content', indicando que não existe conteúdo a ser mostrado, indicando sucesso na exclusão.
</p>