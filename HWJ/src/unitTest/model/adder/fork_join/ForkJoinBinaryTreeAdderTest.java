package model.adder.fork_join;

import model.tree.structure.Node;
import model.tree.utils.CreateBinaryTreeBalanced;
import model.tree.utils.CreateBinaryTreeUnbalanced;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ForkJoinBinaryTreeAdderTest {

    private Node nodeAltezza1;
    private Node nodeAltezza7;
    private Node nodeAltezza10;
    private Node nodeAltezza13;

    private Node nodeUnbalancedAltezza3;
    private Node nodeUnbalancedAltezza7;
    private Node nodeUnbalancedAltezza15;

    private CreateBinaryTreeBalanced simpleBinary;
    private CreateBinaryTreeUnbalanced unbalanced;
    private ForkJoinBinaryTreeAdder adder;

    @Before
    public void init(){
        this.simpleBinary = new CreateBinaryTreeBalanced();
        this.unbalanced = new CreateBinaryTreeUnbalanced();

        this.nodeAltezza1 = this.simpleBinary.treeCreate(1);
        this.nodeAltezza7 = this.simpleBinary.treeCreate(7);
        this.nodeAltezza10 = this.simpleBinary.treeCreate(10);
        this.nodeAltezza13 = this.simpleBinary.treeCreate(13);
        this.nodeUnbalancedAltezza3 = this.unbalanced.treeCreate(3);
        this.nodeUnbalancedAltezza7 = this.unbalanced.treeCreate(7);
        this.nodeUnbalancedAltezza15 = this.unbalanced.treeCreate(15);

        this.adder = new ForkJoinBinaryTreeAdder();
    }


    /*se la somma è quella aspettata sono sicuro che il nodo non è stato visitato più volte
     * la non proliferazione dei thread a seguito dell'invocazione del metodo è garantita dal
     * fatto che con la Cyclicbarrier fino a che tutti non hanno finito, non mi muovo*/
    @Test
    public void testTreeAltezza1(){

        assertEquals(1,this.simpleBinary.getNodeNumber(this.nodeAltezza1));
        assertEquals(1,adder.computeOnerousSum(this.nodeAltezza1));
    }

    @Test
    public void testTreeAltezza7(){
        assertEquals(247,adder.computeOnerousSum(this.nodeAltezza7));
    }

    @Test
    public void testTreeAltezza10(){
        assertEquals(2036,adder.computeOnerousSum(this.nodeAltezza10));
    }

    @Test
    public void testTreeAltezza13(){
        assertEquals(16369,adder.computeOnerousSum(this.nodeAltezza13));
    }

    @Test
    public void testTreeUnbalancedAltezza3(){
        assertEquals(9,adder.computeOnerousSum(this.nodeUnbalancedAltezza3));
    }

    @Test
    public void testTreeUnbalancedAltezza7(){
        assertEquals(215,adder.computeOnerousSum(this.nodeUnbalancedAltezza7));
    }

    @Test
    public void testTreeUnbalancedAltezza15(){
        assertEquals(57327,adder.computeOnerousSum(this.nodeUnbalancedAltezza15));
    }


}