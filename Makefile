PROJECT_NAME    = primes-flix-plus

all:    up

down:
		@yes | docker-compose down -v

up:
		@docker-compose up

up-build:
		@docker-compose up --build

up-detach:
		@docker-compose up -d



.PHONY: all up up-build up-detach down
