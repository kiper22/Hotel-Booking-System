Dwie wersje projektu zal. z javy. (Rozpiska supported by chatGPT) - trzeba się podzielić objętością tej core'owej wersji na równo, a dodatkową może na front i backend się podzielić i git będzie
# Wersja minimum do zaliczenia:
```
hotelService/
├── HotelService.java           # Interfejs RMI (średnio)
├── Hotel.java                  # Klasa hotelu z listą pokoi (średnio)
├── Pokoj.java                  # Dane pokoju (mało)
├── Rezerwacja.java             # Dane rezerwacji klienta (mało)

hotelServer/
├── HotelServiceImpl.java       # Implementacja logiki RMI (dużo)
├── MyServer.java               # Uruchomienie serwera RMI (mało)

hotelClient/
└── MyClient.java               # Aplikacja klienta z interfejsem tekstowym (średnio)

Main.java                       # (opcjonalnie) test uruchomieniowy (mało)
```
Proponowana podziałka:
- osoba 1
```
hotelService/
├── HotelService.java           # Interfejs RMI (średnio)
├── Hotel.java                  # Klasa hotelu z listą pokoi (średnio)

hotelServer/
├── HotelServiceImpl.java       # Implementacja logiki RMI (dużo)

Main.java                       # Test uruchomieniowy (mało)
```
- osoba 2
```
hotelService/
├── Pokoj.java                  # Dane pokoju (mało)
├── Rezerwacja.java             # Dane rezerwacji klienta (mało)

hotelClient/
└── MyClient.java               # Aplikacja klienta z interfejsem tekstowym (średnio)

hotelServer/
└── MyServer.java               # Uruchomienie serwera RMI (mało)
```
# Wersja duża fullstack- rozbudowana:
```
java-core/                     ← część obowiązkowa projektu (RMI)
├── hotelService/
│   ├── HotelService.java
│   ├── Hotel.java
│   ├── Pokoj.java
│   └── Rezerwacja.java
├── hotelServer/
│   ├── HotelServiceImpl.java
│   └── MyServer.java
├── hotelClient/
│   └── MyClient.java
└── Main.java

client/                        ← frontend w React
├── src/
│   ├── App.js                 ← Router + layout (mało)
│   ├── components/
│   │   ├── HotelList.js       ← lista hoteli z MongoDB (średnio)
│   │   ├── HotelDetails.js    ← pokoje w wybranym hotelu (średnio)
│   │   ├── BookingForm.js     ← formularz rezerwacji (średnio)
│   │   └── MapView.js         ← Google Maps + pinezki hoteli (średnio/dużo)

server/                        ← backend w Express.js
├── models/
│   ├── Hotel.js               ← definicja hotelu (średnio)
│   ├── Room.js                ← definicja pokoju (średnio)
│   └── Reservation.js         ← definicja rezerwacji (mało)
├── routes/
│   ├── hotels.js              ← endpointy hoteli (średnio)
│   ├── rooms.js               ← endpointy pokoi (średnio)
│   └── reservations.js        ← endpointy rezerwacji (średnio)
├── controllers/
│   └── logic.js               ← logika rezerwacji, dostępności (dużo)
├── app.js                     ← konfiguracja Express (mało)
config/
└── db.js                      ← połączenie z MongoDB (mało)
```

# Uwagi ogólne

Proponuję zacząć pisać z myślą o dużej części od java-core, a kiedy będzie gotowa zrobić kopię/brancha i tam robić wersję fullstack- w ten sposób bez cofania się po setkach commitów będzie na od razu wersja minimum i fullstack

# Zadanie i wytyczne

System rezerwacji pokoi hotelowych Stwórz aplikację umożliwiającą klientom zdalne
rezerwowanie pokoi hotelowych. Klienci mogą przeglądać dostępne pokoje,
dokonywać rezerwacji i sprawdzać dostępność.

Stworzenie aplikacji rozproszonej w języku Java przy wykorzystaniu zdalnego wywoływania metod
(RMI) to główny cel projektu zaliczeniowego. Specyfikacja zadania określa minimalne wymagania
funkcjonalne, jednak istnieje możliwość uwzględnienia dodatkowych opcji dla rozwiązania.
W ramach projektu należy dostarczyć dokumentację zawierającą:
- Dane autorów i ich wkład w poszczególne części projektu.
- Tytuł programu.
- Krótki opis celu programu.
- Opis i schemat struktury logicznej aplikacji.
- Informacje o wykorzystanych klasach niestandardowych.
- Opis specyficznych metod rozwiązania problemu, takich jak metoda wykorzystana do rozwiązania konkretnego aspektu.
- Krótka instrukcja obsługi.
- Ograniczenia programu, np. maksymalna liczba obsługiwanych klientów.
- Inne istotne informacje związane z tematem projektu
