https://github.com/RybakovV/sclcmd
ветка master
Реализован консольный клиент для работы с разными базами данных (Реализовано для 
PostgreSQL и MySQL). При этом пользователь не заморачивается на какой именно 
платформе реализована БД. Он знает только ее название, логин и пароль.
Реализована настройка конфигурации, а именно - где находятся вышеуказанные базы данных 
(сервер и порт).
Кстати БД может быть, как локальной, так и удаленной. 
Есть все тесты. Coverage - 100%. Акцент сделан на Интеграционные. Понимаю, что это не 
самый хороший вариант (не такие шустрые, как unit), но не могу в себе это перебороть.
Думаю, что связано это с моими личностными качествами.
Формат ввода и вывода немного отличается от формата трубуемого в ТЗ. Поздновато увидел.

Ожидания от ревью, на что обратить внимание - вопросы:

На даный момент команда Conect реализована в Main - классе. Я понимаю, что это плохо но не
могу додуматься, как реализовать по другому. Точнее реализовать знаю, как, но как реализовать, 
так, чтобы заработало, не знаю. Проблема в том, что в Main я определяю 
менеджера БД (mySQL или PostgreQL) manager = new MysqlDatabaseManager(); 
в методе doConnect я переопределю при необходимости менеджера manager = new PostgresqlDatabaseManager(); 
но из внешенего класса, я не могу этого сделать, т.е мое переопределение не работает. Не понимаю - почему. 

Еще один недостаток - это определение типад БД. Пока работал с локальными базами данных, все шло хорошо, 
но когда начал подключать реальные, то понял, что sql-запрос SHOW VARIABLES LIKE "version_comment\" 
для SQL - базы может выдавать, как имя БД, так и что-угодно другое.
Пока решил костылем, что если не удалось определить тип БД, то подключаю менеджер для работы с MySQL.

Базы данных можно импортировать из файлов: mysqlcmd.sql (MySQL) и pgsqlcmd.backup (PostgreSQL)

Как-то так. 
