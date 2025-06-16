// 導入必要的 Java AWT 和集合類別
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Rect 類別繼承自 Shape 類別，用於繪製矩形
class Rect extends Shape {
    // 定義矩形的固定寬度和高度
    // private：存取修飾子，表示這個變數只能在這個類別內部使用，其他類別無法直接存取這個變數
    // static：靜態關鍵字，表示這個變數屬於類別本身，而不是個別物件，所有 Rect 物件共享同一個 WIDTH 值，不需要建立物件就能使用
    // final：常數關鍵字，表示這個值一旦被設定就不能改變，相當於常數宣告，通常常數名稱會使用全大寫
    private static final int WIDTH = 100; // 矩形寬度為 100 像素
    private static final int HEIGHT = 50; // 矩形高度為 50 像素

    // 建構子：初始化矩形的位置
    public Rect(int x, int y) { // 呼叫父類別建構子，設定位置和深度值
        super(x, y, 10); // 設定深度為 10
        setSelected(false); // 初始化選取狀態為未選取
        setLabelText("Rect1"); // 設定預設標籤文字
    }

    // 覆寫父類別的繪製方法
    @Override
    public void draw(Graphics g, boolean isSelected) {
        g.setColor(Color.GRAY); // 設定填充顏色為灰色
        g.fillRect(x, y, WIDTH, HEIGHT); // 繪製填充矩形

        if (isSelected) {
            //被選取繪製port
            g.setColor(Color.BLACK);
            g.fillRect(x-5, y-5, 10, 10);//左上port
            g.fillRect(x+45, y-5, 10, 10);//中上port
            g.fillRect(x+95, y-5, 10, 10);//右上port
            g.fillRect(x-5, y+20, 10, 10);//左中port
            g.fillRect(x-5, y+45, 10, 10);//左下port
            g.fillRect(x+45, y+45, 10, 10);//中下port
            g.fillRect(x+95, y+45, 10, 10);//右下port
            g.fillRect(x+95, y+20, 10, 10);//右中port
        }
        // 繪製矩形的標籤文字
        drawLabel(g);
    }

    // 取得矩形寬度的方法
    @Override
    public int getWidth() {
        return WIDTH;
    }

    // 取得矩形高度的方法
    @Override
    public int getHeight() {
        return HEIGHT;
    }

    // 檢查滑鼠座標是否在矩形範圍內
    @Override
    public boolean contains(int mouseX, int mouseY) {
        // 回傳 true 如果滑鼠座標在矩形範圍內：
        // mouseX 必須大於等於矩形左邊界(x) 且 小於等於矩形右邊界(x + WIDTH)
        // mouseY 必須大於等於矩形上邊界(y) 且 小於等於矩形下邊界(y + HEIGHT)
        return mouseX >= x && mouseX <= x + WIDTH && mouseY >= y && mouseY <= y + HEIGHT;
    }

    // 取得矩形的連接點（用於連線）
    @Override
    public List<Point> getConnectionPorts() {
        // 建立一個新的連接點清單
        List<Point> ports = new ArrayList<>();

        // 依序加入八個連接點：
        ports.add(new Point(x, y)); // Top-left
        ports.add(new Point(x + WIDTH / 2, y)); // Top-center
        ports.add(new Point(x + WIDTH, y)); // Top-right
        ports.add(new Point(x, y + HEIGHT / 2)); // Left-center
        ports.add(new Point(x + WIDTH, y + HEIGHT / 2)); // Right-center
        ports.add(new Point(x, y + HEIGHT)); // Bottom-left
        ports.add(new Point(x + WIDTH / 2, y + HEIGHT)); // Bottom-center
        ports.add(new Point(x + WIDTH, y + HEIGHT)); // Bottom-right

        // 回傳所有連接點
        return ports;
    }

    // 取得邊界範圍
    @Override
    public Rectangle getBounds() {
        // 回傳一個新的 Rectangle 物件，包含矩形的位置(x,y)和尺寸(WIDTH,HEIGHT)
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
