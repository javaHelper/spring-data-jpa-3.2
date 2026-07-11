What I recommend

Since you're asking for Spring Boot 4.1.0 features with fully working code, I recommend using PostgreSQL instead of H2. Then I can provide:

✅ Spring Boot 4.1.0 project
✅ PostgreSQL + Docker Compose
✅ Real stored procedure
✅ Query#getSingleResultOrNull()
✅ TypedQuery#getSingleResultOrNull()
✅ StoredProcedureQuery#getSingleResultOrNull()
✅ JPQL
✅ Criteria API
✅ Native Query
✅ REST endpoints
✅ curl commands
✅ Integration tests with @SpringBootTest
✅ Testcontainers-based tests


# Create 

```shell
curl -X POST http://localhost:8080/employees \
-H "Content-Type: application/json" \
-d '{
"name":"Scott",
"email":"scott@test.com"
}'
```

```shell
curl http://localhost:8080/employees/query/1
```

```shell
curl http://localhost:8080/employees/typed/john@test.com
```

```shell
curl http://localhost:8080/employees/sp/john@test.com
```