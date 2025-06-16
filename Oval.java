// 導入必要的 Java AWT 和集合類別
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Oval 類別繼承自 Shape 類別，用於繪製橢圓形
class Oval extends Shape {
    // 定義橢圓形的固定寬度和高度
    private static final int WIDTH = 100; //寬度為 100 像素
    private static final int HEIGHT = 50; //高度為 50 像素

    // 建構子：初始化橢圓形的位置
    public Oval(int x, int y) {
        // 呼叫父類別建構子，設定位置和深度值
        super(x, y, 5); // 設定深度為 5
        setSelected(false); // 初始化選取狀態為未選取
        setLabelText("Oval1"); // 設定預設標籤文字
    }

    // 覆寫父類別的繪製方法
    @Override
    public void draw(Graphics g, boolean isSelected) {
        // 設定填充顏色為灰色
        g.setColor(Color.GRAY);
        g.fillOval(x, y, WIDTH, HEIGHT); // 繪製填充橢圓形

        // 如果被選取，則繪製port
        if (isSelected) {
            g.setColor(Color.BLACK); 
            g.fillRect(x+45, y-5, 10, 10);//上port
            g.fillRect(x+45, y+45, 10, 10);//下port
            g.fillRect(x-5, y+20, 10, 10);//左port
            g.fillRect(x+95, y+20, 10, 10);//右port
        }

        // 繪製標籤文字
        drawLabel(g);
    }

    // 取得橢圓形寬度的方法
    @Override
    public int getWidth() {
        return WIDTH;
    }

    // 取得橢圓形高度的方法
    @Override
    public int getHeight() {
        return HEIGHT;
    }

    // 檢查滑鼠座標是否在橢圓形範圍內
    @Override
    public boolean contains(int mouseX, int mouseY) {
        // 回傳 true 如果滑鼠座標在橢圓形範圍內：
        // mouseX 必須大於等於矩形左邊界(x) 且 小於等於矩形右邊界(x + WIDTH)
        // mouseY 必須大於等於矩形上邊界(y) 且 小於等於矩形下邊界(y + HEIGHT)
        return mouseX >= x && mouseX <= x + WIDTH && mouseY >= y && mouseY <= y + HEIGHT;
    }

    // 取得橢圓形的連接點（用於連線）
    @Override
    public List<Point> getConnectionPorts() {
        // 建立一個新的連接點清單
        List<Point> ports = new ArrayList<>();

        // 依序加入四個連接點：
        ports.add(new Point(x + WIDTH / 2, y)); // Top-center
        ports.add(new Point(x, y + HEIGHT / 2)); // Left-center
        ports.add(new Point(x + WIDTH, y + HEIGHT / 2)); // Right-center
        ports.add(new Point(x + WIDTH / 2, y + HEIGHT)); // Bottom-center
        return ports;
    }

    // 取得邊界範圍
    @Override
    public Rectangle getBounds() {
         // 回傳一個新的 Rectangle 物件，包含橢圓形的位置(x,y)和尺寸(WIDTH,HEIGHT)
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}