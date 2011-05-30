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
public class Task {
    private TreeMap<String, Double> factories;
    private Double _size;

    public Task()
    {
        factories = new TreeMap<String, Double>();
        _size = 0.0;
    }

    public void AddFactory(String name, Double size) {
        if (!factories.containsKey(name)) {
            factories.put(name, size);
            _size += size;
        }
    }

    public TreeMap<String, Double> getFactories() {
        return factories;
    }

    public Double getSize() {
        return _size;
    }
}
