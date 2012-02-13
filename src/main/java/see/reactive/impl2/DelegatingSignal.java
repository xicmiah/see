package see.reactive.impl2;

import com.google.common.base.Preconditions;
import see.reactive.Signal;

import static com.google.common.collect.ImmutableSet.of;

class DelegatingSignal<T> extends AbstractObserverSignal<T> {

    private AbstractObserverSignal<T> delegate;

    public DelegatingSignal(Signal<T> delegate) {
        super(of(delegate), delegate.now());
        this.delegate = (AbstractObserverSignal<T>) delegate;
    }

    @Override
    protected void handleChangeEvent() {
        fireChangeEvent(delegate.now());
    }

    public Signal<T> getDelegate() {
        return delegate;
    }

    public void setDelegate(Signal<T> delegate) {
        Preconditions.checkArgument(delegate instanceof AbstractObserverSignal<?>);

        unsubscribe(this.delegate);
        this.delegate = (AbstractObserverSignal<T>) delegate;
        subscribe(this.delegate);
        fireChangeEvent(delegate.now());
    }

    private void subscribe(AbstractObserverSignal<?> target) {
        target.getChangeSupport().addPropertyChangeListener(this);
    }

    private void unsubscribe(AbstractObserverSignal<?> target) {
        target.getChangeSupport().removePropertyChangeListener(this);
    }
}
