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
    @Table(schema = "public", name = "user")
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

Etapa 7 - Criar tabela 'user' no database.<br><br>

    user:
    - id (character varying 50)
    - login (character varying 50)
    - password (character varying 50)
    - profile (character varying 50)


Etapa 8 - Testar métodos GET, POST, PUT e DELETE no Postman.<br>

URL:

    http://localhost:8080/user

JSON:

    {
      "login": "Brunosalata",
      "password": "54321"
    }
 
</p>

### Criptografia com SHA-256

<p>Criptografar a senha antes que seja enviada ao banco de dados. Com isso, não é necessário armazenar ou manipular a senha propriamente, mas uma representação dela, aumentando a segurança.
</P>
<p>Etapa 1 - Criar um pacote 'util' dentro de gateway, contendo a classe CryptoUtil.java.<br>
Classes utilitarias são classes que apenas realizam alguma função, retornam o que voce precisa e são encerradas.</p>
<p>A classe fica da seguinte forma:

    package com.brunosalata.  fullstackproject. gatewayserver.util;

    import java.io.   UnsupportedEncodingException;
    import java.security. MessageDigest;
    import java.security.   NoSuchAlgorithmException;

    import org.apache.commons.  codec.binary. Base64;

    public class CryptoUtil {
    
      public static String encrypt(String plainText) throws Exception {
          MessageDigest md = null;
  
          try{
              md = MessageDigest.getInstance("SHA");
          } catch (NoSuchAlgorithmException e){
              throw new Exception(e.getMessage());
          }
  
          try{
              md.update(plainText.getBytes("UTF-8"));
          } catch (UnsupportedEncodingException e) {
              throw new Exception(e.getMessage());
          }
  
          byte raw[] = md.digest();
          try{
              return new String(Base64.encodeBase64(raw), "UTF-8");
          } catch (Exception use) {
              throw new Exception(use);
          }
      }
    }

Então, devemos chamar essa classe e método toda vez que precisarmos criptografar um valor. Por ora, chamamos no método 'create' e 'update' na classe UserController, ficando da seguinte forma:

    
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody UserForm form) throws Exception{
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin(form.getLogin());
        user.setPassword(CryptoUtil.encrypt(form.getPassword()));
        user = userRepository.save(user);
        return ResponseEntity.ok(user.getId());
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@Valid @RequestBody UserForm form, @PathVariable String id) throws Exception {
        Optional<User> op = userRepository.findById(id);
        if(op.isEmpty()) {
            return ResponseEntity.of(op);
        }
        User user = op.get();
        user.setLogin(form.getLogin());
        user.setPassword(CryptoUtil.encrypt(form.getPassword()));
        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }
</p>

### Autenticando rotas com JWT

<p>Autentica o usuário logado por meio de um token, contendo todas as informações que julgarmos necessárias. Sempre que o usuário fizer uma requisição que necessite autenticação, esse token deverá ser enviado no cabeçalho. O servidor descriptografará o token e verificará se as informações batem para retornar o que foi requisitado.
</P>
<p>Etapa 1 - Inclusão da biblioteca JWT no build.gradle

    implementation group: 'io.jsonwebtoken', name: 'jjwt', version '0.9.1'    
</p>
<p>Etapa 2 - Configurações no application.yml

    jwt:
      secret: xx22xxxxxxxxxxxxxxxY
      token:
        validity: 864000000
        prefix: Bearer    
</p>
<p>Etapa 3 - criar um pacote service, contendo a classe JwtService, dentro do projeto gateway

    import java.util.Date;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;

    @Service
    public class JwtService {
    
      @Value("${jwt.secret}")
      private String jwtSecret;
      @Value("${jwt.token.validity}")
      private long tokenValidity;

      public String generateToken(String id){
          Claims claims = Jwts.claims().setSubject(id);
          long nowMillis = System.currentTimeMillis();
          long expMillis = nowMillis + tokenValidity;
          Date exp = new Date(expMillis);
          return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
      }

      public void validateToken(final String token) throws Exception {
          Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      }

      public Claims getClaims(final String token) {
          try{
              return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
          } catch (Exception e) {
              System.out.println(e.getMessage() + " => " + e);
          }
          return null;
      }
    }

</p>
<p>Etapa 4 - criar uma classe controller, para que o usuário possa fazer o login. Para isso, criamos as classes AuthController.java e AuthForm.java dentro do pacote controller.
<br><br>
Classe AuthController

    import org.springframework.web.bind.annotation.RestController;

    import com.brunosalata.fullstackproject.gatewayserver.repository.   UserRepository;
    import com.brunosalata.fullstackproject.gatewayserver.service.    JwtService;
    import com.brunosalata.fullstackproject.gatewayserver.util.   CryptoUtil;

    import org.springframework.boot.autoconfigure.security.   SecurityProperties.User;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;


    @RestController
    public class AuthController {

        private UserRepository userRepository;
        private JwtService jwtService;

        public AuthController(UserRepository userRepository,    JwtService jwtService) {
            super();
            this.userRepository = userRepository;
            this.jwtService = jwtService;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<String> login(@RequestBody AuthForm     form) throws Exception {

            User user = userRepository.findByLogin(form.getLogin());
            if(user == null){
                return new ResponseEntity<String>(HttpStatus.   UNAUTHORIZED);
            }
            if(!user.getPassword().equals(CryptoUtil.encrypt(form.    getPassword()))){
                return new ResponseEntity<String>(HttpStatus.   UNAUTHORIZED);
            }

            String token = jwtService.generateToken(user.getLogin());
            return new ResponseEntity<String>(token, HttpStatus.OK);
        }

    }

<br>
Classe AuthForm

    import org.springframework.web.bind.annotation.RestController;

    import com.brunosalata.fullstackproject.gatewayserver.repository.UserRepository;
    import com.brunosalata.fullstackproject.gatewayserver.service.JwtService;
    import com.brunosalata.fullstackproject.gatewayserver.util.CryptoUtil;
    import com.brunosalata.fullstackproject.gatewayserver.model.User;
    
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    
    
    @RestController
    public class AuthController {
        
        private UserRepository userRepository;
        private JwtService jwtService;
    
        public AuthController(UserRepository userRepository, JwtService jwtService) {
            super();
            this.userRepository = userRepository;
            this.jwtService = jwtService;
        }
    
        @PostMapping("/auth/login")
        public ResponseEntity<String> login(@RequestBody AuthForm form) throws Exception {
            
            User user = userRepository.findByLogin(form.getLogin());
            if(user == null){
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
            if(!user.getPassword().equals(CryptoUtil.encrypt(form.getPassword()))){
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
    
            String token = jwtService.generateToken(user.getLogin());
            return new ResponseEntity<String>(token, HttpStatus.OK);
        }
        
    }

</p>
<p> Para rodar corretamente, é necessário incluir a seguinte biblioteca:

    implementation 'javax.xml.bind:jaxb-api:2.3.1'

Isso porque, a classe javax.xml.bind.DatatypeConverter faz parte do pacote java.xml.bind, que foi removido do Java SE a partir da versão 11. Caso não seja incluída, o sistema dá erro por não encontrar essa classe no projeto.
</p>

<p>Partimos para validação via Postman. Criando um Request POST, para o caminho:

    http://localhost:8080/auth/login

Contendo um json referente a algum usuário já existente, como:

    {
        "login": "bruno",
        "password": "123"
    }

Caso algo não esteja batendo, ele retorna vazio, mas indicando "Não autorizado". Por outro lado, caso login e senha estejam corretos, o sistema retorna um token de autenticação.
</p>

### Proteção de rotas por filtro

<p>Criação de um filtro que permite acesso apenas a usuários autenticados.
</P>
<p>Etapa 1 - Criação de um pacote filter, contendo a da classe AuthFilter

    import java.util.List;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.cloud.gateway.filter.GatewayFilter;
    import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.server.reactive.ServerHttpRequest;
    import org.springframework.http.server.reactive.ServerHttpResponse;
    import org.springframework.stereotype.Component;
    import org.springframework.web.server.ServerWebExchange;

    import com.brunosalata.fullstackproject.gatewayserver.service.JwtService;
    import com.google.common.base.Predicate;

    import io.jsonwebtoken.Claims;
    import reactor.core.publisher.Mono;

    @Component
    public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    	private JwtService jwtService;
    	@Value("${jwt.token.prefix}")
    	private String tokenPrefix;

    	public AuthFilter(JwtService jwtService) {
    		super(Config.class);
    		this.jwtService = jwtService;
    	}

    	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    		ServerHttpResponse response = exchange.getResponse();
    		response.setStatusCode(httpStatus);
    		return response.setComplete();
    	}

    	@Override
    	public GatewayFilter apply(Config config) {
    		return (exchange, chain) -> {

    			ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

    			final List<String> apiEndpoints = List.of("/auth/login");

    			Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
    					.noneMatch(uri -> r.getURI().getPath().contains(uri));

    			if (isApiSecured.apply(request)) {

    				if (!request.getHeaders().containsKey("Authorization")) {
    					return this.onError(exchange, "No Authorization header", HttpStatus.    UNAUTHORIZED);
    				}

    				String token = request.getHeaders().getOrEmpty("Authorization").get(0);
    				token = token.replace(tokenPrefix, "");

    				try {
    					jwtService.validateToken(token);
    				} catch (Exception e) {
    					ServerHttpResponse response = exchange.getResponse();
    					response.setStatusCode(HttpStatus.BAD_REQUEST);
    					return response.setComplete();
    				}

    				Claims claims = jwtService.getClaims(token);
    				exchange.getRequest().mutate().header("x-user", String.valueOf(claims.get   ("sub"))).build();

    				return chain.filter(exchange.mutate().request(request).build());

    			}

    			return chain.filter(exchange);
    		};
    	}

    	public static class Config {
    		// Put the configuration properties
    	}
    }    

Então, incluir 

    filter
    - AuthFilter

para a rota de product, dentro do arquivo application.yml
</p>

<p>Partimos para validação via Postman. Request do tipo GET para produtos, pelo caminho:

    http://localhost:8080/product

Ele deverá retornar vazio, mas contendo Não autorizado. Para acessar o conteúdo, geramos um token pelo auth/login, copiamos ele, selecionamos a aba Authorization no GET product, selecionamos o tipo de autorização (Bearer Token), colamos no campo do Token e repetimos o envio da requisição. Dessa vez, é para retornar o conteúdo requerido.
</p>