# 🎮 Connect4Game

**FourLoopLadies** ekibi tarafından geliştirilen bu oyun, klasik "Connect 4" oyununun Java & LibGDX kullanılarak hazırlanmış dijital versiyonudur. Oyuncular sırayla taş bırakarak yatay, dikey ya da çaprazda dört taşı yan yana getirmeye çalışır.

---

## 🚀 Özellikler

- 👥 PvP ve PvB modları
- 💡 3 zorluk seviyesi (Easy, Normal, Hard)
- 🧠 Bot AI: EasyBot, MediumBot, HardBot (Minimax destekli)
- 🎨 Oyuncu adı, renk ve ikon özelleştirmeleri
- 🔊 Ses ve müzik aç/kapa
- 📱 Masaüstü ve mobil destek

---

## 🛠️ Kurulum

1. Java 8+ ve Gradle yüklü olmalı.
2. Projeyi klonlayın:
   ```bash
   git clone https://github.com/MelikeBeyazli/FourLoopLadies_Connect4Game.git
   cd Connect4Game
   ./gradlew lwjgl3:run
   ```

---

## 👩‍💻 Ekip

| Üye                     | Rol                        | Görev                 |
|-------------------------|----------------------------|-----------------------|
| Sümeyye Alp             | Lider                      | Oyun geliştirme       |
| Melike Beyazlı          | Arayüz                     | Arayüz tasarımı       |
| Hatice Burcu Bulduk     | Arayüz                     | Arayüz geliştirme     |
| Renad El Türkmani       | Geliştirici                | Oyun arka planı       |

---

## 📄 Proje Raporu

📎 [Connect4Game_ProjeRaporu_FourLoopLadies.pdf](./Connect4Game_ProjeRaporu_FourLoopLadies.pdf)

---

## 🎥 Video

📺 [Oynanış Videosu (Google Drive)](https://drive.google.com/file/d/12iyVwTXtVRbh0sjO9gbaRTmQknC71RA2/view?usp=sharing)

---

## 📜 Lisans

MIT Lisansı – kullanmakta özgürsünüz (bkz: [LICENSE](./LICENSE))

---

## 🔧 Geliştirici Notları (gdx-liftoff Template)

Bu proje [libGDX](https://libgdx.com/) çatısı altında [gdx-liftoff](https://github.com/libgdx/gdx-liftoff) kullanılarak oluşturulmuştur.

### Platformlar

- `core`: Uygulama mantığı (ortak)
- `lwjgl3`: Masaüstü (LWJGL3)
- `html`: Web (GWT)

### Faydalı Gradle komutları

```bash
./gradlew lwjgl3:run       # Oyunu başlat
./gradlew lwjgl3:jar       # Jar dosyası oluştur
./gradlew html:dist        # Web için derleme
./gradlew clean            # Build dosyalarını sil
```

Daha fazla bilgi için [Gradle belgeleri](https://docs.gradle.org/) sayfasına göz atabilirsiniz.
