package com.anrei0000.robot_server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import com.neilalexander.jnacl.crypto.NaCl;

public class Server {
	private static String publickey = "0cba66066896ffb51e92bc3c36ffa627c2493770d9b0b4368a2466c801b0184e";
	private static String privatekey = "176970653848be5242059e2308dfa30245b93a13befd2ebd09f09b971273b728";
	private static byte[] nonce = new byte[24];
	private static byte[] nonce2 = new byte[24];

	public static void main(String[] args) throws IOException {

		System.out.println("Server listening");

		ServerSocket clientServer = new ServerSocket(6789);
		Socket clientSocket = clientServer.accept();
		BufferedReader in_from_client = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter out_to_client = new PrintWriter(
				clientSocket.getOutputStream(), true);

//		Socket serverSocket = new Socket("127.0.0.1", 6790);
//		BufferedReader in_from_local = new BufferedReader(
//				new InputStreamReader(serverSocket.getInputStream()));
//		PrintWriter out_to_robot = new PrintWriter(
//				serverSocket.getOutputStream(), true);

		// main server loop
		while (true) {
			String robotText = "";
			try {
//				String encrypted_string = in_from_client.readLine();
//				System.out.println("received from client: " + encrypted_string);
//				String decrypted_string = decrypt(encrypted_string);
//				out_to_robot.println(decrypted_string);
//				System.out.println("sent to local: " + decrypted_string);
//				String robot_reply = in_from_local.readLine();
//				System.out.println("received from local: " + robot_reply);
//				String encrypted_robot_reply = encrypt(robot_reply);
//				out_to_client.println(encrypted_robot_reply);
//				System.out.println("sent to client: " + encrypted_robot_reply);

				String uncrypted_string = in_from_client.readLine();
				System.out.println("uncrypted from client: " + uncrypted_string);
				
				String uncrypted_robot_reply = "ack";
				out_to_client.println(uncrypted_robot_reply);
				System.out.println("uncrypted sent to client: " + uncrypted_robot_reply);
				

			} catch (IOException e) {
				// error ("System: " + "Connection to server lost!");
				System.exit(1);
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.exit(1);
				break;
			}

		}
	}

	private static java.lang.String encrypt(java.lang.String message)
			throws Exception {
		// get string bytes
		byte[] in = message.getBytes();

		NaCl test = new NaCl(privatekey, publickey);

		// encrypt bytes
		byte[] foo1 = test.encrypt(in, nonce);

		// make a new string with the bytes
		String a = Arrays.toString(foo1);

		return a;
	}

	private static String decrypt(String encrypted_string) throws Exception {

		NaCl salt = new NaCl(privatekey, publickey);
		String[] encrypted_bytes_string = encrypted_string.substring(1,
				encrypted_string.length() - 1).split(",");
		
		byte[] encrypted_bytes = new byte[encrypted_bytes_string.length];

		for (int i = 0, len = encrypted_bytes.length; i < len; i++) {
			encrypted_bytes[i] = Byte.parseByte(encrypted_bytes_string[i]
					.trim());
		}

		byte[] decrypted_bytes = salt.decrypt(encrypted_bytes, nonce2);

		String decrypted_string = new String(decrypted_bytes);

		return decrypted_string;
	}

	public static String openFileToString(byte[] _bytes) {
		String file_string = "";

		for (int i = 0; i < _bytes.length; i++) {
			file_string += (char) _bytes[i];
		}

		return file_string;
	}

	public static void crypto(String message) throws Exception {

		NaCl test = new NaCl(privatekey, publickey);
		byte[] in = message.getBytes();

		byte[] foo = test.encrypt(in, nonce);
		byte[] bar = test.decrypt(foo, nonce);

		System.out.println("in:  " + NaCl.asHex(in));
		System.out.println("encoded: " + NaCl.asHex(foo));
		System.out.println("decoded: " + NaCl.asHex(bar));
	}

}