[ Cliente / Postman ]
│
▼
┌───────────────┐
│ REST Controller│
└───────┬───────┘
│
├───> [ Kafka Producer ] ───> ( Kafka Topic ) ───> [ Kafka Consumer ] ───> ( H2 Database )
│
├───> [ Redis Service ] ───> ( Redis Cache )
│
└───> [ Spring AI Service ] ───> ( Gemini API )


---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java 17
- **Framework Principal:** Spring Boot 3.x
- **Gestión de Eventos:** Apache Kafka (Spring Kafka)
- **Caché Distribuida:** Redis (Spring Data Redis)
- **Persistencia:** Spring Data JPA + H2 Database (In-Memory)
- **Seguridad:** Spring Security, JWT (JSON Web Tokens), compatibilidad con IAM (Keycloak / Auth0)
- **IA:** Spring AI (Google Gemini API)
- **Contenedorización:** Docker & Docker Compose
- **Orquestación:** Kubernetes (Manifests YAML)
- **Build Tool:** Gradle

---

## 📂 Estructura del Repositorio

```text
KafkaTraining/
├── docker-compose.yml          # Servicios de Kafka, Zookeeper/KRaft y Redis
├── build.gradle                # Configuración de dependencias Gradle
├── k8s-deployment.yaml         # Configuración de Deployment y Service para Kubernetes
└── src/
    └── main/
        ├── java/com/ejemplo/kafkatraining/
        │   ├── config/          # Configuraciones de Security, Kafka y Redis
        │   ├── controller/      # REST Controllers (Auth, Eventos, AI)
        │   ├── dto/             # Data Transfer Objects (LoginRequest, Response, etc.)
        │   ├── entity/          # Entidades JPA
        │   ├── filter/          # JwtFilter para autenticación stateless
        │   ├── repository/      # Repositorios Spring Data
        │   └── service/         # Productores/Consumidores de Kafka, Lógica de Negocio, AI
        └── resources/
            └── application.yaml # Propiedades de la aplicación
📋 Requisitos Previos
Asegúrate de contar con lo siguiente instalado en tu entorno local:

JDK 17 o superior.

Docker Desktop (con Docker Compose habilitado).

Git.

🚀 Instalación y Configuración
1. Clonar el repositorio
Bash
git clone [https://github.com/tu-usuario/KafkaTraining.git](https://github.com/tu-usuario/KafkaTraining.git)
cd KafkaTraining
2. Levantar la infraestructura local (Kafka + Redis)
Ejecuta Docker Compose para iniciar los contenedores necesarios:

Bash
docker compose up -d
Verifica que los servicios estén activos:

Bash
docker ps
Deberías ver kafka-local (puerto 9092) y redis-local (puerto 6379) en estado Up.

3. Configurar variables de entorno
Edita el archivo src/main/resources/application.yaml o configura la variable de entorno para la clave de IA:

YAML
spring:
  kafka:
    bootstrap-servers: localhost:9092
  data:
    redis:
      host: localhost
      port: 6379
  ai:
    openai/gemini:
      api-key: ${AI_API_KEY:tu_api_key_aqui}
4. Compilar y ejecutar la aplicación
Bash
./gradlew bootRun
⚙️ Componentes Clave
1. Event-Driven Architecture (Kafka)
Producer (KafkaProducer): Publica eventos/mensajes en tópicos definidos.

Consumer (KafkaConsumer): Escucha eventos de forma asíncrona y persiste la información en la base de datos H2.

2. Caché en Memoria (Redis)
Utilizado para optimizar lecturas frecuentes, reducir la carga en la base de datos relacional y gestionar contadores o sesiones distribuidas.

3. Seguridad y JWT
Cadena de Filtros: Implementación de JwtFilter para validar el encabezado Authorization: Bearer <token> en peticiones protegidas.

Autenticación Stateless: Endpoint /api/auth/login para autenticación y generación de tokens.

4. Integración con Spring AI
Uso del módulo oficial Spring AI para canalizar consultas en lenguaje natural directamente hacia modelos de lenguaje (LLM) como Google Gemini.

5. Despliegue en Kubernetes (K8s)
El archivo k8s-deployment.yaml contiene los componentes necesarios para desplegar la aplicación en un clúster:

Deployment: Define el pod, réplicas, variables de entorno y límites de recursos.

Service (LoadBalancer/NodePort): Expone la aplicación internamente o hacia el exterior del clúster.

Para desplegar en Kubernetes:

Bash
kubectl apply -f k8s-deployment.yaml

🔗 Endpoints Principales

Método,Ruta,Descripción
POST,/api/auth/login,Endpoint público para autenticación y obtención de JWT.
POST,/api/messages,Envía un mensaje a Kafka a través del Producer.
GET,/api/messages,Consulta los mensajes persistidos por el Consumer.
POST,/api/ai/ask,Realiza una consulta asistida por IA (Spring AI).
