package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

	private static ArrayList<Socket> socketsCliente;
	private static ServerSocket servidor;

	public static void main(String[] args) throws IOException {

		servidor = new ServerSocket(5001);
		System.out.println("Porta 5001 aberta");
		socketsCliente = new ArrayList<Socket>();

		Socket cliente;

		while (true) {
			cliente = servidor.accept();
			socketsCliente.add(cliente);

			TratadorCliente tratador = new TratadorCliente(cliente,socketsCliente);

			Thread t = new Thread(tratador);
			t.start();
		}
	}

}
