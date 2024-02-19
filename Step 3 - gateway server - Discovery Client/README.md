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

    server:
      port: 8080  

    spring:
      application:
        name: gateway-server
      cloud:
        mvc:
          routes:
          - id: product-service
            uri: lb://PRODUCT-SERVICE
            predicates:
            - Path=/**

    eureka:
      instance:
        hostname: localhost
      client:
        serviceUrl:
          defaultZone: http://localhost:8761/eureka

</P>

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

### Criando Estrutura de Usuários e Autenticação no Gateway

<p>Estrutura de usuários possibilita que o Gateway permita acesso às rodas apenas por usuários autenticados.
</P>
<p>Etapa 1 - no build.gradle do projeto gateway, incluir as seguintes dependências e recarregar projeto:

    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'org.postgresql:postgresql'

Etapa 2 - Fazer a configuração no application.yml

    sql:
      init:
        platform: postgres
    datasource:
      url: jdbc:postgresql:/  localhost:5432/eurekadb
      usename: postgres
      password: Thaisebruno10
      driverClassName: org  postgresql.Driver

E incluir nas routes o seguinte filtro:

    filters:
      - AuthFilter

Ficando da seguinte forma:

    spring:
      application:
        name: gateway-server
    sql:
      init:
        platform: postgres
    datasource:
      url: jdbc:postgresql:// localhost:5432/eurekadb
      usename: postgres
      password: Thaisebruno10
      driverClassName: org.postgresql.    Driver
    cloud:
      gateway:
        mvc:
          routes:
          - id: product-service
            uri: lb://PRODUCT-SERVICE
            predicates:
            - Path=/**
            filters:
            - AuthFilter

Etapa 3 - Criar pacotes e classes para trabalhar com Usuários.<br>
<li>controller
<ol>UserController.java</ol>
<ol>UserForm.java</ol></li>
<li>model<ol>User.java</ol></li>
<li>repository<ol>UserRepository.java</ol></li>
<br>
Etapa 4 - Criar a classe User.java, contendo os atributos id, login, password e profile, ficando da seguinte forma:

    package com.brunosalata.fullstackproject.gatewayserver.model;

    import java.io.Serializable;
    import com.fasterxml.jackson.annotation.JsonIgnore;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;

    @SuppressWarnings("serial")
    @Entity
    @Table(name = "user")
    public class User implements Serializable {
    
        @Id
        @Column(length = 32, nullable = false)
        private String id;
        private String login;
        @JsonIgnore
        private String password;
        private String profile = "ADMIN";

        public User() {
            super();
        }

        ... setters e getters
    }

Etapa 5 - criar a classe UserRepository

    package com.brunosalata.fullstackproject.gatewayserver.repository;

    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository. CrudRepository;
    import org.springframework.data.repository.query.   Param;
    import org.springframework.stereotype.Repository;
    import org.springframework.stereotype.Service;

    import com.brunosalata.fullstackproject.gatewayserver.  model.User;

    @Service
    public interface UserRepository extends     CrudRepository<User, String> {

        @Query("SELECT obj FROM User obj WHERE obj.login = :login")
        public User findByLogin(@Param("login") String login) throws Exception;
    }

Etapa 6 - criar classe UserController e UserForm<br><br>
Replicar a criação do ProductController e ProductForm, mas contendo os atributos Login e Password:

    @NotBlank
    private String login;
    @NotBlank
    private String password;

Etapa 7 - Testar métodos GET, POST, PUT e DELETE no Postman.
</p>

