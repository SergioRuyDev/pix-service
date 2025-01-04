db.createUser(
    {
        user: "coffeeandit",
        pwd: "coffeeandit",
        roles: [
            {
                role: "readWrite",
                db: "pix"
            }
        ]
    }
);
db.createCollection("pix_transaction");
