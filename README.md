# ride-share-backend

This Repo contains generated JAR files and a README on how to go about build the rideshare services

## Build and Run

- To run the database for our backend services you will need Docker installed. If you don't no worries you can [install docker](https://docs.docker.com/engine/install/)

Once Docker is installed run the mysql service from the `docker-compose.yml` file. To do this just run `docker-compose up` from this root folder

To get the services up and running:

- Ensure you have java running on your machine.
- Ensure you have Apache maven build tool running on your computer
- Then run `mvn spring-boot:run`
- Ensure the port selected in `src/main/resources/application.yaml` is free before running the app
