package org.bardframework.flow.form.field.input.captcha;

import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.nio.file.Files;

class AudioCaptchaGeneratorTest {

    public static void main(String[] args) throws Exception {
        int captcha = RandomUtils.secure().randomInt(1000, 999999);
        AudioCaptchaGenerator reader = new AudioCaptchaGenerator(true);
        Files.write(new File("fa.wav").toPath(), reader.generate(String.valueOf(captcha), "fa"));
        Files.write(new File("en.wav").toPath(), reader.generate("salam", "en"));
    }
}