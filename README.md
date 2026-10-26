# Mano Temel Bilgisayarı Assembler (Assembly to Machine Code Converter)

## Proje Hakkında

Bu proje, M. Morris Mano'nun bilgisayar mimarisi literatüründe temel referans olarak kabul edilen 16-bitlik **Temel Bilgisayar (Basic Computer)** mimarisine ait sembolik assembly komutlarını doğrudan makine koduna (Binary ve Hexadecimal) dönüştüren Java tabanlı bir iki geçişli çevirici (assembler) uygulamasıdır.

Program, disk üzerindeki `Program.txt` dosyasından okuduğu assembly satırlarını sözdizimsel olarak ayrıştırır; doğrudan ve dolaylı adresleme (Indirect Addressing - I biti) kiplerini çözümler ve her bir talimatı 16-bitlik işlemci formatına uygun makine diline çevirerek terminal ekranına raporlar.

---

## Mimari Komut Yapısı ve Desteklenen Talimatlar

Program, 16-bitlik talimat formatı üzerinde bellek başvurulu, kayıt başvurulu ve giriş/çıkış komutlarını işleyecek şekilde yapılandırılmıştır:

* **Talimat Formatı:**
  * **Bit 15 (I Biti):** Adresleme kipi (0: Doğrudan Adresleme, 1: Dolaylı Adresleme).
  * **Bit 12–14 (Opcode):** 3-bitlik işlem kodu (Bellek başvurulu komutlar için 0–6).
  * **Bit 0–11 (Address):** 12-bitlik bellek adresi alanı (000–FFF).

* **Desteklenen Komut Kümeleri:**
  * **Bellek Başvurulu Komutlar (MRI):** `AND`, `ADD`, `LDA`, `STA`, `BUN`, `BSA`, `ISZ`
  * **Kayıt Başvurulu Komutlar (RRI):** `CLA`, `CLE`, `CMA`, `CME`, `CIR`, `CIL`, `INC`, `SPA`, `SNA`, `SZA`, `SZE`, `HLT`
  * **Giriş/Çıkış Komutları (IOI):** `INP`, `OUT`, `SKI`, `SKO`, `ION`, `IOF`
  * **Doğrudan Veri Tanımları (HEX Sabitleri):** Bellek adreslerine doğrudan onaltılık sayı yükleme desteği.

---

## Çalışma Mantığı

1. **Girdi Okuma:** `Program.txt` dosyası satır satır taranır, boşluklar ve geçersiz karakterler temizlenir.
2. **Belirteç Ayrıştırma (Tokenizing):** Her satır `[Bellek Adresi] [İşlem/Opcode] [Hedef Adres] [I Biti (Opsiyonel)]` yapısına göre parçalanır.
3. **Opcode Eşleme ve Bit Düzeyinde İşlemler (Bitwise Operations):**
   * Bellek başvurulu komutlarda işlem kodu sola kaydırılır (`opcodeInt << 12`), dolaylı adresleme biti 15. bite yerleştirilir (`iBiti << 15`) ve 12-bitlik hedef adresle bitsel VEYA (`|`) işlemine tabi tutulur.
   * Kayıt başvurulu ve G/Ç komutlarında doğrudan mimariye ait 16-bitlik sabit hex kodları atanır.
4. **Çıktı Formatlama:** Elde edilen 16-bitlik tamsayı, 4'er bitlik bloklar halinde binary (ikili) ve 4 haneli hex formatında formatlanarak konsola basılır.

---

## Örnek Girdi ve Çıktı

### Girdi (`Program.txt`)
```text
100 LDA 250
101 ADD 251 I
102 STA 252
103 HLT
250 0015
251 002A
252 0000

Program.txt dosyasi okundu.
100 0010 0010 0101 0000 (2250)
101 1001 0010 0101 0001 (9251)
102 0011 0010 0101 0010 (3252)
103 0111 0000 0000 0001 (7001)
250 0000 0000 0001 0101 (0015)
251 0000 0000 0010 1010 (002A)
252 0000 0000 0000 0000 (0000)

├── src/
│   └── _23010310075_Esma_Uğuz/
│       └── _23010310075_Esma_Uğuz.java   # Assembler ana kaynak kodu ve mantıksal sınıflar
├── Program.txt                           # Çevrilecek assembly kaynak kod dosyası
└── README.md                             # Proje dokümantasyonu
