package org.bardframework.flow.form.field.input.otp.time;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base32;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

class TotpServiceTest {
    private static final TotpService OTP = new TotpService();

    private static String getQRCodeURL(String issuer, String user, String secret) throws Exception {
        String uri = OTP.getUri(issuer, user, secret);
        generateQRCodeImage(uri, 400, 400, "qr.png");
        return "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=" + OTP.getUri(issuer, user, secret);
    }

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) throws Exception {
        Base32 base32 = new Base32();
        final byte[] secret = base32.decode("4KBGOCGY4D4X2MNWCWK6H37ESILBEZGZWFXNGLGO6S3JWRHOO7TINEMZFEZNL5BW"); //totp.generateSecret();
        String secretBase32 = base32.encodeAsString(secret);
        System.out.println("Secret:\t" + secretBase32);
        System.out.println("QR Code: " + TotpServiceTest.getQRCodeURL("sso.dejh.ir", "Vahid Zafari (va.zafari@gmail.com)", secretBase32));
        try (Scanner scanner = new Scanner(System.in)) {
            int choice = 0;
            while (choice != -1) {
                System.out.print("Enter OTP Code: ");
                if (OTP.verify(scanner.next(), secret)) {
                    System.out.println("Verification successful.");
                } else {
                    System.err.println("Invalid code.");
                }
                System.out.println("Actions:\n[0] Continue, [-1] Exit: ");
                choice = scanner.nextInt();
            }
        }
    }
}