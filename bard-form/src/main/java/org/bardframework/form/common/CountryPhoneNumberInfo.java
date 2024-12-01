package org.bardframework.form.common;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import java.util.Map;

/**
 * Enum representing country phone number information.
 * Each enum value contains the country name, country code, and minimum/maximum length of phone numbers.
 * The name of each enum value represents the country code in ISO 3166-1 alpha-2 format (e.g., VN for Vietnam, IR for Iran).
 */
@Getter
public enum CountryPhoneNumberInfo {
    AD("Andorra", 376, 6, 9),
    AE("United Arab Emirates", 971, 9, 9),
    AF("Afghanistan", 93, 9, 9),
    AL("Albania", 355, 9, 9),
    AM("Armenia", 374, 8, 8),
    AO("Angola", 244, 9, 9),
    AR("Argentina", 54, 10, 10),
    AT("Austria", 43, 10, 13),
    AU("Australia", 61, 9, 9),
    AZ("Azerbaijan", 994, 9, 9),
    BA("Bosnia and Herzegovina", 387, 8, 8),
    BD("Bangladesh", 880, 10, 10),
    BE("Belgium", 32, 9, 9),
    BF("Burkina Faso", 226, 8, 8),
    BG("Bulgaria", 359, 8, 9),
    BH("Bahrain", 973, 8, 8),
    BI("Burundi", 257, 8, 8),
    BJ("Benin", 229, 8, 8),
    BN("Brunei", 673, 7, 7),
    BO("Bolivia", 591, 8, 8),
    BR("Brazil", 55, 10, 11),
    BT("Bhutan", 975, 8, 8),
    BW("Botswana", 267, 7, 7),
    BY("Belarus", 375, 9, 9),
    BZ("Belize", 501, 7, 7),
    CD("Congo, Democratic Republic of", 243, 9, 9),
    CF("Central African Republic", 236, 8, 8),
    CG("Congo", 242, 9, 9),
    CH("Switzerland", 41, 9, 9),
    CI("Ivory Coast", 225, 8, 8),
    CL("Chile", 56, 9, 9),
    CM("Cameroon", 237, 9, 9),
    CN("China", 86, 11, 11),
    CO("Colombia", 57, 10, 10),
    CR("Costa Rica", 506, 8, 8),
    CU("Cuba", 53, 8, 8),
    CV("Cape Verde", 238, 7, 7),
    CY("Cyprus", 357, 8, 8),
    CZ("Czech Republic", 420, 9, 9),
    DE("Germany", 49, 9, 15),
    DJ("Djibouti", 253, 6, 6),
    DK("Denmark", 45, 8, 8),
    DZ("Algeria", 213, 9, 9),
    EC("Ecuador", 593, 9, 9),
    EE("Estonia", 372, 7, 9),
    EG("Egypt", 20, 10, 10),
    ER("Eritrea", 291, 7, 7),
    ES("Spain", 34, 9, 9),
    ET("Ethiopia", 251, 9, 9),
    FI("Finland", 358, 9, 12),
    FJ("Fiji", 679, 7, 7),
    FM("Micronesia", 691, 7, 7),
    FO("Faroe Islands", 298, 6, 6),
    FR("France", 33, 9, 9),
    GA("Gabon", 241, 7, 7),
    GB("United Kingdom", 44, 10, 10),
    GE("Georgia", 995, 9, 9),
    GH("Ghana", 233, 9, 9),
    GL("Greenland", 299, 6, 6),
    GM("Gambia", 220, 7, 7),
    GN("Guinea", 224, 9, 9),
    GQ("Equatorial Guinea", 240, 9, 9),
    GR("Greece", 30, 10, 10),
    GT("Guatemala", 502, 8, 8),
    GU("Guam", 1671, 10, 10),
    GW("Guinea-Bissau", 245, 7, 7),
    GY("Guyana", 592, 7, 7),
    HN("Honduras", 504, 8, 8),
    HR("Croatia", 385, 9, 9),
    HT("Haiti", 509, 8, 8),
    HU("Hungary", 36, 9, 9),
    ID("Indonesia", 62, 9, 12),
    IE("Ireland", 353, 9, 9),
    IL("Israel", 972, 9, 9),
    IN("India", 91, 10, 10),
    IQ("Iraq", 964, 10, 10),
    IR("Iran", 98, 10, 10),
    IS("Iceland", 354, 7, 7),
    IT("Italy", 39, 9, 10),
    JO("Jordan", 962, 9, 9),
    JP("Japan", 81, 10, 10),
    KE("Kenya", 254, 9, 9),
    KG("Kyrgyzstan", 996, 9, 9),
    KH("Cambodia", 855, 8, 8),
    KI("Kiribati", 686, 5, 5),
    KM("Comoros", 269, 7, 7),
    KP("Korea, North", 850, 9, 9),
    KR("Korea, South", 82, 9, 10),
    KW("Kuwait", 965, 8, 8),
    LA("Laos", 856, 9, 10),
    LB("Lebanon", 961, 7, 8),
    LI("Liechtenstein", 423, 7, 9),
    LK("Sri Lanka", 94, 9, 9),
    LR("Liberia", 231, 7, 7),
    LS("Lesotho", 266, 8, 8),
    LT("Lithuania", 370, 8, 8),
    LU("Luxembourg", 352, 8, 9),
    LV("Latvia", 371, 8, 8),
    LY("Libya", 218, 9, 9),
    MA("Morocco", 212, 9, 9),
    MC("Monaco", 377, 8, 9),
    MD("Moldova", 373, 8, 8),
    ME("Montenegro", 382, 8, 8),
    MG("Madagascar", 261, 9, 10),
    MH("Marshall Islands", 692, 7, 7),
    MK("North Macedonia", 389, 8, 8),
    ML("Mali", 223, 8, 8),
    MM("Myanmar", 95, 8, 9),
    MN("Mongolia", 976, 8, 8),
    MP("Northern Mariana Islands", 1670, 10, 10),
    MR("Mauritania", 222, 8, 8),
    MT("Malta", 356, 8, 8),
    MU("Mauritius", 230, 7, 7),
    MV("Maldives", 960, 7, 7),
    MW("Malawi", 265, 7, 9),
    MX("Mexico", 52, 10, 10),
    MY("Malaysia", 60, 9, 10),
    MZ("Mozambique", 258, 9, 9),
    NA("Namibia", 264, 9, 9),
    NE("Niger", 227, 8, 8),
    NG("Nigeria", 234, 10, 10),
    NI("Nicaragua", 505, 8, 8),
    NL("Netherlands", 31, 9, 9),
    NO("Norway", 47, 8, 8),
    NP("Nepal", 977, 10, 10),
    NR("Nauru", 674, 7, 7),
    NZ("New Zealand", 64, 9, 10),
    OM("Oman", 968, 8, 8),
    PA("Panama", 507, 8, 8),
    PE("Peru", 51, 9, 9),
    PG("Papua New Guinea", 675, 8, 8),
    PH("Philippines", 63, 9, 10),
    PK("Pakistan", 92, 10, 10),
    PL("Poland", 48, 9, 9),
    PS("Palestine", 970, 9, 9),
    PT("Portugal", 351, 9, 9),
    PW("Palau", 680, 7, 7),
    PY("Paraguay", 595, 9, 9),
    QA("Qatar", 974, 8, 8),
    RO("Romania", 40, 9, 9),
    RS("Serbia", 381, 8, 9),
    RU("Russia", 7, 10, 10),
    RW("Rwanda", 250, 9, 9),
    SA("Saudi Arabia", 966, 9, 9),
    SB("Solomon Islands", 677, 5, 7),
    SC("Seychelles", 248, 7, 7),
    SD("Sudan", 249, 9, 9),
    SE("Sweden", 46, 9, 9),
    SG("Singapore", 65, 8, 8),
    SI("Slovenia", 386, 8, 8),
    SK("Slovakia", 421, 9, 9),
    SL("Sierra Leone", 232, 8, 8),
    SM("San Marino", 378, 10, 10),
    SN("Senegal", 221, 9, 9),
    SO("Somalia", 252, 7, 8),
    SR("Suriname", 597, 6, 7),
    SS("South Sudan", 211, 9, 9),
    ST("Sao Tome and Principe", 239, 7, 7),
    SV("El Salvador", 503, 8, 8),
    SY("Syria", 963, 9, 9),
    SZ("Eswatini", 268, 9, 9),
    TD("Chad", 235, 8, 8),
    TG("Togo", 228, 8, 8),
    TH("Thailand", 66, 9, 9),
    TJ("Tajikistan", 992, 9, 9),
    TL("Timor-Leste", 670, 7, 8),
    TM("Turkmenistan", 993, 8, 8),
    TN("Tunisia", 216, 8, 8),
    TO("Tonga", 676, 5, 5),
    TR("Turkey", 90, 10, 10),
    TV("Tuvalu", 688, 5, 5),
    TW("Taiwan", 886, 9, 9),
    TZ("Tanzania", 255, 9, 9),
    UA("Ukraine", 380, 9, 9),
    UG("Uganda", 256, 9, 9),
    US("United States", 1, 10, 10),
    UY("Uruguay", 598, 8, 9),
    UZ("Uzbekistan", 998, 9, 9),
    VA("Vatican City", 379, 10, 10),
    VE("Venezuela", 58, 10, 10),
    VI_UK("British Virgin Islands", 1284, 10, 10),
    VI_US("U.S. Virgin Islands", 1340, 10, 10),
    VN("Vietnam", 84, 9, 10),
    VU("Vanuatu", 678, 5, 7),
    WS("Samoa", 685, 5, 7),
    XK("Kosovo", 383, 9, 9),
    YE("Yemen", 967, 9, 9),
    ZA("South Africa", 27, 9, 9),
    ZM("Zambia", 260, 9, 9),
    ZW("Zimbabwe", 263, 9, 9),
    ;

    private static final Map<Integer, CountryPhoneNumberInfo> COUNTRY_CODE_CACHE = EnumUtils.getEnumMap(CountryPhoneNumberInfo.class, CountryPhoneNumberInfo::getCountryCode);
    private static final Map<String, CountryPhoneNumberInfo> ISO_3166_CACHE = EnumUtils.getEnumMap(CountryPhoneNumberInfo.class, CountryPhoneNumberInfo::name);

    /**
     * The name of the country.
     */
    private final String countryName;
    /**
     * The phone code of the country.
     */
    private final int countryCode;
    /**
     * The minimum length of phone numbers for the country.
     */
    private final int minLength;
    /**
     * The maximum length of phone numbers for the country.
     */
    private final int maxLength;

    /**
     * Constructor for CountryPhoneNumberInfo enum.
     *
     * @param countryName The name of the country.
     * @param countryCode The phone code of the country.
     * @param minLength   The minimum length of phone numbers for the country.
     * @param maxLength   The maximum length of phone numbers for the country.
     */
    CountryPhoneNumberInfo(String countryName, int countryCode, int minLength, int maxLength) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * اطلاعات شماره تلفن کشور را با استفاده از کد کشور بازیابی می‌کند.
     *
     * @param countryCode کد کشور برای جستجو.
     * @return شیء CountryPhoneNumberInfo مرتبط با کد کشور داده شده، یا null اگر یافت نشود.
     */
    public static CountryPhoneNumberInfo getByCountryCode(int countryCode) {
        return COUNTRY_CODE_CACHE.get(countryCode);
    }

    /**
     * اطلاعات شماره تلفن کشور را با استفاده از کد کشور ISO 3166-1 alpha-2 بازیابی می‌کند.
     *
     * @param countryCode کد کشور ISO 3166-1 alpha-2 برای جستجو.
     * @return شیء CountryPhoneNumberInfo مرتبط با کد کشور داده شده، یا null اگر یافت نشود.
     */
    public static CountryPhoneNumberInfo getByIso3166Code(String countryCode) {
        return ISO_3166_CACHE.get(countryCode);
    }
}
