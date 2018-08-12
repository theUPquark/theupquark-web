# WoW Achievement Comparison Tool
The inspiration for this project is a bit of a nostalgia trip to identity moments shared between multiple characters. This is done by making multiple queries to the World of Warcraft Web-API for data on completed achievements, and then narrowing down to a subset of achievements that share a similiar (if not identical) timestamp.

This is still a work in progress as I have time or motivation, and a general attempt at to check-out the material-ui library for components within a React application.

### How to use
to be continued...


### Example call
```
curl --header "Content-Type: application/json" --request POST --data '{"region":"us", "characters":[{"name":"rhetaiya","server":"argent-dawn"},{"name":"errai","server":"argent-dawn"}]}' localhost:8080/compare
```
