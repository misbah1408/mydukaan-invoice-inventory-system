# Invoice APIs

---

## Create Invoice

**POST** `/api/invoice/create-invoice`

### Headers

```http
Authorization: Bearer <token>
```

### Request Body

```json
{
  "storeId": 1,
  "customerId": 2,
  "invoiceNumber": "INV-001",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ],
  "payments": [
    {
      "ledgerId": 1,
      "amount": 500,
      "method": "CASH",
      "transactionId": "TXN123"
    }
  ]
}
```

### Success Response

```json
{
  "success": true,
  "message": "Invoice created",
  "data": {
    "id": 1,
    "invoiceNumber": "INV-2026-XXXXXX",
    "storeId": 1,
    "customerId": 2,
    "customerName": "John",
    "subTotal": 1000,
    "gstAmount": 180,
    "totalAmount": 1180,
    "paidAmount": 500,
    "dueAmount": 680,
    "status": "PARTIAL",
    "createdAt": "Date time",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Product Name",
        "quantity": 2,
        "price": 500,
        "total": 1000
      }
    ],
    "payments": [
      {
        "id": 1,
        "ledgerId": 1,
        "ledgerName": "Cash",
        "amount": 500,
        "method": "CASH",
        "status": "SUCCESS",
        "transactionId": "TXN123"
      }
    ]
  }
}
```

### Important Logic

* GST = **18%**
* Stock is **reduced automatically**
* Customer balance is **updated**
* Invoice status:

    * `PENDING` → no payment
    * `PARTIAL` → partial payment
    * `PAID` → full payment

---

## Get Invoice by ID

**GET** `/api/invoice/{id}`

### Headers

```http
Authorization: Bearer <token>
```

### Success Response

```json
{
  "success": true,
  "message": "Invoice fetched successfully",
  "data": {
    
  }
}
```

### Error Response

```json
{
  "success": false,
  "message": "Invoice not found"
}
```

---

## Get All Invoices (by Store)

**GET** `/api/invoice?storeId={storeId}`

### Headers

```http
Authorization: Bearer <token>
```

### Success Response

```json
{
  "success": true,
  "message": "Successfully!!!",
  "data": [
    {
      "id": 1,
      "invoiceNumber": "INV-XXXX",
      "totalAmount": 1180,
      "status": "PAID"
    }
  ]
}
```

---

## Update Invoice

**PUT** `/api/invoice/update/{invoiceId}`

### Headers

```http
Authorization: Bearer <token>
```

### Request Body

(Same as create invoice)

```json
{
  "storeId": 1,
  "customerId": 2,
  "items": [
    {
      "productId": 1,
      "quantity": 3
    }
  ]
}
```

### Behavior ⚠️

* Old invoice is **deleted (reversed)**
* New invoice is **created again**
* Stock, ledger, and customer balance are recalculated

### Success Response

```json
{
  "success": true,
  "message": "Invoice created",
  "data": {
  }
}
```

---

## Delete Invoice

**DELETE** `/api/invoice/{id}`

### Headers

```http
Authorization: Bearer <token>
```

### Success Response

```json
{
  "success": true,
  "message": "Invoice deleted",
  "data": null
}
```

### Internal Effects ⚠️

When deleting:

* ✅ Customer balance is reversed
* ✅ Ledger balances are reversed
* ✅ Product stock is restored

### Error Response

```json
{
  "success": false,
  "message": "Invoice not found"
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
* **404** → Not Found
* **500** → Server Error

---