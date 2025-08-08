# DTO (Data Transfer Object)

This directory contains request/response data structures used exclusively for server communication. Keep DTOs separate from domain models in `model/` so server schema changes do not ripple through the app.

## Purpose

- Mirror server API schemas 1:1 (field names, types, nullability)
- Support serialization (e.g., Moshi)
- Do not contain business logic, validation, or defaults

## Usage Rules

- Use DTOs only in the network/data layers. UI/ViewModel/domain must work with domain models in `model/`.
- Convert DTOs to domain models in the `repository` layer.
- Preserve server field names and nullability exactly.

## Naming

- Requests: `XxxRequest`
- Responses: `XxxResponse`
- Optionally use `XxxDto` for non-request/response shapes.

## Serialization (Moshi)

When server fields differ from Kotlin naming conventions, map them with `@Json(name = "...")`.

```kotlin
import com.squareup.moshi.Json

data class SampleResponse(
    @Json(name = "user_id") val userId: String?,
    @Json(name = "user_name") val userName: String?
)
```

## Mapping DTO ↔ Model

- Implement mapping only in the `repository` layer (prefer extension functions).
- Place mappers under `app/src/main/java/com/example/myapplication/repository/mappers/`.

Example:

```kotlin
// DTO
// app/src/main/java/com/example/myapplication/network/dto/LoginDtos.kt

data class LoginResponse(
    val id: String,
    val email: String,
    val name: String,
    val token: String
)

// Model
// app/src/main/java/com/example/myapplication/model/User.kt
// data class User(
//   val id: String,
//   val email: String,
//   val name: String,
//   val isLoggedIn: Boolean,
//   val lastLoginTime: Long
// )

// Mapper
package com.example.myapplication.repository.mappers

import com.example.myapplication.model.User
import com.example.myapplication.network.dto.LoginResponse

fun LoginResponse.toModel(now: Long = System.currentTimeMillis()): User =
    User(
        id = id,
        email = email,
        name = name,
        isLoggedIn = true,
        lastLoginTime = now
    )
```

## Checklist

- [ ] No business logic or validation inside DTOs
- [ ] Preserve server field names and nullability
- [ ] Use `@Json` for non-camel-case fields
- [ ] Perform DTO↔Model mapping in `repository`
- [ ] UI/ViewModel/domain use `model/` types only

## Templates

```kotlin
// Request
data class XxxRequest(
    val fieldA: String,
    val fieldB: Int
)

// Response
data class XxxResponse(
    val id: String,
    val value: String?
)
```

## Change Management

- On server API changes, update DTOs first, then adjust repository mappings.
- Keep domain models stable to minimize UI impact.

## Testing

- Add unit tests for mappers to verify DTO → Model conversions.
