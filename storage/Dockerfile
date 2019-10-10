FROM openjdk:8
ENV port=8081
ENV response_delay=1000
WORKDIR /usr/storage
COPY ./target/storage-0.0.1-SNAPSHOT.war ./storage.war
CMD if [ "$debug_port" = "" ] ;\
    then\
        java -jar ./storage.war --server.port=$port --app.response.delay=$response_delay `echo $args | sed 's/,/ --/g' | sed -e 's/^/--/' | sed -e 's/^--$//'`;\
    else\
        java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$debug_port ./storage.war --server.port=$port --app.response.delay=$response_delay `echo $args | sed 's/,/ --/g' | sed -e 's/^/--/' | sed -e 's/^--$//'`;\
    fi