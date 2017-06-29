package izzi.connect.controls;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES 
{
private static String algorithm = "AES";
private static byte[] keyValue=new byte[] 
{ 'I', 'Z', 'Z', 'I', 'm', 'o', 'b', 'i', 'l', 'e', 'S', '3', 'C', 'r', '3', '7' };

        // Performs Encryption
        public static String encrypt(String plainText) throws Exception 
        {
                Key key = generateKey();
                Cipher chiper = Cipher.getInstance(algorithm);
                chiper.init(Cipher.ENCRYPT_MODE, key);
                byte[] encVal = chiper.doFinal(plainText.getBytes());
                String encryptedValue = Base64.encodeToString(encVal,Base64.DEFAULT);
                return encryptedValue;
        }

        // Performs decryption
        public static String decrypt(String encryptedText) throws Exception 
        {
                // generate key 
                Key key = generateKey();
                Cipher chiper = Cipher.getInstance(algorithm);
                chiper.init(Cipher.DECRYPT_MODE, key);
            if(encryptedText==null)
                return "";
                byte[] decordedValue = Base64.decode(encryptedText, Base64.DEFAULT);
                byte[] decValue = chiper.doFinal(decordedValue);
                String decryptedValue = new String(decValue , "UTF-8");
                return decryptedValue;
        }

//generateKey() is used to generate a secret key for AES algorithm
        private static Key generateKey() throws Exception 
        {
                Key key = new SecretKeySpec(keyValue, algorithm);
                return key;
        }
}
