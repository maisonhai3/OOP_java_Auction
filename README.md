# Going Going Gone - Auction System 🔨

Đây là bài tập lớn môn OOP của nhóm 7. Dự án này xây dựng một Hệ thống quản lý đấu giá, áp dụng các nguyên tắc Clean Architecture và một số Design Patterns thông dụng.

Người thực hiện:
- Lê Thị Phương Thảo - K24DTCN537: phân tích nghiệp vụ, thiết kế quy trình, thiết kế database.
- Mai Sơn Hải - K24DTCN509: triển khai thiết kế.

## 📋 Giới thiệu

**Going Going Gone** là câu khẩu quyết thông dụng trong ngành đấu giá. Khi một món đồ lên sàn đấu giá, 
và giá đấu đã dần đi tới hồi kết, người chủ phiên sẽ hô to, chậm rãi, ngân dài "going", rồi tiếp "going",
rồi hô to, đanh giọng "gone!", đánh dấu việc món đồ được chốt giá.

Bọn em cũng không ngẫu nhiên ứng tác ra đề bài này. 
**Going Going Gone** là một trong các Architecture kata, 
tức là đề bài để cho các architect luyện tập cách thiêt kế hệ thống.
Bọn em nghĩ, sau khi đã thử thiết kế vụ đấu giá ở mức high-level rồi,
thì tự mình code hệ thống này, cũng hay, nên làm luôn.


Dự án này mô phỏng quy trình đấu giá thực tế với các tính năng:
- Quản lý phiên đấu giá (Auction Sessions)
- Quản lý sản phẩm đấu giá (Lots)
- Đặt giá thầu theo thời gian thực (Real-time bidding)
- Thông báo trạng thái đấu giá cho người tham gia

## 🏗️ Kiến trúc

Dự án tuân theo **Clean Architecture**. 
Việc này có 2 mục đích:
- Tiết kiệm thời gian thiết kế.
- Khi người mới onboard dự án, nhìn cấu trúc thư mục, sẽ hiểu ngay là Clean Architecture, không cần giải thích thêm.

Dù vậy, chúng em không hoàn toàn tuân thủ Clean Architecture 100%,
mà lược bỏ:
- Việc dùng interfaces để giao tiếp giữa các layers, bởi việc này không cần thiết với dự án nhỏ có vài chục file như dự án này.

Cấu trúc source code:

```
src/auction/
├── domain/           # Entities: User, Lot, Bid, AuctionSession...
├── usecases/         # Business Logic: Services
├── infrastructure/   # Data Persistence: SQLite Repositories
└── presentation/     # UI: Swing Applications
```

## 🎨 Design Patterns

| Pattern | Áp dụng | Mục đích |
|---------|---------|----------|
| **Singleton** | Tất cả Services | Đảm bảo một instance duy nhất |
| **Builder** | `Lot` entity | Tạo đối tượng với nhiều optional fields, nhằm khắc phục việc Java không có optional parameters |
| **Observer** | `AuctionSession` | Real-time notification tới mọi người khi có sự kiện mới xảy ra |

## 🖥️ Ứng dụng

| App | Vai trò | Chức năng |
|-----|---------|-----------|
| **EndUserApp** | Người dùng | Đăng nhập, xem lobby, tham gia đấu giá, đặt giá |
| **StaffApp** | Nhân viên | Tạo/quản lý Lots, tạo phiên đấu giá |
| **AuctioneerApp** | Người điều khiển | Điều khiển phiên đấu giá (start, close) |

## 🚀 Chạy ứng dụng

```bash
# Sử dụng Maven
mvn compile exec:java -Dexec.mainClass="Main"

# Hoặc chạy trực tiếp
java -cp src Main
```

## 🛠️ Công nghệ

- **Java 21+**
- **Swing** - GUI Framework
- **SQLite** - Database
- **Maven** - Build Tool


*Last Updated: November 2025*
