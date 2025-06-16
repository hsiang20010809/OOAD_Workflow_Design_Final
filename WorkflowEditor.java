//引入Java的圖形使用者介面(GUI)相關套件
//javax.swing提供GUI元件
//java.awt 提供了基本的視窗工具和圖形繪製功能
import javax.swing.*; 
import java.awt.*;

public class WorkflowEditor { //類別宣告和成員變數
    private Canvas canvas; //畫布相關:用於繪製和顯示圖形的主要區域，這是自定義的Canvas類別，用來處理所有的繪圖操作
    //JButton是Java Swing函式庫中的一個重要元件，是一個可以點擊的按鈕元件
    //使用private來宣告這些變數有幾個重要的原因
    //資料隱藏
    //封裝
    //降低程式的耦合度
    //如果將來需要更改實作方式，只需要修改類別內部的程式碼
    private JButton rectButton;//繪製矩形
    private JButton ovalButton;//繪製橢圓
    private JButton selectButton;//選擇物件
    private JButton groupButton;//群組物件
    private JButton ungroupButton;//解除群組
    private JButton associationButton;//建立關聯連結
    private JButton generalizationButton;//建立泛化連結
    private JButton compositionButton;//建立組合連結
    private boolean isGroupMode = false;//用於記錄Group按鈕是否被選中
    private boolean isUnGroupMode = false;//用於記錄UnGroup按鈕是否被選中
    //JMenuBar:選單列，就是視窗上方的那條橫列，用來容納各個選單
    private JMenuBar menuBar;//選單列
    //JMenu:下拉式選單，點擊後會展開的選單
    private JMenu fileEditMenu;//檔案編輯選單
    //JMenuItem:選單中的單一項目
    private JMenuItem labelMenuItem;//用於新增標籤的選單項目

    //建構子宣告，這是WorkflowEditor類別的建構子，當創建此類別的物件時會執行
    public WorkflowEditor() {
        //初始化畫布，建立一個新的畫布物件，用於繪圖
        canvas = new Canvas();

        //初始化菜單欄
        menuBar = new JMenuBar();//建立選單列
        fileEditMenu = new JMenu("File Edit");//建立名為「File Edit」的選單
        labelMenuItem = new JMenuItem("label");//建立名為「label」的選單項目
        
        //建立視窗
        JFrame frame = new JFrame("Workflow Editor");//建立標題為「Workflow Editor」的視窗
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//設定關閉視窗時結束程式
        frame.setSize(800, 600);//設定視窗大小為800x600像素
        frame.setLayout(new BorderLayout());//使用BorderLayout作為版面配置

        //加入畫布
        frame.add(canvas, BorderLayout.CENTER);//將畫布加入視窗中央
        fileEditMenu.add(labelMenuItem);//將選單項目加入選單
        menuBar.add(fileEditMenu);//將選單加入選單列
        frame.setJMenuBar(menuBar);//將menuBar(選單列)安裝到frame(視窗)的頂部位置，顯示選單

        //按鈕工具列
        JPanel toolbar = new JPanel();//建立工具列面板

        //建立基本形狀和操作的按鈕
        rectButton = new JButton("Rect");//建立矩形工具按鈕
        ovalButton = new JButton("Oval");//建立橢圓形工具按鈕
        selectButton = new JButton("Select");//建立選擇工具按鈕
        groupButton = new JButton("Group");//建立群組工具按鈕
        ungroupButton = new JButton("UnGroup");//建立解除群組工具按鈕

        //新增三個連結按鈕
        associationButton = new JButton("Association");//建立關聯關係按鈕
        generalizationButton = new JButton("Generalization");//建立繼承關係按鈕
        compositionButton = new JButton("Composition");//建立組合關係按鈕

        //將所有按鈕依序加入工具列
        toolbar.add(rectButton); 
        toolbar.add(ovalButton); 
        toolbar.add(selectButton); 
        toolbar.add(groupButton); 
        toolbar.add(ungroupButton); 
        toolbar.add(associationButton); 
        toolbar.add(generalizationButton);
        toolbar.add(compositionButton);

        frame.add(toolbar, BorderLayout.NORTH);//將工具列加入視窗的頂部(BorderLayout.NORTH位置）

        //設置label選單項的點擊事件
        labelMenuItem.addActionListener(_ -> {
            //獲取當前被選中的圖形
            Shape selectedShape = canvas.getSelectedShape();
            if (selectedShape != null) {
                //如果有選中的圖形，創建並顯示自定義標籤對話框
                CustomLabelDialog dialog = new CustomLabelDialog(frame, selectedShape);
                dialog.setVisible(true);
                
                //標籤更新後重新繪製畫布，對話框關閉後重繪畫布
                canvas.repaint();
            } else {
                // 如果沒有選中的圖形，顯示提示訊息
                JOptionPane.showMessageDialog(frame, "請先選擇一個物件", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //設定各個工具按鈕的點擊事件監聽器
        //矩形按鈕事件
        rectButton.addActionListener(_ -> {
            canvas.setMode(Canvas.Mode.RECT);//設定畫布模式為繪製矩形
            isGroupMode = false;//關閉群組模式(用於判斷群組/解群組按鈕顏色變化)
            isUnGroupMode = false;//關閉解除群組模式
            updateButtonColors();//更新按鈕顏色狀態
            System.out.println("Mode switched to: RECT");//輸出模式變更訊息
        });

        ovalButton.addActionListener(_ -> {
            canvas.setMode(Canvas.Mode.OVAL);//設定畫布模式為繪製橢圓
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: OVAL");
        });

        selectButton.addActionListener(_ -> {
            canvas.setMode(Canvas.Mode.SELECT);//設定畫布模式為選擇模式
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: SELECT");
        });

        groupButton.addActionListener(_ -> {
            canvas.groupSelectedShapes();//群組所選取的物件
            canvas.setMode(Canvas.Mode.NONE);//設置為NONE模式
            isGroupMode = true;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Shapes grouped.");
        });

        ungroupButton.addActionListener(_ -> {
            canvas.ungroupSelectedComposite();//執行解除群組操作
            canvas.setMode(Canvas.Mode.NONE);//設置為NONE模式
            isGroupMode = false;
            isUnGroupMode = true;
            updateButtonColors();
            System.out.println("Composite ungrouped.");
        });

        // 設定 UML 連線按鈕的事件監聽器
        // 設定連線按鈕事件
        associationButton.addActionListener(_ -> {
            canvas.setMode(Canvas.Mode.LINK);//切換到LINK模式
            canvas.setCurrentLinkType(LinkType.ASSOCIATION);//設定LINK類型為關聯
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: Association");
        });

        generalizationButton.addActionListener(_ -> {
            canvas.setMode(Canvas.Mode.LINK);//切換到LINK模式
            canvas.setCurrentLinkType(LinkType.GENERALIZATION);//設定LINK類型為繼承
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: Generalization");
        });

        compositionButton.addActionListener(_ -> {
            canvas.setMode(Canvas.Mode.LINK);//切換到LINK模式
            canvas.setCurrentLinkType(LinkType.COMPOSITION);//設定LINK類型為組合
            isGroupMode = false;
            isUnGroupMode = false;
            updateButtonColors();
            System.out.println("Mode switched to: Composition");
        });

        // 顯示視窗
        frame.setVisible(true);
    }

    // 更新按鈕顏色以視覺化顯示當前選擇的模式
    private void updateButtonColors() {
        // 更新矩形按鈕顏色
        // 如果當前模式是矩形模式，設置背景為黑色，文字為白色；否則恢復預設顏色
        rectButton.setBackground(canvas.getMode() == Canvas.Mode.RECT ? Color.BLACK : null);
        rectButton.setForeground(canvas.getMode() == Canvas.Mode.RECT ? Color.WHITE : Color.BLACK);
        // 更新橢圓形按鈕顏色
        // 如果當前模式是橢圓形模式，設置背景為黑色，文字為白色；否則恢復預設顏色
        ovalButton.setBackground(canvas.getMode() == Canvas.Mode.OVAL ? Color.BLACK : null);
        ovalButton.setForeground(canvas.getMode() == Canvas.Mode.OVAL ? Color.WHITE : Color.BLACK);
        // 更新選擇按鈕顏色
        // 如果當前模式是選擇模式，設置背景為黑色，文字為白色；否則恢復預設顏色
        selectButton.setBackground(canvas.getMode() == Canvas.Mode.SELECT ? Color.BLACK : null);
        selectButton.setForeground(canvas.getMode() == Canvas.Mode.SELECT ? Color.WHITE : Color.BLACK);
        // 更新群組按鈕顏色
        // 如果處於群組模式，設置背景為黑色，文字為白色；否則恢復預設顏色
        groupButton.setBackground(isGroupMode ? Color.BLACK : null);
        groupButton.setForeground(isGroupMode ? Color.WHITE : Color.BLACK);
        // 更新解除群組按鈕顏色
        // 如果處於解除群組模式，設置背景為黑色，文字為白色；否則恢復預設顏色
        ungroupButton.setBackground(isUnGroupMode ? Color.BLACK : null);
        ungroupButton.setForeground(isUnGroupMode ? Color.WHITE : Color.BLACK);
        // 更新關聯關係按鈕顏色
        // 如果當前模式是連結模式且連結類型為關聯，設置背景為黑色，文字為白色；否則恢復預設顏色
        associationButton.setBackground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.ASSOCIATION ? Color.BLACK : null);
        associationButton.setForeground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.ASSOCIATION ? Color.WHITE : Color.BLACK);
        // 更新繼承關係按鈕顏色
        // 如果當前模式是連結模式且連結類型為繼承，設置背景為黑色，文字為白色；否則恢復預設顏色
        generalizationButton.setBackground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.GENERALIZATION ? Color.BLACK : null);
        generalizationButton.setForeground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.GENERALIZATION ? Color.WHITE : Color.BLACK);
        // 更新組合關係按鈕顏色
        // 如果當前模式是連結模式且連結類型為組合，設置背景為黑色，文字為白色；否則恢復預設顏色
        compositionButton.setBackground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.COMPOSITION ? Color.BLACK : null);
        compositionButton.setForeground(canvas.getMode() == Canvas.Mode.LINK && canvas.getCurrentLinkType() == LinkType.COMPOSITION ? Color.WHITE : Color.BLACK);
    }

//程式的入口點
    public static void main(String[] args) {
        //創建WorkflowEditor的實例，啟動應用程式
        new WorkflowEditor();
    }
}
