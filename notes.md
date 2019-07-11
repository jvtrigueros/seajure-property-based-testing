```
docker run --rm -it -p 8000:8000 amazon/dynamodb-local -jar DynamoDBLocal.jar -inMemory -sharedDb
```

```
DYNAMO_ENDPOINT=http://localhost:8000 npx dynamodb-admin
```