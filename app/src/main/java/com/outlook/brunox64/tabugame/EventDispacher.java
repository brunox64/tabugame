package com.outlook.brunox64.tabugame;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bruno on 18/09/2016.
 */
public class EventDispacher {
    public interface Listener {
        void invoke(Object arg);
    }
    private static class Event {
        String eventName;
        Listener callback;
        int handler;
    }
    private List<Event> events;
    private int handlers;
    private boolean executing = false;
    private List<Event> addList;
    private List<Event> removeList;
    public EventDispacher() {
        events = new LinkedList<>();
        handlers = 0;
        executing = false;
        removeList = new LinkedList<>();
        addList = new LinkedList<>();
    }
    public int addEventListener(String eventName, Listener callback) {
        if (eventName == null) throw new RuntimeException("eventName é obrigatório!.");
        if (callback == null) throw new RuntimeException("listener é obrigatório!.");
        Event event = new Event();
        event.eventName = eventName;
        event.callback = callback;
        event.handler = ++handlers;
        if (this.executing) this.addList.add(event);
        else this.events.add(0,event);
        return this.handlers;
    }
    public void removeEventListener(int handler) {
        for (Event e : events) {
            if (e.handler == handler) {
                if (this.executing) this.removeList.add(e);
                else this.events.remove(events.indexOf(e));
                break;
            }
        }
    }
    public void dispatchEvent(String eventName) {
        dispatchEvent(eventName,null);
    }
    public void dispatchEvent(String eventName, Object arg) {
        if (eventName == null) throw new RuntimeException("eventName é obrigatório!.");
        Event event = null;
        for (int i = this.events.size()-1; i >= 0; i--) {
            event = this.events.get(i);
            if (event.eventName.equals(eventName)) {
                boolean executing = this.executing;
                if (!executing) this.executing = true;

                event.callback.invoke(arg);

                if (this.removeList.size() > 0) {
                    for (int j = 0; j < this.removeList.size(); j++) {
                        int k = this.events.indexOf(this.removeList.get(j));
                        if (k > -1 && k < i) i--;
                        if (k > -1) this.events.remove(k);
                    }
                    this.removeList = new LinkedList<>();
                }

                if (this.addList.size() > 0) {
                    for (int j = 0; j < this.addList.size(); j++) {
                        this.events.add(0, this.addList.get(j));
                        i++;
                    }
                    this.addList = new LinkedList<>();
                }

                if (!executing) this.executing = false;
            }
        }
    }
}
