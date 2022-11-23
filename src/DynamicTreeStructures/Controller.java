package DynamicTreeStructures;

import DynamicTreeStructures.imagegenerator.Image;
import DynamicTreeStructures.imagegenerator.ImageRedBlackTree;
import DynamicTreeStructures.imagegenerator.ImageTree;
import DynamicTreeStructures.interfaces.TreeImage;
import DynamicTreeStructures.interfaces.TreeStructure;
import DynamicTreeStructures.structure.RedBlackTree;
import DynamicTreeStructures.structure.RootedBinaryTree;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Fiordor
 */
public class Controller {

    public final static int BALANCED_BINARY_TREE = 0;
    public final static int COMPLETE_BINARY_TREE = 1;
    public final static int DEGENERATE_BINARY_TREE = 2;
    public final static int PERFECT_BINARY_TREE = 3;
    public final static int ROOTED_BINARY_TREE = 4;
    public final static int RED_BLACK_TREE = 5;

    private int type;

    private TreeStructure<Integer> tree;
    private RedBlackTree<Integer> redBlackTree;

    private DefaultTableModel table;

    private JLabel label;
    private Canvas canvas;
    private BufferedImage image;

    private int canvasX;
    private int canvasY;
    private double zoom;

    public Controller(JPanel panel, JTable table, JLabel label) {
        this(panel, table, label, RED_BLACK_TREE);
    }

    public Controller(JPanel panel, JTable table, JLabel label, int type) {

        setTreeType(type);
        this.label = label;
        this.table = (DefaultTableModel) table.getModel();
        table.getColumn("#").setPreferredWidth(30);
        this.canvas = new Canvas();
        this.zoom = 1.0;

        canvas.setBackground(Color.WHITE);

        canvas.setBounds(0, 0, panel.getWidth(), panel.getHeight());
        panel.add(canvas);
    }

    public void setTreeType(int type) {
        this.type = type;

        this.redBlackTree = null;
        this.tree = null;

        switch (type) {
            case BALANCED_BINARY_TREE:
                break;
            case COMPLETE_BINARY_TREE:
                break;
            case DEGENERATE_BINARY_TREE:
                break;
            case PERFECT_BINARY_TREE:
                break;
            case ROOTED_BINARY_TREE:
                this.tree = new RootedBinaryTree<>();
                break;
            case RED_BLACK_TREE:
                this.redBlackTree = new RedBlackTree<>();
                break;
        }
    }

    public void setSize(int w, int h) {
        canvas.setSize(w, h);
    }

    public void add(String input) {
        if (input.length() == 0) {
            return;
        }
        int value = Integer.parseInt(input);

        long start = System.nanoTime();
        if (redBlackTree == null) {
            tree.insert(value);
        } else {
            redBlackTree.insert(value);
        }
        long fin = System.nanoTime();

        table.addRow(new Object[]{table.getRowCount() + 1, input, (fin - start)});

        label.setText(String.format("Insert %d: %d ns", value, fin - start));
        print();
    }

    public void clear() {
        this.canvas.clear();
        this.canvas.repaint();
        this.table.setRowCount(0);
        this.zoom = 1.0;

        this.setTreeType(type);
    }

    public void search(String input) {
        if (input.length() == 0) {
            return;
        }
        int value = Integer.parseInt(input);

        long start = System.nanoTime();
        if (redBlackTree == null) {
            tree.search(value);
        } else {
            redBlackTree.search(value);
        }
        long fin = System.nanoTime();

        label.setText(String.format("Search %d: %d ns", value, fin - start));
        print();
    }

    public void delete(String input) {
        if (input.length() == 0) {
            return;
        }
        
        label.setText("Not implemented");
    }

    public void generate(String input) {
        if (input.length() == 0) {
            return;
        }
        int k = Integer.parseInt(input);
        long totalTime = 0;
        Random r = new Random();
        for (int i = 0; i < k; i++) {
            int random = r.nextInt(k * 10);

            ArrayList<Integer> values = new ArrayList<>(table.getRowCount());
            for (int j = 0; j < values.size(); j++) {
                values.add(Integer.valueOf(table.getValueAt(j, 1).toString()));
            }

            while (values.contains(random)) {
                random = r.nextInt(1000);
            }

            long start = System.nanoTime();
            if (redBlackTree == null) {
                tree.insert(random);
            } else {
                redBlackTree.insert(random);
            }
            long fin = System.nanoTime();

            table.addRow(new Object[]{table.getRowCount() + 1, random, (fin - start)});
            totalTime += (fin - start);
        }

        label.setText(String.format("Generate %d: %d ns",k, totalTime));
        print();
    }

    public void movePressed(int x, int y) {
        canvasX = x - canvas.getPoint()[0];
        canvasY = y - canvas.getPoint()[1];
    }

    public void moveDragged(int x, int y) {
        canvas.setPoint(x - canvasX, y - canvasY);
        canvas.repaint();
    }

    public void moveReleased() {

    }

    public void zoom(int zoom) {

        if (this.image == null) {
            return;
        }

        this.zoom = this.zoom - (zoom / 10.0);

        int w = (int) Math.ceil(this.image.getWidth() * (this.zoom > 0 ? this.zoom : 1));
        int h = (int) Math.ceil(this.image.getHeight() * (this.zoom > 0 ? this.zoom : 1));
        BufferedImage scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = AffineTransform.getScaleInstance(this.zoom, this.zoom);
        AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        try {
            scaledImage = ato.filter(image, scaledImage);

            canvas.setImage(scaledImage);
            canvas.repaint();
        } catch (ImagingOpException ex) {
            label.setText(ex.getMessage());
        }
    }

    private void print() {

        /*
        System.out.println(redBlackTree == null
                ? tree.toString()
                : redBlackTree.toString());
         */
        TreeImage treeImage = redBlackTree == null
                ? new ImageTree<>(tree, 16, 16)
                : new ImageRedBlackTree<>(redBlackTree, 16, 16);

        try {
            this.image = new Image(treeImage, typeToString()).create();
            canvas.setImage(this.image);
            canvas.repaint();
        } catch (OutOfMemoryError | NegativeArraySizeException | IllegalArgumentException ex) {
            label.setText(ex.getMessage());
        }
    }

    private String typeToString() {
        switch (type) {
            case BALANCED_BINARY_TREE:
                return "Balanced binary tree";
            case COMPLETE_BINARY_TREE:
                return "Completed binary tree";
            case DEGENERATE_BINARY_TREE:
                return "Degenerate binary tree";
            case PERFECT_BINARY_TREE:
                return "Perfect binary tree";
            case ROOTED_BINARY_TREE:
                return "Rooted binary tree";
            case RED_BLACK_TREE:
                return "Red black tree";
            default:
                return null;
        }
    }
}
