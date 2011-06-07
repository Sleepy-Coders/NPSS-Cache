/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.unikernel.npss.model;

import java.util.TreeMap;

/**
 *
 * @author thp
 */

public class TaskList {
    private TreeMap<String, Task> tasks;

    public TaskList() {
        tasks = new TreeMap<String, Task>();
    }

    public void Add(String task, String factory, Double size) {
        if (!tasks.containsKey(task)) {
            tasks.put(task, new Task());
        }

        tasks.get(task).AddFactory(factory, size);
    }

    public TreeMap<String, Task> getTasks() {
        return tasks;
    }
}