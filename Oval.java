import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Oval extends Shape {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public Oval(int x, int y) {
        super(x, y, 5); // 設定深度為 5
        setSelected(false);
        setLabelText("Oval1"); // 設定預設標籤文字
    }

    @Override
    public void draw(Graphics g, boolean isSelected) {
        // 設定填充顏色為灰色
        g.setColor(Color.GRAY);
        g.fillOval(x, y, WIDTH, HEIGHT);

        // 如果被選取，繪製紅色邊框
        if (isSelected) {
            g.setColor(Color.RED);
            g.drawOval(x, y, WIDTH, HEIGHT);
        }

        // 繪製標籤
        drawLabel(g);
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public boolean contains(int mouseX, int mouseY) {
        double dx = mouseX - (x + WIDTH / 2.0);
        double dy = mouseY - (y + HEIGHT / 2.0);
        return (dx * dx) / (WIDTH * WIDTH / 4.0) + (dy * dy) / (HEIGHT * HEIGHT / 4.0) <= 1.0;
    }

    @Override
    public List<Point> getConnectionPorts() {
        List<Point> ports = new ArrayList<>();
        ports.add(new Point(x + WIDTH / 2, y)); // Top-center
        ports.add(new Point(x, y + HEIGHT / 2)); // Left-center
        ports.add(new Point(x + WIDTH, y + HEIGHT / 2)); // Right-center
        ports.add(new Point(x + WIDTH / 2, y + HEIGHT)); // Bottom-center
        return ports;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
