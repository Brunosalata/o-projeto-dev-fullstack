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

### 2 - Mail-Service
<p>Discovery Client (Eureka) - API que identifica o microserviço ao Discovery Server</p>
<p>Projeto Email Service (Microserviço) responsável por consumir mensagens sendProduct ao cadastrar um novo produto.</p>

### Criação

<p><b>Configurações iniciais</b></p>
<p>Após criado o novo projeto pelo Spring Initializr, foram realizados os seguintes passos:
<li>Incluir as bibliotecas do kafka em build.gradle</li>

    implementation 'org.apache.kafka:kafka-streams:3.7.0'
	implementation 'org.springframework.kafka:spring-kafka:3.1.2'

<li>Inclusão da Anotação @EnableKafka na classe principal, para habilitar os recursos kafka para esse serviço.</li>
<li>No arquivo application.properties --> application.yml, incluir as seguintes configurações:</li>

    server:
      port: 8082

    spring:
      application:
        name: mail-service
      kafka:
        consumer:
          bootstrap-servers: 127.0.0.1:9002 #endereço do Apache Kafka no    localhost, que deverá ser alterado quando a aplicação estiver em   outro servidor
          group-id: backend
          auto-offset-reset: earliest
          key-deserializer: org.apache.kafka.common.serialization.  StringDeserializer
          value-deserializer: org.apache.kafka.common.serialization.    StringDeserializer

    topic:
      name:
        email: send.email

</p>

<p>
Com o Discovery Server ativo, é possível iniciar o ProductApplication e observer seu surgimento no painel do localhost:8761.
</p>
<br>
<p>Criação do pacote service, contendo a classe MailService.java, responsável por buscar no kafka todas as movimentações envolvendo o topico 'send.email' e tomar uma providência a partir disso.</p>

    import org.apache.kafka.clients.consumer.ConsumerRecord;
    import org.springframework.beans.factory.annotation.    Value;
    import org.springframework.kafka.annotation.    KafkaListener;
    import org.springframework.stereotype.Service;

    @Service
    public class MailService {

        @KafkaListener(topics = "${topic.name.email}")
        public void consume(ConsumerRecord<String, String> payload) {
            System.out.println(payload.key());
            System.out.println(payload.value());
        }
    }

payload.key() e payload.value() representam o id e o name do produto sinalizado.

### Iniciando Serviço Kafka

<p><b>Kafka e Zookeeper - Docker</b></p>
<p>Para seja possível utilizar o Kafka, é necessário subir o Kafka e Zookeeper. Para isso, utilizamos a estrutura de docker-compose.yml, inserida na raiz onde estão localizados os projetos.</p>
<li>Criação do docker-compose.yml</li>

    version: '3'

    services:
      zookeeper:
        container_name: ZOOKEEPER
        image: confluentinc/cp-zookeeper:latest
        networks:
          - broker-kafka
        environment:
          ZOOKEEPER_CLIENT_PORT: 2181
          ZOOKEEPER_TICK_TIME: 2000

      kafka:
        container_name: KAFKA
        image: confluentinc/cp-kafka:latest
        networks:
          - broker-kafka
        depends_on:
          - zookeeper
        ports:
          - "9092:9092"
        environment:
          KAFKA_BROKER_ID: 1
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
          KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://192.168.0.103:9092
          KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
          KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
          KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

      kafdrop:
        container_name: KAFDROP
        image: obsidiandynamics/kafdrop:latest
        networks:
          - broker-kafka
        depends_on:
          - kafka
        ports:
          - "19000:9000"
        environment:
          KAFKA_BROKERCONNECT: kafka:29092

    networks:
      broker-kafka:
        driver: bridge

<li>Para subir os recursos necessários, precisamos:</li>
<ol>Instalar o Docker Desktop e o WSL (para maquinas Windows)</ol>
<ol>Pelo terminal, ir até a pasta onde está localizado o docker-compose e verificar se tem alguma imagem docker rodando. Digitar o comando abaixo.</ol>

    docker images

<ol>Então, liste os arquivos com o comando</ol>

    dir

<ol>Verificando a existencia do arquivo .yml, seguir com o comando:</ol>

    docker-compose up -d

<ol>Por fim, vrificar se as imagens estão rodando.</ol>

    docker ps

<p>Agora é possível utilizar as funcionalidades do Kafka sem problemas.</p>