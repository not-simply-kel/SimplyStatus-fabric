# SimplyStatus
> Это мод предозначен для игрового статуса **Minecraft** в **[Discord](https://discord.com/)**, где показывает ваши статистику. Например: В каком вы измерении находитесь, сколько у вас здоровья и брони, так же показывает ваше время `[Утро, день, вечер, ночь]`. Так же может показать на каком вы сервере играете
## Требования
> Minecraft **1.17.1**<br>
> [Fabric Loader 0.12.3 и выше](https://fabricmc.net/)
## Как установить
* Скачиваем .jar файл из [Releases](https://github.com/not-simply-kel/SimplyStatus-fabric/releases 'GitHub'), рекомендую использовать релизы `[не pre-release]`<br>
* Ищем папку Майнкрафта. По умолчанию в `%appdata%/.minecraft` **\[Windows\]**, и ищем папку `/mods`
* Перекидываем наш .jar файл в эту папку
* Запускаем Minecraft `[с Fabric]`
* Готово **:3**
## Примеры статуса
> Когда вы не находитесь в мире / на сервере:<br>
> ![Снимок экрана 2021-10-29 130529](https://user-images.githubusercontent.com/86980879/139416820-2de12e39-924d-46e6-87f5-2e063c7fa993.png) <br>
> Когда вы играете в мире / на сервере:<br> `[В одиночном мире будет показано в каком мире игрок]`<br>`[В мультиплеере будет показано какое время суток и день]`<br>
> ![Снимок экрана 2021-10-29 130820](https://user-images.githubusercontent.com/86980879/139417200-cebb6e3a-f20e-4710-9432-d0c736d3b448.png) ![Снимок экрана 2021-10-30 132520](https://user-images.githubusercontent.com/86980879/139529215-064aaf93-f75c-4238-bd05-5e7b1f11d2f1.png)
## Исходный код
> Исходный код можно настроить под себя, например сменить ID приложения, изменить текст, исправить какие-то ошибки автора и т.д.<br>
> Чтобы скомпилировать код вам нужно в терминал прописать следующее `[в папке исходника]`:
```
gradlew build
```