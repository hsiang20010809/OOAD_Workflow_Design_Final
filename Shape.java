import java.awt.*;//java.awt提供了基本的視窗工具和圖形繪製功能
import java.util.List;//List介面，這是一個介面（interface），定義了有序集合的基本操作方法
//主要方法包括：add(): 添加元素，remove(): 移除元素，get(): 取得元素，size(): 取得集合大小，等等...
import java.util.ArrayList;//ArrayList類別這是List介面的一個具體實作類別，使用陣列作為底層資料結構

// 定義抽象形狀類別，作為所有圖形的基礎類別
    // 特性	         abstract class（抽象類別）	                          public class（一般類別）
    // 建立物件	     不能直接使用 new 建立物件	                           可以直接使用 new 建立物件
    // 方法宣告	     可以包含抽象方法（無實作）和具體方法（有實作）	         只能包含具體方法（必須有實作）
    // 繼承要求	     子類別必須實作所有抽象方法	                            子類別可以直接使用父類別的方法
    // 主要用途	     定義共同的架構和規範	                               實作具體的功能
    // 範例使用	     abstract class Shape { abstract void draw(); }	      public class Rectangle { void draw() { /*實作*/ } }
    // 存取修飾子	 可以是 abstract 或 public	                           通常是 public
    // 類別繼承	     可以繼承其他類別	                                   可以繼承其他類別
    // 介面實作	     可以實作多個介面	                                   可以實作多個介面
    // 完整性	     可以是不完整的（有抽象方法）	                        必須是完整的（所有方法都要有實作）
abstract class Shape {
    // protected是Java的存取修飾子（access modifier）之一
    // 存取修飾子的四個層級（從最嚴格到最寬鬆）：
    // private：最嚴格，只有類別內部可以存取
    // protected：中等，允許：同一個類別，同一個套件，子類別（如 Rect, Oval）能存取這些變數
    // default（無修飾子）：同一個套件
    // public：最寬鬆，所有類別都可存取
    protected int x, y; // 形狀的座標位置
    protected int depth; // 形狀的深度屬性，用於決定繪製順序

    // 建構子：初始化形狀的位置和深度
    public Shape(int x, int y, int depth) {
        this.x = x;
        this.y = y;
        this.depth = depth;
    }

    // 取得形狀的深度值
    public int getDepth() {
        return depth;
    }

    // 設置形狀的深度值
    public void setDepth(int depth) {
        this.depth = depth;
    }

    // 抽象方法：繪製形狀
    // 參數：g - 繪圖環境，isSelected - 是否被選中
    public abstract void draw(Graphics g, boolean isSelected);

    // 抽象方法：檢查滑鼠點擊是否在形狀內
    // 參數：mouseX, mouseY - 滑鼠座標
    public abstract boolean contains(int mouseX, int mouseY);

    // 抽象方法：取得形狀的連接埠點位置列表
    public abstract List<Point> getConnectionPorts();

    // 移動形狀的具體方法
    // 參數：dx, dy - X和Y方向的位移量
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;

        // 更新所有相關的連線
        for (Link link : links) {
            link.updatePorts();
        }
    }

    // 取得圖形的中心點
    public Point getCenter() {
        int width = getWidth();
        int height = getHeight();
        return new Point(x + width/2, y + height/2);
    }


    // 抽象方法：取得形狀的邊界矩形
    public abstract Rectangle getBounds(); //getBounds()返回Rectangle是因為矩形是描述物件邊界最簡單且通用的方式，
    //無論物件實際形狀為何（圓形、多邊形等），用矩形的左上角座標(x,y)加上寬度(width)和高度(height)就能完整表達該物件在螢幕上所佔的空間範圍。

    // 形狀是否被選中的標記
    private boolean isSelected = false;

    // 儲存與此形狀相連的所有連接線
    private List<Link> links = new ArrayList<>();

    // 新增連線到圖形的連線列表
    public void addLink(Link link) {
        if (!links.contains(link)) {
            links.add(link);
        }
    }

    // 從圖形的連線列表中移除連線
    public void removeLink(Link link) {
        links.remove(link);
    }

    // 取得所有連接線的列表
    public List<Link> getLinks() {
        return links;
    }

    // 檢查形狀是否被選中
    public boolean isSelected() {
        return isSelected;
    }

    // 設置形狀的選中狀態
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    // 取得最接近滑鼠位置的連接埠（Port）
    public Point getClosestPort(int mouseX, int mouseY) {
        // 取得所有可用的連接埠清單
        List<Point> ports = getConnectionPorts();
        // 初始化最近的連接埠為 null
        Point closestPort = null;
        // 初始化最小距離為最大可能值
        double minDistance = Double.MAX_VALUE;
    
        // 遍歷所有連接埠，尋找最近的一個
        for (Point port : ports) {
            // 計算當前連接埠到滑鼠位置的距離
            double distance = port.distance(mouseX, mouseY);
            // 如果找到更近的連接埠，更新最小距離和最近連接埠
            if (distance < minDistance) {
                minDistance = distance;
                closestPort = port;
            }
        }   
        // 返回找到的最近連接埠
        return closestPort;
    }

    // ===== 標籤相關屬性宣告 =====
    protected String labelText = ""; // 標籤文字內容
    protected Color labelColor = Color.YELLOW; // 標籤背景顏色，預設為黃色
    protected int fontSize = 12; // 字型大小，預設為 12
    protected boolean isRectLabel = true; // 標籤形狀，true 代表矩形，false 代表橢圓形

    // ===== 標籤屬性的 getter 和 setter 方法 =====
    public String getLabelText() { return labelText; } // 取得標籤文字
    public void setLabelText(String labelText) { this.labelText = labelText; }  // 設置標籤文字
    public Color getLabelColor() { return labelColor; }  // 取得標籤顏色
    public void setLabelColor(Color labelColor) { this.labelColor = labelColor; } // 設置標籤顏色
    public int getFontSize() { return fontSize; } // 取得標籤字體大小
    public void setFontSize(int fontSize) { this.fontSize = fontSize; } // 設置字型大小
    public boolean isRectLabel() { return isRectLabel; } // 檢查是否為矩形標籤
    public void setRectLabel(boolean isRectLabel) { this.isRectLabel = isRectLabel; } // 設置標籤形狀（矩形/橢圓形）

    // ===== 抽象方法宣告 =====
    public abstract int getWidth(); // 取得形狀寬度的抽象方法，需要由子類別實作
    public abstract int getHeight(); // 取得形狀高度的抽象方法，需要由子類別實作

    // 繪製標籤的方法
    protected void drawLabel(Graphics g) {
        // 檢查標籤文字是否為空，如果是則直接返回不進行繪製
        if (labelText == null || labelText.isEmpty()) {
            return;  // 如果沒有標籤文字，不繪製
        }

        // 建立 Graphics2D 物件以獲得更多進階繪圖功能
        Graphics2D g2d = (Graphics2D) g.create();
        // 建立字體度量衡物件，用於計算文字尺寸，使用 Arial 字型，一般樣式，指定的字型大小
        FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.PLAIN, fontSize));

        // 計算文字的實際寬度和高度
        int textWidth = metrics.stringWidth(labelText);// 取得文字寬度
        int textHeight = metrics.getHeight();// 取得文字高度

        // 計算標籤的整體尺寸（加入邊距）
        int labelWidth = textWidth + 20;  // 標籤寬度 = 文字寬度 + 20像素邊距
        int labelHeight = textHeight + 10;  // 標籤高度 = 文字高度 + 10像素邊距
        
        // 計算形狀的中心點座標
        int centerX = x + getWidth() / 2;  // 形狀中心的 X 座標
        int centerY = y + getHeight() / 2;  // 形狀中心的 Y 座標
        
        // 計算標籤的左上角座標（使標籤置中於形狀）
        int labelX = centerX - labelWidth / 2; // 標籤的 X 座標
        int labelY = centerY - labelHeight / 2; // 標籤的 Y 座標
        
        // 設置標籤背景顏色
        g2d.setColor(labelColor);
        
        // 根據 isRectLabel 的值決定繪製矩形或橢圓形標籤
        if (isRectLabel) {
            // 繪製矩形標籤背景
            g2d.fillRect(labelX, labelY, labelWidth, labelHeight);
        } else {
            // 繪製橢圓形標籤背景
            g2d.fillOval(labelX, labelY, labelWidth, labelHeight);
        }
        
        // 設定文字的顏色（黑色）和字體（Arial 粗體）
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        
        // 計算文字位置，使其在標籤中置中
        int textX = labelX + (labelWidth - textWidth) / 2; // 文字的 X 座標
        int textY = labelY + (labelHeight - textHeight) / 2 + metrics.getAscent(); // 文字的 Y 座標
        // labelY + (labelHeight - textHeight) / 2：計算文字區域的垂直中心位置，+ metrics.getAscent()：加上上升高度，使文字正確對齊基準線
        
        // 繪製文字
        g2d.drawString(labelText, textX, textY);
        
        // 釋放圖形資源
        g2d.dispose();
    }  
}
