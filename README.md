# WoW Achievement Comparison Tool
A few years ago some friends of mine got married after having been introduced to each other through World of Warcraft, which gave me the idea of presenting them with a list of achievements they had completed with each other over the years.

At the time I did that by making some one-time api calls and narrowing the list down 'by hand', but I thought it'd be fun to create a bit of software that could do this for anyone wanting to be a bit nostalgic about their wow history.

This is still a work in progress as I have time or motivation, but the process of obtaining a list of achievements between characters should work.


### How to use
to be continued...


### Example call
```
curl --header "Content-Type: application/json" --request POST --data '{"region":"us", "characters":[{"name":"rhetaiya","server":"argent-dawn"},{"name":"errai","server":"argent-dawn"}]}' localhost:8080/compare
```
