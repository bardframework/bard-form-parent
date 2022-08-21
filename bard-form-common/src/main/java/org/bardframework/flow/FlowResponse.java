package org.bardframework.flow;

import org.bardframework.form.BardForm;

public class FlowResponse {
    private BardForm form;
    private Boolean finished;
    private int steps;
    private int current;

    public Boolean getFinished() {
        return finished;
    }

    public FlowResponse setFinished(Boolean finished) {
        this.finished = finished;
        return this;
    }

    public BardForm getForm() {
        return form;
    }

    public FlowResponse setForm(BardForm form) {
        this.form = form;
        return this;
    }

    public int getSteps() {
        return steps;
    }

    public FlowResponse setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    public int getCurrent() {
        return current;
    }

    public FlowResponse setCurrent(int current) {
        this.current = current;
        return this;
    }
}
