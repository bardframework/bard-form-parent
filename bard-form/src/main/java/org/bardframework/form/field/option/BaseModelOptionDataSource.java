package org.bardframework.form.field.option;

import lombok.Getter;
import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.crud.api.base.BaseCriteria;
import org.bardframework.crud.api.base.BaseModel;
import org.bardframework.crud.api.base.BaseRepository;
import org.bardframework.form.model.SelectOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
public abstract class BaseModelOptionDataSource<M extends BaseModel<I>, C extends BaseCriteria<I>, R extends BaseRepository<M, C, I, U>, I, U> extends CachableOptionDataSource {

    private final R repository;

    public BaseModelOptionDataSource(R repository, long cacheExpirationMs) {
        super(cacheExpirationMs);
        this.repository = repository;
    }

    @Override
    protected List<SelectOption> loadOptions(Map<String, String> args, Locale locale) {
        U user = this.getUser();
        List<M> list = repository.get(this.getCriteria(user), null);
        List<SelectOption> options = new ArrayList<>();
        for (M model : list) {
            options.add(this.map(model));
        }
        return options;
    }

    protected C getCriteria(U user) {
        return ReflectionUtils.newInstance(ReflectionUtils.getGenericArgType(this.getClass(), 1));
    }

    protected SelectOption map(M model) {
        return new SelectOption(model.getId().toString(), this.getTitle(model), null);
    }

    protected abstract U getUser();

    protected abstract String getTitle(M model);

}
