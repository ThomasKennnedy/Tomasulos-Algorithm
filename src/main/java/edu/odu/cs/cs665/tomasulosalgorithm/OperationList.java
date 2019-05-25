package edu.odu.cs.cs665.tomasulosalgorithm;

import java.util.List;
import java.util.ArrayList;

/**
 * This class provides all operation queueing and storage funtionality.
 */
public class OperationList {
    private List<Operation> opList; ///< The list op Operations
    private int currOp;             ///< The current Operation

    /**
     * Initialize the opList.
     */
    public OperationList()
    {
        opList = new ArrayList<Operation>();
        currOp = 1;
    }

    /**
     * Generate a new OperationList from the specified OperationList.
     */
    public OperationList(OperationList toCopy)
    {
        this.opList = new ArrayList<Operation>(toCopy.opList);
        this.currOp = toCopy.currOp;
    }

    /**
     * Return an ArrayList<Operation> that contains a copy of all Operations.
     */
    public List<Operation> getOperationList()
    {
        return new ArrayList<Operation>(opList);
    }

    /**
     * Get an Operation given a position number. The first position is "1".
     */
    public Operation getOperation(int num)
    {
        return opList.get(num - 1);
    }

    /**
     * Get the next Operation to be scheduled.
     */
    public Operation getNextOperation()
    {
        return opList.get(currOp - 1);
    }

    /**
     * Get the last Operation in the list.
     */
    public Operation getLastOperation()
    {
        return opList.get(opList.size() - 1);
    }

    /**
     * Returns true when there is at least one Operation that hs not been
     * scheduled.
     *
     * @return false if there is not an Operation to schedule.
     */
    public boolean moreOperationsQueued()
    {
        return currOp <= opList.size();
    }

    /**
     * Add an operation to the OperationList.
     */
    public void addOperation(Operation toAdd)
    {
        opList.add(toAdd);
    }

    /**
     * Return the number of operations in the list.
     */
    public int getNumberOfOperations()
    {
        return opList.size();
    }

    /**
     * Set the iterator to the next Operation in the List.
     */
    public void increment()
    {
        currOp++;
    }

    /**
     * Generates the string representation of the Operation Object.
     */
    public String toString()
    {
        StringBuilder bld = new StringBuilder();
        bld.append("# Operations: " + opList.size());

        for (Operation toPrint : opList) {
            bld.append("\n" + toPrint);
        }

        return bld.toString();
    }
}
