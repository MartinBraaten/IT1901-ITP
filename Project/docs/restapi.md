# MovieRater REST API

## User
<http://localhost:8080/movierater/{user}>

Methods:
* GET - retrieves a User with filmlists from server
  * URI: host:port/movierater/{user}
  * parameters: none
  * available: jetty, springboot
  * returns JSON with the user. 

```json
{
  "userName" : "user",
  "filmListMap" : {
    "Default" : {
      "filmList" : [ {
        "title" : "film",
        "year" : 2000,
        "rating" : 10,
        "comment" : "",
        "seen" : false
      } ]
    }
  }
}
```

* POST - Adds or replaces a user with name {user}. Also used to add/delete filmlist or add/edit/delete film by overwriting existing user. 
  * URI: host:port/movierater/{user}
  * parameters:
    * body -  application/json; charset=UTF-8
    * user content

```json
{
  "userName" : "user",
  "filmListMap" : {
    "Default" : {
      "filmList" : [ {
        "title" : "EditFilm",
        "year" : 2000,
        "rating" : 10,
        "comment" : "",
        "seen" : false
      } ]
    }
  }
}
```

  * available: jetty, springboot
  * returns JSON with boolean true on success

```json
   true
```

* DELETE - deletes the user with the name {user}
  * URI: host:port/movierater/{user}
  * parameters: none
  * available: jetty, springboot
  * returns JSON with boolean true on success

```json
   true
```