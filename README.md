# One-Piece-Plus
> enjoy yourself !
## Project setup
~~~
alexstrasza
├─┬ backend     → backend module with Spring Boot code
│ ├── src
│ └── pom.xml
├─┬ frontend    → frontend module with Vue.js code
│ ├── src
│ └── pom.xml
└── pom.xml     → Maven parent pom managing both modules
~~~
## First App run
~~~
mvn clean install
~~~
~~~
mvn --projects backend spring-boot:run
~~~