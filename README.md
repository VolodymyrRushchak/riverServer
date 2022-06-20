# riverServer
Для того, щоб запустити програму, потрібно у консолі перейти в кореневу директорію проекту та виконати команду "mvn org.springframework.boot:spring-boot-maven-plugin:run".

Щоб надсилати запити та користуватися CRUD операціями, найзручніше використовувати застосунок Postman. Всі запити потрібно надсилати на адресу http://localhost:8080


Доступні команди:

Отримати всі сутності заданого типу(GET):
/rivers
/measurementStations
/measurements

Отримати сутність заданого типу за id(GET/{id}):
/rivers/{id}
/measurementStations/{id}
/measurements/{id}

Отримати всі станції заміру для заданої річки за id(/GET):
/rivers/{riverId}/measurementStations

Отримати всі заміри для заданої станції за id(/GET):
/measurementStations/{stationId}/measurement

Створити сутність заданого типу(POST):
/rivers
/measurementStations
/measurements

Обновити сутність заданого типу за id(PUT/{id}):
/rivers/{id}
/measurementStations/{id}
/measurements/{id}

Видалити сутність заданого типу за id(DELETE/{id}):
/rivers/{id}
/measurementStations/{id}
/measurements/{id}
