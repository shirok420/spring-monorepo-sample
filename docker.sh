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
    echo "  mysql       Connect to MySQL CLI"
    echo "  mysql-dump  Dump MySQL database"
    echo "  mysql-restore FILE  Restore MySQL database from dump file"
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

# Connect to MySQL CLI
mysql_cli() {
    echo "Connecting to MySQL CLI..."
    docker-compose exec mysql mysql -uspringuser -pspringpassword springdb
}

# Dump MySQL database
mysql_dump() {
    echo "Dumping MySQL database..."
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)
    docker-compose exec -T mysql mysqldump -uspringuser -pspringpassword springdb > "mysql_dump_${TIMESTAMP}.sql"
    echo "Database dumped to mysql_dump_${TIMESTAMP}.sql"
}

# Restore MySQL database from dump file
mysql_restore() {
    if [ -z "$1" ]; then
        echo "Error: No dump file specified"
        echo "Usage: $0 mysql-restore DUMP_FILE"
        exit 1
    fi
    
    if [ ! -f "$1" ]; then
        echo "Error: Dump file $1 not found"
        exit 1
    fi
    
    echo "Restoring MySQL database from $1..."
    cat "$1" | docker-compose exec -T mysql mysql -uspringuser -pspringpassword springdb
    echo "Database restored successfully"
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
    mysql)
        mysql_cli
        ;;
    mysql-dump)
        mysql_dump
        ;;
    mysql-restore)
        mysql_restore "$2"
        ;;
    help|*)
        show_help
        ;;
esac