package com.example.formap;
import java.util.ArrayList;

public class NotificationCenter {
    private ArrayList<Observer> observers;

    public NotificationCenter() {
        observers = new ArrayList<Observer>();
    }

    public void register(Observer observer) {
        if(!this.observers.contains(observer))
            this.observers.add(observer);
    }

    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    public void notifyy() {

        this.updateObservers();
    }

    private void updateObservers() {
        for (Observer o:
                this.observers) {
            o.update();
        }
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }
}

