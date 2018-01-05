package model.tree.utils;

import model.tree.structure.Node;
import model.tree.structure.StructureTreeNode;

public class CreateBinaryTreeBalanced extends CreateBinaryTree {

    /**
     * crea un albero binario di altezza altezza
     * @param altezza = altezza dell'albero binario
     * @return la radice dell'albero
     */
    @Override
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

}
