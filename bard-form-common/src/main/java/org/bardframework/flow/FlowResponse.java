package org.bardframework.flow;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.BardForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class FlowResponse {
    private String id;
    private BardForm form;
    private Boolean finished;
    private Integer resetDelaySeconds;
    private int steps;
    private int current;
    private Map<String, String> fieldErrors = new HashMap<>();
    private List<String> errors = new ArrayList<>();

    public FlowResponse finished() {
        this.finished = true;
        return this;
    }

    public FlowResponse setResetDelaySeconds(Integer resetDelaySeconds) {
        this.resetDelaySeconds = resetDelaySeconds;
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
