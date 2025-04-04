import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Rect extends Shape {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public Rect(int x, int y) {
        super(x, y, 10); // 設定深度為 10
        setSelected(false);
        setLabelText("Rect1"); // 設定預設標籤文字
    }

    @Override
    public void draw(Graphics g, boolean isSelected) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, WIDTH, HEIGHT);

        if (isSelected) {
            g.setColor(Color.RED);
            g.drawRect(x, y, WIDTH, HEIGHT);
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
        return mouseX >= x && mouseX <= x + WIDTH && mouseY >= y && mouseY <= y + HEIGHT;
    }

    @Override
    public List<Point> getConnectionPorts() {
        List<Point> ports = new ArrayList<>();
        ports.add(new Point(x, y)); // Top-left
        ports.add(new Point(x + WIDTH / 2, y)); // Top-center
        ports.add(new Point(x + WIDTH, y)); // Top-right
        ports.add(new Point(x, y + HEIGHT / 2)); // Left-center
        ports.add(new Point(x + WIDTH, y + HEIGHT / 2)); // Right-center
        ports.add(new Point(x, y + HEIGHT)); // Bottom-left
        ports.add(new Point(x + WIDTH / 2, y + HEIGHT)); // Bottom-center
        ports.add(new Point(x + WIDTH, y + HEIGHT)); // Bottom-right
        return ports;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
