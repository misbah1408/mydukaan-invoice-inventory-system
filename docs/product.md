

# Product APIs

---

## Create Product

**POST** `/api/product/create`

### Headers

```http
Authorization: Bearer <token>
```

### Request Body

```json
{
  "name": "Product Name",
  "price": 9500,
  "stock": 80,
  "category": "Category name",
  "threshold": 10,
  "storeId": 1
}
```

### Success Response

```json
{
  "success": true,
  "message": "Product added successfully",
  "data": {
    "id": 1,
    "name": "Product Name",
    "price": 9500,
    "stock": 80,
    "category": "Category name",
    "threshold": 10,
    "storeId": 1
  }
}
```

### Error Responses

```json
{
  "success": false,
  "message": "Store not found"
}
```

---

## Create Multiple Products

**POST** `/api/product/create-multiple/{storeId}`

### Headers

```http
Authorization: Bearer <token>
```

### Request Body

```json
[
  {
    "name": "Product Name",
    "price": 9500,
    "stock": 80,
    "category": "Category name",
    "threshold": 10
  },
  {
    "name": "Product Name",
    "price": 9500,
    "stock": 80,
    "category": "Category name",
    "threshold": 15
  }
]
```

### Success Response

```json
{
  "success": true,
  "message": "All products added successfully",
  "data": null
}
```

### Error Responses

```json
{
  "success": false,
  "message": "Store not found"
}
```

---

## Get Products by Store

**GET** `/api/products/{storeId}`

### Headers

```http
Authorization: Bearer <token>
```

### Success Response

```json
{
  "success": true,
  "message": "Products fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "Product Name",
      "price": 9500,
      "stock": 80,
      "category": "Category name",
      "threshold": 10,
      "storeId": 1
    }
  ]
}
```

### Error Response

```json
{
  "success": false,
  "message": "No products found for this store"
}
```

---

## Update Product (Partial Update)

**PUT** `/api/product/update/{productId}`

### Headers

```http
Authorization: Bearer <token>
```

### Request Body (Any field optional)

```json
{
  "name": "Updated Name",
  "price": 10000,
  "stock": 50,
  "category": "Updated Category",
  "threshold": 5,
  "storeId": 2
}
```

### Success Response

```json
{
  "success": true,
  "message": "Product updated successfully",
  "data": {
    "id": 1,
    "name": "Updated Name",
    "price": 10000,
    "stock": 50,
    "category": "Updated Category",
    "threshold": 5,
    "storeId": 2
  }
}
```

### Error Responses

```json
{
  "success": false,
  "message": "Product not found"
}
```

```json
{
  "success": false,
  "message": "Store not found"
}
```

---

## Delete Product

**DELETE** `/api/product/delete/{storeId}/{productId}`

### Headers

```http
Authorization: Bearer <token>
```

### Success Response

```json
{
  "success": true,
  "message": "Product deleted successfully",
  "data": null
}
```

### Error Responses

```json
{
  "success": false,
  "message": "Product not found"
}
```

```json
{
  "success": false,
  "message": "Store not found"
}
```

```json
{
  "success": false,
  "message": "Unauthorized access to this product"
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

## Search Product by Name or Category

**GET** `/api/search/{storeId}/{keyword}`

### Headers

```http
Authorization: Bearer <token>
```

### Success Response

```json
{
  "success": true,
  "message": "Product deleted successfully",
  "data": [
    {
      "id": 1,
      "name": "Product Name",
      "price": 9500,
      "stock": 80,
      "category": "Category name",
      "threshold": 10,
      "storeId": 1
    }
  ]
}
```

## Status Codes

* **200** → Success
* **201** → Created
* **400** → Bad Request
* **401** → Unauthorized
* **403** → Forbidden
* **404** → Not Found
* **500** → Server Error


