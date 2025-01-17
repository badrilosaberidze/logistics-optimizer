# Logistics Optimizer Application

This is a Spring Boot-based application designed to optimize the selection of transfers for a logistics company based on weight and cost constraints using the **Knapsack DP** algorithm.

## Project Setup

To run the application locally, follow the instructions below:

### Prerequisites
- **JDK 17** or higher (download from [Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)).
- **Maven** (or use IntelliJ IDEA to manage Maven dependencies).
- **Git** (for version control).

### Clone the Repository

Clone the repository to your local machine using the following command:

```bash
git clone https://github.com/your-username/logistics-optimizer.git
```

## Build And Run The Application

1. Build the application using maven:

```bash
mvn clean install
```

2. Run the application

```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080. However This Project Does not provides Front end and User Experience From Web

## Curl Commands Examples

### Example Curl Request:

```bash
curl -X POST "http://localhost:8080/api/transfer/optimize" -H "Content-Type: application/json" -d '{
  "maxWeight": 15,
  "availableTransfers": [
    {
      "weight": 5,
      "cost": 10
    },
    {
      "weight": 10,
      "cost": 20
    }
  ]
}'
```

### Example Response

```json
{
  "selectedTransfers": [
    {
      "weight": 5,
      "cost": 10
    },
    {
      "weight": 10,
      "cost": 20
    }
  ],
  "totalCost": 30,
  "totalWeight": 15
}
```

## Testing

### Unit Tests and Integration Tests

Unit and integration tests have been written for the core functionality of the application, including the Knapsack Solver, Transfer Service, and Controller.

### Running Tests

To run tests using Maven, execute the following command:

```bash
mvn test
```
