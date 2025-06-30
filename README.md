# tpi_leo_demo - Spring Boot RESTful API 範例專案

這個專案是一個基於 Spring Boot 的 RESTful API 範例，旨在展示如何串接外部 API、進行資料轉換、以及實現幣別資料的 CRUD 操作。專案遵循 RESTful 設計原則，並使用 Maven 進行建置。

## 功能特色

- **幣別資料管理**：提供幣別的增、刪、改、查 (CRUD) RESTful API。
- **外部 API 整合**：呼叫 Coindesk API 獲取比特幣即時價格數據。
- **資料轉換**：將 Coindesk API 返回的原始數據轉換為符合特定格式的新 API 回應。
- **H2 內嵌資料庫**：使用 H2 資料庫進行資料儲存，方便開發和測試。
- **Lombok 整合**：使用 Lombok 減少樣板程式碼。
- **單元測試**：包含對 Controller 和 Service 層的單元測試，確保程式碼品質。

## 使用技術

- Java 8
- Spring Boot 2.7.18
- Maven
- Spring Data JPA
- H2 Database
- Lombok
- JUnit 5 & Mockito

## 環境要求

- JDK 8
- Maven (建議使用 IntelliJ IDEA 等 IDE 內建的 Maven)

## 專案設定與運行

1.  **複製專案**：
    ```bash
    git clone <您的專案Git網址>
    cd tpi_leo_demo
    ```

2.  **匯入專案**：
    *   開啟 IntelliJ IDEA (或其他您慣用的 IDE)。
    *   選擇 `File` -> `Open` 或 `Import Project`。
    *   導航到 `tpi_leo_demo` 目錄，並選擇 `pom.xml` 檔案，然後點擊 `Open` 或 `OK`。
    *   IDE 會自動識別為 Maven 專案並下載所需的依賴。

3.  **運行應用程式**：
    *   在 IntelliJ IDEA 中，找到 `src/main/java/com/cathaybk/tpi_leo_demo/TpiLeoDemoApplication.java` 檔案。
    *   右鍵點擊該檔案，選擇 `Run 'TpiLeoDemoApplication.main()'`。
    *   或者，您也可以在專案根目錄下使用 Maven 命令運行：
        ```bash
        mvn spring-boot:run
        ```
    *   應用程式將在 `http://localhost:8080` 啟動。

## API 端點

以下是專案提供的 RESTful API 端點：

### 幣別管理 (Currency Management)

- **取得所有幣別**
    `GET /currencies`
    - 回應範例:
        ```json
        [
          {
            "code": "USD",
            "name": "美金",
            "rate": "1.0"
          },
          {
            "code": "GBP",
            "name": "英鎊",
            "rate": "0.8"
          }
        ]
        ```

- **取得特定幣別**
    `GET /currencies/{code}`
    - 範例: `GET /currencies/USD`
    - 回應範例:
        ```json
        {
          "code": "USD",
          "name": "美金",
          "rate": "1.0"
        }
        ```
    - 若找不到幣別，返回 `404 Not Found`。

- **新增幣別**
    `POST /currencies`
    - 請求 Body 範例:
        ```json
        {
          "code": "JPY",
          "name": "日圓",
          "rate": "100.0"
        }
        ```
    - 回應範例 (成功創建):
        ```json
        {
          "code": "JPY",
          "name": "日圓",
          "rate": "100.0"
        }
        ```

- **更新幣別**
    `PUT /currencies/{code}`
    - 範例: `PUT /currencies/JPY`
    - 請求 Body 範例:
        ```json
        {
          "name": "日幣",
          "rate": "105.0"
        }
        ```
    - 回應範例 (成功更新):
        ```json
        {
          "code": "JPY",
          "name": "日幣",
          "rate": "105.0"
        }
        ```
    - 若找不到幣別，返回 `404 Not Found`。

- **刪除幣別**
    `DELETE /currencies/{code}`
    - 範例: `DELETE /currencies/USD`
    - 成功刪除返回 `200 OK` (無 Body)。

### Coindesk 原始資料 (Raw Coindesk Data)

- **取得 Coindesk 原始資料**
    `GET /coindesk`
    - 回應為 Coindesk API 的原始 JSON 格式。

### 轉換後的比特幣價格 (Converted Bitcoin Prices)

- **取得轉換後的比特幣價格資訊**
    `GET /bitcoin-prices`
    - 回應範例:
        ```json
        {
          "updatedTime": "2025/06/30 20:00:00",
          "bpi": {
            "EUR": {
              "code": "EUR",
              "rate": "52,243.287",
              "name": "歐元"
            },
            "GBP": {
              "code": "GBP",
              "rate": "43,984.02",
              "name": "英鎊"
            },
            "USD": {
              "code": "USD",
              "rate": "57,756.298",
              "name": "美金"
            }
          }
        }
        ```

## 運行測試

您可以在專案根目錄下使用 Maven 命令運行所有單元測試：

```bash
mvn test
```

## H2 Console

當應用程式運行時，您可以通過瀏覽器訪問 H2 資料庫的控制台：

`http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User Name**: `sa`
- **Password**: (留空)

點擊 `Connect` 即可進入控制台，查看 `currency` 表格的資料。

## Currency Table DDL

```sql
CREATE TABLE currency (
    code VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    rate DECIMAL(10, 4)
);
```# cubc_demo
