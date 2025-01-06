db.createUser(
    {
        user: "sergioruy",
        pwd: "sergioruy",
        roles: [
            {
                role: "readWrite",
                db: "pix"
            }
        ]
    }
);
db.createCollection("pix_transaction");
