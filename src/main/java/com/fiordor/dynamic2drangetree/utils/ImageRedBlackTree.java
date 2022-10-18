package com.fiordor.dynamic2drangetree.utils;

import com.fiordor.dynamic2drangetree.structure.Node;
import com.fiordor.dynamic2drangetree.structure.RedBlackTree;

public class ImageRedBlackTree<K extends Comparable<K>> {

    private class ImageNode {

        public String data;

        public int x;
        public int y;
        public int width;
        public int height;

        public ImageNode left;
        public ImageNode right;

        public ImageNode(String data, int x, int y, int width, int height, ImageNode left, ImageNode right) {
            this.data = data;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.left = left;
            this.right = right;
        }

    }


    public ImageRedBlackTree(RedBlackTree<K> tree) {

        

    }

    private void next(Node<K> node, int deep) {

        if (node.getLeft() == null && node.getRight() == null) {

        }

        if (node.getLeft() != null) {
            next(node.getLeft(), deep + 1);
        }

        //return node;
    }
}
