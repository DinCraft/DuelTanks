import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

public class ClientThread extends Thread {
    public int port;
    public DataIO dio;
    public String your_ip = "";
	public String another_ip = "";

    @Override
    public void run() {
        while(true){
            runClient();
        }
    }

    public void runClient() {
        try {
			byte[] rm = receiveMessage();
			if (rm!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(rm);
				ObjectInput in = new ObjectInputStream(bis);
				try {
					dio = (DataIO) in.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (Game2.data!=null) {
            sendMessage(Game2.data);
        }
    }

    public void sendMessage(byte[] data){
        try {
            DatagramPacket dp = new DatagramPacket(data, data.length);
            DatagramSocket ds = new DatagramSocket();
            ds.connect(new InetSocketAddress(another_ip, port));
            try {
                ds.send(dp);
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                ds.disconnect();
                ds.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public byte[] receiveMessage(){
        DatagramPacket dp = null;
        try {
            byte[] data = new byte[4096];
            DatagramSocket ds = new DatagramSocket(new InetSocketAddress(your_ip, port));
            ds.setSoTimeout(50);
            dp = new DatagramPacket(data, data.length);
            try {
                ds.receive(dp);
                ds.close();
            } catch (SocketTimeoutException e) {
                //e.printStackTrace();
				return null;
            } finally{
                ds.disconnect();
                ds.close();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } 
        if (dp!=null) {
            return dp.getData();
        }else{
            return null;
        }
	}
}