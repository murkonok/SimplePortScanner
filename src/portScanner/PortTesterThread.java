package portScanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

public class PortTesterThread implements Runnable {
	String address;
	int min;
	int max;
	InputStream in;
	OutputStream out;
	static byte[] b;
	int count;
	
	public PortTesterThread(String address, int min, int max) {
		this.address = address;
		this.min = min;
		this.max = max;
	}
	
	public ArrayList<Integer> test() throws InterruptedException {
		ArrayList<Integer> openPorts = new ArrayList<>();
		for(count = min;count<max;count++) {
			try {
				Socket socket = new Socket(address, count);
				out = socket.getOutputStream();
				in = socket.getInputStream();
				out.write((byte) '1');
				Thread.sleep(1000);
				b = new byte[256];
				int readData = in.read(b);
				socket.close();
				if(readData != -1) {
					openPorts.add(count);
				}
			} catch (IOException e) {
				
			}
		}
		return openPorts;
		
	}
	
	@Override
	public void run() {
		ArrayList<Integer> port = null;
		try {
			port = test();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<Integer> empty = new ArrayList<>();
		if (!port.equals(empty)) {
			System.out.println(port);
			String banner = null;
			try {
				banner = new String(b, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println(banner);
			
			File file = new File("ports.txt");
			System.out.println("File is saved at: " + file.getAbsolutePath());
			try {
				file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write(port.toString());
				out.write(banner);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
	}
		
	
}
