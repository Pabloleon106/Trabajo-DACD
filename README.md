# Aplicación de Análisis de Datos Meteorológicos y Empresariales

## Título
**Aplicación de Análisis de Datos Meteorológicos y Empresariales**

## Asignatura
**Desarrollo de Aplicaciones para Ciencia de Datos**

## Curso
**2023/2024**

## Titulación
**Grado en Ingeniería en Ciencia de Datos**

## Escuela
**Escuela Universitaria de Informática**

## Universidad
**Universidad de Las Palmas de Gran Canaria**

## Resumen de la Funcionalidad
Este proyecto implementa un patrón Publisher/Subscriber para capturar, almacenar y analizar datos meteorológicos y datos de sensores adicionales. Consta de varios módulos:
- `WeatherApp`: Captura datos meteorológicos cada 6 horas.
- `NewSensor`: Captura datos de una fuente adicional y los publica.
- `DatalakeBuilder`: Almacena datos de sensores en un datalake estructurado.
- `BusinessUnit`: Analiza datos y mantiene un datamart.

## Diseño
### Patrones y Principios
- **Patrón Publisher/Subscriber**: Utilizando ActiveMQ para la comunicación entre módulos.
- **Diseño Modular**: Separación de preocupaciones en diferentes módulos.
- **Principios DRY y YAGNI**: Adherencia a los principios de "Don’t Repeat Yourself" y "You Aren’t Gonna Need It".
- **Principios SOLID**: Aplicación de principios de diseño orientado a objetos para asegurar una estructura robusta y mantenible.

### Diagrama de Clases
Incluye las relaciones de dependencia entre las clases `WeatherApp`, `NewSensor`, `DatalakeBuilder`, `BusinessUnit`.

```mermaid
classDiagram
    class WeatherApp {
        -apiKey: String
        -ISLANDS: String[]
        -ISLAND_COORDINATES: double[][]
        +main(String[] args): void
    }
    class NewSensor {
        -BROKER_URL: String
        -TOPIC_NAME: String
        -FREQUENCY: long
        +main(String[] args): void
    }
    class DatalakeBuilder {
        -BROKER_URL: String
        -TOPIC_NAME: String
        +main(String[] args): void
        +getDateString(long timestamp): String
    }
    class BusinessUnit {
        -BROKER_URL: String
        -TOPIC_NAME: String
        -DB_URL: String
        +main(String[] args): void
    }
    WeatherApp --> WeatherAPI
    WeatherApp --> DatabaseManager
    NewSensor --> SensorData
    DatalakeBuilder --> SensorData
    BusinessUnit --> SensorData
