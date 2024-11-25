# Task Management System

## Описание

Это приложение для управления задачами, разработанное с использованием Spring Boot и PostgreSQL.

## Требования

- Docker
- Docker Compose

## Установка

1. **Клонируйте репозиторий**:

   ```bash
   git clone https://github.com/ViajerodeRusia/TaskManagementSystem
   cd TaskManagementSystem
   ```

2. **Соберите проект**:

   Убедитесь, что у вас установлен Maven. Выполните команду:

   ```bash
   mvn clean package
   ```

3. **Создайте файл `docker-compose.yml`**:

   В корне проекта создайте файл `docker-compose.yml` со следующим содержимым:

   ```yaml
   version: '3.8'

   services:
     db:
       image: postgres:latest
       environment:
         POSTGRES_DB: taskManagementSystemDb
         POSTGRES_USER: taskManagementSystemUser
         POSTGRES_PASSWORD: qwerty12345
       ports:
         - "5432:5432"
       volumes:
         - db_data:/var/lib/postgresql/data

     app:
       image: taskmanagementsystem
       build:
         context: .
         dockerfile: Dockerfile
       ports:
         - "8080:8080"
       depends_on:
         - db
       environment:
         SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/taskManagementSystemDb
         SPRING_DATASOURCE_USERNAME: taskManagementSystemUser
         SPRING_DATASOURCE_PASSWORD: qwerty12345

   volumes:
     db_data:
   ```

4. **Запустите приложение с помощью Docker Compose**:

   В корне проекта выполните команду:

   ```bash
   docker-compose up --build
   ```

   Это создаст и запустит контейнеры для вашего приложения и базы данных.

5. **Доступ к приложению**:

   После успешного запуска приложения, вы сможете получить доступ к нему по адресу:

   ```
   http://localhost:8080
   ```

   Для доступа к Swagger UI используйте:

   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Остановка приложения

Чтобы остановить приложение, выполните:

```bash
docker-compose down
```

Эта команда остановит и удалит все контейнеры, созданные с помощью Docker Compose, а также очистит сети и тома, если они были созданы.

## Примечания

- Убедитесь, что у вас установлены все необходимые зависимости в `pom.xml`.
- Если вы вносите изменения в код, не забудьте перезапустить контейнеры с помощью `docker-compose up --build`.

## Лицензия

Этот проект лицензирован под MIT License - смотрите файл [LICENSE](LICENSE) для подробностей.
