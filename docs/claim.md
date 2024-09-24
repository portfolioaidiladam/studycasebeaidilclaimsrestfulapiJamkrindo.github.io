# Claim API Spec

## Get Claims Kur and Pen

Endpoint : GET /api/claims/kur-and-pen


Response Body (Success) :

```json
{
  "data" : {
    "id": 114,
    "lob": "KUR",
    "penyebabKlaim": "Macet",
    "jumlahNasabah": 44957,
    "bebanKlaim": 12.00
  }
}
```
## Post Import Data klaim lob.xlsx 

Endpoint : POST api/claims/import

Request Body :

```json
{
  "form-data" : {
    "file" "File": "upload data klaim lob.xlsx"
  }
}
```

Response Body (Success) :

```json
{
  "data" : "Data imported successfully"
}
```

## POST Transfer To Penampung

Endpoint : POST /api/claims/transfer-to-penampungan


Response Body (Success) :

```json
{
  "data" : "Data berhasil ditransfer"
}
```

