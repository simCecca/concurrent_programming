package model.tree.structure;

/*definizione della struttura ad albero binario*/
public class StructureTreeNode implements Node {
    private Node dx;
    private Node sx;
    private int value;

    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public Node getDx() {
        return dx;
    }

    public void setDx(Node dx){
        this.dx = dx;
    }

    public Node getSx() {
        return sx;
    }

    public void setSx(Node sx) {
        this.sx = sx;
    }
}
