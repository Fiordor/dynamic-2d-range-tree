package DynamicTreeStructures.imagegenerator;

import DynamicTreeStructures.structure.RedBlackTree;
import DynamicTreeStructures.structure.RedBlackTreeNode;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Fiordor
 */
public class ImageRedBlackTree<K extends Comparable<K>> {

    private ImageNode[][] nodeMatrix;
    private int width;
    private int height;

    private Font font;

    private BufferedImage calcBoundsImg;
    private Graphics2D calcBoundsG2D;
    private FontMetrics calcBoundsFontMetrics;

    public ImageRedBlackTree(RedBlackTree<K> tree) {
        this(tree, 16, 16, new Font(Font.MONOSPACED, Font.PLAIN, 16));
    }

    public ImageRedBlackTree(RedBlackTree<K> tree, int gapWidth, int gapHeight) {
        this(tree, gapWidth, gapHeight, new Font(Font.MONOSPACED, Font.PLAIN, 16));
    }

    public ImageRedBlackTree(RedBlackTree<K> tree, int gapWidth, int gapHeight, Font font) {

        this.font = font;

        this.calcBoundsImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        this.calcBoundsG2D = this.calcBoundsImg.createGraphics();
        this.calcBoundsFontMetrics = this.calcBoundsG2D.getFontMetrics(this.font);

        List< List<ImageNode>> rawMatrix = new ArrayList<>();

        if (tree != null) {
            next(null, tree.getRoot(), 0, rawMatrix);

            nodeMatrix = new ImageNode[rawMatrix.size()][];
            for (int i = 0; i < nodeMatrix.length; i++) {
                nodeMatrix[i] = new ImageNode[rawMatrix.get(i).size()];
                for (int j = 0; j < nodeMatrix[i].length; j++) {
                    nodeMatrix[i][j] = rawMatrix.get(i).get(j);
                }
            }

            assign(gapWidth, gapHeight);

        } else {
            nodeMatrix = null;
        }
    }

    public ImageLine[] getLines() {

        List<ImageLine> dynamicLines = new ArrayList<>();

        for (int i = 0; i < nodeMatrix.length; i++) {
            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];
                ImageNode parent = node.parent;
                if (parent != null) {
                    int x1 = parent.x + (parent.w / 2);
                    int y1 = parent.y - (parent.h / 2);
                    int x2 = node.x + (node.w / 2);
                    int y2 = node.y - (node.h / 2);

                    dynamicLines.add(new ImageLine(x1, y1, x2, y2));
                }
            }
        }

        ImageLine[] lines = new ImageLine[dynamicLines.size()];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = dynamicLines.get(i);
        }

        return lines;
    }

    public ImageLabel[] getLabels() {

        List<ImageLabel> dynamicLabels = new ArrayList<>();

        for (int i = 0; i < nodeMatrix.length; i++) {
            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];
                dynamicLabels.add(new ImageLabel(node.value, node.x, node.y, node.w, node.h, node.c));
            }
        }

        ImageLabel[] labels = new ImageLabel[dynamicLabels.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = dynamicLabels.get(i);
        }

        return labels;
    }

    public Font getFont() {
        return font;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Asigna la posición y las dimensiones de los nodos. Primero se obtiene el
     * máximo ancho que opcupa la representación gráfica del árbol. Para cada
     * fila del árbol primero se saca las dimensiones del nodo, con las
     * dimensiones se calcula la posición. Siempre se tiene en cuenta los
     * márgenes indicados. Al final se asigna el alto de la imangen.
     *
     * @param gapWidth espacio entre los datos de una misma fila.
     * @param gapHeight espacio entre la columnas.
     */
    private void assign(int gapWidth, int gapHeight) {

        width = 0;
        height = 0;

        for (int i = 0; i < nodeMatrix.length; i++) {
            int rowWidth = getRowWidth(nodeMatrix[i]);
            if (nodeMatrix[i].length > 0) {
                rowWidth += (nodeMatrix[i].length * gapWidth) - gapWidth;
            }

            if (rowWidth >= width) {
                width = rowWidth;
            }
        }

        int top = 0;
        for (int i = 0; i < nodeMatrix.length; i++) {

            int left = 0;
            int maxHeight = 0, maxWidth = 0;

            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];

                int[] bounds = stringBounds(node.value);
                node.w = bounds[0];
                node.h = bounds[1];

                maxWidth += node.w;
            }

            maxWidth += (nodeMatrix[i].length * gapWidth) - gapWidth;
            left = width - maxWidth;
            left = left <= 0 ? 0 : left / 2;

            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];

                node.x = left;
                node.y = top + node.h;

                left += node.w + gapWidth;
                if (node.h >= maxHeight) {
                    maxHeight = node.h;
                }

            }
            top += maxHeight + gapHeight;
        }
        top -= gapHeight;

        height = top;
    }

    /**
     * Calcula los píxeles que ocupa la suma de strings de una fila en bruto,
     * sin márgenes
     *
     * @param row array de los ImageNode que lee el data
     * @return suma de la longitud en píxeles de todos los strings
     */
    private int getRowWidth(ImageNode[] row) {

        int width = 0;
        for (int i = 0; i < row.length; i++) {
            width += stringBounds(row[i].value)[0];
        }
        return width;
    }

    /**
     * Añade elementos a un array bidimensional dinámico. Si la fila no existe,
     * se crea. Se añade el nodo al array y si no es la raíz se le pone al nodo
     * la referencia de su padre. Luego se continua con los nodos hijos del
     * nodo.
     *
     * @param parent nodo padre parseado al ImageNode || null para el nodo root.
     * @param child nodo que se inserta en el array bidimensional.
     * @param deep profundidad del árbol.
     * @param matrix array bidimensional donde se añaden los nuevos nodos.
     */
    private void next(ImageNode parent, RedBlackTreeNode<K> child, int deep, List<List<ImageNode>> matrix) {

        if (deep >= matrix.size()) {
            matrix.add(new ArrayList<>());
        }

        ImageNode node = parse(child);
        matrix.get(deep).add(node);

        if (parent != null) {
            node.parent = parent;
        }

        if (child.getLeft() != null) {
            next(node, child.getLeft(), deep + 1, matrix);
        }

        if (child.getRight() != null) {
            next(node, child.getRight(), deep + 1, matrix);
        }
    }

    /**
     * Calculate bounds needs a string into image.
     * 
     * @param text label to print into the image
     * @return int[2] where 0 is width and 1 is height
     */
    private int[] stringBounds(String text) {

        Rectangle2D bounds = this.calcBoundsFontMetrics.getStringBounds(text, this.calcBoundsG2D);
        double rawWidth = bounds.getWidth();
        double rawHeight = bounds.getHeight();
        int w = rawWidth % 1 == 0 ? (int) rawWidth : (int) rawWidth + 1;
        int h = rawHeight % 1 == 0 ? (int) rawHeight : (int) rawHeight + 1;
        return new int[]{ w, h };
    }

    private ImageNode parse(RedBlackTreeNode<K> node) {
        return new ImageNode(node.getData().toString(), node.isRed() ? Color.RED : Color.BLACK);
    }

}
