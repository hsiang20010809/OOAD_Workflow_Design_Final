import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

class Canvas extends JPanel {
    private List<Shape> shapes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
    private Shape startShape = null;
    private Point startPort = null;
    private Shape selectedShape = null;
    private LinkType currentLinkType = LinkType.ASSOCIATION;
    private Rectangle selectionRect = null;
    private boolean isDragging = false; // 用於判斷是否正在拖曳
    private Point dragStartPoint = null; // 用於記錄拖曳的起始點

    enum Mode {
        RECT, OVAL, SELECT, LINK, NONE
    }

    private Mode currentMode = Mode.NONE; // 預設為 NONE 模式

    public void addShape(Shape shape) {
        clearSelection();
        shapes.add(shape);
        shapes.sort((s1, s2) -> Integer.compare(s2.getDepth(), s1.getDepth()));
        repaint();
    }

    public void groupSelectedShapes() {
        List<Shape> selectedShapes = new ArrayList<>();
        for (Shape shape : shapes) {
            if (shape.isSelected()) {
                selectedShapes.add(shape);
            }
        }

        if (selectedShapes.size() > 1) {
            Composite composite = new Composite();
            for (Shape shape : selectedShapes) {
                composite.addShape(shape);
            }

            shapes.removeAll(selectedShapes); // 移除原本選取的物件
            shapes.add(composite); // 加入新的 Composite
            clearSelection();
            composite.setSelected(true); // 選取新的 Composite
            repaint();
        }
    }

    public void ungroupSelectedComposite() {
        if (selectedShape instanceof Composite) {
            Composite composite = (Composite) selectedShape;
            shapes.remove(composite); // 移除 Composite
            shapes.addAll(composite.getChildShapes()); // 加回內部的物件
            clearSelection();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Shape shape : shapes) {
            shape.draw(g, shape.isSelected());
        }
        
        for (Link link : links) {
            link.draw(g);
            
            if (link.getStartShape() != null && link.getStartShape().isSelected()) {
                Point startPort = link.getStartPort();
                g.setColor(Color.BLACK);
                g.fillRect(startPort.x - 5, startPort.y - 5, 10, 10);        
            }
        }

        // 繪製框選矩形
        if (selectionRect != null) {
            g.setColor(Color.BLUE);
            ((Graphics2D) g).setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
            g.drawRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
        }
    }

    public Canvas() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickPoint = e.getPoint();
                isDragging = false; // 初始化為非拖曳狀態

                if (currentMode == Mode.RECT) {
                    Rect rect = new Rect(clickPoint.x, clickPoint.y);
                    addShape(rect);
                } else if (currentMode == Mode.OVAL) {
                    Oval oval = new Oval(clickPoint.x, clickPoint.y);
                    addShape(oval);
                } else if (currentMode == Mode.SELECT) {
                    selectedShape = null;
                    boolean shapeClicked = false;

                    for (int i = shapes.size() - 1; i >= 0; i--) {
                        Shape shape = shapes.get(i);
                        if (shape.contains(clickPoint.x, clickPoint.y)) {
                            shapeClicked = true;

                            // 清除其他物件的選取狀態
                            clearSelection();

                            // 設定當前物件為選取狀態
                            selectedShape = shape;
                            shape.setSelected(true);

                            // 記錄拖曳起始點
                            dragStartPoint = clickPoint;

                            break;
                        }
                    }

                    if (!shapeClicked) {
                        // 如果沒有點擊到任何物件，啟動框選模式
                        clearSelection();
                        selectionRect = new Rectangle(clickPoint.x, clickPoint.y, 0, 0);
                    }
                } else if (currentMode == Mode.LINK) {
                    // LINK 模式：設定連結的起始形狀和連結點
                    for (Shape shape : shapes) {
                        if (shape.contains(clickPoint.x, clickPoint.y)) {
                            startShape = shape;
                            startPort = shape.getClosestPort(clickPoint.x, clickPoint.y);
                            break;
                        }
                    }
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentMode == Mode.SELECT) {
                    if (selectionRect != null) {
                        // 框選模式：選取框內的所有物件
                        for (Shape shape : shapes) {
                            if (selectionRect.intersects(shape.getBounds())) {
                                shape.setSelected(true);
                            } else {
                                shape.setSelected(false);
                            }
                        }
                        selectionRect = null;
                    } else if (isDragging && selectedShape != null) {
                        // 拖曳模式：更新連結
                        for (Link link : links) {
                            if (link.getStartShape() == selectedShape || link.getEndShape() == selectedShape) {
                                link.updatePorts();
                            }
                        }
                    }
                } else if (currentMode == Mode.LINK && startShape != null) {
                    // LINK 模式：建立連結
                    for (Shape shape : shapes) {
                        if (shape.contains(e.getX(), e.getY()) && shape != startShape) {
                            Point endPort = shape.getClosestPort(e.getX(), e.getY());
                            Link link = new Link(startShape, startPort, shape, endPort, currentLinkType);
                            links.add(link);
                            break;
                        }
                    }

                    // 清除連結起始點
                    startShape = null;
                    startPort = null;
                }

                repaint();
                isDragging = false; // 重置拖曳狀態
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                isDragging = true; // 設定為拖曳狀態

                if (currentMode == Mode.SELECT && selectedShape != null && dragStartPoint != null) {
                    // 計算移動距離
                    int dx = e.getX() - dragStartPoint.x;
                    int dy = e.getY() - dragStartPoint.y;

                    // 移動選中的物件
                    selectedShape.move(dx, dy);

                    // 更新拖曳起始點
                    dragStartPoint = e.getPoint();

                    // 更新所有相關連結
                    for (Link link : links) {
                        if (link.getStartShape() == selectedShape || link.getEndShape() == selectedShape) {
                            link.updatePorts();
                        }

                        // 如果選中的物件是 Composite，更新其內部子物件的連線
                        if (selectedShape instanceof Composite) {
                            Composite composite = (Composite) selectedShape;
                            for (Shape child : composite.getChildShapes()) {
                                if (link.getStartShape() == child || link.getEndShape() == child) {
                                    link.updatePorts();
                                }
                            }
                        }
                    }
                } else if (currentMode == Mode.SELECT && selectionRect != null) {
                    // 更新框選矩形
                    int x = Math.min(selectionRect.x, e.getX());
                    int y = Math.min(selectionRect.y, e.getY());
                    int width = Math.abs(selectionRect.x - e.getX());
                    int height = Math.abs(selectionRect.y - e.getY());
                    selectionRect.setBounds(x, y, width, height);
                }

                repaint();
            }
        });
    }

    private void clearSelection() {
        for (Shape shape : shapes) {
            shape.setSelected(false);
        }
        selectedShape = null;
        startShape = null;
        startPort = null;
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
        clearSelection();
        repaint();
    }

    public Mode getMode() {
        return currentMode;
    }

    public void setCurrentLinkType(LinkType type) {
        this.currentLinkType = type;
    }

    public LinkType getCurrentLinkType() {
        return currentLinkType;
    }

    // 取得目前選中的物件
    public Shape getSelectedShape() {
        return selectedShape;
    }
}
