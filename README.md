# Drone Project

You can read the task from Drones.docx file and this file for describing the answer

1 - Create a drone with it's properties (id, serial, state, batteryCapacity, model) <br>
- The drone model has name static with dynamic max weight for each one <br>
- The drone state is static using enum <br>
- id and serial is unique

2 - Create Item with it's properties (id, name, code, weight, imageUrl)
- images uploaded to the project in folder [src/main/resources/static/images/], but in real project we should use real sftp server
- id and code is unique
- Item weight must be lower than or equals drone model max (max weight) colum, to make sure that we have a model can hold this item

3 - Trip is managing the connection between Drone and Items
- we use TripItems to manage the item and the count of it per trip

4 - Drone Scheduler is cron jobs for:
- Decrease drone battery percentage (every 10 minutes)
- Change Drone state (change 3 states to it is next states, every 15 minutes)

#### Drone Model Name enum:
* LIGHT_WEIGHT
* MIDDLE_WEIGHT
* CRUISER_WEIGHT
* HEAVY_WEIGHT

#### Drone State enum:
* IDLE
* LOADING
* LOADED
* DELIVERING
* DELIVERED
* RETURNING

## I tried to use:
- Spring Data Jpa
- Java 17
- Functional programming
- Clean code

#### Things can help you:
* [Swagger UI](http://localhost:8090/drone/swagger-ui/#/)
* [Cron expression](https://www.freeformatter.com/cron-expression-generator-quartz.html)
