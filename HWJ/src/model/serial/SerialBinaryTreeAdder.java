package model.serial;

import model.BinaryTreeAdder;
import model.processor.FakeProcessor;
import model.tree.structure.Node;

public class SerialBinaryTreeAdder implements BinaryTreeAdder {

    private FakeProcessor processor;

    public SerialBinaryTreeAdder(){
        this.processor = new FakeProcessor(1000);
    }


    @Override
    public int computeOnerousSum(Node root) {
        if(root == null)
            return 0;
        return this.processor.onerousFunction(root.getValue()) + computeOnerousSum(root.getSx()) + computeOnerousSum(root.getDx());
    }
}
