package model.tree.utils;

import model.tree.structure.Node;
import model.tree.structure.StructureTreeNode;

public class CreateBinaryTreeUnbalanced extends CreateBinaryTree {

    @Override
    public Node treeCreate(int altezza){
        if(altezza == 0)
            return null;
        else
        {
            StructureTreeNode node = new StructureTreeNode();
            node.setValue(altezza);

            node.setDx(treeCreate(altezza-1));
            if(altezza < 3)
                node.setSx(treeCreate(0));
            else node.setSx(treeCreate(altezza-1));

            return node;
        }
    }
}
