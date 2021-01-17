db.createUser(
    {
        user: "spring-app",
        pwd: "password123",
        roles: [
            {
                role: "readWrite",
                db: "books"
            }
        ]
    }
);
