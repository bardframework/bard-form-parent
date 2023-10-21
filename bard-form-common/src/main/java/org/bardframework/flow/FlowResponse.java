package org.bardframework.flow;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.BardForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@ToString
public class FlowResponse {
    private BardForm form;
    private Boolean finished;
    private Boolean reset;
    private int steps;
    private int current;
    private Map<String, String> fieldErrors = new HashMap<>();
    private List<String> errors = new ArrayList<>();

    public FlowResponse setFinished(Boolean finished) {
        this.finished = finished;
        return this;
    }

    public FlowResponse setReset(Boolean reset) {
        this.reset = reset;
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
