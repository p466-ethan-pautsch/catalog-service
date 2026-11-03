
# Catalog Service REST API Demonstration

## GET /books - 200 OK

Command:
```powershell
curl.exe -i http://localhost:9001/books
```
gi
Response:
```text
HTTP/1.1 200
Content-Type: application/json

[]
```

## POST /books - 201 Created

Command:
```powershell
$body = '{"isbn":"1234567892","title":"Northern Lights","author":"Lyra Silverstar","price":9.90}'
Invoke-WebRequest -UseBasicParsing -Uri "http://localhost:9001/books" -Method POST -ContentType "application/json" -Body $body
```

Response:
```text
HTTP/1.1 201
Content-Type: application/json

{"isbn":"1234567892","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}
```

## POST /books - 422 Unprocessable Entity

Command:
```powershell
Invoke-WebRequest -UseBasicParsing -Uri "http://localhost:9001/books" -Method POST -ContentType "application/json" -Body $body
```

Response:
```text
HTTP Status: 422
Response: A book with ISBN 1234567892 already exists.
```

## POST /books - 400 Bad Request

Command:
```powershell
$invalidBody = '{"isbn":"123ABC456Z","title":"","author":"Jon Snow","price":9.90}'
Invoke-WebRequest -UseBasicParsing -Uri "http://localhost:9001/books" -Method POST -ContentType "application/json" -Body $invalidBody
```

Response:
```text
HTTP Status: 400

{"isbn":"The ISBN format must be valid.","title":"The book title must be defined."}
```

## GET /books/1234567891 - 200 OK

Command:
```powershell
curl.exe -i http://localhost:9001/books/1234567891
```

Response:
```text
HTTP/1.1 200
Content-Type: application/json

{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}
```

This response was obtained during the initial API tests before restarting the application.

## GET /books/9999999999 - 404 Not Found

Command:
```powershell
curl.exe -i http://localhost:9001/books/9999999999
```

Response:
```text
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8

The book with ISBN 9999999999 was not found.
```

## PUT /books/1234567892 - 200 OK

Command:
```powershell
$updatedBody = '{"isbn":"1234567892","title":"Northern Lights Updated","author":"Lyra Silverstar","price":12.50}'
Invoke-WebRequest -UseBasicParsing -Uri "http://localhost:9001/books/1234567892" -Method PUT -ContentType "application/json" -Body $updatedBody
```

Response:
```text
HTTP/1.1 200
Content-Type: application/json

{"isbn":"1234567892","title":"Northern Lights Updated","author":"Lyra Silverstar","price":12.5}
```

## DELETE /books/1234567892 - 204 No Content

Command:
```powershell
curl.exe -i -X DELETE http://localhost:9001/books/1234567892
```

Response:
```text
HTTP/1.1 204
```

No response body is returned.
