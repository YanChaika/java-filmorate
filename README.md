# Filmorate
---

# Схема базы данных
![](https://github.com/YanChaika/java-filmorate/blob/add-search/src/main/resources/database_schema.svg)

---
Filmorate является социальной сетью, которая поможет вам выбрать фильмы, основываясь на ваших просмотрах и оценках, а также на просмотрах и оценках ваших друзей.

---

### Возможности приложения:

---
- Создавать, обновлять, удалять пользователей;
- Добавлять пользователей в друзья и удалять из, а также просматривать общих друзей;
- Создавать и обновлять фильмы;
- Оценивать и писать рецензии к фильмах;
- Получать список самых популярных фильмов;
- Находить фильмы по режиссерам;
- Просматривать ленту событий пользователей.

### Технологии:

---
- Java 11;
- Spring Boot;
- Lombok;
- JDBC;
- SQL, H2;
- Maven;
- Postman.

### Реализованные функционалы командной работы

---

* [Ян Чайка](https://github.com/YanChaika)
  * «Рекомендации»
  * «Популярные фильмы»
* [Руслан Захаров](https://github.com/Ruslan103)
  * «Отзывы»
* [Ярослав Большов](https://github.com/bolshovya)
  * «Удаление фильмов и пользователей»
  * «Лента событий»
* [Ада Янибаева](https://github.com/ianibaeva)
  * «Фильмы по режиссёрам»
* [Денис Положаев](https://github.com/DenisPolo)
  * «Поиск»
  * «Общие фильмы»

### Запуск и тестирование приложения:

---
Для запуска приложения необходимо:
1. Склонировать репозиторий и открыть его в IntelliJ IDEA.

2. Подключиться к базе данных H2.


    Connection type: Embedded
    path: ./db/filmorate.mv.db
    spring.datasource.username=sa
    spring.datasource.password=password

3. Запустить FilmorateApplication.

---
Для тестирования приложения можно воспользоваться коллекцией тестов [Postman](https://github.com/yandex-praktikum/java-filmorate/blob/develop/postman/sprint.json).