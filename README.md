# Ktor example of usage of Postgis

## Installation

- Copy your shape files into the **postgres** folder
- Rename them to ***test***.shp/dbf

```bash
./build.sh
```

## Run

```bash
./run.sh
```

## Endpoints

GET
```curl
http://localhost:8080/get/{pointId}
```
Retrieve a point

---

POST
```curl
http://localhost:8080/create
```
Body:
```json
{
	"pointId": {pointId},
	"x": {x},
	"y": {y}
}
```

**Insert a new point**

---

POST
```curl
http://localhost:8080/update
```
Body:
```json
{
	"pointId": {pointId},
	"x": {x},
	"y": {y}
}
```

**Update existing point**

---

GET
```curl
http://localhost:8080/delete/{pointId}
```
**Delete a point**

---

GET
```curl
http://localhost:8080/contains/{pointId}
```

**Return whether the point includes in any shapes**

---

## Features

- CRUD functions
- Contains function which is able to determine whether a point is included in any shapes
- Swagger UI (Work in progress)

## Used technologies

- Kotlin
- KTor
- Exposed
- Hikari
- [KTor OpenAPI Generator](https://github.com/papsign/Ktor-OpenAPI-Generator)

