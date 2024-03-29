# Проект “ExploreWithMe”

Проект “**ExploreWithMe**” представляет собой приложение, которое помогает пользователям делиться информацией о
различных мероприятиях и находить компанию для участия в этих мероприятиях. Этот проект был разработан в рамках
дипломного задания и преследует цель сделать свободное время более продуктивным и интересным для пользователей.

# Основные характеристики проекта:

- Сервисы: приложение включает в себя два сервиса - основной сервис, который обеспечивает основную функциональность
  приложения, и сервис статистики, который собирает и анализирует данные о просмотрах и использовании приложения.
- API: основной сервис включает в себя API, которое разделено на две части: публичную и приватную.  
  Публичная часть
  доступна для всех пользователей без регистрации, используется для просмотра событий и подборок.  
  Приватная часть требует регистрации и авторизации и используется для взаимодействия пользователей
  с событиями.
- Функциональность: приложение предлагает обширный функционал - создание мероприятий, добавление заявок на участие,
  их администрирование, объединение мероприятий в подборки и многие другие функции
- Цель проекта: сделать планирование свободного времени более эффективным и простым для пользователей, сократив время и
  усилия, затрачиваемые на поиск информации о мероприятиях и переговоры с друзьями.

# Примеры основных эндпоинтов:

- /users/{userId}/events/{eventId} - эндпоинт для взаимедойствия авторизованного пользователя с конкретным мероприятием
- /admin/categories/{catId} - эндпоинт для администрирования категории
- /event - публичный эндпоинт для просмотра мероприятий с возможностью настройки фильтрации и сортировки

# Дополнительная функциональность:

В качестве дополнительной функциональности было выбрано комментирование мероприятий.
Возможность комментировать мероприятие есть только у авторизованных пользователей,
которым участие в данном мероприятии одобрено. Так же комментарии можно изменять, в таком случае они будут помеченны
как измененные и будет указано время последнего обновления

ссылка на pr:  
https://github.com/AlexKholopov/java-explore-with-me/pull/4