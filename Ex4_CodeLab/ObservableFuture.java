//Asaf Benor
package test;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Future;

// Class to observe changes in Future and notify observers accordingly.
public class ObservableFuture<T> extends Observable implements Observer {

    private Future<T> futureTask; // The future task being observed
    private T data; // The data value from the future task

    // Updates the data value and notifies observers if the new value is different
    public void updateData(T newData) {
        if (this.data != newData) {
            this.data = newData;
            this.setChanged(); // Marks this Observable object as having been changed
            this.notifyObservers(); // Notifies all of its observers
        }
    }

    // Returns the current data value
    public T get() {
        return this.data;
    }

    // Constructor that assigns the future task to be observed
    public ObservableFuture(Future<T> futureTask) {
        this.futureTask = futureTask;
    }

    // When an observed object is changed, this method is called to update the data value based on the change
    @Override
    public void update(Observable observed, Object argument) {
    	ObservableFuture<T> futureWatcher = (ObservableFuture<T>) observed;
        this.updateData(futureWatcher.get()); // Updates the data value based on the observed object's new data
    }
}
