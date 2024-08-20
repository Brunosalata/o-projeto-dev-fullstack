# o-projeto-dev-fullstack

Modulos desenvolvidos ao longo do curso O Projeto Dev

### 1 - Discovery Server (Eureka)
<p>API usada e desenvolvida pela Netflix</p>
<p>Gerencia o acesso aos microsserviços a partir de uma solicitação, listando e direcionando o Gateway ao endereço mais apropriado em termos de função, status e disponibilidade.</p>

### 2 - Product
<p>Discovery Client (Eureka) - API que identifica o microserviço ao Discovery Server</p>
<p>CRUD responsável pelo gerenciamento de produtos.</p>

### 3 - Gateway
<p>Discovery Client (Eureka) - API que promove comunicação entre Requerimentos e Serviços, por intermédio do Discovery Server</p>
<p>Responsável pelo gerenciamento das rotas de acesso aos serviços.<br>Para que funcione, todos as estruturas devem estar contidas no mesmo workspace, caso contrário, o Gateway não comunica corretamente com os serviços. Logo, não funciona.</p>
<p>Itens presentes:
<li>CRUD responsável pelo gerenciamento de User</li>
<li>Criptografia com SHA-256 para armazenar senha</li>
<li>Autenticação por token (jwt)</li>
<li>Filtro de rotas (acesso liberado apenas a Usuários autenticados, com token válido)</li>
</p>

### 4 - Mail Service
<p>Mail service</p>

### 5 - Front end
<p>Front end com Nuxt.js, baseado em Vue.js</p>
<p>Plugins e módulos:</p>
<li>local storage - armazenamento em memória</li>
<li>axios - REST</li>
<li>i18n - internacionalização</li>
<li>Vuetify - interface</li>

### 6 - Gerando Aplication Android (apk)
<p>Utilizando a ferramenta Capacitor</p>