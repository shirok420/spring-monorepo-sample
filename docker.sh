#!/bin/bash

# Helper script for Docker operations

# Function to display help message
show_help() {
    echo "Usage: ./docker.sh [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  start       Build and start the application using Docker Compose"
    echo "  stop        Stop the application"
    echo "  restart     Restart the application"
    echo "  logs        Show logs from the application"
    echo "  build       Rebuild the Docker image"
    echo "  clean       Remove containers, images, and volumes"
    echo "  help        Show this help message"
    echo ""
}

# Check if Docker and Docker Compose are installed
check_docker() {
    if ! command -v docker &> /dev/null; then
        echo "Error: Docker is not installed or not in PATH"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        echo "Error: Docker Compose is not installed or not in PATH"
        exit 1
    fi
}

# Start the application
start() {
    echo "Starting the application..."
    docker-compose up -d
    echo "Application started at http://localhost:8080"
    echo "Swagger UI available at http://localhost:8080/swagger-ui.html"
}

# Stop the application
stop() {
    echo "Stopping the application..."
    docker-compose down
    echo "Application stopped"
}

# Restart the application
restart() {
    echo "Restarting the application..."
    docker-compose restart
    echo "Application restarted"
}

# Show logs
logs() {
    echo "Showing logs (press Ctrl+C to exit)..."
    docker-compose logs -f
}

# Rebuild the Docker image
build() {
    echo "Rebuilding the Docker image..."
    docker-compose build --no-cache
    echo "Docker image rebuilt"
}

# Clean up Docker resources
clean() {
    echo "Cleaning up Docker resources..."
    docker-compose down -v
    echo "Containers and volumes removed"
}

# Main script logic
check_docker

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    logs)
        logs
        ;;
    build)
        build
        ;;
    clean)
        clean
        ;;
    help|*)
        show_help
        ;;
esac