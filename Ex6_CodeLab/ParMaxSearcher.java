//Asaf Benor
package test;

import java.util.concurrent.RecursiveTask;

public class ParMaxSearcher extends RecursiveTask<Integer> {
    private BinTree currentNode;

    public ParMaxSearcher(BinTree currentNode) {
        this.currentNode = currentNode;
    }

    @Override
    protected Integer compute() {
        if (isLeafNode(currentNode)) {
            return currentNode.get();
        }

        int leftMax = Integer.MIN_VALUE;
        int rightMax = Integer.MIN_VALUE;

        RecursiveTask<Integer> leftTask = processChildNode(currentNode.getLeft());
        RecursiveTask<Integer> rightTask = processChildNode(currentNode.getRight());

        // Wait for completion of tasks and retrieve results
        leftMax = leftTask == null ? leftMax : leftTask.join();
        rightMax = rightTask == null ? rightMax : rightTask.join();

        return Math.max(Math.max(leftMax, rightMax), currentNode.get());
    }

    private boolean isLeafNode(BinTree node) {
        return node != null && node.getLeft() == null && node.getRight() == null;
    }

    private RecursiveTask<Integer> processChildNode(BinTree childNode) {
        if (childNode == null) {
            return null;
        }
        ParMaxSearcher childTask = new ParMaxSearcher(childNode);
        childTask.fork();
        return childTask;
    }
}
