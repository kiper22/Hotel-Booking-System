# Hotel Booking System

## Dane autorów i ich wkład

- Dawid Topyła - Klient
- Kacper Wieleba - Serwer

## Opis celu programu

Celem programu było stworzenie aplikacji umożliwiającej klientom zdalne rezerwowanie pokoi hotelowych. Aplikacja ma umożliwiać przegląd dostępnych pokoi, dokonywanie rezerwacji, anulowanie rezerwacji oraz poglądu własnych rezerwacji.

## Opis i schemat struktury logicznej programu

Klasa `Client` - reprezentuje graficzny interfejs użytkownika (GUI) do komunikacji z systemem rezerwacji hotelowej za pomocą Java RMI. Umożliwia użytkownikom wyszukiwanie dostępnych pokoi, rezerwowanie ich, przeglądanie i anulowanie rezerwacji, oraz reaguje na zmiany dostępności pokoi w czasie rzeczywistym.

Główne pola

- `HotelService hotelService` – referencja do zdalnego interfejsu RMI.
- `JTextArea output` – obszar tekstowy do wyświetlania komunikatów i informacji.
- `JTable roomTable` – tabela z listą dostępnych pokoi.
- `DefaultTableModel tableModel` – model danych dla tabeli.
- `String priceSortOption` – wybrana opcja sortowania cen (rosnąco, malejąco, brak).
- `Date lastStartDate, lastEndDate` – daty ostatniego filtrowania pokoi.
- `int lastGuestCount` – liczba gości z ostatniego filtrowania.

Konstruktor

- `Client()` – Inicjalizuje interfejs graficzny, nawiązuje połączenie RMI, rejestruje nasłuchiwacz zmian (`RoomUpdateListener`) i ustawia działania przycisków GUI.

Główne metody

- `void showFilterRoomsDialog()`  
  Wyświetla dialog do filtrowania pokoi według daty, liczby gości i opcji sortowania, następnie aktualizuje tabelę.
- `void updateRoomTable(Date start, Date end, int guests)`  
  Pobiera dostępne pokoje (z uwzględnieniem sortowania) i aktualizuje widok tabeli oraz pola tekstowego.
- `void showUserBookings()`  
  Wyświetla wszystkie rezerwacje użytkownika po podaniu danych osobowych.
- `void bookRoom()`  
  Tworzy nową rezerwację na podstawie wprowadzonych danych oraz dat.
- `void cancelBooking()`  
  Anuluje istniejącą rezerwację na podstawie ID oraz danych użytkownika.
- `void showInfo(String message)`  
  Pokazuje użytkownikowi informację w formie wyskakującego okienka.
- `void showError(String message)`  
  Pokazuje komunikat o błędzie.
- `public static void main(String[] args)`  
  Uruchamia aplikację Swing.
- `public void onRoomChanged()`  
  Metoda z interfejsu `RoomUpdateListener`. Reaguje na zmiany dostępności pokoi i aktualizuje tabelę.

Klasa `HotelServiceImpl` - reprezentuje implementację zdalnego serwisu hotelowego w oparciu o Java RMI. Udostępnia metody do zarządzania pokojami, rezerwacjami, sortowaniem i powiadamianiem klientów o zmianach w dostępności pokoi.

Główne pola

- `List<Hotel> hotels` – lista hoteli dostępnych w systemie.
- `List<Booking> bookings` – lista wszystkich rezerwacji.
- `int bookingCounter` – licznik służący do przypisywania unikalnych ID rezerwacjom.
- `List<RoomUpdateListener> listeners` – lista zarejestrowanych klientów oczekujących na powiadomienia o zmianach.

Konstruktor

- `HotelServiceImpl()` – Inicjalizuje dane hoteli i pokoi, ustawia przykładowe dane wejściowe, dziedziczy z `UnicastRemoteObject`.

Główne metody:

- `List<Room> getAvailableRooms()`  
  Zwraca listę wszystkich pokoi ze wszystkich hoteli, bez filtrowania.
- `List<Room> getAvailableRooms(Date start, Date end, int guests)`  
  Zwraca listę dostępnych pokoi spełniających wymagania daty oraz liczby gości.
- `boolean bookRoom(int roomId, String name, String surname, String phoneNumber, Date start, Date stop)`    
  Tworzy nową rezerwację, jeśli pokój jest dostępny; oblicza całkowitą cenę na podstawie liczby dni i ceny za noc.
- `boolean cancelBooking(int bookingId, String name, String surname, String phoneNumber)`   
  Anuluje rezerwację użytkownika na podstawie ID i danych osobowych.
- `List<Booking> getBookings()`     
  Zwraca wszystkie rezerwacje w systemie.
- `List<Booking> getBookingsForUser(String name, String surname, String phoneNumber)`   
  Zwraca wszystkie rezerwacje konkretnego użytkownika.
- `List<Room> sortRoomsByPrice(boolean ascending, Date start, Date end, int guests)`    
  Zwraca listę pokoi posortowanych rosnąco lub malejąco według ceny, zgodnie z podanymi filtrami.
- `void registerListener(RoomUpdateListener listener)`  
  Rejestruje klienta do otrzymywania powiadomień o zmianach.
- `void unregisterListener(RoomUpdateListener listener)`    
  Usuwa klienta z listy nasłuchiwaczy.
- `private void notifyListeners()`
  Informuje wszystkich zarejestrowanych nasłuchiwaczy o zmianie dostępności pokoi.

Klasa `Server` - uruchamia serwer RMI, rejestrując instancję `HotelServiceImpl` w rejestrze RMI na porcie `5555`.

- `main(String[] args)`  
  Tworzy nowy obiekt `HotelServiceImpl`, rejestruje go w rejestrze RMI i nasłuchuje na `//localhost:5555/HotelService`.

Klasa `RoomType`
Typ wyliczeniowy reprezentujący rodzaje pokoi:

- `SINGLE_BED` – pokój dla jednej osoby.  
- `DOUBLE_BED` – pokój dwuosobowy (dwa pojedyncze łóżka lub jedno podwójne).  
- `TRIPLE_BED` – pokój dla trzech osób.  
- `FAMILY` – pokój rodzinny (dla 4 do 6 osób).

Klasa `Booking` - reprezentuje rezerwację pokoju hotelowego. Implementuje `Serializable`.

Główne pola

- `int bookingId` – unikalne ID rezerwacji.  
- `int roomId` – ID pokoju.  
- `String customerName`, `customerSurname`, `customerPhone` – dane klienta.  
- `Date startDate`, `endDate` – okres rezerwacji.  
- `double totalPrice` – łączny koszt rezerwacji.

Metody

- Gettery do wszystkich pól.
- `String info()` – zwraca czytelną reprezentację rezerwacji.

Klasa `Hotel` - reprezentuje hotel zawierający wiele pokoi. Implementuje `Serializable`.

Główne pola

- `int hotelId` – unikalne ID hotelu.  
- `String name`, `address` – dane hotelu.  
- `List<Room> rooms` – pokoje dostępne w hotelu.

Metody

- Gettery i settery.  
- `void addRoom(Room room)` – dodaje pokój do listy.  
- `boolean removeRoom(int roomId)` – usuwa pokój na podstawie ID.  
- `private boolean datesOverlap(...)` – pomocnicza metoda sprawdzająca, czy daty się pokrywają.  
- `private Date stripTime(Date date)` – usuwa czas z daty.

Interfejs `HotelService` - zdalny interfejs RMI do obsługi hotelu.

Metody

- `List<Room> getAvailableRooms()`  
  Zwraca wszystkie dostępne pokoje.
- `List<Room> getAvailableRooms(Date start, Date end, int guests)`  
  Zwraca pokoje dostępne w określonym przedziale czasu i dla określonej liczby gości.
- `boolean bookRoom(...)`  
  Dokonuje rezerwacji pokoju.
- `boolean cancelBooking(...)`  
  Anuluje istniejącą rezerwację.
- `List<Booking> getBookings()`  
  Zwraca wszystkie rezerwacje.
- `List<Booking> getBookingsForUser(...)`  
  Zwraca rezerwacje danego użytkownika.
- `List<Room> sortRoomsByPrice(...)`  
  Zwraca listę pokoi posortowaną wg ceny.
- `void registerListener(RoomUpdateListener listener)`  
  Rejestruje klienta do powiadomień.
- `void unregisterListener(RoomUpdateListener listener)`  
  Usuwa klienta z listy nasłuchiwaczy.

Klasa `Room` - reprezentuje pojedynczy pokój hotelowy. Implementuje `Serializable`.

Główne pola

- `int roomId` – unikalny identyfikator pokoju.  
- `RoomType roomType` – typ pokoju (z enum `RoomType`).  
- `String description` – opis pokoju.  
- `int maxOccupancy` – maksymalna liczba gości.  
- `double pricePerNight` – cena za noc.

Metody

- Gettery i settery.  
- `String info()` – zwraca opis pokoju w czytelnej formie.

Interfejs `RoomUpdateListener` - zdalny interfejs RMI służący do powiadamiania klientów o zmianach w dostępności pokoi.

Metody:

- `void onRoomChanged()`  
  Metoda wywoływana przez serwer w celu powiadomienia klienta, że dostępność pokoi uległa zmianie.


## Informacje o wykorzystanych klasach miestandardowych

## Opis specyficznych metod rozwiązania problemu, takich jak metoda wykorzystana do rozwiązania konkretnego aspektu

### Wykorzystanie interfejsu RoomUpdateListener
**Inicjalizacja połączenia i rejestracja listenera**

```java
  hotelService = (HotelService) Naming.lookup("//localhost:5555/HotelService");
  UnicastRemoteObject.exportObject(this, 0);
  hotelService.registerListener(this);
```

- Łączy się z serwerem RMI i pobiera zdalny obiekt `HotelService`.
- Udostępnia klienta jako obiekt zdalny (RMI), umożliwiając serwerowi wywoływanie `onRoomChanged()`.
- Rejestruje klienta jako nasłuchiwacza — dzięki temu klient automatycznie otrzymuje powiadomienia o zmianach dostępności pokoi.

**Zmiana po stronie serwera:**
- Serwer wywołuje wewnętrznie `notifyListener()` w `bookRoom()` oraz `cancelBooking()`.
  ```java
  private void notifyListeners() {
        Iterator<RoomUpdateListener> iterator = listeners.iterator();

        while (iterator.hasNext()) {
            RoomUpdateListener listener = iterator.next();
            try {
                listener.onRoomChanged();
            } catch (RemoteException e) {
                iterator.remove();
            }
        }
    }
  ```
- Metoda ta iteruje po wszystkich klientach i dla każdego wywołuje zdalnie `listener.onRoomChanged()` 
  ```java
  @Override
  public void onRoomChanged() throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            output.append("Aktualizacja dostępnych pokoi.\n");

            if (lastStartDate != null && lastEndDate != null) {
                updateRoomTable(lastStartDate, lastEndDate, lastGuestCount);
            }
        });
    }
  ```
- Służy do uruchamiania zadań w wątku Event Dispatcher (EDT), odpowiadający za obsługę zdarzeń interfejsu graficznego. 
  Reakcja klienta na zmiant wywołując `updateRoomTable()`.

**Obsługa zamknięcia okna**
- Metoda `WindowClosing()` reaguje na zamknięcie okna GUI.
```java
  @Override
public void windowClosing(WindowEvent e) {
  try {
    hotelService.unregisterListener(Client.this);
  } catch (RemoteException ex) {
    System.err.println("Błąd przy wyrejestrowywaniu listenera: " + ex.getMessage());
  }
}
```
- Wysyła do serwera żądanie wyrejestrowania klienta z listy nasłuchiwaczy `unregisterListener()`, aby nie dostawał więcej powiadomień.

## Instrukcja obsługi

W celu uruchomienia serwera oraz klienta należy wykonać następujące komendy:

```
javac -encoding UTF-8 hotelService/enums/RoomType.java hotelService/*.java hotelServer/*.java Client/*.java

java hotelServer.Server

java Client.Client
```

### Obsług okna klienta
Dostępne są następujące opcje:
1) Dostępne pokoje - użytkownik po kliknięciu na przycisk ma do uzupełnienia informacje początek oraz koniec pobytu, liczba gości (opcjonalnie) oraz wybór sortowania wyników. Wyświetlone dane przedstawione są w postaci tabeli z kolumnami ID pokoju, rodzaju , opisu i pojemności pokoju, jak i ceny za noc oraz podany zakres dat.
2) Rezerwój pokój - użytkownik musi wpisać ID pokoju, imie, nazwisko, telefon oraz datę rezerwacji.
3) Moje rezerwacje - po wpisaniu imienia, nazwiska oraz numeru telefonu, użytkownik ma wgląd w dokonane rezerwacje na jego dane.
4) Anuluj rezerwacje - użytkownik może anulować dokonaną na swoje dane rezerwacje - musi wpisać ID rezerwacji, swoje imie, nazwisko oraz numer telefonu.
5) Wyjdź - opcja zakończenia działania klienta

## Ograniczenia programu

## Inne informacje związane z tematem projektu 