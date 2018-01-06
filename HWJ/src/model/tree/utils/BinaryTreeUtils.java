package model.tree.utils;

import model.tree.structure.Node;

import static sun.swing.MenuItemLayoutHelper.max;

public class BinaryTreeUtils {
    public boolean finishVisit;

    public void setFinishVisit(boolean set){
        this.finishVisit = set;
    }

    public boolean getFinishVisit(){
        return this.finishVisit;
    }

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

    public void treeView(Node node){
        if(node == null) return;

        System.out.print(" " + node.getValue() + " D ");
        treeView(node.getDx());
        System.out.print(" S ");
        treeView(node.getSx());
    }
}
