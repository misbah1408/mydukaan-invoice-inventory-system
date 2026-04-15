# 🧾 MyDukaan — Invoice & Inventory Management System

A scalable backend system for managing billing, inventory, and payments for retail businesses.
Built with Spring Boot, this project simulates a real-world SaaS platform with transactional integrity, role-based access, and optimized database design.

---

## 🚀 Features

* 🔐 JWT-based authentication & role-based authorization (Admin / Staff)
* 🏪 Multi-user store management
* 📦 Product & inventory tracking with stock validation
* 🧾 Invoice generation with GST calculation
* 💳 Payment handling with ledger integration
* 📊 Dynamic invoice status (Pending / Partial / Paid)
* 👤 Customer support (walk-in + registered)
* ⚡ Optimized queries (avoiding N+1 problem)
* 📄 API documentation using Swagger & modular docs

---

## 🏗️ Tech Stack

* **Backend:** Java, Spring Boot
* **Security:** Spring Security, JWT
* **Database:** PostgreSQL
* **ORM:** Hibernate (JPA)
* **Docs:** Swagger (OpenAPI)
* **Build Tool:** Maven

---

## 🧠 System Design

* Layered Architecture (Controller → Service → Repository)
* Normalized relational schema with proper entity relationships
* Transaction-safe invoice processing using `@Transactional`

### 🔁 Core Flow

User → Create Invoice → Validate Stock → Deduct Inventory → Calculate GST → Process Payment → Update Ledger → Update Invoice Status

---

## 📂 Project Structure

```
src/main/java/com/mydukaan
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
├── exception
└── util
```

---

## 📄 API Documentation

Detailed API docs are available in the `/docs` folder:

* Auth APIs → `docs/auth.md`
* Product APIs → `docs/product.md`
* Invoice APIs → `docs/invoice.md`
* Payment APIs → `docs/payment.md`
* Customer APIs → `docs/customer.md`

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Authentication

All protected APIs require:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 🧾 Sample Invoice Request

```json
{
  "storeId": 1,
  "customerId": 2,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ],
  "payments": [
    {
      "ledgerId": 1,
      "amount": 100,
      "method": "UPI"
    }
  ]
}
```

---

## ⚙️ Setup & Run

### 1. Clone Repository

```
git clone https://github.com/your-username/mydukaan-invoice-inventory-system.git
cd mydukaan-invoice-inventory-system
```

### 2. Configure Database

Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydukaan_db
    username: postgres
    password: yourpassword
```

### 3. Run Application

```
mvn spring-boot:run
```

---

## 📊 Future Enhancements

* 📈 Sales analytics dashboard
* 📦 Multi-store (SaaS) support with user-store mapping
* 🔔 Low stock alerts & notifications
* 📄 PDF invoice generation
* 💳 Payment gateway integration (Razorpay)
* 📱 Mobile app (Flutter)

---

## 💼 Resume Highlight

> Developed a scalable invoice and inventory management backend using Spring Boot with transactional billing logic, JWT-based security, optimized database design, and real-time stock management.

---

## 🤝 Contributing

Contributions are welcome! Feel free to fork the repo and submit a pull request.

---

## 📜 License

This project is for educational and portfolio purposes.

---

## 👨‍💻 Author

**Mohammed Misba**
Full-Stack Developer (MERN + Spring Boot)
Passionate about building scalable backend systems 🚀

---
