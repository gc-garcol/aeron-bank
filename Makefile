.PHONY: help

help: ## Show all commands
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

setup-dev: ## Setup development environment
	docker-compose -f docker-infra.yml up -d

down-dev: ## Down development environment
	docker-compose -f docker-infra.yml down -v

run-cluster: ## Run cluster using Docker Compose
	docker-compose up -d

down-cluster: ## Down cluster
	docker-compose down -v

build-jar: ## Build jar
	./gradlew

build-image: ## Build image
	docker-compose build --no-cache

run-containers: ## Run container
	docker-compose up &