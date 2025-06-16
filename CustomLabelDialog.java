// 引入必要的 Swing 和 AWT 套件
import javax.swing.*;
import java.awt.*;

/**
 * 自定義標籤樣式對話框類別
 * 用於編輯圖形物件的標籤屬性
 */
public class CustomLabelDialog extends JDialog { // public class - 宣告一個公開的類別 
    // CustomLabelDialog - 這個類別的名稱 
    // extends JDialog - 表示這個類別繼承自 JDialog（Java 視窗對話框的基礎類別）
    // 簡單來說，這行程式碼創建了一個自定義的對話框視窗類別，它具備了 Java 標準對話框（JDialog）的所有基本功能，並可以加入自己想要的額外功能。

    // 宣告對話框中使用的元件
    private JTextField nameField; // 標籤名稱輸入欄位
    private JComboBox<String> shapeComboBox; // 標籤形狀選擇下拉選單
    private JComboBox<String> colorComboBox; // 標籤顏色選擇下拉選單
    private JTextField fontSizeField; // 字體大小輸入欄位
    /**
     * 建構子
     * @param parent 父視窗
     * @param targetShape 要編輯標籤的目標圖形
     */
    public CustomLabelDialog(JFrame parent, Shape targetShape) {
        // 呼叫父類別建構子，設置對話框標題和模態
        super(parent, "Custom label Style", true);
        // 儲存目標圖形的參考

        // 佈局管理：
        // 使用了GridBagLayout佈局管理器，這是一種靈活的網格式佈局，可以精確控制元件的位置和大小
        // 設定了GridBagConstraints參數來控制元件的排列方式
        // 設定了元件之間的間距為5像素(通過new Insets(5, 5, 5, 5))
        // 設定元件水平填充（GridBagConstraints.HORIZONTAL）

        // 設置對話框使用 GridBagLayout 佈局管理器
        setLayout(new GridBagLayout());
        // 創建並配置 GridBagConstraints 用於控制元件佈局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 設置元件間距
        gbc.fill = GridBagConstraints.HORIZONTAL; // 設置元件水平填充
        
        // 內容面板設計：

        // 創建了一個JPanel作為內容面板
        // 為內容面板也設定了GridBagLayout佈局
        // 設定了10像素的內邊距(通過BorderFactory.createEmptyBorder(10, 10, 10, 10)）
        // 設定了面板的背景顏色為灰色(new Color(200, 200, 200))

        // 內容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout()); // 使用 GridBagLayout 佈局
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 設置面板邊框
        contentPanel.setBackground(new Color(200, 200, 200)); // 灰色背景
        
        // 創建並設置對話框標題
        JLabel titleLabel = new JLabel("Custom label Style", JLabel.CENTER); // 建立置中的標題標籤
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14)); // 設置標題字體為 Arial、粗體、14 點大小
        gbc.gridx = 0; // 設置標題在網格的 x 座標為 0
        gbc.gridy = 0; // 設置標題在網格的 y 座標為 0
        gbc.gridwidth = 2; // 設置標題橫跨 2 個網格寬度
        contentPanel.add(titleLabel, gbc); // 將標題加入內容面板
        
        // 創建標籤名稱輸入區域
        JLabel nameLabel = new JLabel("Name", JLabel.LEFT); // 建立靠左對齊的「Name」標籤
        gbc.gridx = 0; // 設置標籤在網格的 x 座標為 0
        gbc.gridy = 1; // 設置標籤在網格的 y 座標為 1
        gbc.gridwidth = 1; // 設置標籤寬度為 1 個網格
        contentPanel.add(nameLabel, gbc); // 將標籤加入內容面板
        
        // 創建名稱輸入文字框
        nameField = new JTextField(targetShape.getLabelText()); // 建立文字輸入框，顯示目前圖形的標籤文字
        gbc.gridx = 1; // 設置文字框在網格的 x 座標為 1
        gbc.gridy = 1; // 設置文字框在網格的 y 座標為 1
        contentPanel.add(nameField, gbc); // 將文字框加入內容面板
        
        // 創建形狀選擇區域
        JLabel shapeLabel = new JLabel("Shape", JLabel.LEFT); // 建立靠左對齊的「Shape」標籤
        gbc.gridx = 0; // 設置標籤在網格的 x 座標為 0
        gbc.gridy = 2; // 設置標籤在網格的 y 座標為 2
        contentPanel.add(shapeLabel, gbc); // 將標籤加入內容面板
        
        // 創建形狀選擇下拉選單
        String[] ShapeOptions = {"Rect", "Oval"};
        shapeComboBox = new JComboBox<>(ShapeOptions); // 建立包含矩形和橢圓選項的下拉選單
        shapeComboBox.setSelectedIndex(targetShape.isRectLabel() ? 0 : 1); // 根據目前圖形的形狀設置預設選項
        gbc.gridx = 1; // 設置下拉選單在網格的 x 座標為 1
        gbc.gridy = 2; // 設置下拉選單在網格的 y 座標為 2
        contentPanel.add(shapeComboBox, gbc); // 將下拉選單加入內容面板
        
        // 創建顏色選擇區域
        JLabel colorLabel = new JLabel("Color", JLabel.LEFT); // 建立靠左對齊的「Color」標籤
        gbc.gridx = 0; // 設置標籤在網格的 x 座標為 0
        gbc.gridy = 3; // 設置標籤在網格的 y 座標為 3
        contentPanel.add(colorLabel, gbc); // 將標籤加入內容面板
        
        // 建立顏色選項陣列
        String[] colorOptions = {"yellow", "red", "green", "blue", "white"}; // 定義可選擇的顏色選項
        colorComboBox = new JComboBox<>(colorOptions); // 建立顏色下拉選單
        
        // 根據目前圖形的標籤顏色設置預設選項
        Color currentColor = targetShape.getLabelColor(); // 取得目前圖形的標籤顏色
        int selectedIndex = 0; // 預設黃色（索引 0）
        // 根據目前顏色設置對應的索引
        if (currentColor.equals(Color.RED)) selectedIndex = 1;
        else if (currentColor.equals(Color.GREEN)) selectedIndex = 2;
        else if (currentColor.equals(Color.BLUE)) selectedIndex = 3;
        else if (currentColor.equals(Color.WHITE)) selectedIndex = 4;
        colorComboBox.setSelectedIndex(selectedIndex); // 設置預選的顏色
        
        // 設置顏色下拉選單的位置
        gbc.gridx = 1; // 設置下拉選單在網格的 x 座標為 1
        gbc.gridy = 3; // 設置下拉選單在網格的 y 座標為 3
        contentPanel.add(colorComboBox, gbc); // 將下拉選單加入內容面板
        
        // 創建字體大小設定區域
        JLabel fontSizeLabel = new JLabel("FontSize", JLabel.LEFT); // 建立靠左對齊的「FontSize」標籤
        gbc.gridx = 0; // 設置標籤在網格的 x 座標為 0
        gbc.gridy = 4; // 設置標籤在網格的 y 座標為 4
        contentPanel.add(fontSizeLabel, gbc); // 將標籤加入內容面板
        
        // 創建字體大小輸入框
        fontSizeField = new JTextField(String.valueOf(targetShape.getFontSize())); // 建立文字框，顯示目前的字體大小
        gbc.gridx = 1; // 設置文字框在網格的 x 座標為 1
        gbc.gridy = 4; // 設置文字框在網格的 y 座標為 4
        contentPanel.add(fontSizeField, gbc); // 將文字框加入內容面板
        
        // 創建按鈕面板
        JPanel buttonPanel = new JPanel(); // 建立靠右對齊的按鈕面板
        JButton cancelButton = new JButton("Cancel"); // 建立取消按鈕
        JButton okButton = new JButton("OK"); // 建立確定按鈕
        
        // 設置取消按鈕的點擊事件處理
        cancelButton.addActionListener(_ -> dispose()); // 點擊取消按鈕時關閉對話框
        
        // 設置確認按鈕的點擊事件處理
        okButton.addActionListener(_ -> {
            try {
                // 更新標籤文字
                targetShape.setLabelText(nameField.getText()); // 將文字框的內容設置為新的標籤文字
                
                // 更新標籤形狀
                //當下拉選單選擇 "Rect"（索引 0）時，getSelectedIndex() == 0 會回傳 true
                //當下拉選單選擇 "Oval"（索引 1）時，getSelectedIndex() == 0 會回傳 false 
                targetShape.setRectLabel(shapeComboBox.getSelectedIndex() == 0); // 索引 0 為矩形，1 為橢圓形
                
                // 更新標籤顏色
                String selectedColor = (String) colorComboBox.getSelectedItem(); // 獲取選擇的顏色名稱
                Color color = Color.YELLOW; // 預設為黃色
                // 根據選擇的顏色名稱設置對應的 Color 物件
                switch (selectedColor) {
                    case "red":
                        color = Color.RED;
                        break;
                    case "green":
                        color = Color.GREEN;
                        break;
                    case "blue":
                        color = Color.BLUE;
                        break;
                    case "white":
                        color = Color.WHITE;
                        break;
                }
                targetShape.setLabelColor(color); // 更新標籤顏色
                
                // 設置字體大小
                int fontSize = Integer.parseInt(fontSizeField.getText()); // 將輸入的字串轉換為整數
                if (fontSize > 0) { // 確保字體大小為正數
                    targetShape.setFontSize(fontSize);
                }
                
                // 更新完成後關閉對話框
                dispose();
            } catch (NumberFormatException ex) {
                // 當字體大小輸入不是有效數字時顯示錯誤訊息
                JOptionPane.showMessageDialog(this, "字體大小必須是正整數", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 將按鈕添加到按鈕面板
        buttonPanel.add(cancelButton); // 添加取消按鈕
        buttonPanel.add(okButton); // 添加確認按鈕
        
        // 設置按鈕面板在內容面板中的位置
        gbc.gridx = 0; // 設置在網格的 x 座標為 0
        gbc.gridy = 5; // 設置在網格的 y 座標為 5
        gbc.gridwidth = 2; // 設置跨越 2 個網格寬度
        contentPanel.add(buttonPanel, gbc); // 將按鈕面板加入主面板
        
        // 完成對話框設置
        add(contentPanel); // 將主面板加入對話框
        
        // 設置對話框大小和位置
        setSize(300, 250); // 設置對話框大小
        setLocationRelativeTo(parent); // 設置對話框相對於父視窗置中
        setResizable(false); // 設置對話框不可調整大小
    }
}
