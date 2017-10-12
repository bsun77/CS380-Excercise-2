import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;

public class Ex2Client {

	public Ex2Client(){

	}

	public static void main(String[]args) throws Exception{
		try (Socket socket = new Socket("18.221.102.182", 38102)) {
			System.out.println("Connected to server.");

			InputStream is = socket.getInputStream();
			Integer a = 0;
			byte g;
			CRC32 crc = new CRC32();
			
			System.out.println("Received bytes: ");
			
			for(int i = 0; i < 100; i++){
				a = is.read()*16;
				a += is.read();
				g = a.byteValue();
				crc.update(g);
			}

			System.out.println("Generated CRC32: " +crc.getValue());

			OutputStream os = socket.getOutputStream();
			
			Long value = crc.getValue();
			byte[] stuff = new byte[4];  

			for(int i = 3; i >= 0; i--){
				stuff[i] = (byte) ((value)&0xFF);
				value = (Long)(value/0x100);
				System.out.println(stuff[i]);
			}
			
			os.write(stuff);
			if(is.read() == 1){
				System.out.println("Response good.");
			} else {
				System.out.println("Response bad.");
			}
			System.out.println("Disconnected from server");
		}
	}

}
