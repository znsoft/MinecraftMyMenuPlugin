MyMenu - плагин:
Версия: v0.3
Данный плагин добавляет возможность любому игроку создать свое (кастомное) меню команд в виде итемов дополнительного инвентаря ()

добавить иконку меню можно командой :
/mymenu add help 1
/меню добавить команда параметры команды
где help 1 это команда которая будет соответствовать этому пункту меню

удалить последнюю в списке иконку можно командой:
/mymenu remove
/меню удалить

Требования:
SQLite - база данных для хранения меню игроков
Особенности:
Несколько команд на один пункт меню (итем) разделив их символом ; команды будут выполняться последовательно.
Плагин создает в основном инвентаре игрока один итем "Часы"
Дополнительный инвентарь с меню игрока открывается по щелчку на часы
"Выкинуть" часы можно взяв любой предмет в инвентаре и положить на место часов , в этом случае "новые" часы появятся лишь при переподключении к серверу или смерти игрока (баг/фича)

Скачать плагин
Исходный код

Лог изменений:
Версия 0.3
Добавил ввод нескольких команд на один пункт меню (итем) разделив их символом ; команды будут выполняться последовательно.
например:
/mymenu add help 1;help 2
Версия 0.2
Добавил русскоязычную команду "менюшка"
исправил баг с исчезновением менюшки из инвентаря при смерти игрока