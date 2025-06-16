# UML Editor

一個使用 Java Swing 開發的簡單 UML 編輯器，可以繪製和編輯基本的 UML 圖形。

## 功能特點

### 基本圖形

- 矩形（Rectangle）

- 橢圓形（Oval）

### 連接線類型

- 關聯（Association）

- 泛化（Generalization）

- 組合（Composition）

### 編輯功能

- 選擇和移動物件

- 群組（Group）和解群組（Ungroup）物件

- 自訂物件標籤

- 調整物件深度

## 系統需求

- Java Runtime Environment (JRE) 8 或更高版本

- Java Development Kit (JDK) 8 或更高版本

## 專案結構

├── Canvas.java          # 主要繪圖區域

├── Composite.java       # 群組物件實作

├── CustomLabelDialog.java # 自訂標籤對話框

├── Link.java           # 連接線類別

├── Oval.java           # 橢圓形類別

├── Rect.java           # 矩形類別

├── Shape.java          # 圖形基礎類別

└── WorkflowEditor.java # 主程式視窗


## 使用說明

### 基本操作

1. **新增圖形**

   - 點選工具列的矩形或橢圓形按鈕

   - 在畫布上點擊即可創建對應圖形

2. **連接物件**

   - 選擇連接線類型（關聯、泛化、組合）

   - 從起始物件拖曳至目標物件

3. **群組操作**

   - 選擇多個物件

   - 點擊 Group 按鈕進行群組

   - 使用 Ungroup 按鈕解除群組

4. **編輯標籤**

   - 選擇物件

   - 從選單中選擇「編輯標籤」

   - 在對話框中設定標籤文字和樣式

### 快捷鍵

- 選擇工具：`S`

- 矩形工具：`R`

- 橢圓形工具：`O`

- 刪除選中：`Delete`

## 開發說明

### 設計模式

- Composite Pattern：用於實現物件群組功能

- Strategy Pattern：用於處理不同類型的連接線

- Observer Pattern：用於更新畫布顯示

### 類別說明

- `Shape`：所有圖形的抽象基礎類別

- `Canvas`：處理繪圖和滑鼠事件

- `WorkflowEditor`：主要應用程式視窗

- `Link`：處理物件間的連接線

- `Composite`：實現群組功能
