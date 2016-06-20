package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TratadorCliente implements Runnable {
	private static int CODE_SIZE = 20;
	
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

				if ((byte) b == Byte.MAX_VALUE) {
					count++;
				} else
					count = 0;
				
				if (count > CODE_SIZE || b < 0) continue;
				else bytes.add((byte) b);
				
				if (count == CODE_SIZE) {
					Byte[] messageBytes = new Byte[bytes.size()];
					byte[] fullmessageBytes = new byte[bytes.size()];
					bytes.toArray(messageBytes);
					bytes.clear();
					for (int i = 0; i < messageBytes.length; i++) {
						fullmessageBytes[i] = messageBytes[i];
					}
					distribuiMensagem(fullmessageBytes);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void distribuiMensagem(byte[] bytesarr) throws IOException {
		System.out.println("Distribuindo mensagens");
		for (Socket cli : lista) {

			 if (cli == this.cliente)
				 continue;

			try {
				OutputStream os = cli.getOutputStream();
				os.write(bytesarr, 0, bytesarr.length);
				//print.write(bytesarr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
