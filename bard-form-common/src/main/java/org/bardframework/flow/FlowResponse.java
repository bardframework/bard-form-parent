package org.bardframework.flow;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.BardForm;

@Slf4j
@Getter
@ToString
public class FlowResponse {
    private BardForm form;
    private Boolean finished;
    private int steps;
    private int current;

    public FlowResponse setFinished(Boolean finished) {
        this.finished = finished;
        return this;
    }

    public FlowResponse setForm(BardForm form) {
        this.form = form;
        return this;
    }

    public FlowResponse setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    public FlowResponse setCurrent(int current) {
        this.current = current;
        return this;
    }
}
