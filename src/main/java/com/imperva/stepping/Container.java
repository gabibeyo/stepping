package com.imperva.stepping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Container {
    //todo improve performance (O)N
    private volatile List<Identifiable> objects = new CopyOnWriteArrayList<>();

    public Container() {
    }

    public <T> Container add(T obj) {
        add(new Identifiable<T>(obj, obj.getClass().getName() + obj.hashCode()));
        return this;
    }

    public <T> Container add(T obj, String id) {
        add(new Identifiable<T>(obj, id));
        return this;
    }

    public <T> Container add(Identifiable<T> identifiable) {
        if (objects.contains(identifiable))
            throw new RuntimeException("Identifiable Object must contain unique ID. " + identifiable.getId() + " already exists!");
        objects.add(identifiable);
        return this;
    }

    public Container remove(String id) {
        objects.removeIf((i) -> i.getId().toLowerCase().equals(id.toLowerCase()));
        return this;
    }

    public Container add(List<Identifiable> identifiables) {
        objects.addAll(identifiables);
        return this;
    }

    public <T> T getById(String id) {
        for (Identifiable identifiable :  objects) {
            if (identifiable.getId().toLowerCase().equals(id.toLowerCase()))
                return (T) identifiable.get();
        }
        return null;
    }

    private boolean getSonOf(Class<?> interf, Class clss, List<Class> context) {
        if (clss == null) {
            for (Class c : context) {
                if (c.equals(interf)) {
                    return true;
                }
            }
            return false;
        }

        if (clss.equals(interf)) {
            return true;
        }

        for (Class intfc : clss.getInterfaces()) {
            if (intfc.equals(interf)) {
                return true;
            }
            context.addAll(Arrays.asList(intfc.getInterfaces()));
        }
        return getSonOf(interf, clss.getSuperclass(), context);
    }

    public <T> List<T> getSonOf(Class<?> interf) {
        List<T> ts = new ArrayList<>();
        List<Object> objects2 = objects.stream().map((obj) -> ((Identifiable) obj).get()).collect(Collectors.toList());
        for (Object o : objects2) {
            Boolean found = getSonOf(interf, o.getClass(), new ArrayList<>());
            if (found)
                ts.add((T) o);
        }
        return ts;
    }

    public <T> List<T> getTypeOf(Class<?> interf) {
        List<T> ts = new ArrayList<>();
        List<Object> objects2 = objects.stream().map((obj) -> ((Identifiable) obj).get()).collect(Collectors.toList());
        for (Object o : objects2) {
            if (o.getClass().equals(interf))
                ts.add((T) o);
        }
        return ts;
    }

    public void clear() {
        if (objects != null)
            objects.clear();
    }

    public int size() {
        return objects.size();
    }

    public boolean exist(String name) {
        for (Identifiable identifiable : objects) {
            if (identifiable.getId().equals(name)) {
                return true;
            }
            return false;
        }
        return false;
    }
}