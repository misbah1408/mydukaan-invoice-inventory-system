# 💰 Ledger APIs

Base URL: `/api/leadger`
All endpoints require authentication:

```http
Authorization: Bearer <token>
```

---

## ➕ Create Ledger

**POST** `/api/leadger/create-ledger`

Creates a new ledger (Cash/Bank account).

### Request Body

```json
{
  "displayName": "Main Cash",
  "storeId": 1,
  "accountType": "CASH",
  "balance": 5000
}
```

### Request Fields

| Field       | Type   | Description     |
|-------------|--------|-----------------|
| displayName | string | Ledger name     |
| storeId     | long   | Store ID        |
| accountType | string | CASH or BANK    |
| balance     | number | Initial balance |

### Success Response (200)

```json
{
  "success": true,
  "message": "Ledger created successfully",
  "data": {
    "id": 1,
    "displayName": "Main Cash",
    "storeId": 1,
    "accountType": "CASH",
    "balance": 5000,
    "createdAt": "2026-04-14T10:00:00",
    "updatedAt": "2026-04-14T10:00:00"
  }
}
```

### Error Response

```json
{
  "success": false,
  "message": "Store not found"
}
```

---

## 📋 Get Ledgers by Store

**GET** `/api/leadger/ledgers?storeId={storeId}`

Fetch all ledgers associated with a store.

### Query Parameters

| Name    | Type | Description |
|---------|------|-------------|
| storeId | long | Store ID    |

### Success Response (200)

```json
{
  "success": true,
  "message": "Ledgers fetched successfully",
  "data": [
    {
      "id": 1,
      "displayName": "Main Cash",
      "storeId": 1,
      "accountType": "CASH",
      "balance": 5000,
      "createdAt": "2026-04-14T10:00:00",
      "updatedAt": "2026-04-14T10:00:00"
    }
  ]
}
```

### Error Response

```json
{
  "success": false,
  "message": "No ledgers found"
}
```

---

## ✏️ Update Ledger

**PUT** `/api/leadger/update-ledger?ledgerId={ledgerId}`

Updates an existing ledger.

### Query Parameters

| Name     | Type | Description |
|----------|------|-------------|
| ledgerId | long | Ledger ID   |

### Request Body

```json
{
  "displayName": "Updated Cash",
  "storeId": 1,
  "accountType": "CASH",
  "balance": 7000
}
```

### Success Response (200)

```json
{
  "success": true,
  "message": "Ledger updated successfully",
  "data": {
    "id": 1,
    "displayName": "Updated Cash",
    "storeId": 1,
    "accountType": "CASH",
    "balance": 7000,
    "createdAt": "2026-04-14T10:00:00",
    "updatedAt": "2026-04-14T11:00:00"
  }
}
```

### Error Response

```json
{
  "success": false,
  "message": "Ledger not found"
}
```

---

## ❌ Delete Ledger

**DELETE** `/api/leadger/delete?ledgerId={ledgerId}`

Deletes a ledger.

### Query Parameters

| Name     | Type | Description |
|----------|------|-------------|
| ledgerId | long | Ledger ID   |

### Success Response (200)

```json
{
  "success": true,
  "message": "Ledger deleted successfully",
  "data": "Ledger deleted successfully"
}
```

### Error Response

```json
{
  "success": false,
  "message": "Ledger not found"
}
```

---
