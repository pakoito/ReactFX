package inhibeans.property;

/**
 * Inhibitory version of {@link javafx.beans.property.SimpleLongProperty}.
 */
public class SimpleLongProperty extends javafx.beans.property.SimpleLongProperty {

    private boolean blocked = false;
    private boolean fireOnRelease = false;

    public void block() {
        blocked = true;
    }

    public void release() {
        blocked = false;
        if(fireOnRelease) {
            fireOnRelease = false;
            super.fireValueChangedEvent();
        }
    }

    @Override
    protected void fireValueChangedEvent() {
        if(blocked)
            fireOnRelease = true;
        else
            super.fireValueChangedEvent();
    }


    /********************************
     *** Superclass constructors. ***
     ********************************/

    public SimpleLongProperty() {
        super();
    }

    public SimpleLongProperty(long initialValue) {
        super(initialValue);
    }

    public SimpleLongProperty(Object bean, String name) {
        super(bean, name);
    }

    public SimpleLongProperty(Object bean, String name, long initialValue) {
        super(bean, name, initialValue);
    }
}