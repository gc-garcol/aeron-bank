.PHONY: help

help: ## Show all commands
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

setup: ## Setup environment using Docker Compose
	docker-compose up -d

setup-dev: ## Setup development environment
	docker-compose -f docker-consul.yml up -d

down: ## Setup development environment using Docker Compose
	docker-compose down -v

build-jar: ## Build jar
	./gradlew

build-image: ## Build image
	docker-compose build --no-cache

run-containers: ## Run container
	docker-compose up &
