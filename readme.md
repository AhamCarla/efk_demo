# Establishment of EFK
Set up docker-compose.yml
``` yml
version: "3"
services:
  elk_server:
    image: lis0x90/efk 
    container_name: efk_server
    ports:
      - "5601:5601"
      - "9200:9200"
      - "24224:24224"
    volumes:
      - ./config:/etc/fluent
      - ./log:/var/log
    logging:
      driver: "json-file"
      options:
         max-size: "200k"
         max-file: "10"
```

## Set up fluent.conf
``` xml
<source>
    @type forward
    port 24224
    bind 0.0.0.0
</source>

<match *.**>
    @type copy
    <store>
    @type elasticsearch
    host localhost
    port 9200
    logstash_format true
    logstash_prefix fluentd
    logstash_dateformat %Y%m%d
    include_tag_key true
    type_name access_log
    tag_key @log_name
    flush_interval 1s
    </store>
    <store>
    @type stdout
    </store>
</match>
```

# Run EFK
```
docker-compose up -d 
```
> -d means run in background

# Run a Http Server for EFK
Set up a docker-compose.yml as well
``` yml
version: '3'
services:
  http_server:
    image: httpd
    container_name: http_server
    ports:
      - "8088:80"
    logging:
      driver: "fluentd"
      options:
        fluentd-address: localhost:24224
        tag: httpd_server
```
> docker-compose up
Then visit
> http://localhost:5601/   For Kibana
> http://localhost:9200/_search?pretty   For indices

# Application settings
Add two dependencies in your pom.xml or build.gradle
``` xml
<!-- https://mvnrepository.com/artifact/org.fluentd/fluent-logger -->
<dependency>
    <groupId>org.fluentd</groupId>
    <artifactId>fluent-logger</artifactId>
    <version>0.3.4</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.sndyuk/logback-more-appenders -->
<dependency>
    <groupId>com.sndyuk</groupId>
    <artifactId>logback-more-appenders</artifactId>
    <version>1.7.5</version>
</dependency>
```

Then log in your application with below logger, it will redirect to kibana.
``` java
     private static FluentLogger log = FluentLogger.getLogger("demo.TestController","localhost",24224);
```
