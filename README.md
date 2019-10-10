# Distributed Cache sample cluster

## Description
Project represents distributed cache effectiveness for few Application nodes (single page web app)
Resources are retrieving from data source, that responds with delay, but cache saves this time for repeated call from any App's node.
Cache effect easily demonstrated by resource loading time, that showed on page.

## Cluster setup
 - Two web Application nodes
 - Data Storage with REST access where App nodes retrieved resources 
 - Redis server as cache-aside resource

## Cluster usage
#### Prerequisites on used environment 
 * Installed Docker
 * Installed JDK (1.8+) 
 * ENV var JAVA_HOME with location of a JDK home dir
 * No bind ports on localhost: 8081, 8082
 
#### How to run (from project's root dir)
 * Build: ./mvnw clean install
 * Run: docker-compose up --build
 * Navigate to: http://localhost:8081