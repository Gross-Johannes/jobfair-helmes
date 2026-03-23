dev-up:
	docker compose up -d

prod-up:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml up --build

down:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml down