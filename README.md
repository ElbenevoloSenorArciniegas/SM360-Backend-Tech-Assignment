# SM360-Backend-Tech-Assignment

Solution for https://github.com/sm360/backend-tech-assignment

## How to run

Just run the Docker-compose file in the project's root. The db folder must be also in the root.
The docker images are downloaded from dockerhub. The only local dependency is the db script that creates a database.


## Troubleshooting

### Dealer or Listing services fail to start automatically

The services need to connect with the config-service, so they must wait until config-service responds on port 8081.
If they don't wait and start inmediatly, they will crash. Just re-start the failed services and they will be ok.

### Database "backendtest" don´t exist

Sometimes in local, the postgres image fails to excecute the sql script that creates the database, so it needs to be created manually ( ¬.¬)
Nothing special, only a user with credentials user: "postgres" and password: "postgres", and the db named "backendtest".


## And that's all!! 
(I hope jeje... See you in the review)
