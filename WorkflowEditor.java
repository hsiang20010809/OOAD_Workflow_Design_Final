import javax.swing.*;
import java.awt.*;

public class WorkflowEditor {
    private Canvas canvas;
    private JButton rectButton;
    private JButton ovalButton;
    private JButton selectButton;
    private JButton groupButton;
    private JButton ungroupButton;
    private JButton associationButton; 
    private JButton generalizationButton; 
    private JButton compositionButton; 
    private boolean isGroupMode = false; // 用於記錄 Group 按鈕是否被選中
    private boolean isUnGroupMode = false; // 用於記錄 UnGroup 按鈕是否被選中
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, fileEditMenu;
    private JMenuItem labelMenuItem;


    public WorkflowEditor() {
        // 初始化畫布
        canvas = new Canvas();

        // 初始化菜單欄
        menuBar = new JMenuBar();
        fileEditMenu = new JMenu("File Edit");
        labelMenuItem = new JMenuItem("label");
        
        // 建立視窗
        JFrame frame = new JFrame("Workflow Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setJMenuBar(menuBar);

        // 加入畫布
        frame.add(canvas, BorderLayout.CENTER);
        fileEditMenu.add(labelMenuItem);
        menuBar.add(fileEditMenu);
        frame.setJMenuBar(menuBar);

        // 加入工具列
        JPanel toolbar = new JPanel();
        rectButton = new JButton("Rect");
        ovalButton = new JButton("Oval");
        selectButton = new JButton("Select");
        groupButton = new JButton("Group");
        ungroupButton = new JButton("UnGroup");

        // 新增三個連結按鈕
        associationButton = new JButton("Association");
        generalizationButton = new JButton("Generalization");
        compositionButton = new JButton("Composition");

        toolbar.add(rectButton);
        toolbar.add(ovalButton);
        toolbar.add(selectButton);
        toolbar.add(groupButton);
        toolbar.add(ungroupButton);
        toolbar.add(associationButton);
        toolbar.add(generalizationButton);
        toolbar.add(compositionButton);

        frame.add(toolbar, BorderLayout.NORTH);

        // 設置 label 選單項的點擊事件
        labelMenuItem.addActionListener(e -> {
            Shape selectedShape = canvas.getSelectedShape();
            if (selectedShape != null) {
                // 創建並顯示自定義標籤對話框
                CustomLabelDialog dialog = new CustomLabelDialog(frame, selectedShape);
                dialog.setVisible(true);
                
                // 對話框關閉後重繪畫布
                canvas.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "請先選擇一個物件", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 設定按鈕事件
        rectButton.addActionListener(e -> {
            canvas.setMode(Canvas.Mode.RECT); // 切換到 RECT 模式
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: RECT");
        });

        ovalButton.addActionListener(e -> {
            canvas.setMode(Canvas.Mode.OVAL); // 切換到 OVAL 模式
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: OVAL");
        });

        selectButton.addActionListener(e -> {
            canvas.setMode(Canvas.Mode.SELECT); // 切換到 SELECT 模式
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: SELECT");
        });

        groupButton.addActionListener(e -> {
            canvas.groupSelectedShapes(); // 群組所選取的物件
            canvas.setMode(Canvas.Mode.NONE); // 設置為 NONE 模式
            isGroupMode = true;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Shapes grouped.");
        });

        ungroupButton.addActionListener(e -> {
            canvas.ungroupSelectedComposite(); // 解構
            canvas.setMode(Canvas.Mode.NONE); // 設置為 NONE 模式
            isGroupMode = false;
            isUnGroupMode = true;
            updateButtonColors();
            System.out.println("Composite ungrouped.");
        });

        // 設定連結按鈕事件
        associationButton.addActionListener(e -> {
            canvas.setMode(Canvas.Mode.LINK); // 切換到 LINK 模式
            canvas.setCurrentLinkType(LinkType.ASSOCIATION);
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: Association");
        });

        generalizationButton.addActionListener(e -> {
            canvas.setMode(Canvas.Mode.LINK); // 切換到 LINK 模式
            canvas.setCurrentLinkType(LinkType.GENERALIZATION);
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: Generalization");
        });

        compositionButton.addActionListener(e -> {
            canvas.setMode(Canvas.Mode.LINK); // 切換到 LINK 模式
            canvas.setCurrentLinkType(LinkType.COMPOSITION);
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: Composition");
        });

        // 顯示視窗
        frame.setVisible(true);
    }

    // 更新按鈕顏色，顯示當前模式
    private void updateButtonColors() {
        rectButton.setBackground(canvas.getMode() == Canvas.Mode.RECT ? Color.BLACK : null);
        rectButton.setForeground(canvas.getMode() == Canvas.Mode.RECT ? Color.WHITE : Color.BLACK);

        ovalButton.setBackground(canvas.getMode() == Canvas.Mode.OVAL ? Color.BLACK : null);
        ovalButton.setForeground(canvas.getMode() == Canvas.Mode.OVAL ? Color.WHITE : Color.BLACK);

        selectButton.setBackground(canvas.getMode() == Canvas.Mode.SELECT ? Color.BLACK : null);
        selectButton.setForeground(canvas.getMode() == Canvas.Mode.SELECT ? Color.WHITE : Color.BLACK);

        groupButton.setBackground(isGroupMode ? Color.BLACK : null);
        groupButton.setForeground(isGroupMode ? Color.WHITE : Color.BLACK);

        ungroupButton.setBackground(isUnGroupMode ? Color.BLACK : null);
        ungroupButton.setForeground(isUnGroupMode ? Color.WHITE : Color.BLACK);

        associationButton.setBackground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.ASSOCIATION ? Color.BLACK : null);
        associationButton.setForeground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.ASSOCIATION ? Color.WHITE : Color.BLACK);

        generalizationButton.setBackground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.GENERALIZATION ? Color.BLACK : null);
        generalizationButton.setForeground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.GENERALIZATION ? Color.WHITE : Color.BLACK);

        compositionButton.setBackground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.COMPOSITION ? Color.BLACK : null);
        compositionButton.setForeground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.COMPOSITION ? Color.WHITE : Color.BLACK);
    }

    public static void main(String[] args) {
        new WorkflowEditor();
    }
}
