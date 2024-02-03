# o-projeto-dev-fullstack

Modulos desenvolvidos ao longo do curso O Projeto Dev

#### Características
<li>Criaçao via Spring Initializr</li>
<li>Java 17</li>
<li>Packaging - Jar</li>
<li>Gerenciador de dependências - Gradle-Groovy</li>
<li>Adição da Dependência 'Eureka Server'</li>

#### Ferramentas
<li>Gradle Plugin para o VSCode - Acesso às ferramentas Gradle</li>
<li>Spring Tool Plugin para o VSCode - Dashboard de Apps, Beans e Endpoints</li>

</br>

### 1 - Discovery Server (Eureka)
<p>API usada e desenvolvida pela Netflix</p>
<p>Gerencia o acesso aos microsserviços a partir de uma solicitação, listando e direcionando o Gateway ao endereço mais apropriado em termos de função, status e disponibilidade.</p><br>

### Criação
<p>Após criado o projeto, </p>

<p>
Com o Servidor estabelecido e sua base configurada, é necessário deixa-lo rodando constantemente, para que o Gateway possa acessá-lo a qualquer momento.<br>
Para isso, utilizamos a função Tasks >> build >> bootJar, no plugin do Gradle, para criar um Jar executável, cuja inicialização acontece via Terminal, pelo comando 'java -jar meuJar.jav'.<br>
Ao executá-lo dessa forma, não é mais necessário manter o projeto aberto na IDE, visto que não está mais usando os recursos da IDE, mas os componentes internos do próprio Jar. Logo, poderá ser acessado pela URL 'localhost:8761'.<br><br>
Dessa forma, será possível mantê-lo ativo no servidor constantemente, disponibilizando seus recursos aos demais componentes da aplicação.
</p>


