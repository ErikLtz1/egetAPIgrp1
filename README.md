egetAPIgrp1

Projektbeskrivning:
Detta är ett projekt med Quarkus. Vårat mål med detta projekt är att skapa ett API som skulle kunna distruberas till företag som vill skapa en annonserings-plattform.
Som utvecklare så kan man skapa ett konto för att få tillgång till en API-nyckel, där man får tillgång till relevanta endpoints för att kunna skapa en sida där användare kan registrera sig och visa befintliga inlägg, skapa inlägg, radera sitt inlägg samt ändra det. 
Utvecklare kan radera sitt konto så att API-nyckeln försvinner. 

Testa våra endpoints genom att använda Postman. Tyvärr är koden under utveckling, så tillfälligt behöver man skriva in väldigt långa URL:er där både API-nyckeln och userUUID behövs. Ett krångligt exempel på en URL är enligt följande: localhost:8080/api/3b6d0cc9-aa9e-4b98-b3c6-237df1b25b63/post/627d776e-7637-46c2-8ee5-666b66c7b36a/edit/1. Vilket på ett tydligare sätt är: localhost:8080/api/{apiKey}/post/{userUUID}/edit/{id}. Det finns ett hårdkodat Admin-inlogg som ni kan hitta längst upp i DeveloperResource-klassen, det kan ni använda er av för att få behörighet i vissa endpoints. När man skapar en developer så får man en API-nyckel som man behöver spara ner för att kunna ha åtkomst till de andra endpointsen. Det finns också några färdiga developers och users i filen import.sql om man vill använda sig av det. När användare skapas så kan det vara smidigt att spara ner användarens genererade userUUID, eftersom det ID't behövs för att få behörigheter till användarens inlägg. 

Läs våran dokumentation för mer information: 

http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui
