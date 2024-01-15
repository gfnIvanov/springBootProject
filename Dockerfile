FROM maven:3.8.5-openjdk-17
WORKDIR /app
COPY . .
RUN mvn clean install
RUN chmod +x ./run.sh
EXPOSE 9090
CMD ./run.sh
