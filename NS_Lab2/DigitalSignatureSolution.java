import javax.xml.bind.DatatypeConverter;
import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;


public class DigitalSignatureSolution {

    public static void main(String[] args) throws Exception {
//Read the text file and save to String data
            String fileName = "largeSize.txt";
            String data = "";
            String line;
            BufferedReader bufferedReader = new BufferedReader( new FileReader(fileName));
            while((line= bufferedReader.readLine())!=null){
                data = data +"\n" + line;
            }
            System.out.println("Original content: "+ data);

//TODO: generate a RSA keypair, initialize as 1024 bits, get public key and private key from this keypair.
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();          

//TODO: Calculate message digest, using MD5 hash function
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data.getBytes());

//TODO: print the length of output digest byte[], compare the length of file smallSize.txt and largeSize.txt
            System.out.println("byte[] Length of "+fileName+" is:"+digest.length);
            
//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as encrypt mode, use PRIVATE key.
            Cipher ecipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, privateKey);

//TODO: encrypt digest message
            byte[] encryptedBytes = ecipher.doFinal(digest);
            
//TODO: print the encrypted message (in base64format String using DatatypeConverter) 
            String base64format = DatatypeConverter.printBase64Binary(encryptedBytes);
            System.out.println("Encrypted message is: ");
            System.out.println(base64format);
            
//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key. 
            Cipher de_cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            de_cipher.init(Cipher.DECRYPT_MODE, publicKey);

//TODO: decrypt message
            byte[] decryptedBytes = de_cipher.doFinal(encryptedBytes);

//TODO: print the decrypted message (in base64format String using DatatypeConverter), compare with origin digest 
            String decryptedString = DatatypeConverter.printBase64Binary(decryptedBytes);
            System.out.println("Decrypted Content: "+decryptedString);
            System.out.println("COMPARISION: SIMILIAR?: "+String.valueOf(decryptedString.equals(data)));

    }

}
