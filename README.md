# 🎟️ Simple Ticketing System

[![Java](https://img.shields.io/badge/Java-24-blue.svg?logo=java)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-green.svg?logo=springboot)](https://spring.io/projects/spring-boot)  
[![Stripe](https://img.shields.io/badge/Payments-Stripe-blue.svg?logo=stripe)](https://stripe.com/)  
[![MySQL](https://img.shields.io/badge/Database-MySQL-336791.svg?logo=mysql)](https://www.mysql.com/)

---

## 📖 About the Project
This is a **Simple Ticketing System** built with Java + Spring Boot.  
It provides event organizers with tools to manage events, venues, and tickets while allowing attendees to purchase tickets securely via **Stripe**.

---

## ✨ Features
👤 **User Features**
- 📅 Create Events
- 🎟️ Create Ticket Types for each Event (VIP, Regular, etc.)
- 🏟️ Add Venues and define their capacity
- 💳 Buy Tickets via Stripe integration

🛠️ **Admin Features** *(future roadmap)*
- 👥 Role-based access control (Admin / Organizer / Attendee)
- 📊 Dashboard for managing events and ticket sales
- 🔒 Authentication & Authorization (JWT-based)

---

## 🏗️ Tech Stack
- **Backend:** Java 24, Spring Boot
- **Database:** Mysql (JPA/Hibernate for ORM)
- **Payments:** Stripe
- **API Docs:** SpringDoc / OpenAPI (Swagger UI)
- **Build Tool:** Maven

---

## 🚀 Getting Started

### ✅ Prerequisites
- [Java 21+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Mysql](https://www.mysql.com/) installed locally
- [Stripe CLI](https://stripe.com/docs/stripe-cli) for webhook testing

### ⚙️ Installation
```bash
# Clone the repo
git clone https://github.com/Tendwa-T/tickets-v3.git

# Navigate into project
cd ticketing-system

# Build the project
mvn clean install
```

### ▶️ Running the App
```bash
# Run with Maven
mvn spring-boot:run
```

The app should now be running at:  
👉 `http://localhost:8080`

---

## 💳 Stripe Integration
- Configure your `.env` or `application.yml` with your Stripe keys:
```yaml
stripe:
  secret-key: sk_test_12345
  publishable-key: pk_test_12345
  webhook-secret: whsec_12345
```

- Start Stripe CLI to forward webhooks:
```bash
stripe listen --forward-to localhost:8080/api/v1/webhook
```

---

## 📡 API Endpoints (Sample)

| Method | Endpoint           | Description               |
|--------|--------------------|---------------------------|
| POST   | `/api/v1/events`   | Create a new Event        |
| POST   | `/api/v1/tickets`  | Create Ticket Types       |
| POST   | `/api/v1/venue`    | Add Venue                 |
| POST   | `/api/v1/checkout` | Purchase Tickets (Stripe) |

*(Swagger UI available at `/swagger-ui.html`)*

---
