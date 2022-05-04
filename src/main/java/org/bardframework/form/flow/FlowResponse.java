package org.bardframework.form.flow;

import org.bardframework.form.model.Form;

public class FlowResponse<D> {
    private final D data;
    private Form form;
    private boolean finished;
    private int steps;
    private int current;

    public FlowResponse(D data) {
        this.data = data;
    }

    public boolean isFinished() {
        return finished;
    }

    public FlowResponse<D> setFinished(boolean finished) {
        this.finished = finished;
        return this;
    }

    public D getData() {
        return data;
    }

    public Form getForm() {
        return form;
    }

    public FlowResponse<D> setForm(Form form) {
        this.form = form;
        return this;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
