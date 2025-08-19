# Invoice Service (Spring Boot)

## Features
- POST `/api/invoices` → Generates PDF invoice with 10% tax, total, timestamp, invoice #, and QR (transactionId).
- Returns downloadable PDF.

## Build & Run Locally
```bash
mvn clean package -DskipTests
java -jar target/invoice-service-0.0.1-SNAPSHOT.jar
# Test:
curl -X POST http://localhost:8080/api/invoices \
  -H "Content-Type: application/json" -H "Accept: application/pdf" \
  -d '{"dealerId":"D001","vehicleId":"V1002","customerName":"John Doe"}' \
  --output invoice.pdf
