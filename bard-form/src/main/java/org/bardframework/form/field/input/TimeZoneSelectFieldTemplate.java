package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import lombok.SneakyThrows;
import org.bardframework.commons.web.utils.ResourceUtils;
import org.bardframework.form.common.Country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeZoneSelectFieldTemplate extends CountrySelectFieldTemplate {

    protected final Map<String, Country> countriesMap = new HashMap<>();
    protected final Map<String, Country> timeZonesMap = new HashMap<>();
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    protected TimeZoneSelectFieldTemplate(String name) {
        super(name);
        CollectionLikeType listType = objectMapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, Country.class);
        List<Country> countries = objectMapper.readValue(ResourceUtils.getResource("classpath:countries.json").getContentAsByteArray(), listType);
        for (Country country : countries) {
            countriesMap.put(country.getAlpha2Code(), country);
            for (String timeZone : country.getTimeZones()) {
                timeZonesMap.put(timeZone, country);
            }
        }
    }

    @Override
    protected String getCountryCode(String value) {
        Country country = timeZonesMap.get(value);
        return null == country ? null : country.getAlpha2Code();
    }

    @Override
    protected TimeZoneSelectField getEmptyField() {
        return new TimeZoneSelectField();
    }

}
