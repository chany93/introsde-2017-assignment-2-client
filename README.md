# introsde-2017-assignment-2-client

## 1. Identification

Giovanni Rafael Vuolo giovannirafael.vuolo@studenti.unitn.it

Partner: Raffaele Perini

My Server: https://assignsde2.herokuapp.com/assign2
Partner's server: https://intro2sdeass2.herokuapp.com/sdelab/

Partner's github repo: https://github.com/javekk/introsde-2017-assignment-2-client

### 2. Project

This is a REST server, which  People and Activities. A person can have a number of Activities(0 to n), to be stored as preferred activities. 
It is possible to perform all the CRUD operations for people.

#### 2.1. Code

The project is composed by a _model_,in which reside the entities, a _dao_, which manages the entities and links the database to the _resources_

* _Model_: has the entities People and Acitivity with all their getters and setters and some other methods to handle them, plus an enumetarive class ActivityType (containing the 5 types for the activities)
* _Dao_: has PersonActivityDao, which provides the connection to the database
* -Resources_: is where reside the classes used to recive requests and send responses from/to the client.
  * PersonResourceCollection: reachable through path /person returns the list of all people and connectes to PersonResource
  * PersonResource: reachable through path /person/{id}, contatins all the methods (GET, POST, DELETE, PUT) for managing a person and its activities.
  * TypeResource: reachable through path /activity_types returns all types from the ActivityType class.
  * DBResource: reachable through path /database_init, which initialize the db (if necessary).
  
#### 2.2. Task
The server can do the following tasks both in __JSON__ and __XML__ format:
 * 0#: __POST /database_init__, initialize the database creating 5 new people.
 * 1#: __GET /person__, returns the list of all the people in the database (wrapped under the root element "people").
 * 2#: __GET /person/{id}__, returns all the information of the person identified by {id}.
 * 3#: __PUT /person/{id}__, updates the personal information of the person identified by {id} (only the person's information, not activity preferences)
 * 4#: __POST /person__, creates a new person and returns the newly created person with its assigned id
 * 5#: __DELETE /person/{id}__, deletes the person identified by {id} from the system
 * 6#: __GET /activity_types__, returns the list of activity_types supported by the model in the following formats:
 ```
<activityTypes>
    <activity_type>Social</activity_type>
    <activity_type>Sport</activity_type>
    <activity_type>Culture</activity_type>
    <activity_type>Entertainment</activity_type>
    <activity_type>Work</activity_type>
</activityTypes>
```
```
  [
    "Social",
    "Sport",
    "Culture",
    "Entertainment",
    "Work"
  ]
```
 * 7#: __GET /person/{id}/{activity_type}__, returns the list of activities of {activity_type} (e.g. Social) for person identified by {id}
 * 8#: __GET /person/{id}/{activity_type}/{activity_id}__, returns the activities of {activity_type} (e.g. Social) identified by {activity_id} for person identified by {id}
 * 10#: __PUT /person/{id}/{activity_type}/{activity_id}__, updates the value for the {activity_type} (e.g., Social) identified by {activity_id}, related to the person identified by {id}
 * 11#: __GET /person/{id}/{activity_type}?before={beforeDate}&after={afterDate}__, returns the activities of {activity_type} (e.g., Social) for person {id} which {start_date} is in the specified range of date. (1 point)

## 3. Execution
It is possible perform all the request above on the following heroku server
or run App.java as application (in package introsde.assign) and connect to localhost:5900/assign2

  ```
  https://assignsde2.herokuapp.com/assign2/
  
  ```
## 4. Additional Notes
Request#9 is not implemented




