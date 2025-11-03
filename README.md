# 🎟️ Coupon Management System

A flexible, extensible **Coupon Management API** built with **Spring Boot 3**, **JPA/Hibernate**, and **MySQL**.
It supports multiple coupon types, dynamic conditions, and clean architecture for future extensions.

---

## 🚀 Features

✅ **Multiple Coupon Types**

* Discount (Percentage/Flat)
* BXGY (Buy X Get Y) *(Progress)*
* Cashback
* Trip Discount( Progress)

✅ **Flexible Condition System**

* Define coupon conditions dynamically using JSON data
* Support for compound conditions (AND / OR logic) 
* Example: “Cart value > ₹500 AND UserType = NEW”

✅ **Clean Architecture**

* Uses **Strategy Pattern** for extensible coupon types
* DTO-based API responses (no infinite recursion issues)
* Modular and future-proof

✅ **Persistence Layer**

* Backed by **MySQL** with JPA/Hibernate ORM

✅ **RESTful APIs**

* CRUD operations for coupons
* Endpoints for coupon application and validation

---

## ⚙️ Tech Stack

| Layer           | Technology      |
| --------------- | --------------- |
| Backend         | Spring Boot 3.x |
| ORM             | Hibernate / JPA |
| Database        | MySQL           |
| Build Tool      | Maven           |
| Language        | Java 17         |
| JSON Processing | Jackson         |

---

## 🧾 API Endpoints

### **1️⃣ Create Coupon**

**POST** `/api/coupon`
**Content-Type:** `application/json`

**Sample Payload:**

```json
{
  "code": "SAVE10",
  "type": "DISCOUNT",
  "description": "10% off on orders above ₹500",
  "expiryDate": "2025-12-31T23:59:59",
  "isActive": true,
  "usageLimit": 100,
  "couponDetails": {
    "discountType": "PERCENTAGE",
    "discountValue": 10,
    "maxDiscountAmount": 200,
    "discountScope": "CART_LEVEL"
  },
  "conditions": [
    {
      "type": "MIN_CART_VALUE",
      "conditionData": "{\"minValue\": 500}",
      "errorMessage": "Cart value must be at least ₹500"
    }
  ]
}
```

---

### **2️⃣ Get All Coupons**

**GET** `/api/coupons`
**Content-Type:** `application/json`

---

### **3️⃣ Get Coupon by ID**

**GET** `/api/coupon/{id}`
**Content-Type:** `application/json`

---

### **4️⃣ Update Coupon**

**PUT** `/api/coupon/{id}`
**Content-Type:** `application/json`

**Sample Payload:**

```json
{
  "code": "SAVE10",
  "type": "DISCOUNT",
  "description": "10% off on orders above ₹500",
  "expiryDate": "2025-12-31T23:59:59",
  "isActive": true,
  "usageLimit": 100,
  "couponDetails": {
    "discountType": "PERCENTAGE",
    "discountValue": 10,
    "maxDiscountAmount": 200,
    "discountScope": "CART_LEVEL"
  },
  "conditions": [
    {
      "type": "MIN_CART_VALUE",
      "conditionData": "{\"minValue\": 500}",
      "errorMessage": "Cart value must be at least ₹500"
    }
  ]
}
```

---

### **5️⃣ Delete Coupon**

**DELETE** `/api/coupon/{id}`

---

### **6️⃣ Get Applicable Coupons**

**POST** `/api/applicable-coupons`
**Content-Type:** `application/json`

**Sample Payload:**

```json
{
  "userId": "USER123",
  "userType": "NEW",
  "paymentMethod": "UPI",
  "items": [
    { "productId": "PROD001", "productName": "Laptop", "categoryId": "ELECTRONICS", "price": 50000, "quantity": 1 },
    { "productId": "PROD002", "productName": "Mouse", "categoryId": "ACCESSORIES", "price": 500, "quantity": 2 }
  ],
  "location": { "city": "Mumbai", "state": "Maharashtra", "pincode": "400001" }
}
```

---

### **7️⃣ Apply Coupon**

**POST** `/api/apply-coupon/{id}`
**Content-Type:** `application/json`

**Sample Payload:**

```json
{
  "userId": "USER123",
  "userType": "REGULAR",
  "items": [
    { "productId": "PROD001", "productName": "Laptop", "price": 50000, "quantity": 1 }
  ]
}
```

---

## 🗄️ Database Tables Overview

* **coupons** → Stores general coupon details
* **discount_coupons** → Inherits from coupons (JOINED strategy)
* **coupon_conditions** → Stores condition data linked to coupons

---

## 🧱 Future Enhancements

* Implement BXGY and Trip Discount logic
* Complete compound condition engine (nested AND/OR)
* Add caching for frequently used coupons
* Add audit trail (createdBy, updatedBy)
* Integrate with Redis for performance boost

---



4. Test APIs via **Postman**
