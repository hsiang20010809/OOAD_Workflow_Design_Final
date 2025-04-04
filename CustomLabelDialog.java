import javax.swing.*;
import java.awt.*;

public class CustomLabelDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> shapeComboBox;
    private JComboBox<String> colorComboBox;
    private JTextField fontSizeField;
    private Shape targetShape;

    public CustomLabelDialog(JFrame parent, Shape targetShape) {
        super(parent, "Custom label Style", true);
        this.targetShape = targetShape;
        
        // 設置對話框佈局
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 背景面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(new Color(200, 200, 200)); // 灰色背景
        
        // 標題
        JLabel titleLabel = new JLabel("Custom label Style", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);
        
        // 標籤名稱
        JLabel nameLabel = new JLabel("Name", JLabel.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(nameLabel, gbc);
        
        nameField = new JTextField(targetShape.getLabelText(), 10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(nameField, gbc);
        
        // 標籤形狀
        JLabel shapeLabel = new JLabel("Shape", JLabel.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(shapeLabel, gbc);
        
        shapeComboBox = new JComboBox<>(new String[]{"Rect", "Oval"});
        shapeComboBox.setSelectedIndex(targetShape.isRectLabel() ? 0 : 1);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(shapeComboBox, gbc);
        
        // 標籤顏色
        JLabel colorLabel = new JLabel("Color", JLabel.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(colorLabel, gbc);
        
        String[] colorOptions = {"yellow", "red", "green", "blue", "white"};
        colorComboBox = new JComboBox<>(colorOptions);
        
        // 設置預設選中的顏色
        Color currentColor = targetShape.getLabelColor();
        int selectedIndex = 0; // 預設黃色
        if (currentColor.equals(Color.RED)) selectedIndex = 1;
        else if (currentColor.equals(Color.GREEN)) selectedIndex = 2;
        else if (currentColor.equals(Color.BLUE)) selectedIndex = 3;
        else if (currentColor.equals(Color.WHITE)) selectedIndex = 4;
        colorComboBox.setSelectedIndex(selectedIndex);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(colorComboBox, gbc);
        
        // 字體大小
        JLabel fontSizeLabel = new JLabel("FontSize", JLabel.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(fontSizeLabel, gbc);
        
        fontSizeField = new JTextField(String.valueOf(targetShape.getFontSize()), 10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(fontSizeField, gbc);
        
        // 按鈕面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton okButton = new JButton("OK");
        
        // 設置取消按鈕事件
        cancelButton.addActionListener(e -> dispose());
        
        // 設置確認按鈕事件
        okButton.addActionListener(e -> {
            try {
                // 設置標籤名稱
                targetShape.setLabelText(nameField.getText());
                
                // 設置標籤形狀
                targetShape.setRectLabel(shapeComboBox.getSelectedIndex() == 0);
                
                // 設置標籤顏色
                String selectedColor = (String) colorComboBox.getSelectedItem();
                Color color = Color.YELLOW;
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
                targetShape.setLabelColor(color);
                
                // 設置字體大小
                int fontSize = Integer.parseInt(fontSizeField.getText());
                if (fontSize > 0) {
                    targetShape.setFontSize(fontSize);
                }
                
                // 關閉對話框
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "字體大小必須是正整數", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        contentPanel.add(buttonPanel, gbc);
        
        // 添加內容面板到對話框
        add(contentPanel);
        
        // 設置對話框大小和位置
        setSize(300, 250);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
}
