# Backend analiza — LMS Univerzitet

**Datum analize:** 2026-06-19
**Obim:** `backend/src/main/java/rs/ac/singidunum/novisad/backend/` — 114 Java fajlova
**Tehnologije (potvrđeno iz `pom.xml`):** Java 17, Spring Boot 3.1.0, `io.jsonwebtoken` (jjwt) 0.11.5
**Napomena:** Ovo je čisto analitički izveštaj. Kod nije menjan.

---

## 1. Struktura paketa

Projekat koristi **layer-based (tehnički sloj) organizaciju na vrhu** — `controller/`, `dto/`, `model/`, `repository/`, `service/`, `security/` — a ne feature-based strukturu. Ovo **potvrđuje opis iz CLAUDE.md**, sa jednim nijansiranjem: `model/` nije potpuno flat, već je **hibridan**.

| Folder | Broj fajlova | Tip |
|---|---|---|
| `controller/` | 19 | flat — jedan controller po entitetu/domenu |
| `dto/` | 23 | flat — jedan DTO po entitetu + nekoliko specijalnih (`MojiPredmetiDTO`, `DodajOcenuDTO`, `StudentIzmenaDTO`...) |
| `model/` (root) | 12 fajla | flat — `GodinaStudija`, `Ishod`, `KancelariskiMaterijal`, `NastavnikNaRealizaciji`, `Obavestenje`, `Permission`, `PermissionEnum`, `Polaganje`, `PredmetnaObavestenja`, `Rektorat`, `StudentNaGodini`, `TipNastave`, `ZavrsniRad` |
| `model/academic/` | 5 fajla | delimično feature-based — `Fakultet`, `NastavniMaterijal`, `Predmet`, `StudijskiProgram`, `Univerzitet` |
| `model/user/` | 5 fajla | delimično feature-based — `RegistrovaniKorisnik` (baza), `Administrator`, `Nastavnik`, `Student`, `StudentskaSluzba` |
| `repository/` | 18 | flat — jedan repository po entitetu |
| `service/` | 17 | flat — jedan service po entitetu |
| `security/` | 1 + 3 podfoldera (`jwt/`, `request/`, `response/`, `services/`) | feature-based |

**Zaključak:** podela `model/` u `academic`/`user` podfoldere postoji, ali je **nedovršena/nekonzistentna** — entiteti kao `Polaganje`, `ZavrsniRad`, `StudentNaGodini`, `PredmetnaObavestenja` direktno referenciraju `Student`/`Nastavnik` (iz `model/user`) i `Predmet` (iz `model/academic`), ali sami ostaju u root `model/`. `controller/`, `dto/`, `service/`, `repository/` su u potpunosti flat — sve 18+ entiteta pomešano u istom folderu bez podele. Ovo potvrđuje fazu 3 iz plana (backend restrukturiranje na feature-based pakete) kao opravdanu.

---

## 2. Entiteti i relacije

Ukupno **22 `@Entity` klase** + 3 enuma (`Ishod`, `TipNastave`, `PermissionEnum`).

### Organizaciona/akademska hijerarhija (`model/academic/`)

| Entitet | Relacija | Kardinalnost | Cascade / orphanRemoval |
|---|---|---|---|
| `Univerzitet` | → `Rektorat` | ManyToOne | nema |
| `Univerzitet` | → `Fakultet` (mappedBy) | OneToMany | nema |
| `Univerzitet` | → `Nastavnik` (mappedBy) | OneToMany | nema |
| `Fakultet` | → `Univerzitet` (`nullable=false`) | ManyToOne | nema |
| `Fakultet` | → `StudijskiProgram` (mappedBy) | OneToMany | nema |
| `StudijskiProgram` | → `Fakultet` (`nullable=false`) | ManyToOne | nema |
| `StudijskiProgram` | → `Predmet` (mappedBy) | OneToMany | nema |
| `Predmet` | → `StudijskiProgram` | ManyToOne | nema |
| `Predmet` | → `Nastavnik` | ManyToOne | nema |
| `Predmet` | → `NastavniMaterijal` (mappedBy) | OneToMany | nema |
| `Predmet` | → `Polaganje` (mappedBy) | OneToMany | nema |
| `Predmet` | → `PredmetnaObavestenja` (mappedBy) | OneToMany | nema |
| `Predmet` ↔ `Student` | `predmet` (vlasnik je `Student.predmet`) | **ManyToMany** | nema |
| `NastavniMaterijal` | → `Predmet` | ManyToOne | nema |
| `Rektorat` | → `Univerzitet` (mappedBy) | OneToMany | nema |

### Korisnici (`model/user/`, single-table inheritance)

`RegistrovaniKorisnik` (baza) → `Administrator`, `Nastavnik`, `Student`, `StudentskaSluzba` (sve `@Inheritance(SINGLE_TABLE)`, ista tabela `registrovani_korisnici`).

| Entitet | Relacija | Kardinalnost | Cascade / orphanRemoval |
|---|---|---|---|
| `RegistrovaniKorisnik` | ↔ `Permission` (join table) | ManyToMany | nema |
| `Nastavnik` | → `Univerzitet` | ManyToOne | nema |
| `Nastavnik` | → `Predmet` (mappedBy) | OneToMany | nema |
| `Nastavnik` | → `Polaganje` (mappedBy) | OneToMany | nema |
| `Student` | → `Fakultet` | ManyToOne | nema |
| `Student` | ↔ `GodinaStudija` (join table `studentNaGodini`) | **ManyToMany** | nema |
| `Student` | → `Polaganje` (mappedBy) | OneToMany | nema |
| `Student` ↔ `Predmet` (join table `studentiPredmeti`) | **ManyToMany** | nema |

### Ostali entiteti (root `model/`)

| Entitet | Relacija | Kardinalnost | Cascade / orphanRemoval |
|---|---|---|---|
| `Polaganje` | → `Predmet`, `Student`, `Nastavnik` | ManyToOne (×3) | nema |
| `Polaganje` | → `Obavestenje` | **OneToOne, `cascade=CascadeType.ALL`** | **jedina cascade relacija u celom modelu**, orphanRemoval nije postavljen |
| `PredmetnaObavestenja` (extends `Obavestenje`) | → `Predmet` | ManyToOne | nema |
| `StudentNaGodini` | → `GodinaStudija`, `Student`, `StudijskiProgram` | ManyToOne (×3) | nema |
| `ZavrsniRad` | → `Student`, `Nastavnik` (mentor) | ManyToOne (×2) | nema |
| `GodinaStudija` | (nema definisane relacije) | — | konstruktor prima `Set<Student>` ali ga ne čuva — mrtav/nedovršen kod |
| `NastavnikNaRealizaciji` | `nastavnik_id: Long` | **plain kolona, NIJE JPA relacija** | nema referencijalnog integriteta na ORM nivou |
| `KancelariskiMaterijal`, `Rektorat` (sopstvena polja) | — | — | — |

### Strukturne nekonzistentnosti vredne pomena

- **`NastavnikNaRealizaciji`** koristi plain `Long nastavnik_id` umesto `@ManyToOne Nastavnik` — nema FK integriteta na nivou JPA.
- **`GodinaStudija`** nema povratnu `@OneToMany(mappedBy=...)` stranu za `Student.godinaStudija`, iako konstruktor sugeriše nameru veze.
- **Dupliran model za istu poslovnu vezu**: postoji i samostalan entitet `StudentNaGodini` (FK na `Student`/`GodinaStudija`/`StudijskiProgram`) i `@ManyToMany` `Student.godinaStudija` koji koristi join tabelu istog imena (`studentNaGodini`) — dva konkurentna mehanizma za upis studenta na godinu.
- **`Ishod` enum** se koristi u `NastavniMaterijal.ishod` bez `@Enumerated(EnumType.STRING)` → implicitna ORDINAL serijalizacija, rizična pri izmeni redosleda enum vrednosti.
- **Fetch tipovi**: nigde nije explicitno naveden `fetch=...` (osim `RegistrovaniKorisnik.permissions`, gde je naveden LAZY iako je to već default) — sve relacije koriste JPA default (`@ManyToOne`/`@OneToOne` = EAGER, `@OneToMany`/`@ManyToMany` = LAZY), što direktno doprinosi N+1 problemu (sekcija 3) jer se nigde ne koristi `@EntityGraph` ili `JOIN FETCH` da se to kompenzuje.

---

## 3. N+1 query problem — konkretna mesta

Pošto nijedan repository ne koristi `@EntityGraph`/`JOIN FETCH`, a relacije su default LAZY/EAGER bez optimizacije, svaka iteracija kroz listu entiteta sa pristupom relaciji generiše dodatne SELECT upite.

| Lokacija | Problem |
|---|---|
| `controller/PredmetController.java:36-51` (`getAll`) | Petlja kroz sve `Predmet`, za svaki `getNastavniMaterijal()`, `getStudijskiProgram()`, `getNastavnik()` |
| `controller/PolaganjeController.java:48-69` (`getAll`) | Za svaki `Polaganje`: `getPredmet()`, `getStudent()`, `getNastavnik()`, pa na `Predmet` ponovo `getNastavniMaterijal()` — **drugostepeni N+1** |
| `controller/PolaganjeController.java:141-184` (`getPredmeteProfesora`) | **Najgori nalaz u kontrolerima**: ugnježdena trostruka petlja — za svaki predmet → za svakog studenta → `for (Polaganje pp : service.findAll())` pozvan **iznutra ugnježdene petlje** → O(predmeti × studenti × svaPolaganja) |
| `controller/PolaganjeController.java:187-232` (`getPrijavljeniIspite`, `getPrijavljeniIspitiPoPredmetu`) | Isti pattern repetiran |
| `controller/StudentController.java:114-155` (`getPolozeniIspiti`) | `getPredmet()` (ManyToMany) → za svaki predmet `getNastavnik()`, `getStudijskiProgram()`, `getNastavniMaterijal()`; plus `polaganjaService.findAll()` učitano nezavisno i ugnježdeno (O(predmeti × svaPolaganja)) |
| `controller/StudentController.java:157-199, 201-279, 358-417` (`getNPIspiti`, `getSviPredmeti`, `getSviAktivniPredmeti`, `getIspitiZaPrijavu`) | Identičan obrazac ponovljen 4×, uključujući ponovljeni `polaganjaService.findAll()` unutar ugnježdene petlje (linija 391) |
| `controller/NastavnikController.java:100-134` (`getMojiPredmeti`) | Četvorostruki N+1 u jednom mapiranju: `getNastavnik()`, `getStudijskiProgram()`, `getNastavniMaterijal()` (sa internim `.stream()`), `getStudenti()` (sa internim `.stream()`) |
| `controller/FakultetController.java:30-37` (`getAll`) | `getUniverzitet()` po fakultetu |
| `controller/StudijskiProgramController.java:33-57` (`getAll`) | `getFakultet()` i `getPredmeti()` po programu |
| `controller/StudentNaGodiniController.java:34-50, 103-121` | `getGodinaStudija()`, `getStudent()`, `getStudijskiProgram()`, pa na `StudijskiProgram` ponovo `getFakultet()` — drugostepeni N+1 |
| `controller/ZavrsniRadController.java:32-44, 64-96` | `getStudent()`, `getMentor()` po radu; `findByStundet`/`findByMentor` rade linearnu pretragu kroz SVE zapise umesto query-ja po FK |
| `controller/ZavrsniRadController.java:119-134` (`create`) | Petlja kroz `service.findAll()` da provери duplikat — treba `existsByStudentId` |
| `controller/RektoratController.java:31-41` | `getUniverziteti()` po rektoratu |
| `controller/UniverzitetController.java:30-40` | `getRektorat()` po univerzitetu |
| `controller/PredmetnaObavestenjaController.java:46-79, 176-218` | Trostruki N+1 (`getPredmet()`, `getNastavnik()`, `getNastavniMaterijal()`) + redundantan `predmetService.findOne(...)` po elementu petlje + interna petlja kroz `getStudenti()` da provери članstvo (umesto `existsByPredmetIdAndStudentId`) |
| **`repository/PolaganjeRepository.java:14-23`** | Najgori pojedinačni nalaz: `private` metoda `findAllByStudent` učitava **celu** tabelu `Polaganje` preko `findAll()`, zatim filtrira u Java kodu (`pp.getStudent().getId() == id`) umesto `WHERE student_id = ?`. Iako je obeležena `@SuppressWarnings("unused")` (mrtav kod), identičan/gori pattern (`service.findAll()` bez filtera) je aktivno korišćen posvuda u kontrolerima gore navedenim. |

---

## 4. `getAll()` / `findAll()` bez paginacije

**Nijedan** od 18 repository-ja ne koristi `Pageable`/`Page<T>`. Svi nasleđuju `JpaRepository<T, Long>` i svi servisi eksponiraju `findAll()` koji vraća kompletnu listu.

| Entitet | Service metod | Endpoint koji koristi |
|---|---|---|
| Administrator | `AdministratorService.findAll()` | `GET /api/administratori` |
| Fakultet | `FakultetService.findAll()` | `GET /api/fakulteti` |
| GodinaStudija | `GodinaStudijaService.findAll()` | nema vidljivog REST endpointa (nema kontrolera) |
| KancelariskiMaterijal | `KancelariskiMaterialService.findAll()` | `GET /api/kancelariskiMaterial` |
| Nastavnik | `NastavnikService.findAll()` | `GET /api/nastavnici` |
| NastavniMaterijal | `NastavniMaterijalService.findAll()` | `GET /api/nastavnimaterijal` |
| Obavestenje | `ObavestenjaService.findAll()` | `GET /api/obavestenja` |
| Polaganje | `PolaganjeService.findAll()` | `GET /api/polaganja` + pozvan iznutra još 5+ metoda (sekcija 3) |
| PredmetnaObavestenja | `PredmetnaObavestenjaService.findAll()` | `GET /api/predmetnaObavestenja`, `/gbp/{id}` |
| Predmet | `PredmetService.findAll()` | `GET /api/predmeti` |
| RegistrovaniKorisnik | `RegistrovaniKorisnikService.findAll()` | `GET /api/registrovaniKorisnici` |
| Rektorat | `RektoratService.findAll()` | `GET /api/rektorati` |
| StudentNaGodini | `StudentNaGodiniService.findAll()` | `GET /api/sng`, `/fbs/{id}` |
| Student | `StudentService.findAll()` | `GET /api/studenti` |
| StudentskaSluzba | `StudentskaSluzbaService.findAll()` | `GET /api/studentskaSluzba` |
| StudijskiProgram | `StudijskiProgramService.findAll()` | `GET /api/studijskiProgrami` |
| Univerzitet | `UniverzitetService.findAll()` | `GET /api/univerziteti` |
| ZavrsniRad | `ZavrsniRadService.findAll()` | `GET /api/zavrsniRad`, `/fbs/{id}`, `/fbm/{id}`, `create` |

**Posebno kritično:** `Polaganje`, `Predmet`, `Student`, `ZavrsniRad`, `PredmetnaObavestenja`, `RegistrovaniKorisnik` — njihov `findAll()` se koristi i kao "in-memory baza" za filtriranje unutar kontrolera (vidi sekciju 3), što kombinuje nedostatak paginacije SA punim učitavanjem tabele u memoriju **na svaki HTTP zahtev**.

---

## 5. Cascade / orphan removal — rizici pri brisanju

**Nalaz:** U celom `model/` paketu postoji **samo jedna** relacija sa explicitnim cascade-om: `Polaganje.obavestenje` (`@OneToOne(cascade = CascadeType.ALL)`). **Nigde** se ne koristi `orphanRemoval = true`. Sve ostale `@OneToMany` relacije nemaju nikakav cascade — što znači da brisanje "roditelja" entiteta dok postoje "deca" zapisi može izazvati `DataIntegrityViolationException` (FK constraint violation) ili ostaviti "sirotinjske" FK zapise, zavisno od nullability kolone i DB ponašanja.

Svi `delete()` metodi u svih 18 service klasa su tanki wrapperi oko `repository.deleteById(id)` / `repository.delete(entity)` — **nijedan ne radi proveru zavisnih zapisa pre brisanja**, i nijedan controller `delete` endpoint nema `try/catch` oko pozivа, što propagira sirovi 500 error klijentu.

Konkretni rizici, od najkritičnijeg:

| Relacija | FK nullable? | Rizik |
|---|---|---|
| `Univerzitet → Fakultet` (`Fakultet.univerzitet_id`) | **`nullable=false`** (`Fakultet.java:33`) | **Najkritičnije** — brisanje Univerziteta dok postoji 1+ Fakultet → garantovan FK constraint violation → HTTP 500 (`UniverzitetService.delete()`, `UniverzitetController.delete()` nemaju proveru/catch) |
| `Fakultet → StudijskiProgram` (`StudijskiProgram.fakultet_id`) | `nullable=false` | Isti problem — `FakultetService.delete()` bez provere |
| `StudijskiProgram → Predmet` | nije `nullable=false` na `Predmet` strani | Manje rigidno, ali i dalje ostavljaju sirotinjske/nekonzistentne podatke bez ON DELETE definicije |
| `Rektorat → Univerzitet` | nije `nullable=false` | Sirotinjski FK bez čišćenja |
| `Predmet → NastavniMaterijal / Polaganje / PredmetnaObavestenja` | FK nije `nullable=false`, ali nema cascade niti SET NULL | Brisanje Predmeta dok postoje povezani zapisi vrlo verovatno puca na FK constraint-u u bazi (default RESTRICT/NO ACTION) |
| `Nastavnik → Predmet / Polaganje` | nije `nullable=false` | Sirotinjski FK pri brisanju Nastavnika |
| `Student → Polaganje` | nije `nullable=false` | Sirotinjski FK pri brisanju Studenta |

**Dodatni rizik bez transakcione zaštite:** `RegistrovaniKorisnikService.promeniTipKorisnika()` i `upisStudenta()` (linije 61-151) rade `repository.delete(korisnik)` praćeno odmah `repository.save(noviTip)` — **bez `@Transactional`** (nigde u celom `service/` folderu se ne koristi `@Transactional`). Ako `save()` ne uspe nakon `delete()`, korisnik je trajno obrisan bez zamene.

**Zaključak:** Ovo direktno potvrđuje poznati bug iz CLAUDE.md ("kaskadno brisanje entiteta ostavlja foreign key-eve na izbrisane entitete"). Preporuka za fazu 6 (bug fixevi): mapirati sve `@OneToMany` relacije i odlučiti po slučaju — `orphanRemoval=true` + `CascadeType.REMOVE` gde je poslovno ispravno (npr. `Predmet → NastavniMaterijal`), ili explicitna provera u service sloju sa smislenom 409 Conflict porukom umesto propuštanja raw exception-a do klijenta.

---

## 6. Security — trenutno stanje

### 6.1 Arhitektura

- `security/WebSecurityConfig.java` koristi Spring Security 6 stil (`SecurityFilterChain` bean, `@EnableMethodSecurity` na klasi — omogućava `@PreAuthorize`).
- JWT: `AuthTokenFilter` (OncePerRequestFilter) čita `Authorization: Bearer`, validira preko `JwtUtils`, postavlja `Authentication` u `SecurityContextHolder`. `JwtUtils` generiše HS256 token. **Code smell:** claim sa rolama se dodaje pod ključem `jwtSecret` (`.claim(jwtSecret, ...)`) umesto fiksnog naziva poput `"roles"`.
- Role su definisane u `PermissionEnum`: `ADMINISTRATOR_PERMISSION`, `STUDENT_PERMISSION`, `NASTAVNIK_PERMISSION`, `KORISNIK_PERMISSION`, `STUDENTSKASLUZBA_PERMISSION` — **nisu** standardni `ROLE_*` Spring autoriteti, provere koriste `hasAnyAuthority('XXX_PERMISSION')`.
- Lozinke: `BCryptPasswordEncoder` je definisan kao bean, ali se **stvarno koristi samo u `AuthentificationController.registerUser()`**.

### 6.2 KRITIČAN nalaz — filter chain `permitAll()` na skoro svim path-ovima

`WebSecurityConfig.java` ima `requestMatchers("/api/<entitet>/**").permitAll()` za **praktično svaki** resursni path u sistemu (fakulteti, predmeti, studenti, nastavnici, polaganja, administratori, obaveštenja, itd.). Posledica: zaštita u praksi zavisi **isključivo** od toga da li postoji `@PreAuthorize` na konkretnoj metodi kontrolera — nema "default deny" na nivou filter chain-a. Ako endpoint nema `@PreAuthorize`, on je **potpuno otvoren**, čak i bez JWT tokena.

### 6.3 KRITIČAN nalaz — privilege escalation kroz signup

`POST /api/auth/signup` je javan (`permitAll()` + `@CrossOrigin(origins="*")`) i dozvoljava **bilo kom anonimnom korisniku da sam sebi dodeli rolu `"administrator"` ili `"studentskaSluzba"`** prilikom registracije — nema provere ko poziva signup niti ograničenja koje role se mogu samododeliti.

### 6.4 KRITIČAN nalaz — nekonzistentno heširanje lozinki + izlaganje hash-a

BCrypt se primenjuje **samo** u `/api/auth/signup`. Svi drugi create-endpointi (`POST /api/studenti`, `/api/nastavnici`, `/api/registrovaniKorisnici`, itd.) pozivaju `service.save(r)` direktno — lozinka se čuva u plain text-u (ili kako klijent pošalje). Dodatno, DTO klase (`StudentDTO`, `NastavnikDTO`, `AdministratorDTO`, `RegistrovaniKorisnikDTO`, `StudentskaSluzbaDTO`) **vraćaju `getLozinka()` u JSON odgovorima** svakog GET endpointa — password hash/plaintext se sistematski izlaže klijentu.

### 6.5 Endpointi potpuno bez ikakve zaštite (ni `@PreAuthorize` ni efektivni filter-chain auth)

- **Cele klase bez ijednog `@PreAuthorize`:** `KancelariskiMaterijalController`, `NastavniMaterijalController`, `StudentNaGodiniController`, `ZavrsniRadController` — sav CRUD (uključujući DELETE) potpuno otvoren.
- **Sve DELETE operacije bez zaštite:** `/api/administratori/{id}`, `/api/nastavnici/{id}`, `/api/registrovaniKorisnici/{id}`, `/api/studentskaSluzba/{id}`, plus sve DELETE u gore navedenim klasama bez `@PreAuthorize` — bilo ko može brisati administratore, nastavnike, korisnike, diplomske radove **bez autentifikacije**.
- **Kreiranje korisnika mimo signup-a:** `POST /api/studenti`, `POST /api/nastavnici`, `POST /api/registrovaniKorisnici` — bez zaštite i bez heširanja lozinke.
- **Skoro sve GET liste/by-id u svim kontrolerima** — masovno izlaganje podataka (uključujući lozinke) bez ikakve autentifikacije.

### 6.6 IDOR nalaz

`StudentController`: `GET /api/studenti/{id}/polozeniIspiti` i `/{id}/nepolozeniIspiti` imaju `@PreAuthorize` po roli, ali **ne provеravaju vlasništvo** (`id == userId`) — za razliku od susednih endpointa u istoj klasi (`sviIspiti`, `sviAPredmeti`, `ispitiZaPrijavu`) koji tu proveru imaju. Svaki ulogovani student trenutno može pregledati ispite bilo kog drugog studenta promenom path varijable.

### 6.7 CORS

Nema centralnog `CorsConfiguration` bean-a — CORS je per-controller preko `@CrossOrigin`:
- Svi 18 resursnih kontrolera: `@CrossOrigin(origins="http://localhost:4200")` (samo dev origin, nema produkcionog).
- `AuthentificationController`: `@CrossOrigin(origins="*")` — u kombinaciji sa otvorenim signup-om (6.3) povećava napadnu površinu.

### 6.8 Endpointi koji JESU ispravno zaštićeni (referenca, da se vidi obrazac koji treba generalizovati)

- `PredmetnaObavestenjaController` — svaki endpoint ima bar minimalnu `@PreAuthorize` zaštitu, i CREATE/UPDATE/DELETE proveravaju vlasništvo nad predmetom.
- `PredmetController.izmeniSilabus`, `NastavnikController.getMojiPredmeti`, `PolaganjeController.dobaviNastavniku` — koriste obrazac `@PreAuthorize(...)` + manuelna provera `id.equals(userId)` iz JWT-a. Ovaj obrazac postoji u kodu i treba ga primeniti svuda gde nedostaje (vidi 6.5, 6.6).

---

## 7. Slike čuvane kao plain String

| Entitet/DTO | Polje | Lokacija |
|---|---|---|
| `Univerzitet` | `slika: String` | `model/academic/Univerzitet.java:32` |
| `Fakultet` | `slika: String` | `model/academic/Fakultet.java:29` |
| `Rektorat` | `slika: String` | `model/Rektorat.java:20` |
| `Obavestenje` | `slika: String` | `model/Obavestenje.java:29` |
| `PredmetnaObavestenja` (nasleđuje `Obavestenje`) | `slika` (preko roditelja) | `model/PredmetnaObavestenja.java` |
| odgovarajući DTO-ovi | `slika`/`slikaURL: String` | `UniverzitetDTO`, `FakultetDTO`, `RektoratDTO`, `ObavestenjeDTO`, `PredmetnaObavestenjaDTO` |

**Nema nijednog `MultipartFile` endpointa u celom `controller/` paketu** — sve slike se trenutno prosleđuju kao gotov String (URL ili Base64) kroz JSON body, bez validacije tipa/veličine. Dodatni bugovi primećeni u prolazu:
- `ObavestenjeController.java:72-74` — poređenje stringova operatorom `==` umesto `.equals()` pri proveri da li je `slika` prazna (referentno poređenje, klasičan Java bug).
- `PredmetnaObavestenjaController.java:116,142` — `slika` se eksplicitno postavlja na `""` pri create/update, što znači da se za predmetna obaveštenja slika praktično nikad ne čuva kroz te endpointe.

---

## 8. FK relacije implementirane kao plain text

| Entitet | Polje | Tip | Trebalo bi FK ka | Napomena |
|---|---|---|---|---|
| `Fakultet` | `dekan` | `String` | `Nastavnik` | `Fakultet` trenutno nema nikakvu relaciju ka `Nastavnik` |
| `StudijskiProgram` | `rukovodilac` | `String` | `Nastavnik` | `StudijskiProgram` trenutno nema relaciju ka `Nastavnik` |

Oba polja se 1:1 propagiraju kao plain String kroz odgovarajuće DTO-ove (`FakultetDTO.dekan`, `StudijskiProgramDTO.rukovodilac`) i kontrolere, bez ikakve veze sa `NastavnikDTO`. Obrazac `@ManyToOne Nastavnik` već postoji u kodu (`Predmet.nastavnik`, `Predmet.java:53-55`) i može se direktno primeniti i ovde — potvrđuje predlog iz CLAUDE.md (zameniti plain text dropdown-om/FK selekcijom).

---

## 9. Validacija unosa

### 9.1 Datum "od"/"do" — kompletno odsustvo validacije

Entiteti sa parom datuma: `Obavestenje.vremePocetka/vremeKraja`, `Predmet.vremePocetka/vremeKraja`, `Polaganje.pocetak/kraj`.

- **Nula** pogodaka za `@AssertTrue` u celom projektu.
- `ObavestenjaService` i `ObavestenjeController.create/update` (linije 55-80) nemaju nikakvu proveru da `vremeKraja` bude posle `vremePocetka` — server čak sam postavlja `datum = LocalDateTime.now()`, ali nikad ne validira opseg.
- Nula pogodaka za `isBefore/isAfter/compareTo` u `service/` paketu.

**Zaključak: ne postoji nijedna validacija "do > od" — ni za Obavestenje, ni za Predmet, ni za Polaganje.** Ovo potvrđuje poznati bug iz CLAUDE.md.

### 9.2 Validacione anotacije — skoro potpuno odsustvo

Pretraga `@NotNull/@NotBlank/@NotEmpty/@Size/@Email/@Pattern/@AssertTrue/@Min/@Max` kroz ceo backend pronalazi anotacije u **samo 2 fajla**:
- `security/request/LoginRequest.java` — `@NotBlank` na email/lozinka.
- `security/request/SignupRequest.java` — `@NotBlank`, `@Email`; ali `korisnickoIme` nema nikakvu anotaciju.

**Nijedan fajl u `model/` ili `dto/` (svi entiteti i svi DTO-ovi) nema ni jednu validacionu anotaciju.** Konkretni primeri praznine:
- `RegistrovaniKorisnik.email` — samo DB `@UniqueConstraint`, nema `@Email` format provere ni `@NotBlank`.
- `Predmet.espb` — primitivni `int` bez `@Min`/`@Max` (moguće espb = -5 ili 0).
- `Obavestenje.naslov/sadrzaj`, `Fakultet.naziv`, `StudijskiProgram.naziv` — nema `@NotBlank`, moguće sačuvati prazne nazive/sadržaje (potvrđuje pomen iz CLAUDE.md o "asdasdasd" test podacima koji prolaze nesmetano).

### 9.3 `@Valid` na `@RequestBody`

`@Valid` se koristi **isključivo** u `AuthentificationController` (signin, signup). Na svih ostalih ~30+ `@RequestBody` parametara u svim ostalim kontrolerima (Obavestenje, PredmetnaObavestenja, Fakultet, StudijskiProgram, Predmet, Student, Nastavnik, Administrator, RegistrovaniKorisnik, Univerzitet, Rektorat, StudentskaSluzba, ZavrsniRad, Polaganje, StudentNaGodini, NastavniMaterijal, KancelariskiMaterijal) **`@Valid` potpuno izostaje**.

**Ovo je dvostruki propust** koji treba rešavati zajedno: (a) dodati `jakarta.validation` anotacije na model/DTO klase, (b) dodati `@Valid` na sve `@RequestBody` parametre — trenutno, i da se dodalo samo jedno od to dvoje, validacija ne bi imala efekta.

---

## 10. Endpointi po kontroleru

> Legenda: ⛔ = potpuno bez zaštite (ni `@PreAuthorize` ni efektivni filter-chain auth) · 🔒 = `@PreAuthorize` postoji · 🔒+ = `@PreAuthorize` + manuelna provera vlasništva (id == userId)

### AuthentificationController (`/api/auth`, CORS: `*`)
| Metod | Path | Zaštita |
|---|---|---|
| POST | `/signin` | ⛔ (javno po dizajnu) |
| POST | `/signup` | ⛔ (javno po dizajnu — **ali dozvoljava samododelu role admin**, vidi 6.3) |

### AdministratorController (`/api/administratori`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/` | ⛔ |
| GET | `/{id}` | ⛔ |
| POST | `/` | 🔒 ADMINISTRATOR |
| PUT | `/{id}` | 🔒 ADMINISTRATOR |
| DELETE | `/{id}` | ⛔ |

### ExportImportController (`/api/export`)
Nema aktivnih endpointa — sav kod zakomentarisan. (Potvrđuje CLAUDE.md bug: izdavanje dokumenata ne radi.)

### FakultetController (`/api/fakulteti`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/` | ⛔ |
| GET | `/{id}` | ⛔ |
| GET | `/s/{sifraFakulteta}` | ⛔ |
| POST | `/` | 🔒 ADMINISTRATOR |
| PUT | `/{id}` | 🔒 ADMINISTRATOR |
| DELETE | `/{id}` | 🔒 ADMINISTRATOR |

### KancelariskiMaterijalController (`/api/kancelariskiMaterial`)
Sve operacije (GET×2, POST, PUT, DELETE) — ⛔ cela klasa bez `@PreAuthorize`.

### NastavnikController (`/api/nastavnici`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/` | ⛔ |
| GET | `/{id}` | ⛔ |
| POST | `/` | ⛔ |
| PUT | `/{id}` | 🔒 NASTAVNIK, ADMINISTRATOR |
| DELETE | `/{id}` | ⛔ |
| GET | `/{id}/mojiPredmeti` | 🔒+ NASTAVNIK |

### NastavniMaterijalController (`/api/nastavnimaterijal`)
Sve operacije (GET×2, POST, PUT, DELETE) — ⛔ cela klasa bez `@PreAuthorize`.

### ObavestenjeController (`/api/obavestenja`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/` | ⛔ |
| GET | `/{id}` | ⛔ |
| POST | `/` | 🔒 STUDENTSKASLUZBA |
| PUT | `/{id}` | 🔒 STUDENTSKASLUZBA |
| DELETE | `/{id}` | 🔒 STUDENTSKASLUZBA |

### PolaganjeController (`/api/polaganja`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/` | ⛔ |
| GET | `/{id}` | ⛔ |
| POST | `/c` | 🔒 STUDENT |
| PUT | `/{id}` | 🔒 NASTAVNIK |
| DELETE | `/{id}` | 🔒 STUDENTSKASLUZBA, ADMINISTRATOR |
| GET | `/dobaviNastavniku/{idNastavnika}` | 🔒+ NASTAVNIK |
| GET | `/prijavljeni/{id}` | ⛔ |
| GET | `/prijavljeniPoPredmetu/{id}` | ⛔ |

### PredmetController (`/api/predmeti`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/`, `/{id}`, `/s/{sifraPredmeta}` | ⛔ |
| POST | `/` | 🔒 ADMINISTRATOR |
| PUT | `/{id}` | 🔒 ADMINISTRATOR |
| DELETE | `/{id}` | 🔒 ADMINISTRATOR |
| POST | `/{id}/dodajNastavnika` | 🔒 STUDENTSKASLUZBA |
| PUT | `/{id}/izmeniSilabus` | 🔒+ NASTAVNIK |

### PredmetnaObavestenjaController (`/api/predmetnaObavestenja`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/`, `/{id}`, `/gbp/{id}` | 🔒 sve 4 role |
| POST | `/` | 🔒+ NASTAVNIK |
| PUT | `/{id}` | 🔒 NASTAVNIK, STUDENTSKASLUZBA, ADMINISTRATOR |
| DELETE | `/{id}` | 🔒 NASTAVNIK, STUDENTSKASLUZBA, ADMINISTRATOR |

*(jedan od retkih kontrolera bez "rupa")*

### RegistrovaniKorisnikController (`/api/registrovaniKorisnici`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/` | ⛔ (vraća sve korisnike sa lozinkama) |
| GET | `/{id}` | 🔒 sve 4 role |
| POST | `/` | ⛔ (bypass-uje signup, bez hash-a lozinke) |
| PUT | `/{id}` | 🔒+ sve role |
| DELETE | `/{id}` | ⛔ |
| PUT | `/izmeniTip/{tip}` | 🔒 ADMINISTRATOR |
| PUT | `/{id}/dodeliStudenta` | 🔒 STUDENTSKASLUZBA |

### RektoratController (`/api/rektorati`)
GET×2 — ⛔; POST/PUT/DELETE — 🔒 ADMINISTRATOR.

### StudentController (`/api/studenti`)
| Metod | Path | Zaštita |
|---|---|---|
| GET | `/`, `/{id}` | ⛔ |
| POST | `/` | ⛔ (bez hash-a lozinke) |
| PUT | `/{id}` | 🔒+ STUDENT |
| GET | `/{id}/polozeniIspiti` | 🔒 (sve role) **bez provere vlasništva — IDOR** |
| GET | `/{id}/nepolozeniIspiti` | 🔒 (sve role) **bez provere vlasništva — IDOR** |
| GET | `/{id}/sviIspiti` | 🔒+ |
| GET | `/{id}/sviAPredmeti` | 🔒+ |
| GET | `/{id}/nepolozeniIspiti/{id_predmeta}` | 🔒+ |
| PUT | `/dsp/{smer_id}` | 🔒 STUDENTSKASLUZBA, ADMINISTRATOR |
| GET | `/{id}/ispitiZaPrijavu` | 🔒+ STUDENT |

### StudentNaGodiniController (`/api/sng`)
Sve operacije (GET×3, POST, PUT, DELETE) — ⛔ cela klasa bez `@PreAuthorize`.

### StudentskaSluzbaController (`/api/studentskaSluzba`)
GET×2 — ⛔; POST — 🔒 ADMINISTRATOR; PUT — 🔒 ADMINISTRATOR/STUDENTSKASLUZBA; DELETE — ⛔.

### StudijskiProgramController (`/api/studijskiProgrami`)
GET×3 — ⛔; POST/PUT/DELETE — 🔒 ADMINISTRATOR.

### UniverzitetController (`/api/univerziteti`)
GET×2 — ⛔; POST/PUT/DELETE — 🔒 ADMINISTRATOR.

### ZavrsniRadController (`/api/zavrsniRad`)
Sve operacije (GET×4, POST, PUT, DELETE) — ⛔ cela klasa bez `@PreAuthorize`.

---

## Rezime — top prioriteti za fazu 4 (security audit)

1. **Default-deny na filter-chain nivou** — ukloniti masovni `permitAll()` u `WebSecurityConfig` i postaviti `.anyRequest().authenticated()` kao default, sa explicitnim `permitAll()` samo za stvarno javne GET endpointe (npr. javne informacije o fakultetima/smerovima iz CLAUDE.md "Posetilac" role).
2. **Zatvoriti signup privilege escalation** — `/api/auth/signup` ne treba da dozvoljava samododelu `administrator`/`studentskaSluzba` role; te role treba da se dodeljuju samo kroz zaštićen admin endpoint.
3. **Hešovati lozinke na SVIM create putevima**, ne samo signup-u; ukloniti `lozinka`/`getLozinka()` iz svih DTO odgovora.
4. **Dodati `@PreAuthorize` na sve nezaštićene endpointe**, posebno DELETE operacije i cele klase (`KancelariskiMaterijalController`, `NastavniMaterijalController`, `StudentNaGodiniController`, `ZavrsniRadController`).
5. **Ispraviti IDOR** u `StudentController` (`polozeniIspiti`, `nepolozeniIspiti`) po obrascu koji već postoji u susednim metodama te klase.
6. Za fazu 5/6: rešiti N+1 kroz `@EntityGraph`/`JOIN FETCH` + uvesti `Pageable` svuda + mapirati cascade/orphanRemoval po tabeli iz sekcije 5.
7. Za fazu 6/9: dodati `@AssertTrue`/custom validator za "do > od" datume, dodati `jakarta.validation` anotacije na model/DTO + `@Valid` na kontrolere (zajedno, jedno bez drugog nema efekta).
