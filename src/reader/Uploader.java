/** 
 * (c) 2018 Nélson Rafael Martins All Rights Reserverd
 **/

package reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Uploader {
	public static String filepath = "\\\\vicaim02\\Download\\factoryDisplayFile\\Z5PPRP94.csv";
	public static String address = "http://192.168.14.34:8080/api/linhas";
	public static int[] lineCodes = { 50010, 50020, 50030, 50050, 50060, 50080, 50090, 50120, 50140, 50180, 50190,
			55010, 55020 };

	private Line[] lines;
	private Line[] linesTotal;
	private String[] lineIds = new String[lineCodes.length];
	private String[] lineTotalIds = new String[lineCodes.length];
	private CSVReader reader;
	private List<String> allIds = new ArrayList<String>();

	private void updateLinha(Line l, String id) {
		try {
			URL url = new URL(address + "/" + id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			String input = "{\n" + "\"ctrab\":" + l.get_cTrab() + ",\r\n" + "    \"descritivo\":\"" + l.get_descritivo()
					+ "\",\r\n" + "    \"turno\":\"" + l.get_turno() + "\",\r\n" + "    \"qtprod\":" + l.get_qtProd()
					+ ",\r\n" + "    \"qtrwk\":" + l.get_qtRwk() + ",\r\n" + "    \"objhora\":" + l.get_objHora()
					+ ",\r\n" + "    \"realhora\":" + l.get_realHora() + ",\r\n" + "    \"tabert\":" + l.get_tAbert()
					+ ",\r\n" + "    \"tprod\":" + l.get_tProd() + ",\r\n" + "    \"tutil\":" + l.get_tUtil() + ",\r\n"
					+ "    \"estado\":\"" + l.get_status() + "\"\n}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			// System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createLinha(Line l) {
		try {
			URL url = new URL(address);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			String input = "{\n" + "\"ctrab\":" + l.get_cTrab() + ",\r\n" + "    \"descritivo\":\"" + l.get_descritivo()
					+ "\",\r\n" + "    \"turno\":\"" + l.get_turno() + "\",\r\n" + "    \"qtprod\":" + l.get_qtProd()
					+ ",\r\n" + "    \"qtrwk\":" + l.get_qtRwk() + ",\r\n" + "    \"objhora\":" + l.get_objHora()
					+ ",\r\n" + "    \"realhora\":" + l.get_realHora() + ",\r\n" + "    \"tabert\":" + l.get_tAbert()
					+ ",\r\n" + "    \"tprod\":" + l.get_tProd() + ",\r\n" + "    \"tutil\":" + l.get_tUtil() + ",\r\n"
					+ "    \"estado\":\"" + l.get_status() + "\"\n}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			// System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getLinhas() {
		try {
			URL url = new URL(address);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output, out = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				out += output;
			}
			conn.disconnect();
			parseResponse(out);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseResponse(String s) {
		String[] tokens = s.split("_id");
		for (int i = 1; i < tokens.length; i++) {
			String id = tokens[i].substring(3, 27);
			this.allIds.add(id);
			int ctrab = Integer.parseInt(tokens[i].substring(37, 42));
			String turno = tokens[i].substring(tokens[i].lastIndexOf("\"turno\":\"") + 9,
					tokens[i].lastIndexOf("\",\"qtprod\":"));
			// System.out.println(turno);
			for (int j = 0; j < lineCodes.length; j++) {
				if (ctrab == lineCodes[j] && !turno.equals("TOTAL")) {
					lineIds[j] = id;
				} else if (ctrab == lineCodes[j] && turno.equals("TOTAL")) {
					lineTotalIds[j] = id;
				}
			}
		}
	}

	private void deleteLinha(String string) {
		try {
			URL url = new URL(address + "/" + string);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startReader() {
		try {
			reader = new CSVReader(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CompareLineCode compare = new CompareLineCode(reader);
		this.lines = compare.getLinesToDisplay();
		this.linesTotal = compare.getLinesToDisplayTotal();
	}

	private void manageLinhas() {
		getLinhas();

		for (int i = 0; i < lines.length; i++) {
			if (lineIds[i] == null) {
				System.out.println("NORMAL-CREATE");
				createLinha(lines[i]);
			} else {
				System.out.println("NORMAL-UPDATE");
				updateLinha(lines[i], lineIds[i]);
			}
		}
		for (int i = 0; i < linesTotal.length; i++) {
			if (lineTotalIds[i] == null) {
				System.out.println("TOTAL-CREATE");
				createLinha(linesTotal[i]);
			} else {
				System.out.println("TOTAL-UPDATE");
				updateLinha(linesTotal[i], lineTotalIds[i]);
			}
		}

		//for (int i=0; i<allIds.size(); i++) { deleteLinha(allIds.get(i)); }

	}

	Uploader() {
		TimerTask repeatedTask = new TimerTask() {
			public void run() {
				startReader();
				manageLinhas();
			}
		};
		Timer timer = new Timer("Timer");
		long delay = 1000L;
		long period = 1000L * 60 * 5;
		timer.scheduleAtFixedRate(repeatedTask, delay, period);
	}
}
