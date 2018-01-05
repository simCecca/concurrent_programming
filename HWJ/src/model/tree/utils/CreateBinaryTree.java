package model.tree.utils;

import model.tree.structure.Node;

import static sun.swing.MenuItemLayoutHelper.max;


public abstract class CreateBinaryTree {

    public abstract Node treeCreate(int altezza);

    /**
     *
     * @param root: radice dell'albero (o sottoalbero)
     * @return numero di nodi nell'albero
     */
    public int getNodeNumber(Node root){
        if(root == null)
            return 0;
        return 1 + getNodeNumber(root.getDx()) + getNodeNumber(root.getSx());
    }

    public int getHeight(Node root){
        if(root == null)
            return 0;

        return 1 + max(getHeight(root.getDx()),getHeight(root.getSx()));
    }
}
