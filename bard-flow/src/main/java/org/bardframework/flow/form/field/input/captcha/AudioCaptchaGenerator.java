package org.bardframework.flow.form.field.input.captcha;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
public class AudioCaptchaGenerator {

    protected final Map<String, Map<String, List<byte[]>>> audios = new HashMap<>();
    protected final boolean enabled;

    public AudioCaptchaGenerator(@Value("${captcha.audio.enabled:false}") boolean enabled) throws Exception {
        this.enabled = enabled;
        if (!enabled) {
            return;
        }
        ClassLoader classLoader = getClass().getClassLoader();
        URL audioCaptchaDir = classLoader.getResource("audio_captcha");
        if (null == audioCaptchaDir) {
            log.warn("audio captcha is enabled, but dir is null");
            return;
        }
        File baseDir = new File(audioCaptchaDir.toURI());
        Files.find(baseDir.toPath(), 10, (path, basicFileAttributes) -> path.toFile().isFile())
                .forEach(path -> {
                    Path relativePath = baseDir.toPath().relativize(path);
                    relativePath = relativePath.getParent().getParent().relativize(relativePath);
                    String locale = relativePath.getParent().toString();
                    audios.putIfAbsent(locale, new HashMap<>());
                    String name = path.getFileName().toFile().getName().split("\\.")[0];
                    audios.get(locale).putIfAbsent(name, new ArrayList<>());
                    try {
                        audios.get(locale).get(name).add(Files.readAllBytes(path));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public byte[] generate(String text, String lang) throws UnsupportedAudioFileException, IOException {
        List<byte[]> captcha = new ArrayList<>();
        for (char c : text.toLowerCase().toCharArray()) {
            List<byte[]> variants = audios.get(lang).get(String.valueOf(c));
            captcha.add(variants.get(RandomUtils.secure().randomInt(0, variants.size())));
        }
        return this.mergeWaves(captcha);
    }

    @SneakyThrows
    public void generate(String text, HttpServletResponse httpResponse, String lang) {
        byte[] audio = this.generate(text, lang);
        httpResponse.setContentType("audio/wav");
        httpResponse.setContentLength(audio.length);
        try (ServletOutputStream outputStream = httpResponse.getOutputStream()) {
            outputStream.write(audio);
            outputStream.flush();
        }
    }

    protected byte[] mergeWaves(List<byte[]> waves) throws UnsupportedAudioFileException, IOException {
        List<AudioInputStream> audioStreams = new ArrayList<>();
        AudioFormat audioFormat = null;

        for (byte[] wave : waves) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(wave);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            audioStreams.add(audioInputStream);

            if (audioFormat == null) {
                audioFormat = audioInputStream.getFormat();
            } else if (!audioInputStream.getFormat().matches(audioFormat)) {
                throw new IllegalStateException("Audio format does not match");
            }
        }

        AudioInputStream appendedStream = audioStreams.getFirst();
        for (int i = 1; i < audioStreams.size(); i++) {
            appendedStream = new AudioInputStream(
                    new SequenceInputStream(appendedStream, audioStreams.get(i)),
                    audioFormat,
                    appendedStream.getFrameLength() + audioStreams.get(i).getFrameLength());
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            AudioSystem.write(appendedStream, AudioFileFormat.Type.WAVE, outputStream);
            return outputStream.toByteArray();
        }
    }
}
