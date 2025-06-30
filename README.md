# Finance Management System

Bu proje, mikroservis mimarisi kullanarak geliştirilmiş bir finans yönetim sistemidir.

## 🚀 Docker ile Çalıştırma

### Gereksinimler
- Docker & Docker Compose
- Maven (jar dosyalarını build etmek için)

### Hızlı Başlangıç

1. **Environment dosyasını hazırlayın:**
   ```bash
   # environment.env dosyasını .env olarak kopyalayın
   cp environment.env .env
   
   # .env dosyasını kendi ortamınıza göre düzenleyin
   # Özellikle veritabanı ve email ayarlarını kontrol edin
   ```

2. **Jar dosyalarını build edin:**
   ```bash
   mvn clean install
   ```

3. **Docker container'ları başlatın:**
   ```bash
   docker-compose up --build -d
   ```

4. **Servislerin durumunu kontrol edin:**
   ```bash
   docker-compose ps
   ```

### 🌐 Servis URL'leri

| Servis | URL | Port |
|--------|-----|------|
| Discovery Server | http://localhost:8761 | 8761 |
| API Gateway | http://localhost:8086 | 8086 |
| Authentication Service | http://localhost:8080 | 8080 |
| Account Service | http://localhost:8081 | 8081 |
| Currency Exchange Service | http://localhost:8083 | 8083 |
| User Service | http://localhost:8084 | 8084 |
| Notification Service | http://localhost:8085 | 8085 |
| Prometheus | http://localhost:5000 | 5000 |
| Grafana | http://localhost:3000 | 3000 |
| RabbitMQ Management | http://localhost:15672 | 15672 |
| Zipkin | http://localhost:9411 | 9411 |

### 📝 Default Kullanıcı Bilgileri
- **Grafana:** admin / password
- **RabbitMQ:** guest / guest

### 🛠 Yönetim Komutları

```bash
# Container'ları durdurmak
docker-compose down

# Log'ları görüntülemek
docker-compose logs -f [service-name]

# Belirli bir servisi yeniden başlatmak
docker-compose restart [service-name]

# Container'ları silmek (volume'lar dahil)
docker-compose down -v
```

### 🗄 Veritabanı
Sistem remote MSSQL Server veritabanı kullanmaktadır. Veritabanı bağlantı bilgileri `.env` dosyasında tanımlanmıştır.

### ⚙️ Environment Variables
Uygulama konfigürasyonu için `environment.env` dosyasını `.env` olarak kopyalayın ve aşağıdaki değerleri düzenleyin:

```bash
# Veritabanı bağlantı bilgileri
DB_HOST=your-database-host
DB_PORT=your-database-port
DB_USERNAME=your-username
DB_PASSWORD=your-password
DB_NAME=your-database-name

# JWT gizli anahtarı (güvenlik için değiştirin)
JWT_SECRET=your-secret-key

# Email ayarları (bildirimler için)
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password

# Grafana admin bilgileri
GRAFANA_ADMIN_USER=admin
GRAFANA_ADMIN_PASSWORD=password
```

**Not:** `.env` dosyası otomatik olarak `.gitignore`'a eklenmiştir ve commit edilmeyecektir.

### 🌐 CORS Konfigürasyonu
Frontend uygulamasından backend'e erişim için CORS (Cross-Origin Resource Sharing) konfigürasyonu yapılmıştır:

- **API Gateway:** Spring Cloud Gateway CorsWebFilter ile tüm origin'lere izin verir
- **Authentication Service:** Spring Security CORS konfigürasyonu ile frontend erişimi sağlanır
- **Desteklenen HTTP Metotları:** GET, POST, PUT, DELETE, OPTIONS, PATCH
- **Frontend Origin'leri:** `environment.env` dosyasında `CORS_ALLOWED_ORIGINS` ile yapılandırılabilir

**CORS Hatası Alıyorsanız:**
1. Container'ların restart edildiğinden emin olun
2. Browser cache'ini temizleyin
3. Frontend uygulamanızın doğru portta (3001) çalıştığını kontrol edin

### 📊 Monitoring
- **Prometheus:** Metrics toplama
- **Grafana:** Dashboard ve görselleştirme
- **Zipkin:** Distributed tracing

### 🔧 Troubleshooting

Eğer herhangi bir sorun yaşarsanız:

1. Container loglarını kontrol edin:
   ```bash
   docker-compose logs -f [service-name]
   ```

2. Container'ı yeniden başlatın:
   ```bash
   docker-compose restart [service-name]
   ```

3. Tüm sistemi temiz başlatın:
   ```bash
   docker-compose down
   docker-compose up --build -d
   ``` 