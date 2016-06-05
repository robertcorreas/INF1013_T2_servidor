package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TratadorCliente implements Runnable {

	private Socket cliente;
	private ArrayList<Socket> lista;

	public TratadorCliente(Socket cliente, ArrayList<Socket> lista) {
		this.cliente = cliente;
		this.lista = lista;
	}

	@Override
	public void run() {

		System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());

		Scanner in;
		try {
			in = new Scanner(cliente.getInputStream());
			while (in.hasNextLine()) {
				distribuiMensagem(in.nextLine());
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void distribuiMensagem(String mensagem) throws IOException {
		for (Socket cli : lista) {
			
			if(cli == this.cliente) continue;
			
			PrintStream print = new PrintStream(cli.getOutputStream());
			
			print.println(mensagem);
		} 
	}
}
