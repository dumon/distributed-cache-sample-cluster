FROM openjdk:8
ENV port=8080
ENV args=""
ENV debug_port=""
WORKDIR /usr/app
COPY target/app-0.0.1-SNAPSHOT.war ./app.war
CMD if [ "$debug_port" = "" ] ;\
    then\
        java -jar ./app.war --server.port=$port `echo $args | sed 's/,/ --/g' | sed -e 's/^/--/' | sed -e 's/^--$//'`;\
    else\
        java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$debug_port ./app.war --server.port=$port `echo $args | sed 's/,/ --/g' | sed -e 's/^/--/' | sed -e 's/^--$//'`;\
    fi