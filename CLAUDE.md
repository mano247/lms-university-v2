# LMS Univerzitet — Projektni kontekst

## O projektu

Univerzitetski portal rađen kao timski fakultetski projekat pre ~2 godine, po dokumentaciji koju su asistenti dali kao "klijenti". Sada se radi kompletan profesionalni upgrade za potrebe CV-a i portfolija.

Cilj: podići projekat iznad junior nivoa — čist kod, moderna arhitektura, jak security, profesionalan dizajn, deployovan sa živim URL-om.

## Tehnologije

**Backend:** Java 17 → upgrade na Java 21, Spring Boot 3.1.0 → upgrade na najnoviju 3.x stabilnu verziju
**Frontend:** Angular 17.3 → upgrade na najnoviju verziju (trenutno 20.x), PrimeNG 17 → **migracija na Angular Material**
**Baza:** MySQL, baza se zove `lms_v2`, na standardnom portu (3306), trenutno prazna — puni se novim seed podacima
**Auth:** Spring Security + JWT (io.jsonwebtoken / jjwt 0.11.5 — proveriti da li upgrade-ovati ili preći na Spring Security Resource Server pristup)
**PDF:** OpenPDF na backendu, jsPDF na frontendu — provjeriti da li se oba koriste ili je jedno mrtav kod

Node.js nije poseban servis — samo npm za Angular build.

## Korisničke role i mogućnosti

### Posetilac (nije ulogovan)
- Pregled javnih informacija: fakulteti, smerovi, upis, opšte informacije o univerzitetu
- Javna obaveštenja

### Registrovani korisnik (tek se registrovao)
- Ograničen pristup, čeka da ga studentska služba upiše na smer/godinu
- Nakon upisa od strane službe automatski postaje "Student" sa punim pristupom

### Student
- Dashboard: Moji predmeti, Obaveštenja, Istorija studiranja, Prijava ispita
- Vidi: raspored nastave (tabela po nedelji — dan, satnica, profesor/asistent, učionica)
- Vidi: predmete za upisanu godinu/smer koje još nije položio, prijavljuje ispite u otvorenim rokovima
- Vidi: istoriju položenih ispita (predmet, bodovi, profesor, ocena, ESPB), istoriju neuspešnih polaganja, prosečnu ocenu, ukupan ESPB
- Vidi: istoriju upisa po godinama (godina, datum, smer)
- Vidi: istoriju školarine
- Ovaj deo aplikacije je trenutno najkompletniji — fokus je na redizajnu, ne na nedostajućoj funkcionalnosti

### Profesor / Asistent
- Ista prava i mogućnosti, razlikuju se samo labelom (tip korisnika) u sistemu
- Relacija: profesor/asistent ↔ predmet ↔ godina ↔ fakultet
- Dashboard: Moji predmeti, Obaveštenja, Spisak studenata, Unos ocena
- Klik na studenta → modal sa kompletnim podacima (lični podaci, upisi, položeni ispiti, neuspešna polaganja, završni rad)
- Kreira obaveštenja na nivou svog predmeta
- **NEDOSTAJE / TREBA IMPLEMENTIRATI:** kreiranje termina u okviru ispitnih rokova za svoj predmet
- **NEDOSTAJE / TREBA IMPLEMENTIRATI:** uvid (read-only) u raspored nastave koji kreira studentska služba

### Studentska služba
- Administracija na nivou svog fakulteta (ne vidi druge fakultete)
- Dashboard: Upis studenata, Izdavanje dokumenata, Obaveštenja, Biblioteka, Kancelarijski inventar
- Upis studenata — **TREBA RAZDVOJITI** u dve odvojene akcije: (1) upis novog registrovanog korisnika u sistem kao studenta, (2) upis postojećeg studenta na narednu godinu studija
- Izdavanje dokumenata (export XML/PDF za profesore i studente) — **TRENUTNO NE RADI, TREBA ISPRAVITI BUG**
- Biblioteka — CRUD udžbenika, izdavanje, "trebovanje" (naručivanje kad ponestane na stanju)
- Kancelarijski inventar — CRUD inventara, "trebovanje"
- **NEDOSTAJE / TREBA IMPLEMENTIRATI:** administracija ispitnih rokova (kreiranje rokova za predmete)
- **NEDOSTAJE / TREBA IMPLEMENTIRATI:** administracija rasporeda nastave (kreiranje termina — dan, vreme, učionica, profesor/asistent, predmet) — **MORA imati real-time provere preklapanja** (ista učionica ne može biti zauzeta u isto vreme od strane dva različita termina)

### Administrator
- Pun pristup svemu, kroz pet tabova: Sifarnik, Registrovani korisnici, Studijski programi, Organizacija, Zaposleni
- Sifarnik — CRUD fakulteta (naziv, opis, šifra, adresa, dekan, kontakt, slika)
- Registrovani korisnici — pregled i promena tipa korisnika (student/nastavnik/sluzba/admin)
- Studijski programi — CRUD smerova (naziv, šifra, rukovodilac)
- Zaposleni — pregled po kategoriji (profesori/studentska sluzba/administracija)
- **NEDOSTAJE / TREBA IMPLEMENTIRATI:** administracija ispitnih rokova (na nivou celog sistema)
- **NEDOSTAJE / TREBA IMPLEMENTIRATI:** administracija rasporeda nastave (na nivou celog sistema)
- **BUG U SVIM CRUD FORMAMA:** polja koja predstavljaju relacije (npr. "dekan" na fakultetu, "rukovodilac" na smeru, "slika" na fakultetu) su trenutno plain text input. Treba ih zameniti pravim selekcijama iz baze (dropdown sa profesorima za dekana/rukovodioca, file upload za slike) umesto da se hardkoduje tekst koji nije povezan FK relacijom

## Obaveštenja — četiri nivoa

1. Globalna — vide svi
2. Fakultetska — vide studenti/profesori tog fakulteta
3. Predmetna — vidi ko je upisan na taj predmet
4. Od studentske službe — ka konkretnom studentu

**Poznat bug:** kod nekih obaveštenja "do" datum je raniji od "od" datuma — nema validacije da do > od. Treba provjeriti i ispraviti logiku/validaciju na svim mestima gde postoji ovaj raspon.

## Entiteti i hijerarhija

```
Univerzitet
  └── Fakultet (naziv, opis, šifra, adresa, dekan [treba biti FK], kontakt, slika [treba biti file upload])
        └── Smer (naziv, šifra, rukovodilac [treba biti FK])
              └── Godina studija
                    └── Predmet (šifra, naziv, ESPB, opis, udžbenici, silabus) ←→ Profesor/Asistent
                          └── Student (upisan na smer + godinu)
                                ├── Prijava ispita
                                ├── Ocena / Istorija polaganja (predmet, bodovi, profesor, ocena, ESPB)
                                ├── Istorija upisa (godina, datum, smer)
                                └── Istorija školarine
```

**Bug poznat iz ranije analize:** kaskadno brisanje entiteta ostavlja foreign key-eve na izbrisane entitete, što blokira API pozive. Treba mapirati sve relacije i ispraviti cascade/orphan removal podešavanja.

**Napomena — sloboda za izmenu modela:** hijerarhija i relacije gore su opis TRENUTNOG stanja, ne propisan finalni dizajn. Claude ima slobodu da predloži i implementira drugačiju strukturu relacija, dodatne tabele, normalizaciju, ili izmenu kardinalnosti veza ako prepozna da postoji bolje arhitektonsko rešenje (npr. bolji pristup za rukovodioca/dekana kao FK, bolja struktura za silabus/udžbenike kao zasebne tabele umesto stringa, itd). Kad predlaže takvu izmenu, treba kratko da objasni zašto je predloženo rešenje bolje od postojećeg pre implementacije.

## Poznati problemi u kodu (potvrđeni kroz screenshotove i ranije korišćenje)

### Backend
- Flat package struktura — `model/`, `dto/`, `service/`, `repository/`, `controller/` foldери sadrže SVE entitete zajedno. Treba preći na feature-based strukturu (svaki folder = jedan entitet/feature sa svojim model/dto/service/repository/controller fajlovima unutra)
- Sve je na srpskom jeziku — nazivi klasa, metoda, varijabli, komentari. Treba prevesti na engleski (bez promene logike)
- N+1 problem kroz upite — nije optimizovano
- Nema paginacije — koristi se `getAll()` koji vraća kompletne liste
- Kaskadno brisanje ostavlja "sirotinjske" foreign key-eve (orphan FK) koji blokiraju buduće API pozive
- Slike se čuvaju kao hardkodovani string putanje (npr. "1.jpg", "2.jpg") bez pravog file upload sistema — treba implementirati upload endpoint sa validacijom tipa/veličine fajla
- "Moji predmeti" i "Unos ocena" tabovi su vraćali "No results found" / praznu listu u testiranju — provjeriti da li je query problem ili nedostatak test podataka
- Validacija unosa generalno nedovoljna (npr. obaveštenja sa "do" < "od", test/placeholder podaci poput "asdasdasd" koji ulaze nesmetano)

### Frontend
- CSS bez globalnog sistema — nema definisane palete boja, tipografije, spacing skale. Trenutno se na različitim stranama javljaju najmanje 4-5 različitih, neusklađenih akcentnih boja (ljubičasta, cyan, bordo/braon, zelena, crvena) i nekonzistentan border-radius (čas oštar, čas pill-shaped)
- Hardkodovan tekst na frontendu umesto konstanti/i18n
- Responzivnost ne postoji — projekat je rađen i testiran samo za autorov ekran
- Slike u folderu nazvane 1.jpg, 2.jpg, 3.jpg... bez sistema
- PrimeNG komponente delom nezavršene/neusklađene
- Nema animacija/tranzicija osim spinnera i toastera
- Sav tekst i sadržaj na srpskom — treba prevesti na engleski
- Input placeholderi na nekim mestima imaju custom bold/centriran stil koji izgleda kao popunjen tekst (zbunjujuće), na drugim mestima default stil — nekonzistentno

**Napomena — sloboda za frontend redizajn:** opis trenutnog stanja iznad je polazna tačka, ne ograničenje. Cilj frontend faza nije "popraviti postojeći izgled" nego napraviti potpuno nov, profesionalan dizajn u rangu sa pravim univerzitetskim sajtovima — Claude ima slobodu da redizajnira layout, navigaciju, strukturu stranica, komponente i celokupan vizuelni jezik od nule ako to dovodi do boljeg rezultata, ne mora da se drži postojećeg rasporeda elemenata na stranici. Konzistentnost, hijerarhija, čitljivost i savremen utisak su prioritet nad očuvanjem postojećeg izgleda.

**Responzivnost je obavezan zahtev:** aplikacija mora da radi ispravno i izgleda profesionalno na svim dimenzijama ekrana — mobilni telefoni (od ~360px), tableti, laptopovi i desktop monitori. Ovo nije opciono poliranje na kraju nego se gradi od početka kroz svaku komponentu — mobile-first pristup, fluid layout, breakpointi za sve ključne prelaze, tabele i forme moraju biti upotrebljivi na malim ekranima (scroll, collapse, reorder elemenata zavisno od breakpointa).

**Vizuelni assets — sloboda generisanja:** Claude ima slobodu da generiše ili kreira sve potrebne vizuelne elemente od nule — logoe fakulteta, ikone, ilustracije, placeholder slike, favicon, i sve ostalo što aplikacija vizuelno potrebuje. Hardkodovane slike (trenutno 1.jpg, 2.jpg itd.) treba zameniti profesionalnim SVG assets-ima ili generisanim grafikama koje su konzistentne sa dizajn sistemom. Nije potrebno koristiti externe slike sa interneta — sve može biti generisano (SVG ilustracije, CSS-based grafike, ikone).

## Plan rada — faze (rade se kao odvojene Claude Code sesije, ne sve odjednom)

1. **Backend analiza** — kompletan izveštaj stanja, bez izmena
2. **Prevod backend koda na engleski** (klase, metode, varijable, komentari)
3. **Backend restrukturiranje** — flat → feature-based paketi
4. **Backend security audit i pojačanje** — JWT, endpoint zaštita po rolama, input validacija, CORS
5. **Backend performanse** — rešavanje N+1, dodavanje paginacije svuda gde je relevantno
6. **Backend bug fixevi** — cascade/FK problem, file upload sistem za slike, validacija datuma kod obaveštenja
7. **Implementacija novih modula (backend)** — ispitni rokovi (CRUD + termini koje kreira profesor), raspored nastave (CRUD sa real-time provером preklapanja termina po učionici), razdvajanje upisa studenta od upisa na godinu, fix CRUD formi da koriste pravi FK select umesto plain text (dekan, rukovodilac), fix bug-a u izdavanju dokumenata
8. **Java/Spring Boot verzija upgrade** — Java 17→21, Spring Boot 3.1→najnovija 3.x
9. **Nova baza i seed podaci** — kompletno nova baza, AI-generisani realistični podaci: 10 fakulteta, svaki sa 7-8 smerova, svaki smer sa godinama × 8 predmeta, 100-200 upisanih studenata po smeru
10. **Frontend analiza** — izveštaj stanja
11. **Prevod frontend sadržaja na engleski**
12. **Migracija PrimeNG → Angular Material**
13. **Angular verzija upgrade** (17→najnovija)
14. **Frontend redesign** — globalni design system (boje, tipografija, spacing, breakpointi), responzivnost, animacije/tranzicije, uklanjanje hardkodovanih slika i teksta
15. **Frontend security** — XSS zaštita, sanitizacija inputa, sigurno rukovanje JWT tokenima na klijentu
16. **Dockerizacija, CI/CD, deployment**
17. **Load/security testiranje**
18. **Dokumentacija** — README sa before/after, Swagger, architecture diagram

## Pravilo za rad sa Claude Code

Svaka sesija ima JEDAN jasan cilj iz faza gore. Pre izmena traži se analiza i plan. Korisnik (junior developer, srednji nivo Spring Boot, samouk Angular bez mentorstva) mora da razume SVAKU promenu — objašnjavaj odluke, ne samo "uradi i gotovo", posebno za Angular/Material deo i za arhitekturalne odluke na backendu.

Claude ima slobodu da predloži bolje rešenje od opisanog u ovom dokumentu (bilo da je u pitanju struktura baze/relacija na backendu ili izgled/komponente na frontendu) kad prepozna da postoji profesionalniji ili ispravniji pristup — opisano stanje je polazna tačka za analizu, ne fiksna specifikacija koje se mora striktno držati. U tom slučaju, ukratko objasni predlog i razlog pre implementacije.
