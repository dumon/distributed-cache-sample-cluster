# Distributed Cache sample cluster (with Terracotta usage)

## Description
### Project goal
Demonstrate distributed cache effectiveness with few clients
### Cluster setup
Web-applications + Terracotta as cache provider (synced nodes) + single data storage
            

## Cluster setup
 - Three web Application nodes
 - Data Storage with REST access where App nodes retrieved resources 
 - Two Terracotta nodes as cache provider

## Cluster usage
#### Prerequisites on used environment 
 * Installed Docker
 * Installed JDK (1.8+) 
 * ENV var JAVA_HOME with location of a JDK home dir
 * No bind ports on localhost: 8081, 8082, 8083
 
#### How to run (from project's root dir)
 * Build: ./mvnw clean install
 * Run: docker-compose up --build
 * Wait for web nodes success run ("[SUCCESS_RUN] nodeName" message in log)
 * Navigate to: http://localhost:8081