# Store APIs

---

## Create Store

**POST** `/api/create-store`

### Headers

```http
Authorization: Bearer <token>
```

### Request Body

```json
{
  "name": "Business Name",
  "address": "Address"
}
```

### Success Response

```json
{
  "success": true,
  "message": "Store created successfully!",
  "data": {
    "id": 1,
    "name": "Business Name",
    "createdAt": "Date time",
    "userName": "Owner name"
  }
}
```

### Error Responses

```json
{
  "success": false,
  "message": "Store name is required"
}
```

```json
{
  "success": false,
  "message": "User not found"
}
```

## Get Store

**GET** `/api/stores`

### Headers

```http
Authorization: Bearer <token>
```

### Request Body

```json
{
  "success": true,
  "message": "Store created successfully!",
  "data": [
    {
      "id": 1,
      "name": "Business Name",
      "createdAt": "Date time",
      "user": {
        "email": "email@test.com",
        "id": 24,
        "name": "Name",
        "role": "ADMIN",
        "createdAt": "Date time",
        "updatedAt": "Date time"
      }
    },
    {
      "id": 2,
      "name": "Business Name",
      "createdAt": "Date time",
      "user": {
        "email": "email@test.com",
        "id": 24,
        "name": "Name",
        "role": "ADMIN",
        "createdAt": "Date time",
        "updatedAt": "Date time"
      }
    }
  ]
}
```

## Delete Store

**POST** `/api/delete-store/{storeId}`

### Headers

```http
Authorization: Bearer <token>
```

### Success Response

```json
{
  "success": true,
  "message": "Store Deleted Successfully!!!",
  "data": {
    "id": 1,
    "name": "Business Name",
    "createdAt": "Date time",
    "username": "Owner Name"
  }
}
```

### Error Response

```json
{
  "success": false,
  "message": "User not found!!!"
}
```

```json
{
  "success": false,
  "message": "Store with {id} Not Found!!!"
}
```

```json
{
  "success": false,
  "message": "Unauthorized User!!!"
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
