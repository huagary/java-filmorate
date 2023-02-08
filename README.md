# java-filmorate
Template repository for Filmorate project.
![Filmorate database diagram](https://github.com/huagary/java-filmorate/blob/main/images/filmorate_db.jpg)
All users query:
SELECT * FROM user;

User friends ids query:
SELECT user_id FROM friendship WHERE user_id=id AND confirmed;

All films query:
SELECT * FROM film;

Film likes query:
SELECT user_id FROM film_likes WHERE film_id=id;
