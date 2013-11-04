package onionisi.judy.tool;
import org.zeromq.ZMQ;

public class JudyZmq {

	public static String Query(String msg){
		ZMQ.Context context = ZMQ.context(1);

		//  Socket to talk to server
		ZMQ.Socket socket = context.socket(ZMQ.REQ);
		socket.connect ("tcp://localhost:5555");

		socket.send(msg.getBytes(), 0);

		byte[] reply = socket.recv(0);

		socket.close();
		context.term();

		return new String(reply);
	}
}
