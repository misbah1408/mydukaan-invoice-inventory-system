# Auth APIs

---

## Register User

**POST** `/api/auth/register`

### Request Body

```json
{
  "name": "Name",
  "email": "email@test.com",
  "password": "password@123",
  "role": "ADMIN"
}
```

### Success Response

```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "email": "email@test.com",
    "id": 24,
    "name": "Name",
    "role": "ADMIN"
  }
}
```

### Error Responses

```json
{
  "success": false,
  "message": "Email already exists"
}
```

```json
{
  "success": false,
  "message": "Invalid input data"
}
```

## Register User with Store

**POST** `/api/auth/register-with-store`

### Request Body

```json
{
  "registerRequest": {
    "name": "Name",
    "email": "email@test.com",
    "password": "password@123",
    "role": "ADMIN"
  },
  "storeRequest": {
    "name": "store name",
    "address": "store address"
  }
}
```

### Notes

* `email` must be unique
* `password` should meet security requirements
* `role` can be: `ADMIN`, `STAFF`

---

## Login User

**POST** `/api/auth/login`

### Request Body

```json
{
  "email": "test@test.com",
  "password": "test@123"
}
```

### Success Response

```json
{
  "success": true,
  "data": {
    "token": "jwt_token_here",
    "user": {
      "id": "123",
      "name": "Test",
      "email": "test@test.com",
      "role": "STAFF"
    }
  }
}
```

### Error Response

```json
{
  "success": false,
  "message": "Invalid credentials"
}
```

---

## Get Current User (Profile)

**GET** `/api/auth/me`

### Headers

```http
Authorization: Bearer <token>
```

### Response

```json
{
  "success": true,
  "user": {
    "id": "123",
    "name": "Test",
    "email": "test@test.com",
    "role": "STAFF"
  }
}
```

---

## Update User

**PUT** `/api/admin/update/{userId}`

### Request Body

```json
{
  "name": "Updated Name",
  "role": "ADMIN"
}
```

### Response

```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "email": "name@test.com",
    "id": 1,
    "name": "Name",
    "role": "ADMIN"
  }
}
```

---

## Delete User

**DELETE** `/api/admin/users/{id}`

### Response

```json
{
  "success": true,
  "message": "User deleted successfully",
  "data": null
}
```

---

## Common Error Format

```json
{
  "success": false,
  "message": "Error message here"
}
```

---

## Status Codes

* **200** → Success
* **201** → Created
* **400** → Bad Request
* **401** → Unauthorized
* **403** → Forbidden
* **404** → Not Found
* **500** → Server Error
