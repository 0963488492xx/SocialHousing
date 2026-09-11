# SocialHousing Project

## 專案概述

這是一個基於 Hibernate + SQL Server 的社會住宅資料管理系統，主要用於讀取、匯入、查詢與匯出社宅資料。

## 資料夾分類

### 1. 核心模型層

- `src/main/java/Entity`  
  實體類別，包含 `Housing` 等資料模型，對應資料庫的社宅資料。

### 2. 持久化層

- `src/main/java/Dao`  
  資料存取邏輯，負責資料的新增與寫入操作。
- `src/main/java/Util`  
  共用工具類，包含 Hibernate 的 `SessionFactory` 設定與管理。

### 3. 業務邏輯層

- `src/main/java/Main`  
  主要的應用邏輯、服務層、GUI 與入口程式，例如 `HousingService`、`HousingCRUD`、`HousingGUI`。

### 4. 練習 / 範例程式層

- `src/main/java/lab01`  
  CSV 讀取、資料轉換與測試型範例。
- `src/main/java/lab02`  
  進一步的練習或展示案例。

### 5. 測試層

- `src/test/java`  
  JUnit 測試與連線測試，包含 `SocialHousingTest`、`TestConnection`、CSV 匯出測試。

## 目前狀態

- 專案已改為 Java 25 LTS
- Maven 建置已通過測試
- 目前仍有 `lab01` / `lab02` 等示範程式與正式業務程式混在同一個專案內，未來可再整理成 `domain`、`repository`、`service`、`ui` 等更明確的模組。

## 驗證方式

```powershell
mvn test
```

## 建議的後續整理方向

1. 將 `Entity`、`Dao`、`Util`、`Main` 做更嚴謹的 package 分層。
2. 將 demo / lab 類別移出正式應用模組。
3. 把 GUI 與資料處理邏輯分離，讓維護更容易。
4. 保留 `README.md` 作為專案入口說明文件。

## 目前驗證結果

已使用 Java 25 執行 Maven 測試，成果為成功執行且未發現失敗測試。
