package model.tree.utils;

import model.tree.structure.Node;
import model.tree.structure.StructureTreeNode;

public class CreateBinaryTree {

    /**
     * crea un albero binario di altezza altezza
     * @param altezza = altezza dell'albero binario
     * @return la radice dell'albero
     */
    public Node treeCreate(int altezza){
        if(altezza == 0)
            return null;
        else
            {
                StructureTreeNode node = new StructureTreeNode();
                node.setValue(altezza);

                node.setDx(treeCreate(altezza-1));
                node.setSx(treeCreate(altezza-1));

                return node;
            }
    }


    public Node unbalancedTreeCreate(int altezza){
        if(altezza == 0)
            return null;
        else
        {
            StructureTreeNode node = new StructureTreeNode();
            node.setValue(altezza);

            node.setDx(unbalancedTreeCreate(altezza-1));
            if(altezza < 3)
                node.setSx(unbalancedTreeCreate(0));
            else node.setSx(unbalancedTreeCreate(altezza-1));

            return node;
        }
    }
}
