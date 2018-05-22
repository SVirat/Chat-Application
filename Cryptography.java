import java.util.ArrayList;
import java.util.Random;

public class Cryptography {

	//default values for keys
	private static int a = 93;
	private static int b = 50;
	private final static int NUMBER_OF_CHARACTERS = 224;

	/**
	 * Used to start a cryptographic device and set up keys of the device.
	 */
	public Cryptography() {
		setUpKeys();
	}

	/**
	 * Sets up the keys for the device. It makes sure that the message is
	 * deterministic by making the keys reversible.
	 */
	private void setUpKeys() {
		Random randomizer = new Random();
		ArrayList<Integer> coprimes = getCoPrimes(NUMBER_OF_CHARACTERS);
		Cryptography.a = coprimes.get(randomizer.nextInt(coprimes.size()));
		Cryptography.b = randomizer.nextInt(NUMBER_OF_CHARACTERS);
	}

	/**
	 * Encrypts the message.
	 * @param message: to be encrypted
	 * @return the encrypted message
	 */
	public String encrypt(String message) {
		char [] values = message.toCharArray();
		int [] encrypted = new int[values.length];
		for(int i = 0; i < encrypted.length; ++i) {
			encrypted[i] = ((a * (int)(values[i]) + b) % NUMBER_OF_CHARACTERS);
			values[i] = (char)encrypted[i];
		}
		for(int i = 0; i < encrypted.length; ++i) {
			values[i] = (char)encrypted[i];
		}
		String encryptedMessage = String.valueOf(values);
		return encryptedMessage;
	}

	/**
	 * Decrypts the message.
	 * @param message: to be decrypted
	 * @return the decrypted message
	 */
	public String decrypt(String message) {
		char [] values = message.toCharArray();
		int [] decrypted = new int[values.length];
		int aInverse = modInverse(a, NUMBER_OF_CHARACTERS);
		for(int i = 0; i < decrypted.length; ++i) {
			int overall = aInverse * (values[i] - b);
			int modded = overall % NUMBER_OF_CHARACTERS;
			//handling case when mod is negative
			if(overall < 0) {
				overall = -1 * overall;
				modded = overall % NUMBER_OF_CHARACTERS;
				modded = NUMBER_OF_CHARACTERS - modded;
			}
			decrypted[i] = (char) ((modded));
		}
		for(int i = 0; i < values.length; ++i) {
			values[i] = (char) decrypted[i];
		}
		String decryptedMessage = String.valueOf(values);
		return decryptedMessage;
	}

	/**
	 * Gets the modular inverse of a number with respect to another number.
	 * @param a: the number to get modular inverse of
	 * @param n: the number of ASCII characters
	 * @return modular inverse of a with respect to n
	 */
	private int modInverse(int a, int n) {
		a = a % n;
		for (int x = 1; x < n; x++)
			if ((a * x) % n == 1)
				return x;
		System.err.println("Error: " + a + " is not invertible with " + n + ".");
		return -1;
	}

	/**
	 * Gets a list of coprimes for some number
	 * @param n: the number to get the coprimes of
	 * @return the list of coprimes to n
	 */
	private ArrayList<Integer> getCoPrimes(int n) {
		ArrayList<Integer> coprimes = new ArrayList<Integer>();
		for(int i = 2; i < n; ++i) {
			if(gcd(i, n) == 1) {
				coprimes.add(i);
			}
		}
		return coprimes;
	}

	/**
	 * Recursively calculates the gcd of two numbers
	 * @param a: number 1
	 * @param b: number 2
	 * @return gcd of a and b
	 */
	private int gcd(int a, int b) {
		if (b==0) return a;
		return gcd(b,a%b);
	}
	
}