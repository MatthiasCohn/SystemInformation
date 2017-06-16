/**
 * 
 */
package sysInf;

import java.io.File;
import java.util.Map;
import java.net.*;

/**
 * @author Cohn, Matthias
 * Ermittelt wesentliche Systemeigenschaften und gibt diese zurück
 * @version 2017-06-16
 */
public class SysInf {
	private static Map<String, String> env;
	private static double FSrootCap, FSrootFree;
	private static String sInet4, sInet6, sMAC;
	

	public SysInf() {
		loadSysInf();
	}

	/**
	 * @param args
	 * Mit dem Erstellen eines PC-Objektes werden alle relevanten Informationen aus dem 
	 * System gelesen
	 */
	private static void loadSysInf(){
		env = System.getenv();
		File[] roots=File.listRoots();
		for (File root : roots) {
			if (root.getAbsolutePath().contains(env.get("HOMEDRIVE"))) {
				//System.out.println("File system root: " + root.getAbsolutePath());
				FSrootCap=Double.valueOf(root.getTotalSpace());
				FSrootFree=Double.valueOf(root.getFreeSpace());
				//System.out.println("Usable space (bytes): " + root.getUsableSpace());
			}
		}
		loadNetworkInf();
	}
	
	/**
	 * Netzwerkinformationen der Standardverbindung wird ausgelesen
	 */
	private static void loadNetworkInf(){
		sInet4=null;
		sInet6=null;
		try {
			sInet4 = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			sInet4 = "127.0.0.1";
		}
		try {
			InetAddress[] Inet=Inet6Address.getAllByName(Inet6Address.getLocalHost().getHostName());
			/*
			int i=0;
			for (i=0; i<=Inet.length-1;i++){
				System.out.println(Inet[i].getHostAddress());
				//if (Inet[i].==Inet6Address)
			}
			*/
			sInet6 =Inet[Inet.length-1].getHostAddress();
			 
					//Inet6Address.getByAddress(Inet6Address.getLocalHost().getHostName(),
					//addr, NetworkInterface.getByName("le0");
					//Inet6Address.getLocalHost().getHostAddress().toString(); 
					//.getLocalHost().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			sInet6 = "::1";
			e.printStackTrace();
		}
		try {
				sMAC = getMacAddress(Inet4Address.getLocalHost(),Inet6Address.getLocalHost());			
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			sMAC = "00:00:00:00:00:00";
			e.printStackTrace();
		}
	}
	
/**
 * Ermittelt die MAC-Adresse der Standardverbindung
 * @return String: MAC-Addresse
 * @throws SocketException
 * @link http://openbook.rheinwerk-verlag.de/java7/1507_11_013.html#dodtpa83dc14c-0485-4fd6-ab1c-8c63242651df
 */
private static String getMacAddress(InetAddress addr4, InetAddress addr6) throws SocketException
{
  String result = "";
  InetAddress addr = null;
  
  if (addr4==null){
	  if(addr6==null){
		  return "Keine Netzwerkprotokoll/ Netzwerkkarte gefunden.";
	  }else{
		  addr=addr6;
	  }
  }else{
	  addr=addr4;
  }
  //for ( NetworkInterface ni : Collections.list( NetworkInterface.getNetworkInterfaces() ) )
  //{
    byte[] hardwareAddress = NetworkInterface.getByInetAddress(addr).getHardwareAddress();//ni.getHardwareAddress();

    if ( hardwareAddress != null )
    {
      for ( int i = 0; i < hardwareAddress.length; i++ )
        result += String.format( (i==0?"":"-")+"%02X", hardwareAddress[i] );

      //return result;
    }
  //}

  return result;
}

/**
 * Gibt die MAC-Adresse der Standardverbindung zurück
 * @return String: MAC-Addresse
 */
public String getMAC() {
	return sMAC;
}

/**
 * Gibt die IPv4-Adresse der Standardverbindung zurück
 * @return String: IPv4-Addresse
 */
public String getIPv4Addr(){
	return sInet4;
}

/**
 *  Gibt die IPv4-Adresse der Standardverbindung zurück
 * @return String: IPv6-Addresse
 */
public String getIPv6Addr(){
	return sInet6;
}

	
	/**
	 * Gibt den Computernamen zurück
	 * @return String Computername
	 */
	public String getComputername(){
		return env.get("COMPUTERNAME");		
	}
	
	/**
	 * Gibt den (sofern vorhandenen) Domain-Server zürück
	 * @return String Logonserver
	 */
	public String getLogonServer(){
		return env.get("LOGONSERVER");
	}
	
	/**
	 * Gibt den aktuellen Nutzer(namen) zurück
	 * @return String Username
	 */
	public String getUserName(){
		return env.get("USERNAME");
	}
	
	/**
	 * Gibt das verwendete Betriebesystem (Typ) zurück
	 * @return String OS
	 */
	public String getOS(){
		return env.get("OS");
	}
	
	/**
	 * Gibt Informationen über die CPU zurück
	 * @return String CPU Information
	 */
	public String getProcessor(){
		return env.get("PROCESSOR_IDENTIFIER");
	}
	
	/**
	 * Gibt die Anzahl der Prozessoren (bzw. Kerne) zurück
	 * @return int Anz. Prozessorkerne
	 */
	public int getNumberOfCPU(){
		return Integer.parseInt(env.get("NUMBER_OF_PROCESSORS"));
	}
	
	/**
	 * Gibt den Pfad der Systempartition zurück
	 * @return String Pfad der Systempartition
	 */
	public String getSysDrive(){
		return env.get("HOMEDRIVE");
	}
	
	/**
	 * Gibt die (Gesamt-)Kapazität der Systempartition in MB zurück
	 * @return Double (Gesamt-)Kapazität der Systempartition in MB
	 */
	public Double getSysDriveCap(){
		return (Double) FSrootCap/Math.pow(1024, 2);
	}
	
	/**
	 * Gibt die Freie Kapazität der Systempartition in MB zurück
	 * @return Double Freie Kapazität der Systempartition in MB
	 */
	public Double getSysDriveFree(){
		return (Double) FSrootFree/Math.pow(1024, 2);
	}
	
	/**
	 * Gibt die verwendete Kapazität der Systempartition in MB
	 * @return Double verwendete Kapazität der Systempartition in MB
	 */
	public Double getSysDriveUsed(){
		return (Double) (FSrootCap-FSrootFree)/Math.pow(1024, 2);
	}
	
	/**
	 * Nutzung der Systempartition in %
	 * @return Float Nutzung der Systempartition in %
	 */
	public Float getSysDriveUsage(){
		return (float) (((getSysDriveCap()-getSysDriveFree())/getSysDriveCap())*100);
	}
	
/*	
	public static void main(String[] args) {
		//Auto-generated method stub
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("%s=%s%n", envName, env.get(envName));
		}

		/* Get a list of all filesystem roots on this system 
		File[] roots = File.listRoots();

		/* For each filesystem root, print some info 
		for (File root : roots) {
			if (root.getAbsolutePath().contains(env.get("HOMEDRIVE"))) {
				System.out.println("File system root: " + root.getAbsolutePath());
				System.out.println("Total space (bytes): " + root.getTotalSpace());
				System.out.println("Free space (bytes): " + root.getFreeSpace());
				System.out.println("Usable space (bytes): " + root.getUsableSpace());
			}
		}

	}
*/
}

/*
 * 
 */



/* RESULT:
 * 
USERDOMAIN_ROAMINGPROFILE=DESKTOP-37F0SIA
LOCALAPPDATA=C:\Users\Ich\AppData\Local
PROCESSOR_LEVEL=22
USERDOMAIN=DESKTOP-37F0SIA
FPS_BROWSER_APP_PROFILE_STRING=Internet Explorer
LOGONSERVER=\\DESKTOP-37F0SIA
SESSIONNAME=Console
ALLUSERSPROFILE=C:\ProgramData
PROCESSOR_ARCHITECTURE=AMD64
PSModulePath=C:\Program Files\WindowsPowerShell\Modules;C:\WINDOWS\system32\WindowsPowerShell\v1.0\Modules
SystemDrive=C:
OneDrive=C:\Users\Ich\OneDrive
APPDATA=C:\Users\Ich\AppData\Roaming
USERNAME=Ich
ProgramFiles(x86)=C:\Program Files (x86)
VBOX_MSI_INSTALL_PATH=C:\Program Files\Oracle\VirtualBox\
CommonProgramFiles=C:\Program Files\Common Files
Path=C:/Program Files/Java/jre1.8.0_131/bin/server;C:/Program Files/Java/jre1.8.0_131/bin;C:/Program Files/Java/jre1.8.0_131/lib/amd64;C:\ProgramData\Oracle\Java\javapath;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\WINDOWS\System32\WindowsPowerShell\v1.0\;C:\Program Files (x86)\AMD\ATI.ACE\Core-Static;C:\Program Files (x86)\Common Files\Acronis\SnapAPI\;C:\Program Files\Crucial\Crucial Storage Executive;C:\Program Files\MySQL\MySQL Utilities 1.6\;C:\Users\Ich\AppData\Local\Microsoft\WindowsApps;;C:\Users\Ich\Desktop;
FPS_BROWSER_USER_PROFILE_STRING=Default
PATHEXT=.COM;.EXE;.BAT;.CMD;.VBS;.VBE;.JS;.JSE;.WSF;.WSH;.MSC
OS=Windows_NT
COMPUTERNAME=DESKTOP-37F0SIA
PROCESSOR_REVISION=3001
CommonProgramW6432=C:\Program Files\Common Files
EMET_CE=I:-1;S:0;F:0;E:0;V:Nov  8 2016 21:10:37
ComSpec=C:\WINDOWS\system32\cmd.exe
ProgramData=C:\ProgramData
ProgramW6432=C:\Program Files
HOMEPATH=\Users\Ich
SystemRoot=C:\WINDOWS
TEMP=C:\Users\Ich\AppData\Local\Temp
HOMEDRIVE=C:
PROCESSOR_IDENTIFIER=AMD64 Family 22 Model 48 Stepping 1, AuthenticAMD
USERPROFILE=C:\Users\Ich
TMP=C:\Users\Ich\AppData\Local\Temp
CommonProgramFiles(x86)=C:\Program Files (x86)\Common Files
ProgramFiles=C:\Program Files
PUBLIC=C:\Users\Public
NUMBER_OF_PROCESSORS=4
windir=C:\WINDOWS
=::=::\
File system root: C:\
Total space (bytes): 134550122496
Free space (bytes): 82844909568
Usable space (bytes): 82844909568
*/
