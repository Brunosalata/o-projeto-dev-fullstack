# o-projeto-dev-fullstack

Modulos desenvolvidos ao longo do curso O Projeto Dev

#### Características
<li>Criaçao via Spring Initializr</li>
<li>Java 17</li>
<li>artifact: gateway-server</li>
<li>com.brunosalata.fullstackproject.gatewayserver (remover hifen entre gateway e server para evitar conflito de endereço futuro)</li>
<li>Packaging - Jar</li>
<li>Gerenciador de dependências - Gradle-Groovy</li>
<li>Adição das Dependências 'Eureka Discovery Client' e 'Gateway' (Spring Cloud Routing)</li>

#### Ferramentas
<li>Gradle Plugin para o VSCode - Acesso às ferramentas Gradle</li>
<li>Spring Tool Plugin para o VSCode - Dashboard de Apps, Beans e Endpoints</li>

</br>

### 2 - Gateway
<p>Discovery Client (Eureka) - API que promove comunicação entre Requerimentos e Serviços, por intermédio do Discovery Server</p>
<p>Responsável pelo gerenciamento das rotas de acesso aos serviços.</p>

### Criação

<p><b>Configurações iniciais</b></p>
<p>Após criado o novo projeto pelo Spring Initializr, foram realizados os seguintes passos:
<li>Inclusão da Anotação @EnableDiscoveryClient na classe principal, indicando ser um cliente disponível ao Discovery Server (Eureka)</li>
<li>no arquivo application.properties, incluir as seguintes configurações:</li>
<p>
<ol>spring.application.name=gateway-server</ol>
<ol>eureka.instance.hostname=localhost</ol>
<ol>server.port=8080</ol>
</p>
</p>

### Configurando Rotas

<p>
Converter a extensão do arquivo 'application.properties' para 'application.yml'. Além de mudar manualmente a extensão no nome do arquivo, a estrutura das propriedades nele presentes passam a ser:<br>
<ol>
<br>server:
<br>__port: 8080  
<br>
<br>spring:
<br>__application:
<br>____name: gateway-server
<br>__cloud:
<br>____mvc:
<br>______routes:
<br>______- id: product-service
<br>________uri: lb://PRODUCT-SERVICE
<br>________predicates:
<br>________- Path=/**
<br>
<br>eureka:
<br>__instance:
<br>____hostname: localhost
<br>__client:
<br>____serviceUrl:
<br>______defaultZone: http://localhost:8761/eureka
<br>
</ol>
Cada '_' representa um espaço. Espaçamento deve ser de dois espaços a mais a cada subitem.</P>

<p>
Cada serviço deve possuir uma route configurada no gateway. Nesse projeto, inicialmente, apenas product-service foi configurado, por ter sido o único microserviço implementado até o momento.<br>
Logo, sempre que o gateway receber um requerimento para product-service (id), ele vai direcionar a rota para a URI respectiva do product-service</p>

<p>lb = Load Balancer -> Representado pelo Discovery Server</p>
<p>lb://PRODUCT-SERVICE indica que o requerimento inclui o mapeamento de PRODUCT-SERVICE, microserviço mapeado no Eureka</p>
<p>'predicates' especifica o caminho de acesso ao serviço. Por exemplo, 'Path=/**' indica que todos os caminhos a partir da raiz podem ser acessados, enquanto 'Path=/product/**' especifica acesso a partir do caminho produtos. Importante para restringir acesso para diferentes microserviços no projeto (product, user, finance, etc). 
</p>

### Aplicação Prática

<p>
A porta do gateway será a única porta acessível no servidor, as demais não estarão disponibilizadas via acesso direto 'localhost:8081' (product-service), por exemplo. Mas com as rotas já definidas no gateway (porta 8080), ele fica responsável por processar o requerimento, solicitar o caminho do serviço ao Discovery Server e direcionar a solicitação ao microserviço solicitado.<br><br>
Até então, o acesso ao product-service acontecia via 'localhost:8081/product', agora pode ser acessado via gateway 'localhost:8080/product'. E o mesmo se aplicará aos demais microserviços.
</P>

