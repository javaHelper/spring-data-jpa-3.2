# Get info – uses ||, id(), version()
curl http://localhost:8080/products/1/info
# → {"concatenated":"Laptop - High-performance portable computer","id":1,"version":0}

# Search by full text – uses || in WHERE
curl "http://localhost:8080/products/search?name=Mouse&description=Wireless%20ergonomic%20mouse"

# Check by id and version – uses id() and version() in WHERE
curl "http://localhost:8080/products/check?id=1&version=0"