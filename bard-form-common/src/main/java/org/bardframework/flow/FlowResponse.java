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

    public void setFinished(Boolean finished) {
        this.finished = finished;
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
