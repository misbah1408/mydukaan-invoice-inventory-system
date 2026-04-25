# 👤 Customer APIs

Base URL: `/api/customers`
All endpoints require authentication:

```http
Authorization: Bearer <token>
```

---

## ➕ Create Customer

**POST** `/api/customers`

Creates a new customer.

### Request Body

```json
{
  "name": "John Doe",
  "phone": "9876543210",
  "email": "john@example.com",
  "address": "Bangalore",
  "storeId": 1,
  "balance": 1000
}
```

### Success Response (201)

```json
{
  "success": true,
  "message": "Customer created successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "phone": "9876543210",
    "email": "john@example.com",
    "address": "Bangalore",
    "storeId": 1,
    "storeName": "My Store",
    "balance": 1000,
    "createdAt": "2026-04-14T10:00:00",
    "updatedAt": "2026-04-14T10:00:00"
  }
}
```

---

## 🔍 Get Customer by ID

**GET** `/api/customers/{id}`

Fetch a customer using ID.

### Success Response (200)

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "John Doe",
    "phone": "9876543210",
    "email": "john@example.com",
    "address": "Bangalore",
    "storeId": 1,
    "storeName": "My Store",
    "balance": 1000,
    "createdAt": "2026-04-14T10:00:00",
    "updatedAt": "2026-04-14T10:00:00"
  }
}
```

### Error Response

```json
{
  "success": false,
  "message": "Customer not found"
}
```

---

## 📋 Get Customers by Store

**GET** `/api/customers/store/storeId`

Fetch all customers for a specific store.

### Success Response (200)

```json
{
  "success": true,
  "message": "Customers fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "phone": "9876543210",
      "email": "john@example.com",
      "address": "Bangalore",
      "storeId": 1,
      "storeName": "My Store",
      "balance": 1000,
      "createdAt": "2026-04-14T10:00:00",
      "updatedAt": "2026-04-14T10:00:00"
    }
  ]
}
```

---

## ✏️ Update Customer

**PUT** `/api/customers/customerId`

Updates an existing customer.

### Request Body

```json
{
  "name": "John Updated",
  "phone": "9876543210",
  "email": "john@example.com",
  "address": "Updated Address",
  "storeId": 1,
  "balance": 2000
}
```

### Success Response (200)

```json
{
  "success": true,
  "message": "Customer updated successfully",
  "data": {
    "id": 1,
    "name": "John Updated",
    "phone": "9876543210",
    "email": "john@example.com",
    "address": "Updated Address",
    "storeId": 1,
    "storeName": "My Store",
    "balance": 2000,
    "createdAt": "2026-04-14T10:00:00",
    "updatedAt": "2026-04-14T11:00:00"
  }
}
```

---

## ❌ Delete Customer

**DELETE** `/api/customers/customerId`

Deletes a customer.


### Success Response (200)

```json
{
  "success": true,
  "message": "Customer deleted successfully",
  "data": "Customer deleted successfully"
}
```

### Error Response

```json
{
  "success": false,
  "message": "Customer not found"
}
```

---
