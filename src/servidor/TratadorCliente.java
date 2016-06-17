package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

		try {
			InputStream in = cliente.getInputStream();
			
			List<Byte> bytes = new ArrayList<Byte>();
			int count = 0;
			while (true) {
				int b = in.read();
				
				if((byte)b == Byte.MAX_VALUE) {
					count++;
				} else count = 0;
				if(count > 5) continue;
				bytes.add((byte)b);
				if (count == 5) {
					Byte[] bytesarr = new Byte[bytes.size()];
					byte[] bytesarr2 = new byte[bytes.size()];
					bytes.toArray(bytesarr);
					bytes.clear();
					for(int i = 0; i < bytesarr.length; i++) {
						bytesarr2[i] = bytesarr[i];
					}
					distribuiMensagem(bytesarr2);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void distribuiMensagem(byte[] bytesarr) {
		System.out.println("distribuindo mensagem");
		for (Socket cli : lista) {
			
			if(cli == this.cliente) continue;
			
			PrintStream print;
			try {
				print = new PrintStream(cli.getOutputStream());
				print.write(bytesarr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
	}
}
