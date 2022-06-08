package org.bardframework.form.field.option;

import org.bardframework.form.common.SelectOption;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberOptionDataSource extends ConstantDataSource {

    public NumberOptionDataSource(int from, int to) {
        super(IntStream.range(from, to + 1)
                .mapToObj(number -> new SelectOption(String.valueOf(number), String.valueOf(number), null))
                .collect(Collectors.toList()));
    }
}
