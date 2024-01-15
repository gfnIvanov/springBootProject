#!/bin/bash

mvn spring-boot:run &

while ! curl -s http://127.0.0.1:9090 | grep DOCTYPE > /dev/null; do
    sleep 1
done

curl -X POST http://127.0.0.1:9090/add-categories

tail -f /dev/null
