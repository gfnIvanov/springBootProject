.PHONY: run build down

run:
	mvn spring-boot:run

build-dev:
	docker-compose up --build

build:
	docker-compose up --build -d

down:
	docker-compose down