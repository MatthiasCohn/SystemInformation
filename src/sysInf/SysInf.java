/**
 * 
 */
package sysInf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.net.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * @author Cohn, Matthias Ermittelt wesentliche Systemeigenschaften und gibt
 *         diese zurück
 * @version 1.0 (2017-06-22)
 */
public class SysInf {
	private static Map<String, String> env;
	private static double dFSrootCap, dFSrootFree;
	private static String sInet4, sInet6, sMAC, sFilePath;
	final static String sDLL = "jRegistryKey.dll";

	public SysInf() {
		loadSysInf();
	}

	/**
	 * @param args
	 *            Mit dem Erstellen eines PC-Objektes werden alle relevanten
	 *            Informationen aus dem System gelesen
	 */
	private static void loadSysInf() {
		env = System.getenv();
		File[] roots = File.listRoots();
		for (File root : roots) {
			if (root.getAbsolutePath().contains(env.get("HOMEDRIVE"))) {
				// System.out.println("File system root: " +
				// root.getAbsolutePath());
				dFSrootCap = Double.valueOf(root.getTotalSpace());
				dFSrootFree = Double.valueOf(root.getFreeSpace());
				// System.out.println("Usable space (bytes): " +
				// root.getUsableSpace());
			}
		}
		loadNetworkInf();
	}

	/**
	 * Netzwerkinformationen der Standardverbindung wird ausgelesen
	 */
	private static void loadNetworkInf() {
		sInet4 = null;
		sInet6 = null;
		try {
			sInet4 = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			sInet4 = "127.0.0.1";
		}
		try {
			InetAddress[] Inet = Inet6Address.getAllByName(Inet6Address.getLocalHost().getHostName());
			/*
			 * int i=0; for (i=0; i<=Inet.length-1;i++){
			 * System.out.println(Inet[i].getHostAddress()); //if
			 * (Inet[i].==Inet6Address) }
			 */
			sInet6 = Inet[Inet.length - 1].getHostAddress();

			// Inet6Address.getByAddress(Inet6Address.getLocalHost().getHostName(),
			// addr, NetworkInterface.getByName("le0");
			// Inet6Address.getLocalHost().getHostAddress().toString();
			// .getLocalHost().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			sInet6 = "::1";
			e.printStackTrace();
		}
		try {
			sMAC = getMacAddress(Inet4Address.getLocalHost(), Inet6Address.getLocalHost());
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			sMAC = "00:00:00:00:00:00";
			e.printStackTrace();
		}
	}

	/**
	 * Ermittelt die MAC-Adresse der Standardverbindung
	 * 
	 * @return String: MAC-Addresse
	 * @throws SocketException
	 * @link http://openbook.rheinwerk-verlag.de/java7/1507_11_013.html#dodtpa83dc14c-0485-4fd6-ab1c-8c63242651df
	 */
	private static String getMacAddress(InetAddress addr4, InetAddress addr6) throws SocketException {
		String result = "";
		InetAddress addr = null;

		if (addr4 == null) {
			if (addr6 == null) {
				return "Keine Netzwerkprotokoll/ Netzwerkkarte gefunden.";
			} else {
				addr = addr6;
			}
		} else {
			addr = addr4;
		}
		// for ( NetworkInterface ni : Collections.list(
		// NetworkInterface.getNetworkInterfaces() ) )
		// {
		byte[] hardwareAddress = NetworkInterface.getByInetAddress(addr).getHardwareAddress();// ni.getHardwareAddress();

		if (hardwareAddress != null) {
			for (int i = 0; i < hardwareAddress.length; i++)
				result += String.format((i == 0 ? "" : "-") + "%02X", hardwareAddress[i]);

			// return result;
		}
		// }

		return result;
	}

	/**
	 * Gibt die MAC-Adresse der Standardverbindung zurück
	 * 
	 * @return String: MAC-Addresse
	 */
	public String getMAC() {
		return sMAC;
	}

	/**
	 * Gibt die IPv4-Adresse der Standardverbindung zurück
	 * 
	 * @return String: IPv4-Addresse
	 */
	public String getIPv4Addr() {
		return sInet4;
	}

	/**
	 * Gibt die IPv4-Adresse der Standardverbindung zurück
	 * 
	 * @return String: IPv6-Addresse
	 */
	public String getIPv6Addr() {
		return sInet6;
	}

	/**
	 * Gibt den Computernamen zurück
	 * 
	 * @return String Computername
	 */
	public String getComputername() {
		return env.get("COMPUTERNAME");
	}

	/**
	 * Gibt den (sofern vorhandenen) Domain-Server zürück
	 * 
	 * @return String Logonserver
	 */
	public String getLogonServer() {
		return env.get("LOGONSERVER");
	}

	/**
	 * Gibt den aktuellen Nutzer(namen) zurück
	 * 
	 * @return String Username
	 */
	public String getUserName() {
		return env.get("USERNAME");
	}

	/**
	 * Gibt das verwendete Betriebesystem (Typ) zurück
	 * 
	 * @return String OS
	 */
	public String getOS() {
		return env.get("OS");
	}

	/**
	 * Gibt Informationen über die CPU zurück
	 * 
	 * @return String CPU Information
	 */
	public String getProcessor() {
		return env.get("PROCESSOR_IDENTIFIER");
	}

	/**
	 * Gibt die Anzahl der Prozessoren (bzw. Kerne) zurück
	 * 
	 * @return int Anz. Prozessorkerne
	 */
	public int getNumberOfCPU() {
		return Integer.parseInt(env.get("NUMBER_OF_PROCESSORS"));
	}

	/**
	 * Gibt den Pfad der Systempartition zurück
	 * 
	 * @return String Pfad der Systempartition
	 */
	public String getSysDrive() {
		return env.get("HOMEDRIVE");
	}

	/**
	 * Gibt die (Gesamt-)Kapazität der Systempartition in MB zurück
	 * 
	 * @return Double (Gesamt-)Kapazität der Systempartition in MB
	 */
	public Double getSysDriveCap() {
		return (double) (Math.round((Double) dFSrootCap / Math.pow(1024, 2) * 100) / 100);
	}

	/**
	 * Gibt die Freie Kapazität der Systempartition in MB zurück
	 * 
	 * @return Double Freie Kapazität der Systempartition in MB
	 */
	public Double getSysDriveFree() {
		return (double) Math.round((Double) dFSrootFree / Math.pow(1024, 2) * 100) / 100;
	}

	/**
	 * Gibt die verwendete Kapazität der Systempartition in MB
	 * 
	 * @return Double verwendete Kapazität der Systempartition in MB
	 */
	public Double getSysDriveUsed() {
		return (double) Math.round((Double) (dFSrootCap - dFSrootFree) / Math.pow(1024, 2) * 100) / 100;
	}

	/**
	 * Nutzung der Systempartition in %
	 * 
	 * @return Float Nutzung der Systempartition in %
	 */
	public Float getSysDriveUsage() {
		return (float) Math.round((float) (((getSysDriveCap() - getSysDriveFree()) / getSysDriveCap()) * 100) * 100)
				/ 100;
	}
	
	/**
	 * Funktioniert nur auf Windows
	 * Listet alle Programme mit Uninstall informationen auf
	 * @return String[] bei Erfolg; Null, wenn nicht möglich
	 */
	public String[] arrWinSoftware(){
		ArrayList<String> sTableInf = arrWinSoftwareList();
		try {
			return sTableInf.toArray(new String[sTableInf.size()]);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Funktioniert nur auf Windows
	 * Listet alle Programme mit Uninstall informationen auf
	 * @return ArrayList<String> bei Erfolg; Null, wenn nicht möglich
	 */
	public ArrayList<String> arrWinSoftwareList() {
		ArrayList<String> arrlSW=new ArrayList<String>();
		arrlSW=null;
		if (getOS().contains("Windows")) { // Funktioniert nur mit Windows
			
			if (bCopyVBS()) {
				String sInput=System.getProperty("user.dir")+("\\getInstalledSW.vbs");
				String sOutput=System.getProperty("user.dir")+("\\InstalledSW.txt");
				String command = "wscript "+ 
						"\""+sInput+"\" "+"\""+ sOutput+"\"";
				
				if(iExcecuteVBS(command)==0){
					
					try {
						 arrlSW=arrlInstalledSW(sOutput);
						 bDeleteFile(sOutput);
					} catch (Exception e) {
						arrlSW=null;
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					arrlSW=null;
					System.out.println("Auslesen Nicht möglich");
				}
			}
		}
		return arrlSW;
	}
	
	@SuppressWarnings("unused")
	private static ArrayList<String> arrlInstalledSW(String sFile) throws Exception{
		ArrayList<String> arrlSW=new ArrayList<String>();
		FileReader frFile = null;
		try {
			frFile = new FileReader(sFile); // String mit Pfad
			BufferedReader brPuffer = new BufferedReader(frFile);
			String sLine = "";
			try {
				while (null != (sLine = brPuffer.readLine())) {
					if(sLine!="")
					arrlSW.add(sLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("Datei kann nicht geöffnet werden.");
			} finally {
				if (null != brPuffer) {
					try {
						brPuffer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new Exception("Datenpuffer kann nicht aufgehoben werden.");
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("Kann Datei, bei angegebenem Pfad, nicht selektieren.");
		}
		if (arrlSW != null){
			return arrlSW;
		}
		return arrlSW;
	}
	
	private static boolean bDeleteFile(String sFile){
		File fFile=new File(sFile);
		return fFile.delete();
	}

	/**
	 * 
	 * @param sCommand
	 * @return
	 * {@link: https://www.java-forum.org/thema/runtime-ausfuehrung-abwarten.124728/}
	 */
	private int iExcecuteVBS(String sCommand){
		/* VB Script ausführen */
		Process p;
		int exitValue = 0;
		try {
			p= Runtime.getRuntime().exec(sCommand);
			exitValue = p.waitFor();
		} catch (InterruptedException | IOException e) {
			System.out.println("Fehlgeschlagen");
			e.printStackTrace();
		}

		/* Nur zur Kontrolle */
		//System.out.println("Exit-Value: " + exitValue);
		return exitValue;
	}

	private boolean bCopyVBS() {
		return true;
		/*
		boolean bOK = false;
		try {
			String outputFile = System.getProperty("user.dir") + "\\getInstalledSW.vbs";
			if (!(new File(outputFile)).exists()) {
				
				String inputFile = getClass().getResource("/application/getInstalledSW.vbs").toExternalForm();
				FileReader fr = new FileReader(inputFile);
				BufferedReader br = new BufferedReader(fr);
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
				String line;
				while ((line = br.readLine()) != null) {
					bw.write(line);
				}
				br.close();
				bw.close();
				bOK = true;
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Kopieren der Datei");
			bOK = false;
		}
		return bOK;
		*/
	}

	/**
	 * Manuelles setzen des Pfades einer Configurationsdatei
	 * 
	 * @param sPath
	 *            Pfades einer Configurationsdatei
	 * @throws Exception
	 */
	public void setsFilePath(String sPath) throws Exception {

		if (sFilePath == null && sPath == "") {
			// https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
			JFileChooser fcFile = new JFileChooser();
			fcFile.setDialogTitle("Wählen Sie die jRegistryKey.dll");
			fcFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
			fcFile.getCurrentDirectory();
			System.out.println(fcFile.getCurrentDirectory().getAbsolutePath());
			// fcFile.getCurrentDirectory();
			FileFilter ff = new FileNameExtensionFilter("jRegistryKey-DLL", "dll");
			fcFile.setFileFilter(ff);
			int returnVal = fcFile.showOpenDialog(null);
			fcFile.setDialogType(JFileChooser.OPEN_DIALOG);
			fcFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File fFile = fcFile.getSelectedFile();
				sFilePath = fFile.getPath().toString();
				// This is where a real application would open the file.
				sFilePath = sFilePath.replaceAll("\\\\", "\\\\\\\\");
				// http://stackoverflow.com/questions/13696461/replace-special-character-with-an-escape-preceded-special-character-in-java
				// System.out.println("Opening: " + sFilePath);
			} else {
				sFilePath = null;
			}
		}
		if (sPath != "") {
			sFilePath = sPath;
		}
		// System.out.println(sFilePath);
		try { // ausgewählter Pfad wird getestet
			FileReader frFile = null;
			frFile = new FileReader(sFilePath); // String mit Pfad
			if (frFile.ready())
				frFile.close();
			// System.out.println("Datei ("+ sFilePath+" bereit.");
		} catch (FileNotFoundException e) {
			sFilePath = null;
			e.printStackTrace();
			throw new Exception("Kann Datei, bei angegebenem Pfad, nicht selektieren.");
		}
	}

}