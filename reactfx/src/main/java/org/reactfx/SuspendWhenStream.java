package org.reactfx;

import javafx.beans.value.ObservableValue;

class SuspendWhenStream<T> extends LazilyBoundStream<T> {
    private final SuspendableEventStream<T> source;
    private final ObservableValue<Boolean> condition;

    private Guard suspensionGuard = null;

    public SuspendWhenStream(
            SuspendableEventStream<T> source,
            ObservableValue<Boolean> condition) {
        this.source = source;
        this.condition = condition;
    }

    @Override
    protected Subscription subscribeToInputs() {
        Subscription s1 = subscribeTo(
                EventStreams.valuesOf(condition), this::suspendSource);
        Subscription s2 = subscribeTo(source, this::emit);
        return s1.and(s2).and(this::resumeSource);
    }

    private void suspendSource(boolean suspend) {
        if(suspend) {
            suspendSource();
        } else {
            resumeSource();
        }
    }

    private void suspendSource() {
        if(suspensionGuard == null) {
            suspensionGuard = source.suspend();
        }
    }

    private void resumeSource() {
        if(suspensionGuard != null) {
            Guard toClose = suspensionGuard;
            suspensionGuard = null;
            toClose.close();
        }
    }
}
