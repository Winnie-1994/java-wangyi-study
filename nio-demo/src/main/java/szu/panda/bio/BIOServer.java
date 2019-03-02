package szu.panda.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class BIOServer {

	private static Charset charset = Charset.forName("UTF-8");

	public static void main(String[] args) {
		int port = 9010;

		try (ServerSocket ss = new ServerSocket(port);) {
			while (true) {
				try {
					// 接收连接
				Socket s = ss.accept();

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(s.getInputStream(), charset));

					String mess = null;
					// 接收数据
					while ((mess = reader.readLine()) != null) {
						System.out.println(mess);
					}
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
