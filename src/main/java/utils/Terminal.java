package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class Terminal {
	
	public static String WINDOWS = "WINDOWS";
	public static String LINUX   = "LINUX";
	public static String MAC_OS = "MAC_OS";
			
	
	public static ArrayList<String> execute(String command) throws IOException {
		
		if (getOSPlatformInfo().equals(LINUX)) {			
			String[] args = {"bash", "-c", command};
			return Terminal.handler(args);
		}
		
		if (getOSPlatformInfo().equals(MAC_OS)) {			
			String[] args = {"bash", "-c",command};
			return Terminal.handler(args);
		}
		
		if (getOSPlatformInfo().equals(WINDOWS)) {			
			String[] args = {"cmd.exe", "/c", command};
			return Terminal.handler(args);
		}
		
		return null;
	}
	
	public static String getOSPlatformInfo() {
		
		if (SystemUtils.IS_OS_LINUX) {			
		     return LINUX;
		}
		
		if (SystemUtils.IS_OS_MAC){
			return MAC_OS;
		}
		
		if (SystemUtils.IS_OS_WINDOWS) {
			return WINDOWS;
		}
		
		return null;
	}
	
	public static ArrayList<String> handler(String[] args) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(args);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        ArrayList<String> output = new ArrayList<String>();
        while (true) {
            String line = r.readLine();
            if (line != null) { 
             	output.add(line);
            }else{
            	break;
            }
        }
        
        System.out.println(output);
		return output;
	}	
	
	public static void execRemoteCommand(String host, Integer port, String user, String password, String command) throws Exception{
		
	    Properties config = new java.util.Properties(); 
	    config.put("StrictHostKeyChecking", "no");
	    JSch jsch = new JSch();
	    Session session=jsch.getSession(user, host, port);
	    session.setPassword(password);
	    session.setConfig(config);
	    session.connect();
		
		Channel cmdChannel = session.openChannel("exec");
		((ChannelExec)cmdChannel).setCommand(command);
	    cmdChannel.setInputStream(null);
	    ((ChannelExec)cmdChannel).setErrStream(System.err);
	     InputStream in=cmdChannel.getInputStream();
	     cmdChannel.connect();
	     byte[] tmp=new byte[1024];
	     
	     while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            System.out.print(new String(tmp, 0, i));
	          }
	          if(cmdChannel.isClosed()){
	            System.out.println("exit-status: "+cmdChannel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	           
	    session.disconnect();
	
	     }
		
	}
	
	public static Session createSession(String user, String password,String host, int port) throws JSchException {
		
	    JSch jsch = new JSch();

	    Properties config = new java.util.Properties();
	    config.put("StrictHostKeyChecking", "no");

	    Session session = jsch.getSession(user, host, port);
	    session.setPassword(password);
	    session.setConfig(config);

	    if (session.isConnected()) {
	        session.disconnect();
	    }

	    session.connect();

	    return session;
    }
	
	public static void copyRemoteToLocal(String host, Integer port, String user, String password, String from, String to) throws JSchException, IOException {
		Session session = Terminal.createSession(user, password, host, port);
		
        String prefix = null;

        if (new File(to).isDirectory()) {
            prefix = to + File.separator;
        }

        // exec 'scp -f rfile' remotely
        String command = "scp -f " + from;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] buf = new byte[1024];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while (true) {
            int c = checkAck(in);
            if (c != 'C') {
                break;
            }

            // read '0644 '
            in.read(buf, 0, 5);

            long filesize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ') break;
                filesize = filesize * 10L + (long) (buf[0] - '0');
            }

            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }

            System.out.println("file-size=" + filesize + ", file=" + file);

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            // read a content of lfile
            FileOutputStream fos = new FileOutputStream(prefix == null ? to : prefix + file);
            int foo;
            while (true) {
                if (buf.length < filesize) foo = buf.length;
                else foo = (int) filesize;
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                fos.write(buf, 0, foo);
                filesize -= foo;
                if (filesize == 0L) break;
            }
            
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            if (fos != null) fos.close();
            
        }

        channel.disconnect();
        session.disconnect();
    }
	
	public static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }
	
	public static void copyLocalToRemote(String host, Integer port, String user, String password, String from, String to) throws JSchException, IOException {
		Session session = Terminal.createSession(user, password, host, port);
		
        boolean ptimestamp = true;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + to;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        File _lfile = new File(from);

        if (ptimestamp) {
            command = "T" + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (from.lastIndexOf('/') > 0) {
            command += from.substring(from.lastIndexOf('/') + 1);
        } else {
            command += from;
        }

        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        // send a content of lfile
        FileInputStream fis = new FileInputStream(from);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
        }

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        try {
            if (fis != null) {
            	fis.close();
            }
        } catch (Exception ex) {}

        channel.disconnect();
        session.disconnect();
    }
	
}
